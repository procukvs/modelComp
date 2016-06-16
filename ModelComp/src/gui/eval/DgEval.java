package gui.eval;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.sql.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Date;

import main.*;
import db.*;
import gui.FrMain;
import gui.PnTree;
import gui.model.PnDescription;

public class DgEval extends JDialog {
	private JLabel lWhat;
	private PnDescription pDescription;
	private PnForming pForming;
	private PnValFunction pValFunction;
	private PnSelFunction pSelFunction;
	private PnEvalFunctionString pEval;
	private PnEvExpr pEvExpr;   //!!!!!!!!!!!!!!!!!!!!!!
	PnStepsTable pStepsTable; 
	// повинно бути доступним для управління його видимістю (або необхідно створити відповідний метод)
	PnTree showTree;
		
	private Box evalBox ;
	private Box headBox; 
	private Box endBox;
	
	JButton eval;
	JButton show;
	JButton quit;
	
	//==========================
	private AllModels env=null;
	
	private String type = "Algorithm";
	private Model model = null;
	private Function f = null;
	private LambdaDecl ld = null;
	private boolean isNumeric = true;
	private int rank = 2;
	private String main = "";
	private ArrayList sl = null;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public DgEval(FrMain owner){
		super(owner, "Work");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		lWhat = new JLabel("Робота з нормальним алгоритмом");
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		pDescription = new PnDescription(false);
		//showForm = new ShowForm(this);  //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pForming = new PnForming();        //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pForming.setParent(this);         //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pValFunction = new PnValFunction();
		pSelFunction = new PnSelFunction();  // !!!!!!ref!!!!!!!!!!!!!!!!!!
		pSelFunction.setEnv(this);
		//showEval = new ShowEval(this);  // !!!!!!ref!!!!!!!!!!!!!!!!!!
		pEval = new PnEvalFunctionString();        // !!!!!!ref!!!!!!!!!!!!!!!!!!  
		pEval.setParent(this);        // !!!!!!ref!!!!!!!!!!!!!!!!!!
		pEvExpr = new PnEvExpr();           //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pStepsTable = new PnStepsTable();
		
		eval = new JButton("Виконати");
		show = new JButton("Переглянути");
		quit = new JButton("Вийти");
		
		// формуємо розміщення
		getContentPane().setLayout(new BorderLayout());
		headBox = Box.createVerticalBox();
		headBox.add(lWhat);
		headBox.add(pDescription);
		//-----------------------------
		evalBox = Box.createVerticalBox();
		evalBox.add(pForming);
		evalBox.add(pValFunction);
		evalBox.add(pSelFunction);
		evalBox.add(pEval);
		evalBox.add(pEvExpr);                             //!!!!!!!!!!!!!!!!!!!!!!
		evalBox.add(pStepsTable);
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
		endBox.add(buttons);
		//--------------------
		getContentPane().add(headBox, BorderLayout.NORTH);
		//===================================
		getContentPane().add(evalBox,BorderLayout.CENTER);
		//===================================
		getContentPane().add(endBox, BorderLayout.SOUTH);  //buttons
		pForming.setVisible(false);
		pSelFunction.setVisible(false);
		setSize(560,684);
	
		
		// встановити слухачів !!!	
		eval.addActionListener(new LsEval());
		show.addActionListener(new LsShow());
		quit.addActionListener(new LsQuit());
		
	}	
	
	
	/*
	public void setModel(String type,Model model) {
		this.type = type;
	    this.model = model;
	    
	    if (showTree != null)evalBox.remove(showTree);
	     lWhat.setText("Робота з " + Model.title(type, 12));		
	    lWhat.setAlignmentX(CENTER_ALIGNMENT);
	    if(!type.equals("Recursive") && !type.equals("Calculus")){
	    	isNumeric = model.getIsNumeric();
	    	rank = model.getRank();
	    	main = model.getMain();
	    } 
	    pDescription.show(env);
	    
	    pForming.setVisible(type.equals("Post"));
	    pValFunction.setVisible(false);
	    pSelFunction.setVisible(type.equals("Recursive"));
	    pEval.setVisible(!type.equals("Post") && !type.equals("Calculus") && !type.equals("Recursive"));
	    pEvExpr.setVisible(type.equals("Calculus"));        // !!!!!!!!!!!!!!!!!!!!!
	    pStepsTable.setVisible(false);
	    
		eval.setText("Виконати");
		show.setText("Переглянути");  
	    switch(type){
		case "Post":
			pForming.setModel(type, model);
	    	pValFunction.setModel(type, model);
	      	eval.setText("Формувати дані");
	    	show.setText("Переглянути дані");
			break;
		case "Recursive":
			pSelFunction.setModel((Recursive)model);
			eval.setVisible(model.program.size()>0);
			break;
		case "Calculus":
			pEvExpr.setModel(type, model);
			break;
		default:
			System.out.println("DgEval:SetModel " + type + " " + model.id );
			pEval.setModel(type, model);
	    }	
	    show.setEnabled(false);
		pack();
	}
	*/
	public void show(AllModels env){
		this.env=env;
		this.type = env.getType();
		this.model = env.getModel();
		if (showTree != null)evalBox.remove(showTree);
	   	lWhat.setText("Робота з " + Model.title(type, 12));		
	    lWhat.setAlignmentX(CENTER_ALIGNMENT);
	    if(!type.equals("Recursive") && !type.equals("Calculus")){
	    	isNumeric = model.getIsNumeric();
	    	rank = model.getRank();
	    	main = model.getMain();
	    } 
	    pDescription.show(env);
	    
	    pForming.setVisible(type.equals("Post"));
	    pValFunction.setVisible(false);
	    pSelFunction.setVisible(type.equals("Recursive"));
	    pEval.setVisible(!type.equals("Post") && !type.equals("Calculus") && !type.equals("Recursive"));
	    pEvExpr.setVisible(type.equals("Calculus"));        // !!!!!!!!!!!!!!!!!!!!!
	    pStepsTable.setVisible(false);
	    
		eval.setText("Виконати");
		show.setText("Переглянути");  
	    switch(type){
		case "Post":
			//pForming.setModel(type, model);
			pForming.show(env);
	    	//pValFunction.setModel(type, model);
			pValFunction.show(env);
	      	eval.setText("Формувати дані");
	    	show.setText("Переглянути дані");
			break;
		case "Recursive":
			//pSelFunction.setModel((Recursive)model);
			pSelFunction.show(env);
			eval.setVisible(model.program.size()>0);
			break;
		case "Calculus":
			//pEvExpr.setModel(type, model);
			pEvExpr.show(env);
			break;
		default:
			//System.out.println("DgEval:show2 " + type + " " + model.id );
			//pEval.setModel(type, model);
			pEval.show(env);
	    }	
	    show.setEnabled(false);
		pack();
	}
/*
	// викликається зразу після setModel !!!!! 
	public void setLambdaDecl(LambdaDecl f) {
		this.ld = f;
		//rank = f.getRank();
		lWhat.setText("Робота з " + Model.title(type, 12) + ".  Функція " + f.getName() +".");
		showFunction.setLambdaDecl(f);
		//showEval.setFunction(f);
	}	
*/	
	public void resertFromShowStep(){
		pValFunction.setVisible(false);
		pEval.setVisible(false);
		pStepsTable.setVisible(false);
		show.setEnabled(false);
		pack();
		eval.requestFocus();	
	}
	
	public void setShow(boolean isEnabled){show.setEnabled(false);}
	class LsEval implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "";
			int nodef=0 ;
			       // after showEval change definition !!!!!!! 
//			int[] arg = {0};
			int[] arg ;
			show.setEnabled(false);
			pStepsTable.setVisible(false);
			pEval.setVisible(!type.equals("Recursive"));
			switch(type){
			case "Post":
				pEval.setVisible(false);
				pack();
				sl = pForming.formingData();
				if (sl != null){
					pValFunction.setVisible(model.getIsNumeric());
					pack();
					show.setEnabled(true);	
				}; break;
			case "Recursive":
				if (showTree != null)evalBox.remove(showTree);
				pack();
				text = pSelFunction.testArguments();
				//System.out.println("LsEval:  "+ type + ".." );
				if (text.isEmpty()){
					nodef = pSelFunction.getNodef();
					arg = (int[])pSelFunction.getArguments();
					text = ((Recursive) model).evalFunction(pSelFunction.getFunction(), arg, nodef);
					pSelFunction.setResult(text, ((Recursive) model).getAllStep());
					show.setEnabled(true);
				} 
				else pSelFunction.setResult(text, -1);
				//System.out.println("DEval:LsEval1: " );
				break;
			case "Calculus":
				//ArrayList sl;
				LamStep res;
				int step;
				pEval.setVisible(false);
				show.setEnabled(false);
				pack();
				text = pEvExpr.getLambda();
				nodef = pEvExpr .getNodef();
				//System.out.println("Eval Lambda " + text + " on " + nodef + " teps!!" );
				if (!text.isEmpty()){
					sl = ((Calculus)model).eval(text, nodef, pEvExpr.getTest());
					step = sl.size()- 1;
					res = (LamStep)sl.get(step);
					if (res.getWhat().equals("Err")){
						pEvExpr.setResult(res.getName(), ""+(nodef+1));
					} else{
						Lambda rLd = ((Calculus)model).compressFull(res.getTerm()[0]);
						pEvExpr.setResult(rLd.toStringShort(0), res.getName());
						//showEval.setVisible(showEvalLambda.getTest());
						show.setEnabled(pEvExpr.getTest());
					}
				} else {
					pEvExpr.setResult("Введіть ламбда-вираз для обрахування !", "");
					//System.out.println("Введіть ламбда-вираз для обрахування !" );
				}
				break;
			default:
				text = pEval.testArguments();
				if (text.isEmpty()){
					nodef = pEval.getNodef();
					if(type.equals("Computer")) 
						sl = model.eval((int[])pEval.getArguments(), nodef);
					else sl = model.eval((String)pEval.getArguments(), nodef);
					//System.out.println("ShowWork: Eval sl " + ((sl==null)?"Null": ""+sl.size() ));
					pEval.setResult(model.takeResult(sl, nodef), model.takeCountStep(sl,nodef));
					show.setEnabled(true);
					//System.out.println("ShowWork: Eval sl2 " + ((sl==null)?"Null": ""+sl.size() ));
				}
				else pEval.setResult(text, -1);
				pStepsTable.setVisible(false);
				pack();
			}
			//System.out.println("DEval:LsEval2: " );
		}
	}
		
	class LsShow implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			show.setEnabled(false);
			if (type.equals("Recursive")){
//				     int arg[] = {0};  int nodef = 0;  // after showEval delete !!!
				int[] arg = (int[])pSelFunction.getArguments();
				int nodef = pSelFunction.getNodef();
				f = pSelFunction.getFunction();
				String result = (((Recursive) model).getNoUndef()?""+((Recursive) model).getResult():"...");
				String sRoot = f.getName() + "<" + StringWork.argString(arg) + ">=" + result;
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(sRoot,true);
				((Recursive) model).testFunction(f, arg, nodef, root);
				if (showTree != null)evalBox.remove(showTree);
				showTree = new PnTree(root);
				evalBox.add(showTree);
				pack();
			} else {
				//System.out.println("ShowWork: showsteps Show " + ((sl==null)?"Null": ""+sl.size() ) );
				pStepsTable.setShowSteps(type, model, sl);
				pStepsTable.setVisible(true);
				pStepsTable.showGroup.th.setSelected(true);
				pack();
			}	
		}	
	}
	
	class LsQuit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
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
//							   sParam = "0";  // after showEval delete
							sParam = pEval.tParam[i].getText();
							if(StringWork.isNatur(sParam)) {
								arg[i] = new Integer(sParam);
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
						input = pEval.tInit.getText();
						text = StringWork.isAlfa(main, input);
						if(!text.isEmpty()) text = "Вхідне слово " + input + 
											" містить символи " + text + " що не належать основному алфавіту !";
					}
					if(text.isEmpty()) {
//						       sParam = "0";  // after showEval delete
						sParam = pEval.tNodef.getText();
						if(!StringWork.isNatur(sParam)) 	text = "Кількість кроків " + sParam + " не натуральне число.";
						else nodef = new Integer(pEval.tNodef.getText());
					}	
					if(text.isEmpty()) {
						//=================================
						if(type.equals("Computer")) sl = model.eval(arg, nodef);
						else sl = model.eval(input, nodef);
						text = model.takeResult(sl, nodef);
						pEval.tResult.setText(text);
						if(type.equals("Computer")) pEval.tStep.setText((sl.size()-2) +""); 
						else pEval.tStep.setText(sl.size()+""); 
						show.setEnabled(true);
					}  else {
						pEval.tResult.setText(text);
						show.setEnabled(false);
					}
					pStepsTable.setVisible(false);
					pack();
				} else {
					// Recursive !!!!!!
					if (showTree != null)evalBox.remove(showTree);
					show.setEnabled(false);
					pStepsTable.setVisible(false);
					pack();
					text = pEval.testArguments();
					if (text.isEmpty()){
						text = ((Recursive) model).evalFunction(f, (int[])pEval.getArguments(), pEval.getNodef());
						pEval.setResult(text, ((Recursive) model).getAllStep());
						show.setEnabled(true);
					} 
					else pEval.setResult(text, -1);
				}
			} 
			else {
				pEval.setVisible(false);
				pStepsTable.setVisible(false);
				show.setEnabled(false);
				pack();
				                  sParam ="0";  // after showStep  delete
				sParam = pForming.getTStep();
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
						pForming.setMessage(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
						cnt = post.stepForm(i+1);
					}
					cur = new Date();
					sl = post.finalForm();
					pForming.setMessage(text + formatter.format(cur) + " закінчено. За " + step + " кроків створено " + sl.size() + ".");
					pEval.setVisible(post.getIsNumeric());
					pack();
					show.setEnabled(true);
					
				}
				else {
					text = "Кількість кроків - не натуральнe число!";
					pForming.setMessage(text);
					pForming.setFocusIntStep();
				}	
			}
		}	
	}
	
}
