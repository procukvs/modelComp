package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;

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
	private ShowForm showForm;
	private ShowFunction showFunction;
	private ShowEval showEval;
	ShowSteps showSteps; 
	// повинно бути доступним для управління його видимістю (або необхідно створити відповідний метод)
	private ShowTree showTree;
	//private ShowTest showTest;
		
	private Box evalBox ;
	private Box headBox; 
	private Box endBox;
	
	JButton eval;
	JButton show;
	JButton quit;
	
	//==========================
	private String type = "Algorithm";
	private Model model = null;
	private Function f = null;
	private boolean isNumeric = true;
	private int rank = 2;
	private String main = "";
	private ArrayList sl = null;
	//private boolean visibleSteps = false;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowWork(ShowModels owner){
		super(owner, "Work");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		lWhat = new JLabel("Робота з нормальним алгоритмом");
		//lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		showDescription = new ShowDescription(false);
		showForm = new ShowForm(this);
		showFunction = new ShowFunction(this);
		showEval = new ShowEval(this);
																			//showTest = new ShowTest(owner);
		showSteps = new ShowSteps();
		//========================
		//showTree = new ShowTree();
		//=====================
		
		eval = new JButton("Виконати");
		show = new JButton("Переглянути");
		quit = new JButton("Вийти");
		
		// формуємо розміщення
		setLayout(new BorderLayout());
		headBox = Box.createVerticalBox();
		//evalBox.setBorder(new EtchedBorder());
		headBox.add(lWhat);
		headBox.add(showDescription);
		//-----------------------------
		evalBox = Box.createVerticalBox();
		evalBox.add(showForm);
		evalBox.add(showFunction);
		evalBox.add(showEval);
		evalBox.add(showSteps);
		//========================
		//evalBox.add(showTree);
		//========================
		
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
		endBox = Box.createVerticalBox();
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
		showFunction.setVisible(false);
		//========================
		//showTree.setVisible(false);
		//========================
		setSize(200,500);
		//pack();
		
		
		// встановити слухачів !!!	
		//showForm.tStep.addActionListener(new LsFormStep());
		eval.addActionListener(new LsEval());
		show.addActionListener(new LsShow());
		quit.addActionListener(new LsQuit());
		
	}	
	
	public void setModel(String type,Model model) {
		this.type = type;
	    this.model = model;
	    
	    if (showTree != null)evalBox.remove(showTree);
	   
	    if(!type.equals("Recursive")){
	    	lWhat.setText("Робота з " + Model.title(type, 12));		
	    	isNumeric = model.getIsNumeric();
	    	rank = model.getRank();
	    	main = model.getMain();
	    } 
	    else lWhat.setText("Робота з " + Model.title(type, 12) + ".  Обчислити функцію.");
	    lWhat.setAlignmentX(CENTER_ALIGNMENT);
	    showDescription.setModel(type, model);
	    
	    showForm.setModel(type, model);
	    showEval.setModel(type, model);
   
	    showForm.setVisible(type.equals("Post"));
	    showFunction.setVisible(type.equals("Recursive"));
	    showEval.setVisible(!type.equals("Post"));
	    showSteps.setVisible(false);
	    if (type.equals("Post")){
	    	eval.setText("Формувати дані");
	    	show.setText("Переглянути дані");
	    } else{
	    	eval.setText("Виконати");
	    	show.setText("Переглянути");
	    }
		show.setEnabled(false);
		pack();
	}
	
	// викликається зразу після setModel !!!!! 
	public void setFunction(Function f) {
		this.f = f;
		rank = f.getRank();
		lWhat.setText("Робота з " + Model.title(type, 12) + ".  Функція " + f.getName() +".");
								//showFunction.setVisible(true);
		showFunction.setFunction(f);
		showEval.setFunction(f);
		//===============================
		// тестування --показ структури тіла функції  == взаємодія вікон -- розміри
		/*
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Edit Testing");
		RecBody rb =((Recursive)model).map.get(f.getName());
		rb.formTree(root);
		if (testTree != null)evalBox.remove(testTree);
		testTree = new ShowTree(root);
		//testTree.setMaximumSize(new Dimension(600,100));
		//testTree.setSize(100,100);
		evalBox.add(testTree);
		//testTree.setVisible(false);
		pack();
		*/
		//============================
	}

	public void resertFromShowStep(){
		showEval.setVisible(false);
		showSteps.setVisible(false);
		show.setEnabled(false);
		pack();
		eval.requestFocus();	
	}
	
	class LsEval implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "";
			int nodef=0 ;
			int[] arg;
			show.setEnabled(false);
			showSteps.setVisible(false);
			showEval.setVisible(true);
			switch(type){
			case "Post":
				showEval.setVisible(false);
				pack();
				sl = showForm.formingData();
				if (sl != null){
					showEval.setVisible(model.getIsNumeric());
					pack();
					show.setEnabled(true);	
				}; break;
			case "Recursive":
				if (showTree != null)evalBox.remove(showTree);
				pack();
				text = showEval.testArguments();
				if (text.isEmpty()){
					nodef = showEval.getNodef();
					arg = (int[])showEval.getArguments();
					text = ((Recursive) model).evalFunction(f, arg, nodef);
					showEval.setResult(text, ((Recursive) model).getAllStep());
					show.setEnabled(true);
				} 
				else showEval.setResult(text, -1);
				break;
			default:
				text = showEval.testArguments();
				if (text.isEmpty()){
					nodef = showEval.getNodef();
					if(type.equals("Computer")) 
						sl = model.eval((int[])showEval.getArguments(), nodef);
					else sl = model.eval((String)showEval.getArguments(), nodef);
					//text = model.takeResult(sl, nodef);
					showEval.setResult(model.takeResult(sl, nodef), model.takeCountStep(sl,nodef));
					//showEval.tResult.setText(text);
					//if(type.equals("Computer")) showEval.tStep.setText((sl.size()-2) +""); 
					//else showEval.tStep.setText(sl.size()+""); 
					show.setEnabled(true);
				}
				else showEval.setResult(text, -1);
				showSteps.setVisible(false);
				pack();
			}
		}
	}
		
	class LsShow implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			show.setEnabled(false);
			if (type.equals("Recursive")){
				int[] arg = (int[])showEval.getArguments();
				int nodef = showEval.getNodef();
				String result = (((Recursive) model).getNoUndef()?""+((Recursive) model).getResult():"...");
				String sRoot = f.getName() + "<" + StringWork.argString(arg) + ">=" + result;
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(sRoot,true);
				((Recursive) model).testFunction(f, arg, nodef, root);
				if (showTree != null)evalBox.remove(showTree);
				showTree = new ShowTree(root);
						//testTree.setMaximumSize(new Dimension(1600,100));
						//testTree.setSize(100,100);
				evalBox.add(showTree);
							//endBox.add(testTree);
							//testTree.setVisible(false);
				pack();
			} else {
				showSteps.setShowSteps(type, model, sl);
											//System.out.println("show......panel" + type + "ooo..");
				showSteps.setVisible(true);
				showSteps.showGroup.th.setSelected(true);
				pack();
			}	
		}	
	}
	
	class LsQuit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(WorkAlgorithm.this,"Quit");
			hide();
		}	
	}

	
	
	class LsEvalOLd implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			// testing parameters
			String text = "";
			String input = "";
			String sParam;
			int[] arg = new int[rank];
			int nodef=0 ;
			if (!type.equals("Post")){
				if(!type.equals("Recursive")){
					if (isNumeric){
						for (int i = 0; i < rank; i++) {
							sParam = showEval.tParam[i].getText();
							if(StringWork.isNatur(sParam)) {
								arg[i] = new Integer(sParam);
								//if (i> 0) input = input + "#";
								//input = input + StringWork.toInternal(new Integer(sParam));
							} else text = text + " " + sParam;
						}
						if(text.isEmpty()){
							if (!type.equals("Computer"))
								for (int i = 0; i < rank; i++) {
									if (i> 0) input = input + "#";
									input = input + StringWork.toInternal(arg[i]);		
								}
						} else text = "Аргументи:" + text + " - не натуральні числа";
						
					} else {
						input = showEval.tInit.getText();
						text = StringWork.isAlfa(main, input);
						if(!text.isEmpty()) text = "Вхідне слово " + input + 
											" містить символи " + text + " що не належать основному алфавіту !";
					}
					if(text.isEmpty()) {
						sParam = showEval.tNodef.getText();
						if(!StringWork.isNatur(sParam)) 	text = "Кількість кроків " + sParam + " не натуральне число.";
						else nodef = new Integer(showEval.tNodef.getText());
					}	
					if(text.isEmpty()) {
						//=================================
						if(type.equals("Computer")) sl = model.eval(arg, nodef);
						else sl = model.eval(input, nodef);
						//text = ((Substitution)(sl.get(sl.size()- 1))).str;
						//if (isNumeric) text = StringWork.transNumeric(text);
						//if (sl.size() == nodef + 1) text = "Невизначено";
						text = model.takeResult(sl, nodef);
						showEval.tResult.setText(text);
						if(type.equals("Computer")) showEval.tStep.setText((sl.size()-2) +""); 
						else showEval.tStep.setText(sl.size()+""); 
						show.setEnabled(true);
					}  else {
						showEval.tResult.setText(text);
						show.setEnabled(false);
					}
					//System.out.println("eval......panel");
					showSteps.setVisible(false);
					pack();
				} else {
					// Recursive !!!!!!
					if (showTree != null)evalBox.remove(showTree);
					show.setEnabled(false);
					showSteps.setVisible(false);
					pack();
					text = showEval.testArguments();
					if (text.isEmpty()){
						text = ((Recursive) model).evalFunction(f, (int[])showEval.getArguments(), showEval.getNodef());
						showEval.setResult(text, ((Recursive) model).getAllStep());
						show.setEnabled(true);
					} 
					else showEval.setResult(text, -1);
					
					/*
					for (int i = 0; i < rank; i++) {
						sParam = showEval.tParam[i].getText();
						//System.out.println("Recursive eval " + i + " = " + sParam +  " " + rank );
						if(StringWork.isNatur(sParam)) {
							int a = new Integer(sParam);
							arg[i] = a;
						} else text = text + " " + sParam;
					}
					if(text.isEmpty()){
						sParam = showEval.tNodef.getText();
						if(!StringWork.isNatur(sParam)) 	text = "Кількість кроків " + sParam + " не натуральне число.";
						else nodef = new Integer(showEval.tNodef.getText());
					} else text = "Аргументи:" + text + " - не натуральні числа";
					if(text.isEmpty()){
						text = ((Recursive) model).evalFunction(f, arg, nodef);
						showEval.tResult.setText(text);
						showEval.tStep.setText(((Recursive) model).getAllStep()+""); 
					*/	
									//======================================================
									//  тестування отримання дерева обрахунку для перегляду=== взаємодія вікон --розміри і т.і.
									//      після підгону можна !!!!!!!!
					/*	
						String sRoot = f.getName()+"("+StringWork.argString(arg) + ")=";
						DefaultMutableTreeNode root = new DefaultMutableTreeNode(sRoot,true);
						text = ((Recursive) model).testFunction(f, arg, nodef, root);
						showEval.tResult.setText(text);
						showEval.tStep.setText(((Recursive) model).getAllStep()+""); 
						
						if (showTree != null)evalBox.remove(showTree);
						showTree = new ShowTree(root);
								//testTree.setMaximumSize(new Dimension(1600,100));
								//testTree.setSize(100,100);
						evalBox.add(showTree);
									//endBox.add(testTree);
									//testTree.setVisible(false);
						pack();
					
						//=============================================================
					}
					else showEval.tResult.setText(text);
					*/	
				}
			} 
			else {
				showEval.setVisible(false);
				showSteps.setVisible(false);
				show.setEnabled(false);
				pack();
				sParam = showForm.getTStep();
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
						showForm.setMessage(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
						//showForm.lMessage.setText(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
								//showForm.revalidate();
								//showForm.repaint();
								//System.out.println(text + formatter.format(cur) + " крок " + i + " .. " + cnt + ".");
						cnt = post.stepForm(i+1);
					}
					cur = new Date();
					sl = post.finalForm();
					showForm.setMessage(text + formatter.format(cur) + " закінчено. За " + step + " кроків створено " + sl.size() + ".");
					//showForm.lMessage.setText(text + formatter.format(cur) + " закінчено. За " + step + " кроків створено " + sl.size() + ".");
									//System.out.println(text + formatter.format(cur) + " закінчено .. " + cnt + ".");
					showEval.setVisible(post.getIsNumeric());
					pack();
					show.setEnabled(true);
					
				}
				else {
					text = "Кількість кроків - не натуральнe число!";
					showForm.setMessage(text);
					//showForm.lMessage.setText(text);
					showForm.setFocusIntStep();
				}	
				//JOptionPane.showMessageDialog(ShowWork.this,"Forming dat Post!!! " + showForm.tStep.getText());
			}
		}	
	}
	/*
	class LsFormStep implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String step = showForm.getTStep();
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
	*/
	
	
}
