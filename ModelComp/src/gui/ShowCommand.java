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
		super(owner, "Command");
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
			
			String text = showRule.testAllCommand();
			if (text.isEmpty()) {
				command = showRule.getCommand();
				//command = new Rule(showRule.sLeft.getText(),showRule.sRigth.getText(),
				//				showRule.checkEnd.isSelected(),showRule.sComm.getText());
				hide();
			} else JOptionPane.showMessageDialog(ShowCommand.this,text);;
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
		if (what == "Add") lWhat.setText(Model.title(type, 9)); else lWhat.setText("Редагувати");
		showRule.setRule(type, model, id, what);
	}
	public Command getCommand() { return command;}
		
}
