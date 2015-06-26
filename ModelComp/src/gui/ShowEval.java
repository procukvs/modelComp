package gui;

import main.*;
import db.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class ShowEval extends JPanel {
	private JLabel lParam;
	private JLabel lInit;
	private JLabel lFunction;
	JTextField tParam[];
	JTextField tInit;
	JLabel lNodef;
	JTextField tNodef;
	JTextField tResult;
	JLabel lStep;
	JTextField tStep;
	private JButton see;
	private ShowWork owner;
	private String main = "";
	private int rank = 0;
	private Model model;
	private String type;
	
	
	ShowEval(ShowWork owner) {
		//сформувати необхідні gui-елементи 
		lParam = new JLabel("Аргументи");
		lInit = new JLabel("Початкове слово");
		lFunction = new JLabel("Значення функції");
		tParam = new JTextField[10];//JTextField(30);
		String si;
		for (int i = 0; i < 10; i++){
			tParam[i] = new JTextField(2);
			//tParam[i].setColumns(5);
			tParam[i].setMaximumSize(new Dimension(10,20));
			si = ((Integer)i).toString();
			tParam[i].setActionCommand(si);
			//tParam[i].addActionListener(new LtParam());
		}
		tInit = new JTextField(30);
		tInit.setMaximumSize(new Dimension(100,20));	
		//tParam.setMaximumSize(new Dimension(100,20));
		lNodef = new JLabel("Невизначено");
		
		tNodef = new JTextField(5);
		tNodef.setMaximumSize(new Dimension(40,20));
		JLabel lResult = new JLabel("Результат");
		tResult = new JTextField(30);
		tResult.setMaximumSize(new Dimension(100,20));
		tResult.setEditable(false);
		lStep = new JLabel("Виконано кроків");
		tStep = new JTextField(5);
		tStep.setMaximumSize(new Dimension(40,20));
		tStep.setEditable(false);
		see = new JButton("Показати результат");
		
		this.owner = owner;
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//---------------------------
		Box init = Box.createHorizontalBox();
		init.add(Box.createGlue());
		init.add(lParam);
		for(int i = 0; i < 10; i++)	init.add(tParam[i]);
		init.add(lInit);
		init.add(tInit);
		init.add(Box.createGlue());
		init.add(lNodef);
		init.add(tNodef);
		init.add(lFunction);
		init.add(Box.createGlue());
		Box result = Box.createHorizontalBox();
		result.add(Box.createGlue());
		result.add(lResult);
		result.add(tResult);
		result.add(Box.createGlue());
		result.add(lStep);
		result.add(tStep);
		result.add(see);
		result.add(Box.createGlue());
		//------------------------------------
		add(Box.createGlue());
		add(Box.createVerticalStrut(5));
		add(init);	
		add(Box.createVerticalStrut(3));
		add(result);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
		
		// встановити слухачів !!!			
		for (int i = 0; i < 10; i++) tParam[i].addActionListener(new LtParam());
		tInit.addActionListener(new LtInit());
		see.addActionListener(new LSee());
		
	}
	
	public void setModel(String type, Model model) {
		//Algorithm algo = (Algorithm) model;
		this.model = model;
		this.type = type;
		boolean isPost = type.equals("Post");
		if(!type.equals("Recursive")){
			rank = model.getRank();
			main = model.getMain();
			
			for(int i = 0; i < 10; i++)	tParam[i].setVisible(false);
			lInit.setVisible(!model.getIsNumeric());
			tInit.setVisible(!model.getIsNumeric());
			lParam.setVisible(model.getIsNumeric());
			if(model.getIsNumeric()){
				for(int i = 0; i < rank; i++){
					tParam[i].setVisible(true);
					tParam[i].setText("");
					//	if (i < model.rank-1) tParam[i].setNextFocusableComponent(tParam[i+1]);
				}
				//tParam[0].setFocusable(true);
				tParam[0].requestFocus();
			} else {
				tInit.setText(""); 
				tInit.requestFocus();
			}
		} else{
			for(int i = 0; i < 10; i++)	tParam[i].setVisible(false);
			lInit.setVisible(false);
			tInit.setVisible(false);
			lParam.setVisible(true);	
		}
		
		tNodef.setText("1000");	
		tResult.setText("");
		tStep.setText("");
		lNodef.setVisible(!isPost);
		tNodef.setVisible(!isPost);
		lFunction.setVisible(isPost);
		lStep.setVisible(!isPost);
		tStep.setVisible(!isPost);
		see.setVisible(isPost); 
	}
	
	public void setFunction(Function f) {
		rank = f.getRank();
		for(int i = 0; i < f.getRank(); i++){
			tParam[i].setVisible(true);
			tParam[i].setText("");
			//	if (i < model.rank-1) tParam[i].setNextFocusableComponent(tParam[i+1]);
		}
		tParam[0].requestFocus();
	}
	
	// перевіряє встановлені аргументи і кількiсть кроків на коректність !!! 
	public String testArguments(){
		String text = "";
		if (!type.equals("Post")){
			if(type.equals("Recursive") || type.equals("Computer") || model.getIsNumeric()){
				for(int i=0; i < rank; i++) 
					if(!StringWork.isNatur(tParam[i].getText())) text = text + tParam[i].getText() + ":"; 
				if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
			}	
			else text = testInitial();
			if (text.isEmpty()) 
				if(!StringWork.isNatur(tNodef.getText())) text = "Кількість кроків " + tNodef.getText() + " не натуральне число.";
		}
		return text;
	}
	
	private String testInitial() {
		String sInit = tInit.getText();
		String noAlfa = StringWork.isAlfa(main, sInit);
		if(!noAlfa.isEmpty())
			noAlfa = "Вхідне слово " + sInit + " містить символи " + noAlfa + " що не належать основному алфавіту !"; 
		return noAlfa;
	}
	
	public Object getArguments() {
		if(type.equals("Recursive") || type.equals("Computer")|| model.getIsNumeric()){
			int[] arg = new int[rank];
			for(int i=0; i < rank; i++)  arg[i] = new Integer(tParam[i].getText());
			return arg;
		} 
		else return tInit.getText();
	}
	
	public int getNodef() { 
		return new Integer(tNodef.getText());
	}
	
	public void setResult(String text,int step)	{
		tResult.setText(text); 
		if (step == -1) tStep.setText(""); else tStep.setText("" + step);
	}
	
	class LtParam implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String sindex = e.getActionCommand();
			Integer index = new Integer(sindex);
			String sParam = tParam[index].getText();
			tResult.setText("");
			tStep.setText("");
			if (!type.equals("Post")) {
				owner.show.setEnabled(false);
				owner.showSteps.setVisible(false);
				owner.pack();
			}	
			if(StringWork.isNatur(sParam)) {
				if(index < rank-1) tParam[index+1].requestFocus();
				else if (!type.equals("Post"))tNodef.requestFocus();
				else see.requestFocus();
			} else {
				JOptionPane.showMessageDialog(ShowEval.this, 
						"Значення " + sindex + " аргументу " + sParam + " - не натуральне число !");
			}
		}	
	}
	
	class LtInit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String sInit = tInit.getText();
			String noAlfa = StringWork.isAlfa(main, sInit);
			//System.out.println("init=" + sInit+ " main="+main + " noAlfa="+noAlfa);
			tResult.setText("");
			tStep.setText("");
			owner.show.setEnabled(false);
			owner.showSteps.setVisible(false);
			owner.pack();
			if(noAlfa.isEmpty()) tNodef.requestFocus();
			else JOptionPane.showMessageDialog(ShowEval.this, 
						"Вхідне слово " + sInit + " містить символи " + noAlfa + 
							" що не належать основному алфавіту !");
		}	
	}
	class LSee implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "";
			String sParam;
			String input = "";
			//JOptionPane.showMessageDialog(ShowEval.this, "See result");
			for (int i = 0; i < rank; i++) {
				sParam = tParam[i].getText();
				if(StringWork.isNatur(sParam)) {
					if (i> 0) input = input + "#";
					input = input + StringWork.toInternal(new Integer(sParam));
				} else text = text + " " + sParam;
			}
			if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
			if(text.isEmpty()) {
				//=================================
				//sl = model.eval(input, nodef);
				//text = model.takeResult(sl, nodef);
				text = ((Post)model).findResult(input); 
				tResult.setText(text);
				//showEval.tStep.setText(sl.size()+"")
			} else JOptionPane.showMessageDialog(ShowEval.this, text);
		}
	}

}
