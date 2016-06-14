package gui.model;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import db.*;
import gui.*;
import gui.command.*;
import gui.eval.*;
import gui.model.*;
import main.*;

public class PnComButtons extends JPanel {
	//private DbAccess db;
	private String type = "Algorithm";
	private AllModels env=null;
	
	private PnComTable pComTable;
	private PnDescription pDescription;
	private FrMain fMain;
	private Model model = null;
	private DgEdCommand dEdCommand;
	private Command command;
	private DgInsert dInsert; 
	
	private JLabel selection;
	//private JButton test;
	private JButton add;
	//private JButton addAs ;
	private JButton up ;
	private JButton down ;
	private JButton rename;
	private JButton insert ;
	private JButton see;
		
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public PnComButtons(DbAccess db){                             
		//сформувати необхідні gui-елементи 
		selection = new JLabel("  0:0 " );  ///+ dbm.getRowCount());  
		JButton first = new JButton("|<");
		first.setMaximumSize(new Dimension(20,20));
		JButton prev = new JButton("<");
		prev.setMaximumSize(new Dimension(20,20));
		JButton next = new JButton(">");
		next.setMaximumSize(new Dimension(20,20));
		JButton last = new JButton(">|");
		last.setMaximumSize(new Dimension(20,20));
		//test = new JButton("Перевірка");
		add = new JButton("Нова");
		//addAs = new JButton("Новий на основі");
		JButton edit = new JButton("Редагувати");
		JButton delete = new JButton("Вилучити");
		up = new JButton("Перемістити вверх");
		down = new JButton("Перемістити вниз");
		rename = new JButton("Переіменувати");
		insert = new JButton("Вставити програму");
		see = new JButton("Переглянути");
		//eval = new JButton("Виконати");
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		dEdCommand = new DgEdCommand(fMain);
		dInsert = new DgInsert(fMain);
		
		//this.db = db;
		
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		//---------------------------
		Box select = Box.createHorizontalBox();
		select.add(Box.createHorizontalStrut(5));
		select.add(first); select.add(prev); 
		select.add(selection);
		select.add(next); 
		select.add(last);
		select.add(Box.createHorizontalGlue());
		//-----------------------------
		Box buttons = Box.createHorizontalBox();
		//buttons.add(test);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(add);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(edit); 
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(delete);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(see);
		buttons.add(up);
		buttons.add(Box.createHorizontalStrut(5));
		//buttons.add(eval);
		buttons.add(down); 
		buttons.add(rename);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(insert);
		buttons.add(Box.createHorizontalStrut(5));
		//----------------------------------
		add(Box.createVerticalStrut(3));
		add(select);
		add(buttons);	
		add(Box.createVerticalStrut(5));
		
		// встановити слухачів !!!	
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		
		//test.addActionListener(new LsTesting());
		add.addActionListener(new LsAdd());
		edit.addActionListener(new LsEdit());
		delete.addActionListener(new LsDelete());
		//delete.addActionListener(new LsInsertLumbda());
		up.addActionListener(new LsUp());
		down.addActionListener(new LsDown());
		//addAs.addActionListener(new LsAddAs());
		rename.addActionListener(new LsRename());
		//insert.addActionListener(new LsInsert());
		insert.addActionListener(new LsInsertNew());
	}
	
	public void setEnv(FrMain owner, PnDescription pDescription, PnComTable table){   
		fMain = owner;  
		this.pComTable = table;
		this.pDescription = pDescription;
	}         	
	
	public void setModel(String type, Model model){
		/*
		boolean isVisible = type.equals("Machine");
		boolean rec = type.equals("Recursive") ;
		boolean comp = type.equals("Computer");
		this.type = type;
		this.model = model;
		
		//test.setVisible(!comp);
		add.setText(Model.title(type, 9));
		//addAs.setVisible(isVisible);
		up.setVisible(!isVisible && !rec);
		down.setVisible(!isVisible && !rec);
		rename.setVisible(isVisible);
		insert.setVisible(isVisible || comp || rec || type.equals("Calculus"));
		see.setVisible(false);
		*/
	}	
	
	public void show(AllModels env){
		String text = "програму";
		this.env = env; 
		type = env.getType();
		model = env.getModel();
		boolean isVisible = type.equals("Machine");
		boolean rec = type.equals("Recursive") ;
		boolean comp = type.equals("Computer");
		boolean calc = type.equals("Calculus");
		add.setText(Model.title(type, 9));
		if(rec) text = "функцію";
		if(calc) text = "вираз";
		insert.setText("Вставити "+ text);
		up.setVisible(!isVisible && !rec);
		down.setVisible(!isVisible && !rec);
		rename.setVisible(isVisible);
		insert.setVisible(isVisible || comp || rec || type.equals("Calculus"));
		see.setVisible(false);
		//System.out.println("PnCommButtons: show="+env.getType()+".."+env.getPos());
	}
	public void setSelection(String txt){
		selection.setText(txt);
	}
	
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			if(dInsert != null) SwingUtilities.updateComponentTreeUI(dInsert);
			if(dEdCommand != null) SwingUtilities.updateComponentTreeUI(dEdCommand);
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	// Класи слухачі 
	class SelectFirst implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			pComTable.showTable(false, 1);
		}	
	}
	class SelectPrev implements ActionListener  {
		public void actionPerformed(ActionEvent event){	
			pComTable.showPrevRow(false);	
		}	
	}
	class SelectNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){	
			pComTable.showNextRow(false);	
		}	
	}
	class SelectLast implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			int selected = pComTable.getCountRows();
			pComTable.showTable(false, selected);   //dbm.getRowCount());
		}	
	}
	/*	
	class LsTesting implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if (pDescription.testAndSave()){
					String[] text= model.iswfModel();
					if (text != null) JOptionPane.showMessageDialog(PnComButtons.this,text);
				}	
			}
		}	
	}
	*/
	class LsAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				if (pDescription.testAndSave()){
					Rule rule;
					int id = pComTable.selectedRule();
					int idNew;
					dEdCommand.setCommand("Add", type, model, id);
					//System.out.println("ShowCommandButton:LsAdd "  + type + " " + id);
					dEdCommand.show();
					command= dEdCommand.getCommand();
					if (command != null) { 
						//db.newCommand(type, model, command);
						//fMain.setModel(type, model.id);
						env.newCommand(command);
						pComTable.showRow(false,model.findCommand(command.getId())+1);
					}
				}
			}
		}  
	}
	
	class LsEdit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				if (pDescription.testAndSave()){
					int row = pComTable.selectedRule();
					if (row > 0){
						dEdCommand.setCommand("Edit", type, model, row);
						dEdCommand.show();
						command= dEdCommand.getCommand();
				
						if (command != null) { 
							//db.editCommand(type, model, row, command);
							//fMain.setModel(type, model.id);
							env.editCommand(row, command);
						}
					}
				}
			}
		}	
	}

	class LsDelete implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				if (pDescription.testAndSave()){
					int row = pComTable.selectedRule();
					if (row > 0){
						int row1 = model.findCommand(row);
						Command c = (Command) model.program.get(row1);                 //get(row1-1);
						String [] text = new String[] {"Вилучити " + Model.title(type, 11) + " з номером " + row + ". ",  c.show(model.getAllChar())} ;
						if (type.equals("Recursive")){
							Function f = (Function)c;
							String using = ((Recursive)model).usingName(f.getName());
							if (!using.isEmpty()){
								text = new String[] {"Вилучити " + Model.title(type, 11) + " з номером " + row + ". ",  c.show(model.getAllChar()),
										"Функція використовується в означенні: ", "<" + using + ">."} ;
							}
						}
						UIManager.put("OptionPane.yesButtonText", "Так");
						UIManager.put("OptionPane.noButtonText", "Ні");	
						int response = 
								JOptionPane.showConfirmDialog(PnComButtons.this,text, "Вилучити ?",JOptionPane.YES_NO_OPTION);
						if (response == JOptionPane.YES_OPTION) { 
							//db.deleteCommand(type, model.id, row,c);
							//fMain.setModel(type, model.id);
							env.deleteCommand(row, c);
							pComTable.showRow(false,row1);
						} 	
					}
				}
			}
		}	
	} 
	class LsUp implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if (pDescription.testAndSave()){
					int row = pComTable.numberSelectedRow();
					if (row > 1) {
						//db.moveUp(type, model, row);
						//fMain.setModel(type, model.id);
						env.moveUpCommand(row);
						pComTable.showPrevRow(false);
					}
				}
			} 
		}	
	}
 	class LsDown implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if (pDescription.testAndSave()){
					int row = pComTable.numberSelectedRow();
					if ((row > 0) && (row < model.program.size())){
						//db.moveDown(type, model, row);
						//fMain.setModel(type, model.id);
						env.moveDownCommand(row);
						pComTable.showNextRow(false);
					}
				}
			} 
		}	
	}
 	
 	class LsRename implements ActionListener  {
 		String init;
 		JTextField newState;
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if (pDescription.testAndSave()){
					String name = pComTable.selectedName();
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
						int res = JOptionPane.showOptionDialog(PnComButtons.this, panel, "Переіменування стану",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
						if (res == JOptionPane.OK_OPTION){
							String name1 = newState.getText();
							if (!((Machine)model).isState(name1)) {
								//model.dbRenameState(name, name1);
								//fMain.setModel(type, model.id);
								env.renameStateMachine(name, name1);
								pComTable.showRow(false,model.findCommand(name1)+1);
							}
							else text = "Програма машини " + model.name + " вже використовує стан " + name1 + " !";
						}
						if(!text.isEmpty())  JOptionPane.showMessageDialog(PnComButtons.this, text);
					}
				}
			} 
		}
		// клас-обробник введення інформації в полі  newState
		class LsText implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				String name1 = newState.getText();
				if (((Machine)model).isState(name1)) {
					JOptionPane.showMessageDialog(PnComButtons.this, 
							"Програма машини " + model.name + " вже використовує стан " + name1 + " !");
					newState.setText(init);
				}	
			}
		}
	}
  
 	class LsInsertNew implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String nmModel = "", nmFunction="";
			if (model != null) {
				if (pDescription.testAndSave()){
					int row = pComTable.numberSelectedRow();
					dInsert.setInsert(type, model, env, row);
					dInsert.show();
					nmModel = dInsert.getNmModel();
					nmFunction = dInsert.getNmFunction();
					if ((nmModel !=null) &&!nmModel.isEmpty()){
						/*
						String text = "Insert Model=" + nmModel + "..Function=" + nmFunction + " after=" + row;
						switch(type){
						case "Computer" : text = model.dbInsertModel(row, nmModel);  break;
						case "Machine"  : text = model.dbInsertModel(nmModel);  break;
						case "Recursive":  
						case "Calculus" : text = model.dbInsertModel(row, nmModel, nmFunction); break;
						default: break;
						}	
						if (text.isEmpty()) fMain.setModel(type, model.id);
						else JOptionPane.showMessageDialog(PnComButtons.this,text);
						*/
						String text = env.insertModel(row, nmModel, nmFunction);
						if (!text.isEmpty()) JOptionPane.showMessageDialog(PnComButtons.this,text);
					}
				}
			}
		}	
	}
 	
 	/*
 	  Перша версія вставки програми -- працююча 
 	  --- замінена на більш універсальну 
 	class LsInsert implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (pDescription.testAndSave()){
				int row = 0;
				String sWhere = "";
				if (model == null) return;
				if (type.equals("Computer")){
					row = pComTable.numberSelectedRow();
					if (row == 0) return;
					sWhere = "після команди " + row + " "; 
				}
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
				UIManager.put("OptionPane.okButtonText", "Так");
				UIManager.put("OptionPane.cancelButtonText", "Вийти");
				Object res = 
					JOptionPane.showInputDialog(PnComButtons.this, 
							"Вставити " + sWhere + "програму машини", "Вибір програми", JOptionPane.QUESTION_MESSAGE, null,  values, values[0]);
				if(res != null){
					if (type.equals("Computer"))
						text = model.dbInsertModel(row, (String)res);
					else text = model.dbInsertModel((String)res);	
					if (text.isEmpty()) fMain.setModel(type, model.id);
					else JOptionPane.showMessageDialog(PnComButtons.this,text);
				}
			}
		}	
	}	
 	*/
 	 	 	 	
   /*	
 	Перша версія створення діалогового вікна напряму!!!! Через створення панелі, котру потім вставляємо
 	  ---- проблеми взаємодії двох випадаючих списків -- похоже незалежна !!!
	class LsInsertLumbda implements ActionListener  {
 		String init;
 		JTextField newState;
 		private JComboBox cbSet;
 		private JComboBox cbName;
 		private JLabel what;
 		ArrayList aSet; 
 	 	
		public void actionPerformed(ActionEvent event){
			if (model != null){
				String name = pComTable.selectedName();
				String text = "";
				String fstSet ="";
				int id=0;
				aSet = db.getAllModel(type);
				if (aSet.size()>1) {    
					// формуємо Діалогове вікно "Select function"
					 String[] textButtons = {"Створити копію", "Вийти"};
					 JPanel panel = new JPanel();
					 panel.add(new JLabel("Набір "));
					 cbSet = new JComboBox();
					 for (int i = 0; i < aSet.size(); i++){
							String name1 = (String)aSet.get(i);
							if(!name1.equals(model.name)) {
								cbSet.addItem(name1);
								if(fstSet.isEmpty()) fstSet = name1;
							}
					 }
					 panel.add(cbSet);
					
					 what = new JLabel("Expr ");
					 panel.add(what);
					 cbName = new JComboBox();
					 setNames(fstSet);
					 panel.add(cbName);
			       	 cbSet.addActionListener(new LsSet());
					 int res = JOptionPane.showOptionDialog(PnComButtons.this, panel, "Select function",
							 		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
					 if (res == JOptionPane.OK_OPTION){
						String nmSet = (String)cbSet.getSelectedItem();
						String nmFunc = (String)cbName.getSelectedItem();
						text = "select " + nmSet + "  ==  " + nmFunc; 
					 }
					 if(!text.isEmpty())  JOptionPane.showMessageDialog(PnComButtons.this, text);
				}
			} 
		}
		// клас-обробник введення інформації в полі  cbSet
		class LsSet implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				setNames((String)cbSet.getSelectedItem());
				
			}
		}
		private void setNames(String nmSet){
			 aSet = db.getAllNameFunction(type, nmSet);
			 cbName.removeAllItems();
			 for (int i = 0; i < aSet.size(); i++){
				cbName.addItem((String)aSet.get(i));
			 }
		}
	}
 
	*/
	
	/*
	    // Друга версія варіантів створення діалогового вікна -- вставка панелі в  JOptionPane.showOptionDialog
	    // ... в цілому працює -- є проблеми взаємодії двох випадаючих списків !!! -- похоже це незалежна проблема
	//private PnSelectFunction panel;
	//panel = new PnSelectFunction();
	   
	class LsInsertLumbda1 implements ActionListener  {
 		public void actionPerformed(ActionEvent event){
 			String text = "";
			if (model != null){
				// формуємо Діалогове вікно "Select function"
				String[] textButtons = {"Створити копію", "Вийти"};
				panel.setPanel(db,type, model.name);
				text = "";
				int res = JOptionPane.showOptionDialog(PnComButtons.this, panel, "Select function",
							 		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
				if (res == JOptionPane.OK_OPTION){
						String nmSet = panel.getSet();
						String nmFunc = panel.getFunction();
						text = "select " + nmSet + "  ==  " + nmFunc; 
					 }
				if(!text.isEmpty())  JOptionPane.showMessageDialog(PnComButtons.this, text);
			} 
		}
	}
	
	class PnSelectFunction extends JPanel {
		private DbAccess db;
		private String type;
		private JComboBox cbSet;
 		private JComboBox cbName;
 		private JLabel what;
 		ArrayList aSet, aSet1; 
 		
		PnSelectFunction(){
			add(new JLabel("Набір "));
			cbSet = new JComboBox();
			add(cbSet);
			what = new JLabel("Expr ");
			add(what);
			cbName = new JComboBox();
			add(cbName);
		}
		public void setPanel(DbAccess db, String type, String nmModel){
			this.db = db;
			this.type = type;
			String fstSet="";
		
			cbSet.removeAllItems();
			aSet = db.getAllModel(type);
			for (int i = 0; i < aSet.size(); i++){
				String name1 = (String)aSet.get(i);
				if(!name1.equals(nmModel)) {
					cbSet.addItem(name1);
					if(fstSet.isEmpty()) fstSet = name1;
				}
			}
			setNames(fstSet);
			cbSet.addActionListener(new LsSetInt());
		}
		public String getSet(){
			return (String)cbSet.getSelectedItem();
		}
		public String getFunction(){
			return (String)cbName.getSelectedItem();
		}
		private void setNames(String nmSet){
			 aSet1 = db.getAllNameFunction(type, nmSet);
			 System.out.println("PnComButtons:PnSelectFunction: setNames " + nmSet + " " + aSet1.size()); 
			 cbName.removeAllItems();
			 for (int i = 0; i < aSet1.size(); i++){
				cbName.addItem((String)aSet1.get(i));
			 }
		}
		class LsSetInt implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				setNames((String)cbSet.getSelectedItem());
			}
		}
	}
	*/
}
