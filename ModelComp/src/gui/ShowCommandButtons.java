package gui;


import javax.swing.*;

import java.util.*;
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
	JButton add;
	JButton addAs ;
	JButton up ;
	JButton down ;
	JButton rename;
	JButton insert ;
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModels owner){
	//ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModelAll owner, ShowMenu frame){
		//сформувати необхідні gui-елементи 
		JButton test = new JButton("Перевірка");
		add = new JButton("Нова");
		addAs = new JButton("Новий на основі");
		JButton edit = new JButton("Редагувати");
		JButton delete = new JButton("Вилучити");
		up = new JButton("Перемістити вверх");
		down = new JButton("Перемістити вниз");
		rename = new JButton("Переіменувати");
		insert = new JButton("Вставити програму");
		
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
		//buttons.add(addAs);
		//buttons.add(Box.createHorizontalStrut(5));
		buttons.add(edit); 
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(delete);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(up);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(down); 
		buttons.add(rename);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(insert);
		buttons.add(Box.createHorizontalStrut(5));
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
		addAs.addActionListener(new LsAddAs());
		rename.addActionListener(new LsRename());
		insert.addActionListener(new LsInsert());	
	}
	
	public void setModel(String type, Model model){
		boolean isVisible = type.equals("Machine");
		this.type = type;
		this.model = model;
		
		add.setText(Model.title(type, 9));
		addAs.setVisible(isVisible);
		up.setVisible(!isVisible);
		down.setVisible(!isVisible);
		rename.setVisible(isVisible);
		insert.setVisible(isVisible);
		
	}	
	
	// Класи слухачі 
	class LsTesting implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				//System.out.println("LsTesting : " + model.name + " " + model.getMain() + " " +  model.getAdd() );
				String[] text= model.iswfModel();
				if (text != null) JOptionPane.showMessageDialog(ShowCommandButtons.this,text);
			}
		}	
	}
	class LsAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				//if (type.equals("Machine")){
				//	return;
				//}
				Rule rule;
				int id = table.selectedRule();
				int idNew;
				//if (row == 0) rule = new Rule("","",false,"====");
				//else rule = (Rule)model.program.get(row-1);
				//System.out.println("New.."+row + ".."+rule.show());
				workCommand.setCommand("Add", type, model, id);
				workCommand.show();
				command= workCommand.getCommand();
				if (command != null) { 
					//idNew = workCommand.showRule.getIdCom();
					db.newCommand(type, model, command);
					showMain.showModel(type, model.id);
					//System.out.println("New.."+row);
					table.showRow(false,model.findCommand(command.getId())+1);
				}
			}
		}  
	}
	
	class LsEdit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				Rule rule;
				int row = table.selectedRule();
				//System.out.println("ShowCommandButton ..Edit"+row);
				if (row > 0){
					//	rule = (Rule)model.asub.get(row-1);
					//	workRule.setInit("edit", algo,row, rule);
					//	workRule.show();
					//	rule= workRule.getRule();
					workCommand.setCommand("Edit", type, model, row);
					workCommand.show();
					command= workCommand.getCommand();
				
					if (command != null) { 
						//System.out.println("New.."+command.output());
						db.editCommand(type, model, row, command);
						showMain.showModel(type, model.id);
						//model = db.getModel(type, model.id);
						//table.showModel(type, model);
					}
				}
			}
		
		}	
	}
	class LsDelete implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		if (model != null) {
			int row = table.selectedRule();
			//System.out.println(row);
			if (row > 0){
				/*Rule rule = (Rule)model.program.get(row-1);
				String text1;
				if (rule.getisEnd()) text1 = "заключна: " ; else  text1 = "             : ";
				text1 = text1 + "\"" + rule.getsLeft() + " -> " + rule.getsRigth() + "\"";
				String text2 = "Коментар: \"" + rule.gettxComm() + "\"";
				String [] text = new String[] {"Вилучити підстановку з номером  " + row + "..",  text1, text2} ; */
				//int row1 = (type.equals("Algorithm")? row-1: ((Machine)model).findCommand(row)) ;
				//int row1 = table.selectedRule();
				//System.out.println("LsDelet row1= " + row1);
				int row1 = model.findCommand(row);
				Command c = (Command) model.program.get(row1);                 //get(row1-1);
				String [] text = new String[] {"Вилучити " + Model.title(type, 11) + " з номером " + row + "..",  c.show(model.getAllChar())} ;
				UIManager.put("OptionPane.yesButtonText", "Так");
				UIManager.put("OptionPane.noButtonText", "Ні");	
				int response = 
						JOptionPane.showConfirmDialog(ShowCommandButtons.this,text, "Вилучити ?",JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) { 
					db.deleteCommand(type, model.id, row,c);
					showMain.showModel(type, model.id);
					//table.showPrevRow(false);
					table.showRow(false,row1);
				} 	
			}
		}
		//	JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDelete...");
		}	
	} 
	class LsUp implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				int row = table.numberSelectedRow();
				if (row > 1) {
					db.moveUp(type, model, row);
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
				int row = table.numberSelectedRow();
				if ((row > 0) && (row < model.program.size())){
					db.moveDown(type, model, row);
					showMain.showModel(type, model.id);
					table.showNextRow(false);
				}
			} 
			//JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDown...");
		}	
	}
 	
 	class LsRename implements ActionListener  {
 		String init;
 		JTextField newState;
		public void actionPerformed(ActionEvent event){
			if (model != null){
				String name = table.selectedName();
				String text = "";
				if (!name.isEmpty()) {
					// формуємо Діалогове вікно "Переіменування стану"
					 String[] textButtons = {"Переіменувати", "Вийти"};
					 JPanel panel = new JPanel();
					 init = ((Machine)model).newState();
					 panel.add(new JLabel("Змінити імя стану " + name + " на "));
					 newState = new JTextField(3);
					 newState.setText(init);
					 panel.add(newState);
					 newState.addActionListener(new LsText());	
					 int res = JOptionPane.showOptionDialog(ShowCommandButtons.this, panel, "Переіменування стану",
							 		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
					 if (res == JOptionPane.OK_OPTION){
						String name1 = newState.getText();
						if (!((Machine)model).isState(name1)) {
							//db.moveUp(type, model.id, row);
							//showMain.showModel(type, model.id);
							//table.showPrevRow(false);
							model.dbRenameState(name, name1);
							showMain.showModel(type, model.id);
							table.showRow(false,model.findCommand(name1)+1);
							//JOptionPane.showMessageDialog(ShowCommandButtons.this, "Rename.." + name + " -> " + name1);
						}
						else text = "Програма машини " + model.name + " вже використовує стан " + name1 + " !";
					 }
					 if(!text.isEmpty())  JOptionPane.showMessageDialog(ShowCommandButtons.this, text);
				}
			} 
		}
		// клас-обробник введення інформації в полі  newState
		class LsText implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				String name1 = newState.getText();
				if (((Machine)model).isState(name1)) {
					JOptionPane.showMessageDialog(ShowCommandButtons.this, 
							"Програма машини " + model.name + " вже використовує стан " + name1 + " !");
					newState.setText(init);
				}	
			}
		}
		
	}
 	
 	class LsInsert implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		/*	if (model != null){
				int row = table.selectedRule();
				if (row > 1) {
					db.moveUp(type, model.id, row);
					showMain.showModel(type, model.id);
					table.showPrevRow(false);
				}
			} */
			String text;
			ArrayList <String> all = db.getAllModel(type);
			String name1;
			int j;
			String[] values = new String [all.size() - 1];
			j = 0;
			for (int i = 0; i < all.size(); i++){
				name1 = all.get(i);
				if(!name1.equals(model.name)) {values[j] = name1; j++;}
			}
			//ImageIcon icon = new ImageIcon("c:\\iconimage\\msg_admin.gif");
			UIManager.put("OptionPane.okButtonText", "Так");
			UIManager.put("OptionPane.cancelButtonText", "Вийти");
			Object res = 
					JOptionPane.showInputDialog(ShowCommandButtons.this, 
							"Вставити програму машини", "Вибір програми", JOptionPane.QUESTION_MESSAGE, null,  values, values[0]);
			//res = JOptionPane.showInputDialog
			//JOptionPane.showMessageDialog(InputDialogs.this,res);
			text = model.dbInsertModel((String)res);	
			if (text.isEmpty()) showMain.showModel(type, model.id);
			else JOptionPane.showMessageDialog(ShowCommandButtons.this,text);
		}	
	}
	
 	
 	class LsAddAs implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				int row = table.selectedRule();
				if (row > 1) {
					JOptionPane.showMessageDialog(ShowCommandButtons.this,"New As..." + row);
					//db.moveUp(type, model.id, row);
					//showMain.showModel(type, model.id);
					//table.showPrevRow(false);
				}
			
			}
		}	
	} 	
}
