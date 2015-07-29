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
	private Recursive recur = null;
	private Computer comp = null;
	private String type = "Algorithm";
	private int idCom = 0;  // Algorithm =>  порядковий номер, else => ключ в БД 
	private String comAlfa;
	private String what = "";
	private ShowCommand showCommand;
	//private boolean initialBCod;
	JLabel lModel;
	JLabel lId;
	JTextField state;     // стан або порядковий номер зовнішня ідентифікація == редагуємо при створенні !!!!!!!!
	JCheckBox checkAxiom;
	JLabel labelLeft;
	JTextField sLeft;
	JLabel labelRigth; 
	JTextField sRigth;
	JCheckBox checkEnd;
	//----------------------Computer
	JLabel lCod;
	JComboBox bCod;
	JLabel lReg1;
	JTextField tReg1;
	JLabel lReg2;
	JTextField tReg2;
	JLabel lNext;
	JTextField tNext;
	Box codBox;
	Box reg1Box;
	Box reg2Box;
	Box nextBox;
	//---------------
	JLabel lChar[];
	JTextField sMove[];
	JLabel labelBody;
	JTextField sBody;
	JTextField sComm;
	JTextField lTesting;
	//JLabel lTesting;
	Box mainBox;
	Box leftBox;
	Box rigthBox; 
	Box bodyBox;
	
	//-------------------for Machine--------------
	Box moveBox[];
		
	ShowRule(ShowCommand owner) {
		//сформувати необхідні gui-елементи 
		showCommand = owner;
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
		//----------------------Computer
		String[] codl = {"Z","S","T","J"};
		lCod = new JLabel("Код");
		lCod.setAlignmentX(lCod.CENTER_ALIGNMENT);         
		bCod = new JComboBox(codl);
		//bCod.setPrototypeDisplayValue("11");
		bCod.setBackground(Color.WHITE);
		bCod.setMaximumSize(new Dimension(50,20));
		lReg1 = new JLabel("Регістр 1");
		lReg1.setAlignmentX(lReg1.CENTER_ALIGNMENT);      
		tReg1 = new JTextField(4);
		tReg1.setText("1");
		tReg1.setMaximumSize(new Dimension(30,20));
		//tReg1.setAlignmentX(lReg1.CENTER_ALIGNMENT);         
		lReg2 = new JLabel("Регістр 2");
		lReg2.setAlignmentX(lReg2.CENTER_ALIGNMENT);    
		tReg2 = new JTextField(4);
		tReg2.setMaximumSize(new Dimension(30,20));
		lNext = new JLabel("Наступна");
		lNext.setAlignmentX(lNext.CENTER_ALIGNMENT);    
		tNext = new JTextField(5);
		tNext.setMaximumSize(new Dimension(40,20));
		//---------------
				
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
		labelBody = new JLabel("Тіло функції");
		sBody = new JTextField(50);
		sBody.setMaximumSize(new Dimension(600,20));
		JLabel labelComm = new JLabel("Коментар");
		sComm = new JTextField(50);
		sComm.setMaximumSize(new Dimension(600,20));
		//lTesting = new JLabel("....");
		lTesting = new JTextField(50);
		lTesting.setMaximumSize(new Dimension(700,20));
		
		
		
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
	 	//---------------------Computer
		codBox = Box.createVerticalBox();
		codBox.add(lCod);
		codBox.add(Box.createVerticalStrut(3));
		codBox.add(bCod);
		                                                  //codBox.setBorder(new EtchedBorder());
		codBox.setPreferredSize(new Dimension(100,60));
		reg1Box = Box.createVerticalBox();
		reg1Box.add(lReg1);
		reg1Box.add(Box.createVerticalStrut(3));
		reg1Box.add(tReg1);
		                                                 // reg1Box.setBorder(new EtchedBorder());
		reg1Box.setPreferredSize(new Dimension(100,60));
		reg2Box = Box.createVerticalBox();
		reg2Box.add(lReg2);
		reg2Box.add(Box.createVerticalStrut(3));
		reg2Box.add(tReg2);
		                                                 // reg2Box.setBorder(new EtchedBorder());
		reg2Box.setPreferredSize(new Dimension(100,60));
		nextBox = Box.createVerticalBox();
		nextBox.add(lNext);
		nextBox.add(Box.createVerticalStrut(3));
		nextBox.add(tNext);
		                                                //  nextBox.setBorder(new EtchedBorder());
		nextBox.setPreferredSize(new Dimension(100,60));
		//------------------------------
		mainBox = Box.createHorizontalBox();
		mainBox.add(checkAxiom);
		mainBox.add(leftBox);
								//mainBox.add(Box.createHorizontalStrut(5));
		mainBox.add(rigthBox);
								//mainBox.add(Box.createHorizontalStrut(5));
		mainBox.add(checkEnd);
		//------------------------------
		bodyBox = Box.createHorizontalBox();
		bodyBox.add(labelBody);
		bodyBox.add(sBody);
		//------------------------------
		Box comBox = Box.createHorizontalBox();
		comBox.add(labelComm);
		comBox.add(sComm);
		//------------------------------------
		add(Box.createGlue());
		add(nameBox);
		add(Box.createVerticalStrut(5));
		add(mainBox);
		add(bodyBox);
		add(Box.createVerticalStrut(3));
		add(comBox);
		add(lTesting);
		//add(testTree);
		add(Box.createGlue());
		
		//testTree.setVisible(false);
		// встановити слухачів !!!
		state.addActionListener(new LsState());
		checkAxiom.addActionListener(new LsCheckAxiom());
		sLeft.addActionListener(new LssLeft());
		sRigth.addActionListener(new LssRigth());
		for (int i = 0; i < 30; i++) sMove[i].addActionListener(new LsMove());
		sBody.addActionListener(new LsBody());
		bCod.addActionListener(new LsCod());
		tReg1.addActionListener(new LsReg1());
		tReg2.addActionListener(new LsReg2());
		tNext.addActionListener(new LsNext());
	}
	
	public void setRule(String type, Model model, int id, String what) {
		boolean isAlgo = type.equals("Algorithm");
		boolean isEdit = what.equals("Edit");
		//System.out.println("isEdit = " + isEdit);
		this.model = model;
		this.type = type;
		this.what = what;
		//state.setVisible(!type.equals("Algorithm"));
		lModel.setText(Model.title(type, 2)+ " " + model.name + "     № " + model.id);
		// 
		//idCom = (id==0?model.findMaxNumber()+1:(type.equals("Algorithm") || isEdit ? id : model.findMaxNumber()+1));
		idCom = (isEdit ? id : model.findMaxNumber()+1);
		lId.setText(Model.title(type, 10) + "  № " + idCom);
		mainBox.setVisible(!type.equals("Recursive"));
		bodyBox.setVisible(type.equals("Recursive"));
		lTesting.setVisible(type.equals("Recursive"));
		//testTree.setVisible(false);
		state.setMaximumSize(new Dimension(50,20));
		//---------------------Computer
		if (type.equals("Computer")){
			mainBox.add(codBox);
			mainBox.add(reg1Box);
			mainBox.add(reg2Box);
			mainBox.add(nextBox);	
			mainBox.remove(checkAxiom);
			mainBox.remove(leftBox);
			mainBox.remove(rigthBox);
			mainBox.remove(checkEnd);
			for (int i = 0; i < 30; i++){
				mainBox.remove(moveBox[i]);
			}
		} else {
			mainBox.remove(codBox);
			mainBox.remove(reg1Box);
			mainBox.remove(reg2Box);
			mainBox.remove(nextBox);		
		}
		//---------------------Computer
		if (isAlgo) {
			Rule rule;
			algo = (Algorithm)model;
			if (id == 0) rule = new Rule(model.program.size()+1,"","",false,"====",idCom);
			else rule = (Rule)model.program.get( model.findCommand(id));
			state.setText("" + (rule.getNum()+1));
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
			if (isEdit) sLeft.requestFocus(); else state.requestFocus();
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
			// id --- key in DB !!!!!!!!
			//int num = model.findCommand(id);
			if (id == 0) rule = new Derive(model.program.size()+1,false,"","","====",idCom);
			else rule =  (Derive)model.program.get( model.findCommand(id));                                   //(Derive)model.program.get(id-1);
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
				labelRigth.setText("Аксіома");
				mainBox.add(rigthBox);
				sRigth.requestFocus();
				mainBox.remove(leftBox);
			} else {
				labelRigth.setText("Права частина правила");
				mainBox.add(leftBox);
				mainBox.add(rigthBox);
				sLeft.requestFocus();
			}
			mainBox.remove(checkEnd);
			for (int i = 0; i < 30; i++){
				mainBox.remove(moveBox[i]);
			}
			//sLeft.requestFocus();			
		}  
		else if (type.equals("Recursive")){
			Function f;
			recur = (Recursive)model;
			state.setMaximumSize(new Dimension(100,20));
			if (id==0) f = recur.newFunction(null);
			else f = (Function)model.program.get(recur.findCommand(id));
			sBody.setText(f.gettxBody());
			sComm.setText(f.gettxComm());
			lTesting.setText(f.geterrorText());
			if (isEdit) {
				state.setText(f.getName());
				sBody.requestFocus();
			}
			else {
				state.setText(id==0?f.getName():recur.findName(f.getName()));
				state.requestFocus();
			}
			state.enable(!isEdit);
			lTesting.setEditable(false);
			
			//lTesting.enable(false);
			
		}else if (type.equals("Machine")){
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
		} else if (type.equals("Computer")){
			//System.out.println("Computer");
			Instruction inst;
			String cod;
			String sReg2 = "";
			String sNext = "";
			boolean bZS = true;
			boolean bJ = false;
			comp = (Computer)model;
			lId.setText(Model.title(type, 10) + "(" + idCom + ")");	
			if (id == 0) inst = new Instruction(model.program.size()+1,"Z",1,0,0,"====",idCom);
			else inst =  (Instruction)model.program.get( model.findCommand(id));                                   //(Derive)model.program.get(id-1);
			state.setText("" + inst.getNum());
			sComm.setText(inst.gettxComm());
			state.enable(!isEdit);
			cod = inst.getCod();
			//initialBCod = true;
			tReg1.setText("" + inst.getReg1());
			bCod.setSelectedItem(cod);
			
			//tReg2.setText(""); tNext.setText("");
			bZS = cod.equals("Z") ||cod.equals("S");
			bJ = cod.equals("J"); 
			if (!bZS) tReg2.setText("" + inst.getReg2());
			if (bJ) tNext.setText("" + inst.getNext());
			//tReg2.enable(!bZS);
			//tNext.enable(bJ);
			//initialBCod = false;
			if (isEdit) {
				bCod.requestFocus(); 
			}
			else {
				state.requestFocus();
			}
		}
	}
	
	public ArrayList <String> testAllCommand(){
		ArrayList <String> mes = new ArrayList <String> ();
		String text = "";
		if(type.equals("Machine")){
			int i = 0;
			if(what.equals("New")){
				text = mach.testState(state.getText());
				if(!text.isEmpty()) mes.add(text);
			}
			while ((i < comAlfa.length()-1) && (i < 29) ){
				text = mach.testMove(sMove[i].getText());
				if(!text.isEmpty()) mes.add(text);
				i++;
			}
		} else if(type.equals("Computer")){
			String cod = (String)bCod.getSelectedItem();
			text = comp.testPart(cod, 1, tReg1.getText());
			if(!text.isEmpty()) mes.add(text);
			text = comp.testPart(cod, 2, tReg2.getText());
			if(!text.isEmpty()) mes.add(text);
			text = comp.testPart(cod, 3, tNext.getText());
			if(!text.isEmpty()) mes.add(text);
		}
		return mes;
	}
	
	public Command getCommand(){
		Command com = null;
		String st = state.getText();
		int num = model.program.size()+1;
		switch(type){
		case "Algorithm":
			if (StringWork.isPosNumber(st)) num = new Integer(st);  
			com = new Rule(num, sLeft.getText(),sRigth.getText(), checkEnd.isSelected(),sComm.getText(), idCom);
			break;
		case "Machine":	
			String  move = "";
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
			if (StringWork.isPosNumber(st)) num = new Integer(st);  
			com = new Derive(num,checkAxiom.isSelected(),sLeft.getText(),sRigth.getText(),sComm.getText(),idCom);
			//System.out.println(com.output());
			break;
		case "Recursive":
			com = new Function (idCom, st, sBody.getText(),sComm.getText());
			break;
		case "Computer":
			int reg2 = 0;
			int next = 0;
			String cod = (String)bCod.getSelectedItem();
			if (!cod.equals("Z") && !cod.equals("S")) {
				reg2 = new Integer(tReg2.getText());
				if (cod.equals("J"))next = new Integer(tNext.getText());  
			}
			com = new Instruction (new Integer(st), cod, new Integer(tReg1.getText()), reg2, next, sComm.getText(), idCom); break;	
		}
		return com;
	}
	
	public int getIdCom() {return idCom;}
	
	// Класи слухачі 
	class LsState implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			ArrayList <String> mes;
			String text;
			switch (type){
			case "Machine":
				text = mach.testState(state.getText());
				if (!text.isEmpty()) JOptionPane.showMessageDialog(ShowRule.this, text);	
				else  sMove[0].requestFocus();
				break;
			case "Recursive":
				text = recur.testName(state.getText());
				if (!text.isEmpty()) JOptionPane.showMessageDialog(ShowRule.this, text);	
				else  sBody.requestFocus();
				break;	
			case "Post":
				mes = post.iswfNum(state.getText());
				if (mes.size() == 0) {
					if (checkAxiom.isSelected()) sRigth.requestFocus(); else sLeft.requestFocus();
				} else JOptionPane.showMessageDialog(ShowRule.this,  StringWork.transferToArray(mes));
				break;
			case "Computer":
				mes = comp.iswfNum(state.getText());
				if (mes.size() == 0) bCod.requestFocus();
				else JOptionPane.showMessageDialog(ShowRule.this,  StringWork.transferToArray(mes));
				break;
			case "Algorithm":
				mes = algo.iswfNum(state.getText());
				if (mes.size() == 0)  sLeft.requestFocus();
				else JOptionPane.showMessageDialog(ShowRule.this,  StringWork.transferToArray(mes));
				break;		
			/*	String numS = state.getText();
				int numI = -1;
				if (StringWork.isPosNumber(numS)) numI = new Integer(numS);
				if((numI < 0) || (numI > model.program.size())) {
					text = "Порядковий номер аксіоми/правила виводу повинен бути не меньше 1 і не більше " + model.program.size() + ".";
					JOptionPane.showMessageDialog(ShowRule.this, text);
				} else {
					if (checkAxiom.isSelected()) sRigth.requestFocus(); else sLeft.requestFocus();
				}	
				break; */
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
				//sLeft.requestFocus();
				mainBox.remove(rigthBox);
				mainBox.add(leftBox);
				mainBox.add(rigthBox);
				sLeft.requestFocus();
			}
		}	
	}	
	class LssLeft implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			switch (type) {
			case "Algorithm":
				String noAlfa = StringWork.isAlfa(comAlfa, sLeft.getText());
				if (!noAlfa.isEmpty()){
					
					String text = "Ліва частина підстановки містить символи " + 
								noAlfa + " що не входять до спільного алфавіту " + comAlfa + " !";
					JOptionPane.showMessageDialog(ShowRule.this,text);	
				} else { sRigth.requestFocus();}
				break;
			case "Post":
				ArrayList <String> mes = post.iswfLeft(sLeft.getText());
				if (mes.size() == 0) sRigth.requestFocus();
				else JOptionPane.showMessageDialog(ShowRule.this,  StringWork.transferToArray(mes));
				break;
			}
			
		}	
	}
	class LssRigth implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			switch (type) {
			case "Algorithm":
				String noAlfa = StringWork.isAlfa(comAlfa, sRigth.getText());
				if (!noAlfa.isEmpty()){
					String text = "Ліва частина підстановки містить символи " + 
							noAlfa + " що не входять до спільного алфавіту " + comAlfa + " !";
					JOptionPane.showMessageDialog(ShowRule.this,text);	
				} else { sComm.requestFocus();}
				break;
			case "Post":
				ArrayList <String> mes = null;
				if(checkAxiom.isSelected())	 mes = post.iswfAxiom(sRigth.getText());
				else mes = post.iswfRigth(sLeft.getText(),sRigth.getText());
				if (mes.size() == 0) sComm.requestFocus();
				else JOptionPane.showMessageDialog(ShowRule.this,  StringWork.transferToArray(mes));
				break;
			}
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
	class LsBody implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String st = recur.fullAnalys(state.getText(), sBody.getText());
			showCommand.showTree.setVisible(false);
			showCommand.pack();
			lTesting.setText(st);
			if(st.isEmpty()) sComm.requestFocus();
		}
	}
	class LsCod implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String cod = (String)bCod.getSelectedItem();
			tReg2.setText(""); tReg2.enable(false);
			tNext.setText(""); tNext.enable(false);
			if(!cod.equals("Z") && !cod.equals("S")){
				tReg2.setText("1"); tReg2.enable(true);
				if (cod.equals("J")) {tNext.setText("1"); tNext.enable(true);}
			}
			
			tReg1.requestFocus();
		}
	}
	class LsReg1 implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String cod = (String)bCod.getSelectedItem();
			String text = comp.testPart(cod, 1, tReg1.getText());
			if (text.isEmpty()){  
				if (cod.equals("Z") ||cod.equals("S")) sComm.requestFocus(); else tReg2.requestFocus();
			} else 	JOptionPane.showMessageDialog(ShowRule.this,text);
		}
	}
	class LsReg2 implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String cod = (String)bCod.getSelectedItem();
			String text = comp.testPart(cod, 2, tReg2.getText());
			if (text.isEmpty()){  
				if (!cod.equals("J")) sComm.requestFocus();	else tNext.requestFocus();
			} else JOptionPane.showMessageDialog(ShowRule.this,text);
		}
	}
	class LsNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String text = comp.testPart((String)bCod.getSelectedItem(), 3, tNext.getText());
			if (text.isEmpty())	sComm.requestFocus();
			else JOptionPane.showMessageDialog(ShowRule.this,text);
			
		}
	}
}
