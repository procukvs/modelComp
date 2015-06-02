package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import main.*;
import db.*;

public class ShowWork extends JDialog {
	private JLabel lWhat;
	private ShowDescription showDescription;
	private ShowEval showEval;
	//private ShowTest showTest;
	ShowSteps showSteps;
	JButton eval;
	JButton show;
	JButton quit;
	private String type = "Algorithm";
	private Model model = null;
	private boolean isNumeric = true;
	private int rank = 2;
	private String main = "";
	private ArrayList sl = null;
	//private boolean visibleSteps = false;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowWork(ShowModels owner){
	//ShowWork(ShowMenu owner){
		super(owner, "mWork");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		lWhat = new JLabel("Робота з нормальним алгоритмом");
		//lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		showDescription = new ShowDescription(false);
		showEval = new ShowEval(this);
		//showTest = new ShowTest(owner);
		showSteps = new ShowSteps();
		
		eval = new JButton("Виконати");
		show = new JButton("Переглянути");
		quit = new JButton("Вийти");
		
		// формуємо розміщення
		setLayout(new BorderLayout());
		Box headBox = Box.createVerticalBox();
		//evalBox.setBorder(new EtchedBorder());
		headBox.add(lWhat);
		headBox.add(showDescription);
		//-----------------------------
		Box evalBox = Box.createVerticalBox();
		//evalBox.setBorder(new EtchedBorder());
		evalBox.add(showEval);
		evalBox.add(showSteps);
		//-----------------------------
		
		Box buttons = Box.createHorizontalBox();
		buttons.add(Box.createGlue());
		buttons.add(eval);
		buttons.add(Box.createGlue());
		buttons.add(show);
		buttons.add(Box.createGlue());
		buttons.add(quit);
		buttons.add(Box.createGlue());
		//---------------------
		add(headBox, BorderLayout.NORTH);
		//===================================
		//add(showEval, BorderLayout.CENTER);
		add(evalBox,BorderLayout.CENTER);
		//===================================
		add(buttons, BorderLayout.SOUTH);
		//setSize(200,500);
		//pack();
		
		
		// встановити слухачів !!!			
		eval.addActionListener(new LsEval());
		show.addActionListener(new LsShow());
		quit.addActionListener(new LsQuit());
		
	}	
	
	public void setModel(String type,Model model) {
		Algorithm algo = (Algorithm)model; 
		this.type = type;
	    this.model = model;
	    isNumeric = algo.isNumeric;
	    rank = algo.rank;
	    main = algo.main;
	    
	    showDescription.setModel(type, model);
	    showEval.setModel(type, model);
	    
    	
		show.setEnabled(false);
		//System.out.println("setModel......panel");
		showSteps.setVisible(false);
		pack();
	}

	class LsEval implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			// testing parameters
			String text = "";
			String input = "";
			String sParam;
			if (isNumeric){
				for (int i = 0; i < rank; i++) {
					sParam = showEval.tParam[i].getText();
					if(StringWork.isNatur(sParam)) {
						if (i> 0) input = input + "#";
						input = input + StringWork.toInternal(new Integer(sParam));
					} else text = text + " " + sParam;
				}
				if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
			} else {
				input = showEval.tInit.getText();
				text = StringWork.isAlfa(main, input);
				if(!text.isEmpty()) text = "Вхідне слово " + input + 
							   " містить символи " + text + " що не належать основному алфавіту !";
			}
			if(text.isEmpty()) {
				//=================================
				//ArrayList sl;
				Substitution sb;
				int nodef = new Integer(showEval.tNodef.getText());
				//System.out.println("Eval begin:" + input + " " + nodef);
				sl = model.eval(input, nodef);
				text = ((Substitution)(sl.get(sl.size()- 1))).str;
			/*	for(int i = 0; i < sl.size(); i++ ){
					sb = (Substitution)sl.get(i);
					System.out.println("step:" + i + " rule: " + sb.rule + " prev: " + 
								model.extractPrev( sb) + " next: " + model.extract(sb));
				} */
				if (isNumeric) text = StringWork.transNumeric(text);
				if (sl.size() == nodef + 1) text = "Невизначено";
				//tResult.setText("=" + sl.size() + ".." + text);
				showEval.tResult.setText(text);
				showEval.tStep.setText(sl.size()+""); 
				show.setEnabled(true);
			}  else {
				show.setEnabled(false);
				//showSteps.setVisible(false);
			}
			//System.out.println("eval......panel");
			showSteps.setVisible(false);
			pack();
			//JOptionPane.showMessageDialog(WorkAlgorithm.this,text);
		}	
	}
	class LsShow implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
					//showTest.workCol();
			//showTest.setShowTest(type,model,sl);
			//showTest.show();
			//JOptionPane.showMessageDialog(ShowWork.this,"Show");
			show.setEnabled(false);
			showSteps.setShowSteps(type, model, sl);
			//System.out.println("show......panel");
			showSteps.setVisible(true);
			pack();
		}	
	}
	
	class LsQuit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(WorkAlgorithm.this,"Quit");
			hide();
		}	
	}

}
