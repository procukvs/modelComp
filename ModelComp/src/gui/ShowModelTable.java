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
		setBorder(new TitledBorder("Підстановки алгоритму"));
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
		String[][] inform = {
				{"№","I","N"},
				{"Ліва частина підстановки","S","E"},
				{"Права частина підстановки","S","E"},
				{"Заключна ?","B","E"},
				{"Коментар","S","E"},
				{"№Ал","I","N"}
		};
		dbm.setInitialModel(inform);
		//setColumnWidth();
		setTableStructure();
		
		table.addMouseListener(new SelectRow());
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		
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
		
	// методи, що змінюють підстановки -- можливо переставляють і т. і.
	public int selectedRule() {
		// номер виділеної підстановки або 0 !!
		int r = table.getSelectedRow();
		int rule = 0;
		if (r >= 0) rule = (Integer)dbm.getValueAt(r,0);
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
	
	public void showModel(String type, Model model){
		this.type = type; this.model = model;
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
			if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);
			else setTableStructure();
		}
		int all = dbm.getRowCount();
		if (selected > 0){ 
			if (selected > all) selected = all;
			table.getSelectionModel().setSelectionInterval(selected - 1, selected - 1);
			selection.setText(selected  + " : " + all);
		}	else selection.setText("0  :  " + all); 
	}
	
	private void setTableStructure() {
		dbm.setDataSource(null);
		dbm.fireTableStructureChanged();
		setColumnWidth();
		//System.out.println("setTableStructure ");
	}
	
	private void setColumnWidth() {
		TableColumn column = null;
		int[] widthCol = {10,100,100,10,400,10};
	    for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }     
	}
}
