package gui;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import main.*;

public class ShowCommand extends JDialog  {
	JLabel lWhat;
	ShowRule showRule;
	JButton yes;
	JButton cancel;
	Command command = null;
	private String type = "Algorithm";
	private Model model = null;
	
	ShowCommand(Frame owner){
		super(owner, "Command");
		setModal(true);
		
		//���������� ��������� gui-��������
		//ShowCommandHead showHead = new ShowCommandHead();
		lWhat = new JLabel("Edit");
		lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		showRule = new ShowRule();
		yes = new JButton("��������");
		cancel = new JButton("�� ��������");
		
		// ������� ���������
		setLayout(new BorderLayout());
		//---------------------------		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createGlue());
		buttonBox.add(yes);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(cancel);
		buttonBox.add(Box.createGlue());
		//---------------------
		add(lWhat, BorderLayout.NORTH);
		add(showRule, BorderLayout.CENTER);
		add(buttonBox, BorderLayout.SOUTH);
		pack();
		//setSize(400,200);
		// ���������� �������� !!!			
		yes.addActionListener(new LsYes());
		cancel.addActionListener(new LsCancel());
	}
	
	//������� ����� - ������� !!!!!!
	class LsYes implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			command = showRule.getCommand();
			if (type.equals("Post")){
				command = showRule.getCommand();
				ArrayList <String> mes = ((Derive)command).iswfCommand((Post)model);
				if (mes.size() == 0) hide();
				else JOptionPane.showMessageDialog(ShowCommand.this,StringWork.transferToArray(mes)); 
				
			} else {
				String text = showRule.testAllCommand();
				if (text.isEmpty()) {
					command = showRule.getCommand();
					hide();
				} else JOptionPane.showMessageDialog(ShowCommand.this,text); 
			}	
		}	
	}
	class LsCancel implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//	JOptionPane.showMessageDialog(ShowCommand.this,"Cancel !");
			command = null;
			hide();
		}	
	}
	public void setCommand(String what, String type, Model model,  int id){
		command = null;
		this.type = type; this.model = model;
		if (what == "Add") lWhat.setText(Model.title(type, 9)); else lWhat.setText("����������");
		showRule.setRule(type, model, id, what);
	}
	public Command getCommand() { return command;}
		
}
