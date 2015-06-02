package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import main.*;

public class ShowCommand extends JDialog  {
	JLabel lWhat;
	ShowRule showRule;
	JButton yes;
	JButton cancel;
	Command command = null;
	
	ShowCommand(Frame owner){
		super(owner, "mRule");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		//ShowCommandHead showHead = new ShowCommandHead();
		lWhat = new JLabel("Edit");
		lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		showRule = new ShowRule();
		yes = new JButton("Зберегти");
		cancel = new JButton("Не зберігати");
		
		// формуємо розміщення
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
		// встановити слухачів !!!			
		yes.addActionListener(new LsYes());
		cancel.addActionListener(new LsCancel());
	}
	
	//описуємо класи - слухачі !!!!!!
	class LsYes implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//	JOptionPane.showMessageDialog(ShowCommand.this,"Yes !");
			command = new Rule(showRule.sLeft.getText(),showRule.sRigth.getText(),
							showRule.checkEnd.isSelected(),showRule.sComm.getText());
			hide();
		}	
	}
	class LsCancel implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//	JOptionPane.showMessageDialog(ShowCommand.this,"Cancel !");
			command = null;
			hide();
		}	
	}
	public void setCommand(String what, Model model,  int id){
		command = null;
		if (what == "Add") lWhat.setText("Нова"); else lWhat.setText("Редагувати");
		showRule.setRule((Algorithm)model, id);
	}
	public Command getCommand() { return command;}
		
}
