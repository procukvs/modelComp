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
	JTextField tParam[];
	JTextField tInit;
	JTextField tNodef;
	JTextField tResult;
	JTextField tStep;
	private ShowWork owner;
	private String main = "";
	private int rank = 0;
	
	
	ShowEval(ShowWork owner) {
		//сформувати необхідні gui-елементи 
		lParam = new JLabel("Аргументи");
		lInit = new JLabel("Початкове слово");
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
		JLabel lNodef = new JLabel("Невизначено");
		
		tNodef = new JTextField(5);
		tNodef.setMaximumSize(new Dimension(40,20));
		JLabel lResult = new JLabel("Результат");
		tResult = new JTextField(30);
		tResult.setMaximumSize(new Dimension(100,20));
		tResult.setEditable(false);
		JLabel lStep = new JLabel("Виконано кроків");
		tStep = new JTextField(5);
		tStep.setMaximumSize(new Dimension(40,20));
		tStep.setEditable(false);
		
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
		init.add(Box.createGlue());
		Box result = Box.createHorizontalBox();
		result.add(Box.createGlue());
		result.add(lResult);
		result.add(tResult);
		result.add(Box.createGlue());
		result.add(lStep);
		result.add(tStep);
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
		
	}
	
	public void setModel(String type, Model model) {
		Algorithm algo = (Algorithm) model;
		rank = algo.rank;
		main = algo.main;
		for(int i = 0; i < 10; i++)	tParam[i].setVisible(false);
    	lInit.setVisible(!algo.isNumeric);
    	tInit.setVisible(!algo.isNumeric);
    	lParam.setVisible(algo.isNumeric);
    	if(algo.isNumeric){
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
		tNodef.setText("1000");	
		tResult.setText("");
		tStep.setText("");
	}
	
	class LtParam implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String sindex = e.getActionCommand();
			Integer index = new Integer(sindex);
			String sParam = tParam[index].getText();
			tResult.setText("");
			tStep.setText("");
			owner.show.setEnabled(false);
			//System.out.println("Param......panel");
			owner.showSteps.setVisible(false);
			owner.pack();
			if(StringWork.isNatur(sParam)) {
				if(index < rank-1) tParam[index+1].requestFocus();
				else tNodef.requestFocus();
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

}
