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
	private ShowModels showModels;
	//private ShowModelAll showMain;
	private Model model = null;
	private ShowCommand showCommand;
	private Command command;
	private ShowWork showWork; 
	
	private JButton test;
	private JButton add;
	private JButton addAs ;
	private JButton up ;
	private JButton down ;
	private JButton rename;
	private JButton insert ;
	private JButton see;
	private JButton eval;
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModels owner){
	//ShowCommandButtons(DbAccess db, ShowModelTable table, ShowModelAll owner, ShowMenu frame){
		//���������� ��������� gui-�������� 
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
		eval = new JButton("��������");
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showCommand = new ShowCommand(owner);
		showWork = new ShowWork(showModels);
		
		//workCommand = new ShowCommand(frame);
		this.db = db;
		this.table = table;
		showModels = owner;
		//=================================
		// ������� ���������
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
		buttons.add(see);
		buttons.add(up);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(eval);
		buttons.add(down); 
		buttons.add(rename);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(insert);
		buttons.add(Box.createHorizontalStrut(5));
		//----------------------------------
		add(Box.createVerticalStrut(3));
		add(buttons);	
		add(Box.createVerticalStrut(5));
		// ���������� �������� !!!			
		test.addActionListener(new LsTesting());
		add.addActionListener(new LsAdd());
		edit.addActionListener(new LsEdit());
		delete.addActionListener(new LsDelete());
		up.addActionListener(new LsUp());
		down.addActionListener(new LsDown());
		addAs.addActionListener(new LsAddAs());
		rename.addActionListener(new LsRename());
		insert.addActionListener(new LsInsert());
		eval.addActionListener(new LsEval());	
	}
	
	public void setModel(String type, Model model){
		boolean isVisible = type.equals("Machine");
		boolean rec = type.equals("Recursive");
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
		eval.setVisible(rec  || type.equals("Calculus"));
	}	
	
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			if(showWork != null) SwingUtilities.updateComponentTreeUI(showWork);
			if(showCommand != null) SwingUtilities.updateComponentTreeUI(showCommand);
			//ShowModels.this.pack();
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	// ����� ������� 
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
				//System.out.println("ShowCommandButtons:LsAdd id = "+id);
				showCommand.setCommand("Add", type, model, id);
				showCommand.show();
				command= showCommand.getCommand();
				if (command != null) { 
					//idNew = workCommand.showRule.getIdCom();
					db.newCommand(type, model, command);
					showModels.showModel(type, model.id);
					//System.out.println("New.."+row);
					table.showRow(false,model.findCommand(command.getId())+1);
				}
			}
		}  
	}
	
	class LsEdit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null) {
				//Rule rule;
				int row = table.selectedRule();
				//System.out.println("ShowCommandButton ..Edit"+row);
				if (row > 0){
					//	rule = (Rule)model.asub.get(row-1);
					//	workRule.setInit("edit", algo,row, rule);
					//	workRule.show();
					//	rule= workRule.getRule();
					showCommand.setCommand("Edit", type, model, row);
					showCommand.show();
					command= showCommand.getCommand();
				
					if (command != null) { 
						//System.out.println("New.."+command.output());
						db.editCommand(type, model, row, command);
						showModels.showModel(type, model.id);
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
				if (rule.getisEnd()) text1 = "��������: " ; else  text1 = "             : ";
				text1 = text1 + "\"" + rule.getsLeft() + " -> " + rule.getsRigth() + "\"";
				String text2 = "��������: \"" + rule.gettxComm() + "\"";
				String [] text = new String[] {"�������� ���������� � �������  " + row + "..",  text1, text2} ; */
				//int row1 = (type.equals("Algorithm")? row-1: ((Machine)model).findCommand(row)) ;
				//int row1 = table.selectedRule();
				//System.out.println("LsDelet row1= " + row1);
				int row1 = model.findCommand(row);
				Command c = (Command) model.program.get(row1);                 //get(row1-1);
				String [] text = new String[] {"�������� " + Model.title(type, 11) + " � ������� " + row + ". ",  c.show(model.getAllChar())} ;
				if (type.equals("Recursive")){
					Function f = (Function)c;
					String using = ((Recursive)model).usingName(f.getName());
					if (!using.isEmpty()){
						text = new String[] {"�������� " + Model.title(type, 11) + " � ������� " + row + ". ",  c.show(model.getAllChar()),
								"������� ��������������� � ���������: ", "<" + using + ">."} ;
					}
				}
				UIManager.put("OptionPane.yesButtonText", "���");
				UIManager.put("OptionPane.noButtonText", "ͳ");	
				int response = 
						JOptionPane.showConfirmDialog(ShowCommandButtons.this,text, "�������� ?",JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) { 
					db.deleteCommand(type, model.id, row,c);
					showModels.showModel(type, model.id);
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
					showModels.showModel(type, model.id);
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
					showModels.showModel(type, model.id);
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
					// ������� ĳ������� ���� "������������� �����"
					 String[] textButtons = {"������������", "�����"};
					 JPanel panel = new JPanel();
					 init = ((Machine)model).newState();
					 panel.add(new JLabel("������ ��� ����� " + name + " �� "));
					 newState = new JTextField(3);
					 newState.setText(init);
					 panel.add(newState);
					 newState.addActionListener(new LsText());	
					 int res = JOptionPane.showOptionDialog(ShowCommandButtons.this, panel, "������������� �����",
							 		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
					 if (res == JOptionPane.OK_OPTION){
						String name1 = newState.getText();
						if (!((Machine)model).isState(name1)) {
							//db.moveUp(type, model.id, row);
							//showMain.showModel(type, model.id);
							//table.showPrevRow(false);
							model.dbRenameState(name, name1);
							showModels.showModel(type, model.id);
							table.showRow(false,model.findCommand(name1)+1);
							//JOptionPane.showMessageDialog(ShowCommandButtons.this, "Rename.." + name + " -> " + name1);
						}
						else text = "�������� ������ " + model.name + " ��� ����������� ���� " + name1 + " !";
					 }
					 if(!text.isEmpty())  JOptionPane.showMessageDialog(ShowCommandButtons.this, text);
				}
			} 
		}
		// ����-�������� �������� ���������� � ���  newState
		class LsText implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				String name1 = newState.getText();
				if (((Machine)model).isState(name1)) {
					JOptionPane.showMessageDialog(ShowCommandButtons.this, 
							"�������� ������ " + model.name + " ��� ����������� ���� " + name1 + " !");
					newState.setText(init);
				}	
			}
		}
		
	}
 	
 	class LsInsert implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			int row = 0;
			String sWhere = "";
		/*	if (model != null){
				int row = table.selectedRule();
				if (row > 1) {
					db.moveUp(type, model.id, row);
					showMain.showModel(type, model.id);
					table.showPrevRow(false);
				}
			} */
			if (model == null) return;
			if (type.equals("Computer")){
				row = table.numberSelectedRow();
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
			//ImageIcon icon = new ImageIcon("c:\\iconimage\\msg_admin.gif");
			UIManager.put("OptionPane.okButtonText", "���");
			UIManager.put("OptionPane.cancelButtonText", "�����");
			Object res = 
					JOptionPane.showInputDialog(ShowCommandButtons.this, 
							"�������� " + sWhere + "�������� ������", "���� ��������", JOptionPane.QUESTION_MESSAGE, null,  values, values[0]);
			//res = JOptionPane.showInputDialog
			//JOptionPane.showMessageDialog(InputDialogs.this,res);
			//System.out.println("ShowCommandButton..LsInsert0 ..." + (String) res + "...  "); 
			if(res != null){
				if (type.equals("Computer"))
					text = model.dbInsertModel(row, (String)res);
				else text = model.dbInsertModel((String)res);	
				//System.out.println("ShowCommandButton..LsInsert1 ..." + text + "...  "); 
				if (text.isEmpty()) showModels.showModel(type, model.id);
				else JOptionPane.showMessageDialog(ShowCommandButtons.this,text);
			}
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
 	class LsEval implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				int row = table.selectedRule();
				//System.out.println("LsEval1" + row + type);
				if (type.equals("Calculus")){
					showWork.setModel(type, model);
					showWork.show();
				}  else	if (row > 0){
					if (type.equals("Recursive")){
						Function f;
						f = (Function)model.program.get(model.findCommand(row));
						if (f.getiswf()){
							showWork.setModel(type, model);
							showWork.setFunction(f);
							showWork.show();
						}	
					} 
				}
			}
		}	
	}
}
