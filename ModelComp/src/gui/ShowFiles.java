package gui;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableColumn;

import java.util.*;

//import main.*;
import db.*;

public class ShowFiles extends JPanel {
	private DbAccess db;
	private ModelTable  dbm;
	private JTable table;
	private TitledBorder border;
	
	ShowFiles(DbAccess  db){
		//сформувати необхідні gui-елементи 
		dbm = new ModelTable(true);
		table = new JTable(dbm);
		JScrollPane forTable = new JScrollPane(table);
		
		this.db = db; 
		//=================================
		// формуємо розміщення
		setLayout(new BorderLayout());
		border = new TitledBorder("Список моделей");
		setBorder(border);
		add(forTable, BorderLayout.CENTER);
	}
	
	public void showTableFiles(String what, ArrayList fs){
		//boolean output = what.equals("Output");
		String titleSt = "";
		TableColumn column = null;
		int[] widthCol = findWidth(what); //{10,100,100,10,400,10};
		
		switch (what){
		case "Input":	titleSt = "Список введених моделей"; break;
		case "Output":	titleSt = "Список моделей"; break;
		case "State":	titleSt = "Список встановлених параметрів"; break;
		case "Parameters":	titleSt = "Список всіх допустимих значень параметрів"; break;
		}	
		border.setTitle(titleSt);
		//if (output) border.setTitle("Список моделей");
		//else border.setTitle("Список введених моделей");
		//setTableStructure();
		//if (what.equals("State") || what.equals("Parameters")) return;
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
		for(int i = 0; i < dbm.getRowCount(); i++){
			if((boolean)dbm.getValueAt(i, 5)){
				row = new ArrayList();
				row.add(dbm.getValueAt(i, 0));
				row.add(dbm.getValueAt(i, 6));
				ml.add(row);
			}
		}
		return ml;
	}
	
	public void showInputModel(ArrayList ml){
		if ((ml != null) && (ml.size() > 0)) dbm.setDataSource(ml);
	}
	// інформація про колонки = назва + тип + редагуємість	
	private String[][] findInform(String type){
		String[][] info = null;
		switch (type){
		case "Input":	
			info = new String[][]{
				{"Тип","S","N"},
				{"Назва","S","N"},
				{"Коментар","S","N"},
				{"Числова?","B","N"},
				{"Арність","I","N"},
				{"№","I","N"}
			}; break;
		case "Output":
			info = new String[][]{
				{"Тип","S","N"},
				{"Назва","S","N"},
				{"Коментар","S","N"},
				{"Функція?","B","N"},
				{"Арність","I","N"},
				{"Вивести?","B","E"},
				{"№","I","N"}
			}; break;
		case "State":	
			info = new String[][]{
				{"Назва","S","N"},
				{"Значення","S","N"},
				{"Коментар","S","N"}
			}; break;
		case "Parameters":	
			info = new String[][]{
				{"Назва","S","N"},
				{"Значення","S","N"},
				{"Коментар","S","N"}
			}; break;
		}	
		/*
		if(output) info = new String[][]{
									{"Тип","S","N"},
									{"Назва","S","N"},
									{"Коментар","S","N"},
									{"Функція?","B","N"},
									{"Арність","I","N"},
									{"Вивести?","B","E"},
									{"№","I","N"}
				};
		else info = new String[][]{
									{"Тип","S","N"},
									{"Назва","S","N"},
									{"Коментар","S","N"},
									{"Числова?","B","N"},
									{"Арність","I","N"},
									{"№","I","N"}
				}; */
		return info;
	}
	private int[] findWidth(String type){
		int [] w = null;
		switch (type){
		case "Input":w = new int[]{70,90,540,10,10,10}; break;
		case "Output":w = new int[]{70,90,440,30,20,50,20}; break;
		case "State":w = new int[]{80,100,550};  break;
		case "Parameters":w = new int[]{80,100,550};  break;
		}
		/*
		if(output) w = new int[]{70,90,440,30,20,50,20}; 
		else w = new int[]{70,90,540,10,10,10}; ; */
		return w;
	}
	
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
	public String getValue(int row, int col){
		return (String)dbm.getValueAt(row,col);		
	}
}
