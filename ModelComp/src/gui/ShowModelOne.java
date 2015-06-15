package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import db.*;
import main.*;

public class ShowModelOne extends JPanel {
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	//private ShowModelAll showMain;
	private String type = "Algorithm";
	private int r = 0;
	private Model model = null;
	// завжди виділяється одна модель із списку моделей !!!
	// порядок в списку визначається в БД 
	private int selected = 0;
		
	private JLabel selection;
	private ShowDescription description;
	private ShowModelProgram program;
	
	
	//!!!!!!!!!!!!!!!!!!!!!
	ShowModelOne(DbAccess  db, ShowModels owner){
	//ShowModelOne(DbAccess  db, ShowModelAll owner, ShowMenu frame){
		
		//сформувати необхідні gui-елементи 
		description = new ShowDescription(true);
		description.setLink(db, owner);
					//JButton program = new JButton("This is place for program");
					//description = new ShowDescription(db);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		program = new ShowModelProgram(db, owner);
		//program = new ShowModelProgram(db, owner,frame);
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
		showMain = owner;
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
	
	public void setModel(String type, Model model) {
		this.model = model;
		this.type = type;
		if (model == null) selected = 0; else selected = db.getOrder(type, model.name);
	   	description.setModel(type, model);
    	program.setModel(type, model);
    	r = db.getModelCount(type);
    	//System.out.println("setModel  " + selected + "  cnt = " + r);
    	if (selected > r) selected = r;
    	selection.setText(selected + " : " + r);
	}
	
	// класи - слухачі виділення (selection...)
	class SelectFirst implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			r = db.getModelCount(type);
			//System.out.println("SelectFirst  " + r);
			selected = 1;
			if (r==0){ 
				selected = 0; 
				showMain.showModel(type, 0);
			} else showMain.showModel(type, db.getNumber(type, selected));
		}	
	}
	class SelectPrev implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			selectPrev();
		}	
	}
	class SelectNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if ((db.getModelCount(type)> selected) && (selected > 0)){ 
				selected++;
				showMain.showModel(type, db.getNumber(type, selected));
			}
		}	
	}
	class SelectLast implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			selectLast();
		}	
	}
			
	public void selectLast(){
		r = db.getModelCount(type);
		selected = r;
		if (r==0){ 
			selected = 0; showMain.showModel(type, 0);
		} else showMain.showModel(type, db.getNumber(type, selected));
	}	
	public void selectPrev() {
		if (selected > 1) {
			selected--;
			showMain.showModel(type, db.getNumber(type, selected));
		}
	}

}
