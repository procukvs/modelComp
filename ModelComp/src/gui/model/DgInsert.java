package gui.model;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.event.*;

import main.*;
import model.AllModels;
import model.Model;
import db.*;

public class DgInsert extends JDialog {
	private String type = "Machine";
	private Model model = null;
	//private DbAccess db;
	private AllModels env=null;
	
	private String nmModel = "";
	private String nmFunction = "";
	
	private JLabel title;
	private JLabel lModel;
	private JComboBox cbModel;
	private JLabel lFunction;
	private JComboBox cbFunction;
	private JButton yes;
	private JButton cancel;
	
	private ArrayList aSet; 
	private boolean workModel= false;
	
	public DgInsert(Frame owner) {
		super(owner,"Select");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		title = new JLabel("Вставити програму");
		//title.setMinimumSize(new Dimension(1000, 14));
		title.setHorizontalAlignment(title.CENTER);
		title.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,14));
		
		lModel = new JLabel("Набір");
		cbModel = new JComboBox();
		lFunction = new JLabel("Функція");
		cbFunction= new JComboBox();
		
		yes = new JButton("Так");
		cancel = new JButton("Вийти");
		
		// формуємо розміщення
		setLayout(new BorderLayout());
		Box head = Box.createHorizontalBox();
		head.add(Box.createHorizontalStrut(25));
		head.add(title);
		head.add(Box.createHorizontalStrut(25));
		//-----------------------------
		Box main = Box.createHorizontalBox();
		main.add(Box.createGlue());
		main.add(lModel);
		main.add(cbModel);
		main.add(Box.createHorizontalStrut(15));
		main.add(lFunction);
		main.add(cbFunction);
		main.add(Box.createGlue());
		//-----------------------------
		Box	rMain = Box.createVerticalBox();
		rMain.add(Box.createVerticalStrut(2));
		rMain.add(main);
		rMain.add(Box.createVerticalStrut(5));
		//rMain.setBorder(new EtchedBorder());
		//---------------------
		Box button = Box.createHorizontalBox();
		button.add(Box.createGlue());
		button.add(yes);
		button.add(Box.createHorizontalStrut(15));
		button.add(cancel);
		button.add(Box.createGlue());
		//---------------------
		add(head, BorderLayout.NORTH);
		add(rMain, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		///pack();
		// встановити слухачів !!!
		cbModel.addActionListener(new LsModel());
		yes.addActionListener(new LsYes());
		cancel.addActionListener(new LsCancel());
	}

	public void setInsert(String type, Model model, AllModels env, int num){
		String fstSet = "";
		nmModel = ""; nmFunction = "";
		this.type = type; this.model = model;
		this.env = env;
		//this.db = db;
		lFunction.setVisible(type.equals("Recursive")||type.equals("Calculus"));
		cbFunction.setVisible(type.equals("Recursive")||type.equals("Calculus"));
		switch(type){
		case "Computer" :  
			title.setText("Вставити після команди " + num + " програму");
			lModel.setText("Машина  ");
			break;
		case "Machine" :
			title.setText("Вставити програму");
			lModel.setText("Машина  ");
			break;
		case "Recursive" :  lFunction.setText("Функція  ");
			title.setText("Вставити функцію з іншого набору");
			lModel.setText("Набір  ");
			break;
		case "Calculus" :	lFunction.setText("Вираз  ");
			title.setText("Вставити після виразу " + num + " вираз з іншого набору");
			lModel.setText("Набір  ");
			break;
		default: break;
		}	
		
		workModel = false;
		//aSet = db.getAllModel(type);
		aSet = env.getAllModel();
		cbModel.removeAllItems();
		for (int i = 0; i < aSet.size(); i++){
			String name1 = (String)aSet.get(i);
			if(!name1.equals(model.name)) {
				cbModel.addItem(name1);
				if(fstSet.isEmpty()) fstSet = name1;
			}
		}
		workModel = true;
		if (type.equals("Recursive")||type.equals("Calculus"))	setNames(fstSet);
		//System.out.println("ShowCommand:setCommand " + what + " " + type + " " + id);
		//if (what == "Add") lWhat.setText(Model.title(type, 9)); else lWhat.setText("Редагувати");
		//test.setVisible(type.equals("Recursive")||type.equals("Calculus"));
		//structure.setVisible(type.equals("Recursive"));
		//pCommand.setRule(type, model, id, what);
		pack();
	}
	
	
	public String getNmModel() { return nmModel;}
	public String getNmFunction() { return nmFunction;}
	
	class LsModel implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (workModel && (type.equals("Recursive")||type.equals("Calculus")))	
				setNames((String)cbModel.getSelectedItem());
		}
	}
	
	class LsYes implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			nmModel = (String)cbModel.getSelectedItem();
			nmFunction = (String)cbFunction.getSelectedItem();
			hide();
		}	
	}
	class LsCancel implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			nmModel = ""; nmFunction = "";
			hide();
		}	
	}

	private void setNames(String nmSet){
		 cbFunction.removeAllItems();
		 if ((nmSet!=null) && !nmSet.isEmpty()){
			 //aSet = db.getAllNameFunction(type, nmSet);
			 aSet = env.getAllNameFunction(nmSet);
			 for (int i = 0; i < aSet.size(); i++){
				 cbFunction.addItem((String)aSet.get(i));
			 }
		 }
	}
}
