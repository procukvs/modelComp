package gui;

import javax.swing.*;
import javax.swing.border.*;

import java.sql.SQLException;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.*;

import main.*;
import db.*;

public class ShowWork extends JDialog {
	private JLabel lWhat;
	private ShowDescription showDescription;
	private ShowEval showEval;
	//private ShowGroup showGroup;
	private ShowForm showForm;
	private Box evalBox ;
		
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
		showForm = new ShowForm(this);
		showEval = new ShowEval(this);
		//showTest = new ShowTest(owner);
		showSteps = new ShowSteps();
		
		//showGroup = new ShowGroup(this);
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
		evalBox = Box.createVerticalBox();
		evalBox.add(showForm);
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
		Box endBox = Box.createVerticalBox();
		//endBox.add(showGroup);
		endBox.add(buttons);
		//--------------------
		add(headBox, BorderLayout.NORTH);
		//===================================
		//add(showEval, BorderLayout.CENTER);
		add(evalBox,BorderLayout.CENTER);
		//===================================
		add(endBox, BorderLayout.SOUTH);  //buttons
		showForm.setVisible(false);
		//setSize(200,500);
		//pack();
		
		
		// встановити слухачів !!!	
		showForm.tStep.addActionListener(new LsFormStep());
		eval.addActionListener(new LsEval());
		show.addActionListener(new LsShow());
		quit.addActionListener(new LsQuit());
		
	}	
	
	public void setModel(String type,Model model) {
		//Algorithm algo = (Algorithm)model; 
		this.type = type;
	    this.model = model;
	    isNumeric = model.getIsNumeric();
	    rank = model.getRank();
	    main = model.getMain();
	    lWhat.setText("Робота з " + Model.title(type, 12));
	    lWhat.setAlignmentX(CENTER_ALIGNMENT);
	    
	    showDescription.setModel(type, model);
	    if (type.equals("Post")){
	    	//evalBox.add(showForm);
	    	showForm.tMessage.setText("");
	    	eval.setText("Формувати дані");
	    	show.setText("Переглянути дані");
	    } else{
	    	//evalBox.remove(showForm);
	    	eval.setText("Виконати");
	    	show.setText("Переглянути");
	    }
	    showForm.setVisible(type.equals("Post"));
	    showEval.setVisible(!type.equals("Post"));
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
			if (!type.equals("Post")){
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
					int nodef = new Integer(showEval.tNodef.getText());
					//System.out.println("Eval begin:" + input + " " + nodef);
					sl = model.eval(input, nodef);
					//text = ((Substitution)(sl.get(sl.size()- 1))).str;
					//if (isNumeric) text = StringWork.transNumeric(text);
					//if (sl.size() == nodef + 1) text = "Невизначено";
					text = model.takeResult(sl, nodef);
					showEval.tResult.setText(text);
					showEval.tStep.setText(sl.size()+""); 
					show.setEnabled(true);
				}  else {
					show.setEnabled(false);
				}
				//System.out.println("eval......panel");
				showSteps.setVisible(false);
				pack();
			} else {
				showEval.setVisible(false);
				showSteps.setVisible(false);
				show.setEnabled(false);
				pack();
				sParam = showForm.tStep.getText();
				if(StringWork.isPosNumber(sParam)) {
					int step = 0;
					String template = "HH:mm:ss";  
					DateFormat formatter = new SimpleDateFormat(template);
					Post post = (Post)model;
					Date cur = new Date();
					int cnt;
					step = new Integer(sParam);
					text = "Формування " + formatter.format(cur) + " : ";
					cnt = post.initialForm();
					for(int i = 0; i < step; i++){
						cur = new Date(); 
						showForm.tMessage.setText(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
						//showForm.revalidate();
						//showForm.repaint();
						//System.out.println(text + formatter.format(cur) + " крок " + i + " .. " + cnt + ".");
						cnt = post.stepForm(i+1);
					}
					cur = new Date();
					sl = post.finalForm();
					showForm.tMessage.setText(text + formatter.format(cur) + " закінчено. За " + step + " кроків створено " + sl.size() + ".");
					//System.out.println(text + formatter.format(cur) + " закінчено .. " + cnt + ".");
					showEval.setVisible(post.getIsNumeric());
					pack();
					show.setEnabled(true);
					
				}
				else {
					text = "Кількість кроків - не натуральнe число!";
					showForm.tMessage.setText(text);
					showForm.tStep.requestFocus();
				}	
				//JOptionPane.showMessageDialog(ShowWork.this,"Forming dat Post!!! " + showForm.tStep.getText());
			}
		}	
	}
	class LsFormStep implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String step = showForm.tStep.getText();
			if(StringWork.isNatur(step)) {
				showEval.setVisible(false);
				showSteps.setVisible(false);
				show.setEnabled(false);
				pack();
				eval.requestFocus();
				//JOptionPane.showMessageDialog(ShowWork.this,"Form");
			} else JOptionPane.showMessageDialog(ShowWork.this,"Кількість кроків " + step + " - не натуральне число");;
		}
		
	}
	
	class LsShow implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			show.setEnabled(false);
			showSteps.setShowSteps(type, model, sl);
											//System.out.println("show......panel");
			showSteps.setVisible(true);
			showSteps.showGroup.th.setSelected(true);
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
