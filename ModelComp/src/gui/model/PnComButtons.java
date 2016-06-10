package gui.model;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import db.*;
import gui.FrMain;
import gui.command.DgEdCommand;
import gui.eval.DgEval;
import gui.model.*;
import main.*;

public class PnComButtons extends JPanel {
	private DbAccess db;
	private String type = "Algorithm";
	private PnComTable pComTable;
	private PnDescription pDescription;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private FrMain fMain;
	private Model model = null;
	private DgEdCommand dEdCommand;
	private Command command;
	//private DgEval showWork; 
	
	private JLabel selection;
	
	private JButton test;
	private JButton add;
	private JButton addAs ;
	private JButton up ;
	private JButton down ;
	private JButton rename;
	private JButton insert ;
	private JButton see;
	
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public PnComButtons(DbAccess db){                              //!!!!!!!!!!!!! ref!!!!!!!!!!!!!!!!!!		
		//���������� �������� gui-�������� 
		selection = new JLabel("  0:0 " );  ///+ dbm.getRowCount());  !!!!!!!!!!!!!!!!!!
		JButton first = new JButton("|<");
		first.setMaximumSize(new Dimension(20,20));
		JButton prev = new JButton("<");
		prev.setMaximumSize(new Dimension(20,20));
		JButton next = new JButton(">");
		next.setMaximumSize(new Dimension(20,20));
		JButton last = new JButton(">|");
		last.setMaximumSize(new Dimension(20,20));
		test = new JButton("��������");
		add = new JButton("����");
		addAs = new JButton("����� �� �����");
		JButton edit = new JButton("����������");
		JButton delete = new JButton("��������");
		up = new JButton("���������� �����");
		down = new JButton("���������� ����");
		rename = new JButton("������������");
		insert = new JButton("�������� ��������");
		see = new JButton("�����������");
		//eval = new JButton("��������");
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		dEdCommand = new DgEdCommand(fMain);
		//showWork = new DgEval(fMain);
		
		this.db = db;
		//=================================
		// ������� ���������
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
		// ���������� �������� !!!	
		
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		
		test.addActionListener(new LsTesting());
		add.addActionListener(new LsAdd());
		edit.addActionListener(new LsEdit());
		delete.addActionListener(new LsDelete());
		up.addActionListener(new LsUp());
		down.addActionListener(new LsDown());
		addAs.addActionListener(new LsAddAs());
		rename.addActionListener(new LsRename());
		insert.addActionListener(new LsInsert());
		//eval.addActionListener(new LsEval());	
	}
	
	public void setEnv(FrMain owner, PnDescription pDescription, PnComTable table){     // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		fMain = owner;  
		this.pComTable = table;// !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		this.pDescription = pDescription;
	}         	
	/*
	public void setTable(PComTable table){
		this.table = table;
	}
	*/
	public void setModel(String type, Model model){
		boolean isVisible = type.equals("Machine");
		boolean rec = type.equals("Recursive") ;
		boolean comp = type.equals("Computer");
		this.type = type;
		this.model = model;
		
		test.setVisible(!comp);
		add.setText(Model.title(type, 9));
		addAs.setVisible(isVisible);
		up.setVisible(!isVisible && !rec);
		down.setVisible(!isVisible && !rec);
		rename.setVisible(isVisible);
		insert.setVisible(isVisible || comp);
		see.setVisible(false);
	//	eval.setVisible(rec  || type.equals("Calculus"));
	}	
	
	public void setSelection(String txt){
		selection.setText(txt);
	}
	
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			//if(showWork != null) SwingUtilities.updateComponentTreeUI(showWork);
			if(dEdCommand != null) SwingUtilities.updateComponentTreeUI(dEdCommand);
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	// ����� ������� 
	
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
						db.newCommand(type, model, command);
						fMain.setModel(type, model.id);
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
							db.editCommand(type, model, row, command);
							fMain.setModel(type, model.id);
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
						String [] text = new String[] {"�������� " + Model.title(type, 11) + " � ������� " + row + ". ",  c.show(model.getAllChar())} ;
						if (type.equals("Recursive")){
							Function f = (Function)c;
							String using = ((Recursive)model).usingName(f.getName());
							if (!using.isEmpty()){
								text = new String[] {"�������� " + Model.title(type, 11) + " � ������� " + row + ". ",  c.show(model.getAllChar()),
										"������� ��������������� � ��������: ", "<" + using + ">."} ;
							}
						}
						UIManager.put("OptionPane.yesButtonText", "���");
						UIManager.put("OptionPane.noButtonText", "ͳ");	
						int response = 
								JOptionPane.showConfirmDialog(PnComButtons.this,text, "�������� ?",JOptionPane.YES_NO_OPTION);
						if (response == JOptionPane.YES_OPTION) { 
							db.deleteCommand(type, model.id, row,c);
							fMain.setModel(type, model.id);
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
						//System.out.println("LsUp "+ row);
						db.moveUp(type, model, row);
						fMain.setModel(type, model.id);
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
						db.moveDown(type, model, row);
						fMain.setModel(type, model.id);
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
						// ������� ĳ������� ���� "������������� �����"
						String[] textButtons = {"������������", "�����"};
						JPanel panel = new JPanel();
						init = ((Machine)model).newState();
						panel.add(new JLabel("������ ��� ����� " + name + " �� "));
						newState = new JTextField(3);
						newState.setText(init);
						panel.add(newState);
						newState.addActionListener(new LsText());	
						int res = JOptionPane.showOptionDialog(PnComButtons.this, panel, "������������� �����",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
						if (res == JOptionPane.OK_OPTION){
							String name1 = newState.getText();
							if (!((Machine)model).isState(name1)) {
								model.dbRenameState(name, name1);
								fMain.setModel(type, model.id);
								pComTable.showRow(false,model.findCommand(name1)+1);
							}
							else text = "�������� ������ " + model.name + " ��� ����������� ���� " + name1 + " !";
						}
						if(!text.isEmpty())  JOptionPane.showMessageDialog(PnComButtons.this, text);
					}
				}
			} 
		}
		// ����-�������� �������� ���������� � ���  newState
		class LsText implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				String name1 = newState.getText();
				if (((Machine)model).isState(name1)) {
					JOptionPane.showMessageDialog(PnComButtons.this, 
							"�������� ������ " + model.name + " ��� ����������� ���� " + name1 + " !");
					newState.setText(init);
				}	
			}
		}
		
	}
 	
 	class LsInsert implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (pDescription.testAndSave()){
				int row = 0;
				String sWhere = "";
				if (model == null) return;
				if (type.equals("Computer")){
					row = pComTable.numberSelectedRow();
					if (row == 0) return;
					sWhere = "���� ������� " + row + " "; 
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
				UIManager.put("OptionPane.okButtonText", "���");
				UIManager.put("OptionPane.cancelButtonText", "�����");
				Object res = 
					JOptionPane.showInputDialog(PnComButtons.this, 
							"�������� " + sWhere + "�������� ������", "���� ��������", JOptionPane.QUESTION_MESSAGE, null,  values, values[0]);
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
	
 	
 	class LsAddAs implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if (pDescription.testAndSave()){
					int row = pComTable.selectedRule();
					if (row > 1) {
						JOptionPane.showMessageDialog(PnComButtons.this,"New As..." + row);
					}
				}
			}
		}	
	} 

}
