package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import db.*;

public class ShowCommandButtons extends JPanel {
	DbAccess db;
	
	ShowCommandButtons(DbAccess db){
		//���������� �������� gui-�������� 
		JButton test = new JButton("��������");
		JButton add = new JButton("����");
		JButton edit = new JButton("����������");
		JButton delete = new JButton("��������");
		JButton up = new JButton("���������� �����");
		JButton down = new JButton("���������� ����");
		this.db = db;
		//=================================
		// ������� ���������
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
		// ���������� �������� !!!			
		test.addActionListener(new SelectTesting());
		add.addActionListener(new RuleAdd());
		edit.addActionListener(new RuleEdit());
		delete.addActionListener(new RuleDelete());
		up.addActionListener(new RuleUp());
		down.addActionListener(new RuleDown());
			
	}
	
	// ����� ������� 
	class SelectTesting implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		//	if (model != null){
		//		int all = model.asub.size();
		//		int r = table.selectedRule();
				//table.getSelectionModel().setSelectionInterval(r-1, r-1);
		//		String text = "������� ����������  " + r + " � " + all + " ����������  ��������� " + model.nmAlgo +".";
		//		if (r==0) text = "�� ������� ���� ����������  � " + all + " ���������� ��������� " + model.nmAlgo+ ".";
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"Testing..."); //text);
		//	}
		}	
	}
	class RuleAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		/*	if (model != null) {
				Rule rule;
				int row = table.selectedRule();
				if (row == 0) rule = new Rule("","",false,"====");
			else rule = (Rule)model.asub.get(row-1);
				//System.out.println("New.."+row + ".."+rule.show());
				workRule.setInit("new", algo, row+1, rule);
				workRule.show();
				rule= workRule.getRule();
				if (rule != null) { 
					db.newRule(algo, row, rule);
					if (row == 0) table.showFirstRow(true);
					else table.showNextRow(true);
					model = db.getAlgorithm(algo);
				}
			}  */
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleAdd...");
		}	
	}
	class RuleEdit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		/*	if (model != null) {
				Rule rule;
				int row = table.selectedRule();
				if (row > 0){
					rule = (Rule)model.asub.get(row-1);
					workRule.setInit("edit", algo,row, rule);
					workRule.show();
					rule= workRule.getRule();
					if (rule != null) { 
						db.editRule(algo, row, rule);
						table.showTable();
						model = db.getAlgorithm(algo);
					}
				}
			}*/
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleEdit...");
		}	
	}
	class RuleDelete implements ActionListener  {
		public void actionPerformed(ActionEvent event){
		/*	if (model != null) {
				int row = table.selectedRule();
				if (row > 0){
					Rule rule = (Rule)model.asub.get(row-1);
					String text = "�������� ������� � �������  " + row + ".." + rule.show() + " !";
					int response = 
							JOptionPane.showConfirmDialog(ShowProgram.this,text);
					if (response == JOptionPane.YES_OPTION) { 
						db.deleteRule(algo, row);
						model = db.getAlgorithm(algo);
						table.showPrevRow(true);
					}	
				}
			} */
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDelete...");
		}	
	} 
	class RuleUp implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			/*if (model != null){
				//int all = model.asub.size();
				int row = table.selectedRule();
				if (row > 1) {
					db.moveUp(algo, row);
					model = db.getAlgorithm(algo);
					table.showPrevRow(true);
				}
			} */
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleUp...");
		}	
	}
 	class RuleDown implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			/*if (model != null){
				int all = model.asub.size();
				//table.getSelectionModel().setSelectionInterval(r-1, r-1);
				int row = table.selectedRule();
				if ((row > 0) && (row < all)){
					db.moveDown(algo, row);
					model = db.getAlgorithm(algo);
					table.showNextRow(true);
					//dbm.setDataSource(db.getDataSource());
					//table.getSelectionModel().setSelectionInterval(r+1, r+1);
					//selection.setText((table.getSelectedRow() + 1)  + " : " + dbm.getRowCount());
					//String text1 = "������� ���� ������� ��������� " + algo + " � ������� " + row + "!";
					//JOptionPane.showMessageDialog(ModelProgram.this,text1);
				}
			}*/
			JOptionPane.showMessageDialog(ShowCommandButtons.this,"RuleDown...");
		}	
	}
	
}
