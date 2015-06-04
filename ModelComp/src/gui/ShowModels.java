package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import db.*;
import main.*;


public class ShowModels extends JFrame {
	private DbAccess db;
	private String type = "Algorithm";
	private Model model = null;
	private JLabel label;
	private ShowModelOne showModel;
	private ShowModelButtons modelButtons;
	
	private ShowTesting sTest;
	
	public ShowModels(DbAccess db){
		super("Models of computation");
		this.db = db;
		//  сформувати необхідні gui-елементи 
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("Вийти з програми");
		menuBar.add(createModelMenu());
		menuBar.add(quit);
		quit.addActionListener(new Quit());
		setJMenuBar(menuBar);
		
		label = new JLabel("Нормальні алгоритми Маркова");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showModel = new ShowModelOne(db, this);
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		modelButtons = new ShowModelButtons(db,this);
		//===============================
		// розташуємо їх
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(showModel, BorderLayout.CENTER);
		getContentPane().add(modelButtons, BorderLayout.SOUTH);
		setVisiblePane(false);
				//label.setVisible(false);
				//showModel.setVisible(false);
				//modelButtons.setVisible(false);
		
		
		//sTest =new ShowTesting();
		//getContentPane().add(sTest, BorderLayout.NORTH);
		
		setSize(800,500);
		//pack();
	}	
	
	private void setVisiblePane(boolean isVisible) {
		label.setVisible(isVisible);
		showModel.setVisible(isVisible);
		modelButtons.setVisible(isVisible);
	}
	
	public void showModel(String type, int id) {
		//System.out.println("ShowModel start ..." + type + "  " + id + " " + this.type);
		setVisiblePane(!type.equals("NoModel"));
		if (!type.equals(this.type)) {
			// set label !!!!!!!!
			String text = Model.title(type, 1);
			if (text.isEmpty()) text = "Not realise " + type + " !";
			label.setText(text);
			this.type = type;
			/*
			String text;
			switch(type){
			case "Algorithm" : text = "Нормальні алгоритми Маркова"; break;
			case "Machine" : text = "Машини Тьюрінга"; break;
			default: text = "Not realise " + type + " !";
			} */
		}
		if (id == 0) model = null; else model = db.getModel(type, id);
		showModel.setModel(type,model);
		modelButtons.setModel(type,model);
	}
	
	private JMenu createModelMenu () {
		// створюємо випадаюче меню, що містить звичайні елементи меню
		JMenu model= new JMenu("Модель обчислень");
		// елемент меню - команда
		JMenuItem algorithm = new JMenuItem(new AlgorithmAction()); // ("Open", new ImageIcon("images\\icon_arrow.gif"));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  //("Машини Тьюрінга"); 
		machine.addActionListener(new LsMachine());
		
		model.add(algorithm);
		model.add(machine);
		return model;
		// розподільник
		//file.addSeparator();
	}
	
	class AlgorithmAction extends AbstractAction {
		AlgorithmAction() {putValue(NAME,Model.title("Algorithm", 1));}   //"Нормальні алгоритми Маркова"
		public void actionPerformed(ActionEvent e) {
			showModel("Algorithm",0);
		}
	}
	class LsMachine implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			showModel("Machine",0);
		  // JOptionPane.showMessageDialog(ShowModels.this,"Machine.");
		}	
	}
	
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	
	/*class MachineAction extends AbstractAction {
		MachineAction() {putValue(NAME,Model.title("Machine", 1));}  // "Машини Тьюрінга"
		public void actionPerformed(ActionEvent e) {
			showModel("Machine",0);
			  //	JOptionPane.showMessageDialog(ShowModels.this,"Machine..");
		}
	} */
	
	


}
