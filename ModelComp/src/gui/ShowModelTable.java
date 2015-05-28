package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import db.*;

public class ShowModelTable extends JPanel {
	DbAccess db;
	private ModelTable  dbm;
	private JTable table;
	private JLabel selection;
	
	private String type = "Algorithm";
	
	ShowModelTable(DbAccess  db, Frame owner){
		
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
		
	/*	
		TableColumnModel colModel;
		colModel = table.getColumnModel();
		for (int i = 0; i < colModel.getColumnCount(); i++){
			TableColumn col = colModel.getColumn(i);
			//col.setPreferredWidth(widthCol[i]);
			System.out.println("i=" + i + " width=" + col.getWidth() + 
					" min=" + col.getMinWidth() + " pre=" + col.getPreferredWidth() +
					" max=" + col.getMaxWidth());
			
		}
		*/
	
	//  table.addMouseListener(new SelectRow());
	//	first.addActionListener(new SelectFirst());
	//	prev.addActionListener(new SelectPrev());
	//	next.addActionListener(new SelectNext());
	//	last.addActionListener(new SelectLast());
		
	}
	
	public void showTable(String type, int idModel){
		int r = table.getSelectedRow();
		ArrayList ds = db.getDataSource(type,idModel);
		if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);
		else setTableStructure();
		//System.out.println(".." + r+ ".."+table.getSelectedRow() );
		if (r >= 0){ 
			table.getSelectionModel().setSelectionInterval(r, r);
			selection.setText((r+1)  + " : " + dbm.getRowCount());
		}	
	}
	
	private void setTableStructure() {
		dbm.fireTableStructureChanged();
		setColumnWidth();
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
