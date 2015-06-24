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
		boolean output = what.equals("Output");
		TableColumn column = null;
		int[] widthCol = findWidth(output); //{10,100,100,10,400,10};
		if (output) border.setTitle("Список моделей");
		else border.setTitle("Список введених моделей");
		//setTableStructure();
		dbm.setDataSource(null);
		dbm.setInitialModel(findInform(output));
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
	private String[][] findInform(boolean output){
		String[][] info = null;
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
				};
		return info;
	}
	private int[] findWidth(boolean output){
		int [] w = null;
		if(output) w = new int[]{70,90,440,30,20,50,20}; 
		else w = new int[]{70,90,540,10,10,10}; ;
		return w;
	}
}
