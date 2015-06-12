package gui;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import main.*;

public class ShowRule extends JPanel {
	private Model model;
	private Algorithm algo = null;
	private Machine mach = null;
	private Post post = null;
	private String type = "Algorithm";
	private int idCom = 0;  // Algorithm =>  порядковий номер, else => ключ в БД 
	private String comAlfa;
	private String what = "";
	JLabel lModel;
	JLabel lId;
	JTextField state;     // стан або порядковий номер зовнішня ідентифікація == редагуємо при створенні !!!!!!!!
	JCheckBox checkAxiom;
	JLabel labelLeft;
	JTextField sLeft;
	JLabel labelRigth; 
	JTextField sRigth;
	JCheckBox checkEnd;
	JLabel lChar[];
	JTextField sMove[];
	JTextField sComm;
	Box mainBox;
	Box leftBox;
	Box rigthBox; 
	//-------------------for Machine--------------
	Box moveBox[];
		
	ShowRule() {
		//сформувати необхідні gui-елементи 
		lModel = new JLabel("Algorithm   NNNNN    # xxx");
		lId = new JLabel("Rule   (yyy)");
		state = new JTextField(5);
		state.setMaximumSize(new Dimension(50,20));
		state.setText("@a0");
		checkAxiom = new JCheckBox("Аксіома ?");
		labelLeft = new JLabel("Ліва частина підстановки");
		labelLeft.setAlignmentX(labelLeft.CENTER_ALIGNMENT);                                 
		sLeft = new JTextField(30);
		sLeft.setMaximumSize(new Dimension(400,20));
		                        //sLeft.setPreferredSize(new Dimension(100,20));
		labelRigth = new JLabel("Права частина підстановки");
		labelRigth.setAlignmentX(labelRigth.CENTER_ALIGNMENT);  
		sRigth = new JTextField(30);
		sRigth.setMaximumSize(new Dimension(400,20));
		                         //sRigth.setPreferredSize(new Dimension(100,20));
		checkEnd = new JCheckBox("Заключна підстановка ?");
		//-------------------for Machine--------------
		lChar = new JLabel[30];
		sMove = new JTextField[30];
		String si;
		for (int i = 0; i < 30; i++){
			lChar[i] = new JLabel("'c'");
			lChar[i].setAlignmentX(lChar[i].CENTER_ALIGNMENT);
			sMove[i] = new JTextField(5);
			sMove[i].setMaximumSize(new Dimension(50,20));
			sMove[i].setText("@a0_>");
			si = ((Integer)i).toString();
			sMove[i].setActionCommand(si);
		}
		JLabel labelComm = new JLabel("Коментар");
		sComm = new JTextField(50);
		sComm.setMaximumSize(new Dimension(600,20));
		
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//------------------------------
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(lModel);
		nameBox.add(Box.createHorizontalStrut(10));
		nameBox.add(lId);
		nameBox.add(Box.createHorizontalStrut(3));
		nameBox.add(state);
		//--------------------------------
		leftBox = Box.createVerticalBox();
		leftBox.add(labelLeft);
		leftBox.add(Box.createVerticalStrut(3));
		leftBox.add(sLeft);
		                                                //  leftBox.setBorder(new EtchedBorder());
		leftBox.setPreferredSize(new Dimension(200,60));
		//-------------------------
		rigthBox = Box.createVerticalBox();
		rigthBox.add(labelRigth);
		rigthBox.add(Box.createVerticalStrut(3));
		rigthBox.add(sRigth);
		                                                //  rigthBox.setBorder(new EtchedBorder());
	 	rigthBox.setPreferredSize(new Dimension(200,60));
		moveBox = new Box[30];
		for (int i = 0; i < 30; i++){
			moveBox[i] = Box.createVerticalBox();
			moveBox[i].add(lChar[i]);
			moveBox[i].add(Box.createVerticalStrut(3));
			moveBox[i].add(sMove[i]);
			moveBox[i].setPreferredSize(new Dimension(1000,60));
														//moveBox[i].setBorder(new EtchedBorder());
		}
	 	
		//------------------------------
		mainBox = Box.createHorizontalBox();
		mainBox.add(checkAxiom);
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
				
		// встановити слухачів !!!
		state.addActionListener(new LsState());
		checkAxiom.addActionListener(new LsCheckAxiom());
		sLeft.addActionListener(new LssLeft());
		sRigth.addActionListener(new LssRigth());
		for (int i = 0; i < 30; i++) sMove[i].addActionListener(new LsMove());
	}
	
	public void setRule(String type, Model model, int id, String what) {
		boolean isAlgo = type.equals("Algorithm");
		boolean isEdit = what.equals("Edit");
		//System.out.println("isEdit = " + isEdit);
		this.model = model;
		this.type = type;
		this.what = what;
		state.setVisible(!type.equals("Algorithm"));
		lModel.setText(Model.title(type, 2)+ " " + model.name + "     № " + model.id);
		// 
		idCom = (id==0?model.findMaxNumber()+1:(type.equals("Algorithm") || isEdit ? id : model.findMaxNumber()+1));
		lId.setText(Model.title(type, 10) + "  № " + idCom);	
		if (isAlgo) {
			Rule rule;
			algo = (Algorithm)model;
			if (id == 0) rule = new Rule("","",false,"====");
			else rule = (Rule)model.program.get(id-1);
			labelLeft.setText("Ліва частина підстановки");
			sLeft.setText(rule.getsLeft());
			labelRigth.setText("Права частина підстановки");
			sRigth.setText(rule.getsRigth());
			checkEnd.setSelected(rule.getisEnd());
			sComm.setText(rule.gettxComm());
			comAlfa = StringWork.isAlfa("",algo.main);
			comAlfa = comAlfa + StringWork.isAlfa(comAlfa,algo.add);
			//System.out.println(".." + comAlfa + "..");
			sLeft.requestFocus();
			mainBox.remove(checkAxiom);
			mainBox.add(leftBox);
			mainBox.add(rigthBox);
			mainBox.add(checkEnd);
			for (int i = 0; i < 30; i++){
				mainBox.remove(moveBox[i]);
			}
			sLeft.requestFocus();
		}
		else if (type.equals("Post")){
			Derive rule;
			post = (Post)model;
			lId.setText(Model.title(type, 10) + "(" + idCom + ")");	
			//state.setText("1");
			if (id == 0) rule = new Derive(post.program.size()+1,false,"","","====",idCom);
			else rule = (Derive)model.program.get(id-1);
			state.setText("" + rule.getNum());
			if (isEdit) {
				if (rule.getisAxiom()) sRigth.requestFocus(); else sLeft.requestFocus();
			}
			else {
				state.requestFocus();
			}
			state.enable(!isEdit);
			checkAxiom.setSelected(rule.getisAxiom());
			labelLeft.setText("Ліва частина правила");
			sLeft.setText(rule.getsLeft());
			labelRigth.setText("Права частина правила");
			sRigth.setText(rule.getsRigth());
			sComm.setText(rule.gettxComm());
			comAlfa = StringWork.isAlfa("",post.main);
			comAlfa = comAlfa + StringWork.isAlfa(comAlfa,post.add);
			//System.out.println(".." + comAlfa + "..");
			mainBox.add(checkAxiom);
			if(rule.getisAxiom()){
				sRigth.requestFocus();
				mainBox.remove(leftBox);
			} else {
				sLeft.requestFocus();
				mainBox.add(leftBox);
			}
			mainBox.add(rigthBox);
			mainBox.remove(checkEnd);
			for (int i = 0; i < 30; i++){
				mainBox.remove(moveBox[i]);
			}
			//sLeft.requestFocus();			
		}  
		else {
			State st;
			mach = (Machine)model;
			if (id == 0) st = mach.emptyState("@a0");
			else st = (State)model.program.get(mach.findCommand(id));
			sComm.setText(st.gettxComm());
			if (isEdit) {
				state.setText(st.getState());
				sLeft.requestFocus();
			}
			else {
				state.setText(mach.newState());
				state.requestFocus();
			}
			state.enable(!isEdit);
			comAlfa = "_" + model.getMain() + model.getAdd() + model.getNo();
			for(int i = 0; (i < comAlfa.length()) && (i < 30); i++){
				lChar[i].setText("'" + comAlfa.substring(i,i+1) + "'");
				sMove[i].setText(st.getGoing().get(i));
			}
			mainBox.remove(checkAxiom);
			mainBox.remove(leftBox);
			mainBox.remove(rigthBox);
			mainBox.remove(checkEnd);
			for (int i = 0; (i < comAlfa.length()) && (i < 30); i++){
			  mainBox.add(moveBox[i]);
			}
		}
	}
	
	public String testAllCommand(){
		String text = "";
		if(type.equals("Machine")){
			int i = 0;
			if(what.equals("New"))text = mach.testState(state.getText());
			while ((i < comAlfa.length()-1) && (i < 29) && text.isEmpty()){
				text = mach.testMove(sMove[i].getText());
				i++;
			}
		}
		return text;
	}
	
	public Command getCommand(){
		Command com = null;
		switch(type){
		case "Algorithm":
			com = new Rule(sLeft.getText(),sRigth.getText(), checkEnd.isSelected(),sComm.getText());
			break;
		case "Machine":	
			String  move = "";
			String st = state.getText();
			String ch = "";
			ArrayList <String> going = new ArrayList<String>();
			for(int i =0; i < comAlfa.length(); i++) {
				ch = comAlfa.substring(i,i+1);
				if (i < 30) {
					move = sMove[i].getText();
					if (!move.isEmpty()){
						if(st.equals(move.substring(0,3)) && ch.equals(move.substring(3,4)) && move.substring(4,5).equals(".")) move = ""; 
					}
				}
				else move = "";
				going.add(move);
			}
			com = new State(st,idCom,going,sComm.getText());
			break;
		case "Post":
			String numS = state.getText();
			int numI = 1;
			if (StringWork.isPosNumber(numS)) numI = new Integer(numS);  
			com = new Derive(numI,checkAxiom.isSelected(),sLeft.getText(),sRigth.getText(),sComm.getText(),idCom);
			break;
		}
		return com;
	}
	
	public int getIdCom() {return idCom;}
	
	// Класи слухачі 
	class LsState implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			switch (type){
			case "Machine":
				String text = mach.testState(state.getText());
				if (!text.isEmpty()) JOptionPane.showMessageDialog(ShowRule.this, text);	
				else  sMove[0].requestFocus();
				break;
			case "Post":
				if (checkAxiom.isSelected()) sRigth.requestFocus(); else sLeft.requestFocus();
				break;
			}
			
		}	
	}
	class LsCheckAxiom implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if(checkAxiom.isSelected()){
				labelRigth.setText("Аксіома");
				sRigth.requestFocus();
				mainBox.remove(leftBox);	
			} else {
				labelRigth.setText("Права частина правила");
				sLeft.requestFocus();
				mainBox.add(leftBox);
			}
		}	
	}	
	class LssLeft implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String noAlfa = StringWork.isAlfa(comAlfa, sLeft.getText());
			if (!noAlfa.isEmpty()){
				
				String text = "Ліва частина підстановки містить символи " + 
							noAlfa + " що не входять до спільного алфавіту " + comAlfa + " !";
				JOptionPane.showMessageDialog(ShowRule.this,text);	
			} else { sRigth.requestFocus();}
		}	
	}
	class LssRigth implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String noAlfa = StringWork.isAlfa(comAlfa, sRigth.getText());
			if (!noAlfa.isEmpty()){
				String text = "Ліва частина підстановки містить символи " + 
							noAlfa + " що не входять до спільного алфавіту " + comAlfa + " !";
				JOptionPane.showMessageDialog(ShowRule.this,text);	
			} else { sComm.requestFocus();}
		}	
	}		
	
	class LsMove implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			Integer index = new Integer(e.getActionCommand());
			String text = mach.testMove(sMove[index].getText());
			//System.out.println("move="+sMove[index].getText()+ " text=" +text );
			if (text.isEmpty()) {
				if ((index < comAlfa.length()-1) && (index < 29)) sMove[index + 1].requestFocus();
				else sComm.requestFocus();
			}
			else JOptionPane.showMessageDialog(ShowRule.this, text);	
			//JOptionPane.showMessageDialog(ShowRule.this,"index = " + index + " move = " + move);
		}	
	}
}
