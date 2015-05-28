package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import db.*;
import main.*;

public class ShowModelOne extends JPanel {
	DbAccess db;
	// завжди виділяється одна модель із списку моделей !!!
	// порядок в списку визначається в БД 
	private int selected = 0;
	private String type = "Algorithm";
	
	private JLabel selection;
	private ShowDescription description;
	private ShowModelProgram program;
	
	
	ShowModelOne(DbAccess  db, Frame owner){
		
		//сформувати необхідні gui-елементи 
		description = new ShowDescription(db);
		//JButton program = new JButton("This is place for program");
		//description = new ShowDescription(db);
		program = new ShowModelProgram(db, owner);
		selection = new JLabel("  0:0  ");  //("  0:" + db.getModelCount());
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
				//setBorder(new BevelBorder(BevelBorder.LOWERED));
				//setBorder(new BevelBorder(SoftBevelBorder.RAISED));
				//setBorder(new LineBorder(Color.green,5));
		setBorder(new EtchedBorder());
		//-----------------------------
		Box select = Box.createHorizontalBox();
		select.add(Box.createHorizontalStrut(5));
		select.add(first); select.add(prev); 
		select.add(selection);
		select.add(next); 
		select.add(last);
		select.add(Box.createHorizontalGlue());
		//-------------------------------------
		Box mainBox = Box.createVerticalBox();
		mainBox.add(program);
		mainBox.add(select);
	
		add(description, BorderLayout.NORTH);
		add(mainBox, BorderLayout.CENTER);
				
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		
	}	
	
	// класи - слухачі виділення (selection...)
	class SelectFirst implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			int r = db.getModelCount(type);
			if (r>0){ 
				selected = 1;
				
					//table.getSelectionModel().setSelectionInterval(0, 0);
					//int r = table.getSelectedRow() + 1;
					//selection.setText((table.getSelectedRow() + 1)  + " : " + dbm.getRowCount());
			}
			selection.setText(selected +" : " + r);
			showSelected();
		}	
	}
	class SelectPrev implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			//int r = db.getModelCount();
			//if (selected > 1){
			//	selected--;
			//}
			//selection.setText(selected +" : " + r);
			//showSelected();
			showPrev();
		}	
	}
	class SelectNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			int r = db.getModelCount(type);
			//System.out.println(".." + r + "..");
			if ((r > selected) && (selected > 0)){ 
				//int all = dbm.getRowCount();
				selected++;
					//table.getSelectionModel().setSelectionInterval(r-1, r-1);
					//selection.setText((table.getSelectedRow() + 1)  + " : " + dbm.getRowCount());
			}
			selection.setText(selected +" : " + r);
			showSelected();
		}	
	}
	class SelectLast implements ActionListener  {
		public void actionPerformed(ActionEvent event){
				//int r = db.getModelCount();
				//if (r > 0){
				//	selected = r;
				//}	
				//selection.setText(selected +" : " + r);
				//showSelected();
			showLast();
		}	
	}
	private void showSelected() {
		Model model;
		int id;
		if (selected > 0) {
			id = db.getNumber(type,selected);
			model = null;
			if (id > 0) model = db.getModel(type, id);
		}
		else model = null;
	    if (model == null) {
	    	//nameModel.setText("-----------");
	    }
	    else {
	    	                         //nameModel.setText(model.nmAlgo);
	    	description.setModel(model);
	    	program.setModel(model);
	    	                     //work.setModel(model);
	    }
	}
		
	public void showLast(){
		int r = db.getModelCount(type);
		if (r > 0)	selected = r;
		selection.setText(selected +" : " + r);
		showSelected();
	}	
		
		public void showPrev() {
			int r = db.getModelCount(type);
			if (selected > 1){
				selected--;
			}
			selection.setText(selected +" : " + r);
			showSelected();
		}
				
	
}
