package gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import db.*;
import main.*;

public class ShowCommandButtons extends JPanel {
	private DbAccess db;
	private String type = "Algorithm";
	private ShowModelTable table;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	//private ShowModelAll showMain;
	private Model model = null;
	private ShowCommand workCommand;
	private Command command;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModels owner){
	//ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModelAll owner, ShowMenu frame){
		//сформувати необхідні gui-елементи 
		JButton test = new JButton("Перевірка");
		JButton add = new JButton("Нова");
		JButton edit = new JButton("Редагувати");
		JButton delete = new JButton("Вилучити");
		JButton up = new JButton("Перемістити вверх");
		JButton down = new JButton("Перемістити вниз");
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		workCommand = new ShowCommand(owner);
		//workCommand = new ShowCommand(frame);
		this.db = db;
		this.table = table;
		showMain = owner;
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Box buttons = Box.createHorizontalBox();
		buttons.add(test);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(add);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(edit); 
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(delete);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(up);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(down); 
		//----------------------------------
		add(Box.createVerticalStrut(3));
		add(buttons);	
		add(Box.createVerticalStrut(5));
		// встановити слухачів !!!			
		test.addActionListener(new LsTesting());
		add.addActionListener(new LsAdd());
		edit.addActionListener(new LsEdit());
		delete.addActionListener(new LsDelete());
		up.addActionListener(new LsUp());
		down.addActionListener(new LsDown());
			
	}
	
	public void setModel(String type, Model model){
		this.type = type;
		this.model = model;
	}	
	
	// Класи слухачі 
	class LsTesting implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				String[] text= model.testingRules();
				if (text != null) JOptionPane.showMessageDialog(ShowCommandButtons.this,text);
			}
		}	
	}
	class LsAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				Rule rule;
				int row = table.selectedRule();
				//if (row == 0) rule = new Rule("","",false,"====");
				//else rule = (Rule)model.program.get(row-1);
				//System.out.println("New.."+row + ".."+rule.show());
				workCommand.setCommand("Add", model, row);
				workCommand.show();
				command= workCommand.getCommand();
				if (command != null) { 
					db.newCommand(type, model.id, row, command);
					showMain.showModel(type, model.id);
					//System.out.println("  row = " + row);
					table.showNextRow(false);
					/*model = db.getModel(type, model.id);
					if (row == 0) table.showFirstRow(true);
					else table.showNextRow(true);*/
				}
			}
		}  
	}
	
	class LsEdit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		if (model != null) {
			Rule rule;
			int row = table.selectedRule();
			if (row > 0){
			//	rule = (Rule)model.asub.get(row-1);
			//	workRule.setInit("edit", algo,row, rule);
			//	workRule.show();
			//	rule= workRule.getRule();
				workCommand.setCommand("Edit", model, row);
				workCommand.show();
				command= workCommand.getCommand();
				if (command != null) { 
					db.editCommand(type, model.id, row, command);
					showMain.showModel(type, model.id);
					//model = db.getModel(type, model.id);
					//table.showModel(type, model);
				}
			}
		}
		//	JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleEdit...");
		}	
	}
	class LsDelete implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		if (model != null) {
			int row = table.selectedRule();
			if (row > 0){
				Rule rule = (Rule)model.program.get(row-1);
				//Command c = (Command) model.program.get(row-1);
				String text1;
				if (rule.getisEnd()) text1 = "заключна: " ; else  text1 = "             : ";
				text1 = text1 + "\"" + rule.getsLeft() + " -> " + rule.getsRigth() + "\"";
				String text2 = "Коментар: \"" + rule.gettxComm() + "\"";
				String [] text = new String[] {"Вилучити підстановку з номером  " + row + "..",  text1, text2} ;
				UIManager.put("OptionPane.yesButtonText", "Так");
				UIManager.put("OptionPane.noButtonText", "Ні");	
				int response = 
						JOptionPane.showConfirmDialog(ShowCommandButtons.this,text, "Вилучити ?",JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) { 
					db.deleteCommand(type, model.id, row);
					showMain.showModel(type, model.id);
					table.showPrevRow(false);
				}	
			}
		}
		//	JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDelete...");
		}	
	} 
	class LsUp implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				int row = table.selectedRule();
				if (row > 1) {
					db.moveUp(type, model.id, row);
					showMain.showModel(type, model.id);
					table.showPrevRow(false);
				}
			} 
			//JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleUp...");
		}	
	}
 	class LsDown implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				//int all = model.program.size();
				int row = table.selectedRule();
				if ((row > 0) && (row < model.program.size())){
					db.moveDown(type, model.id, row);
					showMain.showModel(type, model.id);
					table.showNextRow(false);
				}
			} 
			//JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDown...");
		}	
	}
	
}
