package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import main.*;

public class ShowRule extends JPanel {
	private Algorithm algo = null;
	private String comAlfa;
	JLabel lModel;
	JLabel lId;
	JTextField sLeft;
	JTextField sRigth;
	JCheckBox checkEnd;
	JTextField sComm;
	
	ShowRule() {
		//���������� �������� gui-�������� 
		lModel = new JLabel("Algorithm   NNNNN    # xxx");
		lId = new JLabel("Rule   # yyy");
		JLabel labelLeft = new JLabel("˳�� ������� ����������");
		                                 
		sLeft = new JTextField(30);
		sLeft.setMaximumSize(new Dimension(400,20));
		                        //sLeft.setPreferredSize(new Dimension(100,20));
		JLabel labelRigth = new JLabel("����� ������� ����������");
		sRigth = new JTextField(30);
		sRigth.setMaximumSize(new Dimension(400,20));
		                         //sRigth.setPreferredSize(new Dimension(100,20));
		checkEnd = new JCheckBox("�������� ���������� ?");
		JLabel labelComm = new JLabel("��������");
		sComm = new JTextField(50);
		sComm.setMaximumSize(new Dimension(600,20));
			
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		
		//------------------------------
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(lModel);
		nameBox.add(Box.createHorizontalStrut(10));
		nameBox.add(lId);
		//--------------------------------
		Box leftBox = Box.createVerticalBox();
		leftBox.add(labelLeft);
		                                                   //labelLeft.setHorizontalAlignment(labelLeft.CENTER);
		leftBox.add(Box.createVerticalStrut(3));
		leftBox.add(sLeft);
		                                                  //leftBox.setBorder(new EtchedBorder());
		leftBox.setPreferredSize(new Dimension(200,60));
		//-------------------------
		Box rigthBox = Box.createVerticalBox();
		rigthBox.add(labelRigth);
		rigthBox.add(Box.createVerticalStrut(3));
		rigthBox.add(sRigth);
		                                                  //rigthBox.setBorder(new EtchedBorder());
	 	rigthBox.setPreferredSize(new Dimension(200,60));
		//------------------------------
		Box mainBox = Box.createHorizontalBox();
		mainBox.add(leftBox);
		//mainBox.add(Box.createHorizontalStrut(5));
		mainBox.add(rigthBox);
		//mainBox.add(Box.createHorizontalStrut(5));
		mainBox.add(checkEnd);
		//------------------------------
		Box comBox = Box.createHorizontalBox();
		comBox.add(labelComm);
		comBox.add(sComm);
		//------------------------------------
		add(Box.createGlue());
		add(nameBox);
		add(Box.createVerticalStrut(5));
		add(mainBox);
		add(Box.createVerticalStrut(3));
		add(comBox);	
		add(Box.createGlue());
		
		// ���������� �������� !!!			
		sLeft.addActionListener(new LssLeft());
		sRigth.addActionListener(new LssRigth());
	}
	
	public void setRule(Algorithm model, int id) {
		Rule rule;
		algo = model;
		if (id == 0) rule = new Rule("","",false,"====");
		else rule = (Rule)model.program.get(id-1);
		lModel.setText("��������  " + model.name + "     � " + model.id);
		lId.setText("ϳ���������  � " + id);	
		sLeft.setText(rule.getsLeft());
		sRigth.setText(rule.getsRigth());
		checkEnd.setSelected(rule.getisEnd());
		sComm.setText(rule.gettxComm());
		comAlfa = StringWork.isAlfa("",algo.main);
		comAlfa = comAlfa + StringWork.isAlfa(comAlfa,algo.add);
		System.out.println(".." + comAlfa + "..");
		sLeft.requestFocus();
	}
	
	// ����� ������� 
	class LssLeft implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String noAlfa = StringWork.isAlfa(comAlfa, sLeft.getText());
			if (!noAlfa.isEmpty()){
				
				String text = "˳�� ������� ���������� ������ ������� " + 
							noAlfa + " �� �� ������� �� �������� ������� " + comAlfa + " !";
				JOptionPane.showMessageDialog(ShowRule.this,text);	
			} else { sRigth.requestFocus();}
		}	
	}
	class LssRigth implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String noAlfa = StringWork.isAlfa(comAlfa, sRigth.getText());
			if (!noAlfa.isEmpty()){
				String text = "˳�� ������� ���������� ������ ������� " + 
							noAlfa + " �� �� ������� �� �������� ������� " + comAlfa + " !";
				JOptionPane.showMessageDialog(ShowRule.this,text);	
			} else { sComm.requestFocus();}
		}	
	}		
	

}
