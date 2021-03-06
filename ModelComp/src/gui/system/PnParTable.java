package gui.system;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.*;

import main.*;
import db.*;
import gui.ModelTable;

public class PnParTable extends JPanel {
	private DbAccess db;
	private ModelTable  dbm;
	private JTable table;
	private TitledBorder border;
	
	public PnParTable(DbAccess  db){
		//���������� ��������� gui-�������� 
		dbm = new ModelTable(true);
		table = new JTable(dbm);
		JScrollPane forTable = new JScrollPane(table);
		
		this.db = db; 
		//=================================
		// ������� ���������
		setLayout(new BorderLayout());
		border = new TitledBorder("������ �������");
		setBorder(border);
		add(forTable, BorderLayout.CENTER);
	}
	
	public void showTableFiles(String what, ArrayList fs){
		String titleSt = "";
		TableColumn column = null;
		int[] widthCol = findWidth(what); //{10,100,100,10,400,10};
		
		switch (what){
		case "Input":	titleSt = "������ �������� �������"; break;
		case "Output":	titleSt = "������ �������"; break;
		case "State":	titleSt = "������ ������������ ���������"; break;
		case "Parameters":	titleSt = "������ ��� ���������� ������� ���������"; break;
		}	
		border.setTitle(titleSt);
		dbm.setDataSource(null);
		dbm.setInitialModel(findInform(what));
		dbm.fireTableStructureChanged();
		for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }  
		//
		if ((fs != null) && (fs.size() > 0)) dbm.setDataSource(fs);
	}
	
	public ArrayList getOutputModel(){
		ArrayList ml = new ArrayList();
		ArrayList row;
		int add1 = 0;
		if (Parameters.getRegime().equals("teacher")) add1++;
		for(int i = 0; i < dbm.getRowCount(); i++){
			if((boolean)dbm.getValueAt(i, 5+add1)){
				row = new ArrayList();
				row.add(dbm.getValueAt(i, 0+add1));
				row.add(dbm.getValueAt(i, 6+add1));
				ml.add(row);
			}
		}
		return ml;
	}
	
	public void showInputModel(ArrayList ml){
		if ((ml != null) && (ml.size() > 0)) {
			dbm.fireTableStructureChanged();
			dbm.setDataSource(ml);
		}
		//System.out.println("ShowFiles: showInputModel row = "+ getSelectedRow());
	}
	// ���������� ��� ������� = ����� + ��� + ����������	
	private String[][] findInform(String type){
		String[][] info = null;
		switch (type){
		case "Input":	
			info = new String[][]{
				{"���","S","N"},
				{"�����","S","N"},
				{"��������","S","N"},
				{"�������?","B","N"},
				{"�������","I","N"},
				{"�","I","N"}
			}; break;
		case "Output":
			if (Parameters.getRegime().equals("teacher")) {
				info = new String[][]{
					{"�����","S","N"},
					{"���","S","N"},
					{"�����","S","N"},
					{"��������","S","N"},
					{"�������?","B","N"},
					{"�������","I","N"},
					{"�������?","B","E"},
					{"�","I","N"}
				};
			} else {
				info = new String[][]{
						{"���","S","N"},
						{"�����","S","N"},
						{"��������","S","N"},
						{"�������?","B","N"},
						{"�������","I","N"},
						{"�������?","B","E"},
						{"�","I","N"}
					};				
			} break;
		case "State":	
			info = new String[][]{
				{"�����","S","N"},
				{"��������","S","N"},
				{"��������","S","N"}
			}; break;
		case "Parameters":	
			info = new String[][]{
				{"�����","S","N"},
				{"��������","S","N"},
				{"��������","S","N"}
			}; break;
		}	
		return info;
	}
	private int[] findWidth(String type){
		int [] w = null;
		switch (type){
		case "Input":w = new int[]{70,90,540,10,10,10}; break;
		case "Output":
			if (Parameters.getRegime().equals("teacher")) 
				w = new int[]{60,70,90,380,30,20,50,20}; 
			else w = new int[]{70,90,440,30,20,50,20}; 
			break;
		case "State":w = new int[]{80,100,550};  break;
		case "Parameters":w = new int[]{80,100,550};  break;
		}
		return w;
	}
	
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
	public String getValue(int row, int col){
		return (String)dbm.getValueAt(row,col);		
	}
}
