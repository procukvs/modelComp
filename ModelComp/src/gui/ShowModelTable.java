package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import db.*;
import main.*;

public class ShowModelTable extends JPanel {
	private DbAccess db;
	private TitledBorder border;
	private ModelTable  dbm;
	private JTable table;
	private JLabel selection;
	
	private int idModel = 0;
	private String type = "Algorithm";
	private Model model = null;
	
	ShowModelTable(DbAccess  db){  //, ShowModels owner
		
		//сформувати необхідні gui-елементи 
		dbm = new ModelTable(false);
		table = new JTable(dbm);
		JScrollPane forTable = new JScrollPane(table);
		//JLabel head = new JLabel("Підстановка ");
		selection = new JLabel("  0:" + dbm.getRowCount());
		JButton first = new JButton("|<");
		first.setMaximumSize(new Dimension(20,20));
		JButton prev = new JButton("<");
		prev.setMaximumSize(new Dimension(20,20));
		JButton next = new JButton(">");
		next.setMaximumSize(new Dimension(20,20));
		JButton last = new JButton(">|");
		last.setMaximumSize(new Dimension(20,20));
			
		this.db = db; 
		//=================================
		// формуємо розміщення
		setLayout(new BorderLayout());
		border = new TitledBorder("Підстановки алгоритму");
		setBorder(border); //new TitledBorder("Підстановки алгоритму"));
		
		//-----------------------------------
		Box select = Box.createHorizontalBox();
		select.add(Box.createHorizontalStrut(5));
		select.add(first); select.add(prev); 
		select.add(selection);
		select.add(next); 
		select.add(last);
		select.add(Box.createHorizontalGlue());
		//-----------------------------------
		add(forTable, BorderLayout.CENTER);
		add(select, BorderLayout.SOUTH);
		
		// встановлюємо параметри таблиці включаючи модель колонок
		setTableStructure();
		
		table.addMouseListener(new SelectRow());
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		
	}

	public void showModel(String type, Model model){
		this.type = type; this.model = model;
		border.setTitle(Model.title(type, 4));
							//setTableStructure(); 
		showTable(true, table.getSelectedRow() + 1);
						//showTable(true, 0);
	}
	
	private void showTable(boolean update, int selected){
		//System.out.println(" showTable : ShowModelTable " + update + " "  + ((model == null)?0:model.id));
		if (update) {
			ArrayList ds = null;
			if(model != null) ds = model.getDataSource(model.id);
			//System.out.println(" showTable : ShowModelTable:ds " +  ((ds == null)?-1:ds.size()));
			setTableStructure();
			if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);
		}
		int all = dbm.getRowCount();
		if (selected > 0){ 
			if (selected > all) selected = all;
			table.getSelectionModel().setSelectionInterval(selected - 1, selected - 1);
			selection.setText(selected  + " : " + all);
		}	else selection.setText("0  :  " + all); 
	}


	// класи - слухачі виділення (selection...)
	class SelectFirst implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			showTable(false, 1);
		}	
	}
	class SelectPrev implements ActionListener  {
		public void actionPerformed(ActionEvent event){	showPrevRow(false);	}	
	}
	class SelectNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){	showNextRow(false);	}	
	}
	class SelectLast implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			showTable(false, dbm.getRowCount());	
		}	
	}
	class SelectRow extends MouseAdapter {
		// рядок можна виділити мишкою
		public void  mousePressed(MouseEvent e) {
			selection.setText((table.getSelectedRow() + 1)  + " : " + dbm.getRowCount());
		}
	}
	
	public String selectedName() {
		// номер виділеної підстановки або 0 !!
		int r = table.getSelectedRow();
		String name = "";
		if (r >= 0) {
			name = (String)dbm.getValueAt(r,0);
		}
		return name;
	}
	
	// методи, що змінюють підстановки -- можливо переставляють і т. і.
	public int selectedRule() {
		// номер в БД виділеної підстановки/команди  або 0 !!
		int r = table.getSelectedRow();
		int rule = 0;
		if (r >= 0) {
			int col = 0;
			switch (type) {
			case "Algorithm": col = 5; break; 
			case "Machine": 
				col = model.getMain().length() + model.getAdd().length() + model.getNo().length() + 3;
				break;
			case "Post": col = 5; break;	
			}
			rule = (Integer)dbm.getValueAt(r,col);
		}
		return rule;
	}
	
	// номер виділеної команди для Algorithm / Post/ Computer
	public int numberSelectedRow(){
		int r = table.getSelectedRow();
		int rule = 0;
		if (r >= 0) {
			int col = 0;
			switch (type) {
			//case "Algorithm": col = 1; break; 
			case "Machine": 
				col = model.getMain().length() + model.getAdd().length() + model.getNo().length() + 3;
				break;
			//case "Post": col = 1; break;
			//case "Computer": col = 1; break;	
			}
			rule = (Integer)dbm.getValueAt(r,col);		
		}
		return rule;
	}
	
	public void showPrevRow(boolean update){
		int r = table.getSelectedRow();
		if (r==0) r++;
		showTable(update, r);
	}
	public void showNextRow(boolean update){
		//System.out.println(" table.getSelectedRow() + 2 = " + table.getSelectedRow() + 2);
		showTable(update, table.getSelectedRow() + 2);
	}
	
	public void showFirstRow(boolean update){
		showTable(update, 1);
	}		
	
	public void showRow(boolean update, int row){
		showTable(update, row);
	}		
		
	private void setTableStructure() {
		dbm.setDataSource(null);
		dbm.setInitialModel(findInform(type));
		dbm.fireTableStructureChanged();
		setColumnWidth();
		//System.out.println("setTableStructure ");
	}
	
	private void setColumnWidth() {
		TableColumn column = null;
		int[] widthCol = findWidth(); //{10,100,100,10,400,10};
	    for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }     
	}
	
	private int[] findWidth(){
		int [] w = null;
		switch(type){
		case "Algorithm" : w = new int[]{10,100,100,10,390,10,10}; break;
		case "Post" : w = new int[]{10,10,100,100,390,10,10}; break;
		case "Recursive" : w = new int[]{60, 10,10,10,170,350,10,10}; break;
		case "Machine": 
			if (model != null) {
				Machine m = (Machine)model;
				int l = m.main.length() + m.add.length() + m.no.length() + 1;
				w = new int[l+4];
				w[0] = 20;
				for(int i = 1; i <= l; i++) w[i] = 30;
				if(490 > l*30) w[l+1] = 590 - l*30; else w[l+1] = 100;
				w[l+2] = 10; w[l+3] = 10;
			} else w = new int[]{10,30,30,30,510, 10,10};	
			break;
		default:  w = new int[]{10,100,100,10,390,10,10}; 
		}
		return w;
	}
	
	// інформація про колонки = назва + тип + редагуємість	
	private String[][] findInform(String type){
		String[][] info = null;
		switch(type){
		case "Algorithm" : 
			info = new String[][]{
									{"№","I","N"},
									{"Ліва частина підстановки","S","N"},
									{"Права частина підстановки","S","N"},
									{"Заключна ?","B","N"},
									{"Коментар","S","N"},
									{"№П","I","N"},
									{"№Ал","I","N"}
								}; break;
		case "Post" : 
			info = new String[][]{
									{"№","I","N"},
									{"Аксіома ?","B","N"},
									{"Ліва частина правила","S","N"},
									{"Права частина правила","S","N"},
									{"Коментар","S","N"},
									{"№Пр","I","N"},
									{"№С","I","N"}
								}; break;
		case "Recursive" : 
			info = new String[][]{
									{"Назва","S","N"},
									{"Арність","I","N"},
									{"Костанта","B","N"},
									{"Вірна","B","N"},
									{"Тіло функції","S","N"},
									{"Коментар","S","N"},
									{"№Ф","I","N"},
									{"№Н","I","N"}
								}; break;						
		case "Machine": 
			int l;
			String allS;
			if (model != null) {
				Machine m = (Machine)model;
				l = m.main.length() + m.add.length() + m.no.length() + 1;
				allS = "_" + m.main + m.add + m.no;
			}
			else {
				l = 3; allS = "_|#";
			}
			info = new String[l+4][3];
			info[0][0] = "Стан"; info[0][1] ="S"; info[0][2] ="N";
			for(int i = 1; i <= l; i++){
				info[i][0] = allS.substring(i-1,i); info[i][1] ="S"; info[i][2] ="N";
			}
			info[l+1][0] = "Коментар"; info[l+1][1] ="S"; info[l+1][2] ="N";
			info[l+2][0] = "№Ст"; info[l+2][1] ="I"; info[l+2][2] ="N";
			info[l+3][0] = "№М"; info[l+3][1] ="I"; info[l+3][2] ="N";
			break;
		default: 	info = new String[][]{
				{"№","I","N"},
				{"Ліва частина підстановки","S","E"},
				{"Права частина підстановки","S","E"},
				{"Заключна ?","B","E"},
				{"Коментар","S","E"},
				{"№П","I","N"},
				{"№Ал","I","N"}
			};
			
		}
		return info;
	}
	
	/*
	  String[][] inform = {
				{"№","I","N"},
				{"Ліва частина підстановки","S","E"},
				{"Права частина підстановки","S","E"},
				{"Заключна ?","B","E"},
				{"Коментар","S","E"},
				{"№Ал","I","N"}
		};
		dbm.setInitialModel(inform);
		 */
}
