package gui.model;

import main.*;
import model.AllModels;
import model.Model;
import db.*;
import gui.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class PnDescription extends JPanel {
	//private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!
	private FrMain fMain;
	private AllModels env=null;
	
	
	private String type = "Algorithm";
	private Model model;
	boolean isVisibleMany;
	
	private JLabel txtAlgo;
	private JTextField sName;
	private JLabel txtNumb;
	
	private JLabel txtInit;
	private JTextField sInit;
	private JLabel txtFin;
	private JTextField sFin;
	
	private JLabel txtNumeric;
	private JCheckBox isNumeric;
	private JLabel txtRank;
	private JTextField iRank;
	private JLabel txtMain;
	private JTextField sMain;
	private JLabel txtAdd;
	private JTextField sAdd;
	private JLabel txtComm;
	private JTextField sComm;
	
	
	public PnDescription(boolean isEdit){
		initialize();
		sName.setEnabled(isEdit);
		sMain.setEnabled(isEdit);
		sAdd.setEnabled(isEdit);
		isNumeric.setEnabled(isEdit);
		iRank.setEnabled(isEdit);
		sComm.setEnabled(isEdit);
		
		sInit.setEnabled(isEdit);
		sFin.setEnabled(isEdit);
		
		if (isEdit) {
			// 	встановити слухачів !!!			
			sName.addActionListener(new LssName());
			sMain.addActionListener(new LssMain());
			sAdd.addActionListener(new LssAdd());
			isNumeric.addActionListener(new LsisNumeric());
			iRank.addActionListener(new LsiRank());
			sComm.addActionListener(new LsComm());
			sInit.addActionListener(new LssInit());
			sFin.addActionListener(new LssFin());
		}
	}
	private void initialize() {
			//сформувати необхідні gui-елементи 
		txtAlgo = new JLabel("Алгоритм ");
		sName = new JTextField(10);
		sName.setMaximumSize(new Dimension(30,100));
		txtNumb = new JLabel("--------");
		
		txtInit = new JLabel("Початковий стан");
		sInit = new JTextField(3);
		sInit.setMaximumSize(new Dimension(5,100));
		txtFin = new JLabel("Заключний стан");
		sFin = new JTextField(3);
		sFin.setMaximumSize(new Dimension(5,100));
		
		txtNumeric = new JLabel("Функція ");
		isNumeric = new JCheckBox();
		isNumeric.setSelected(true);
		txtRank = new JLabel("Арність");
		iRank = new JTextField(2);
		iRank.setMaximumSize(new Dimension(3,100));
		iRank.setText("2");
		txtMain = new JLabel("Алфавіт основний");
		sMain = new JTextField(10); 
		sMain.setMaximumSize(new Dimension(20,100));
		txtAdd = new JLabel("Алфавіт додатковий");
		sAdd = new JTextField(10); 
		sAdd.setMaximumSize(new Dimension(20,100));
		txtComm = new JLabel("Опис алгоритму");
		sComm = new JTextField(50); 
		sComm.setMaximumSize(new Dimension(100,100));
		
	
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		//---------------------------
		Box headBox = Box.createHorizontalBox();
		headBox.add(Box.createGlue());
		headBox.add(txtAlgo);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(sName);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(txtNumb);
		headBox.add(Box.createGlue());
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(txtInit);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(sInit);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(txtFin);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(sFin);
		headBox.add(Box.createGlue());
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(txtNumeric);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(isNumeric);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(txtRank);
		headBox.add(Box.createHorizontalStrut(5));
		headBox.add(iRank);
		headBox.add(Box.createGlue());
		//--------------------------
		Box alfaBox = Box.createHorizontalBox();
		alfaBox.add(Box.createGlue());
		alfaBox.add(txtMain);
		alfaBox.add(Box.createHorizontalStrut(5));
		alfaBox.add(sMain);
		alfaBox.add(Box.createHorizontalStrut(10));
		alfaBox.add(txtAdd);
		alfaBox.add(Box.createHorizontalStrut(5));
		alfaBox.add(sAdd);
		alfaBox.add(Box.createGlue());
		//--------------------------------------
		Box commBox = Box.createHorizontalBox();
		commBox.add(Box.createGlue());
		commBox.add(txtComm);
		commBox.add(Box.createHorizontalStrut(5));
		commBox.add(sComm);
		commBox.add(Box.createGlue());
		//------------------------------------
		add(Box.createVerticalStrut(5));
		add(headBox);	
		add(Box.createVerticalStrut(3));
		add(alfaBox);	
		add(Box.createVerticalStrut(5));
		add(commBox);	
		add(Box.createVerticalStrut(5));
		txtInit.setVisible(false);
		sInit.setVisible(false);
		txtFin.setVisible(false);
		sFin.setVisible(false);
	}
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void setEnv(FrMain owner) {
		//this.db = db;
		fMain = owner;		
	}
	
	public void setModel(String type,Model model) {
		/*
		boolean isVisible = type.equals("Machine");
		boolean isComputer = type.equals("Computer");
		isVisibleMany = !type.equals("Recursive") && !type.equals("Calculus");
		this.type = type;
	    this.model = model;
	  	txtAlgo.setText(Model.title(type, 2)); 
		txtComm.setText("Опис " + Model.title(type, 3));
	   
	    
	    txtMain.setVisible(isVisibleMany && !isComputer);
	    sMain.setVisible(isVisibleMany && !isComputer);
	    txtAdd.setVisible(isVisibleMany && !isComputer);
	    sAdd.setVisible(isVisibleMany && !isComputer);
	    txtNumeric.setVisible(isVisibleMany && !isComputer);
	    isNumeric.setVisible(isVisibleMany && !isComputer);
	    
		txtRank.setVisible(isVisibleMany);
		iRank.setVisible(isVisibleMany);

		txtInit.setVisible(isVisible);
		sInit.setVisible(isVisible);
		txtFin.setVisible(isVisible);
		sFin.setVisible(isVisible);
		
		if (model == null) showEmpty( );
		else showModel();
		*/
	}
	
	public void show(AllModels env){
		this.env = env;
		type = env.getType();
	    model = env.getModel();
		boolean isVisible = type.equals("Machine");
		boolean isComputer = type.equals("Computer");
		isVisibleMany = !type.equals("Recursive") && !type.equals("Calculus");
		
	  	txtAlgo.setText(Model.title(type, 2)); 
		txtComm.setText("Опис " + Model.title(type, 3));
	   
	    
	    txtMain.setVisible(isVisibleMany && !isComputer);
	    sMain.setVisible(isVisibleMany && !isComputer);
	    txtAdd.setVisible(isVisibleMany && !isComputer);
	    sAdd.setVisible(isVisibleMany && !isComputer);
	    txtNumeric.setVisible(isVisibleMany && !isComputer);
	    isNumeric.setVisible(isVisibleMany && !isComputer);
	    
		txtRank.setVisible(isVisibleMany);
		iRank.setVisible(isVisibleMany);

		txtInit.setVisible(isVisible);
		sInit.setVisible(isVisible);
		txtFin.setVisible(isVisible);
		sFin.setVisible(isVisible);
		
		if (model == null) showEmpty( );
		else showModel();
	}
	
	private void showModel() {
		sName.setText(model.name);
		txtNumb.setText("Номер " + model.id);
		sComm.setText(model.descr);
		sMain.setText(model.getMain());
		sAdd.setText(model.getAdd());
		isNumeric.setSelected(model.getIsNumeric());
		iRank.setText(((Integer)model.getRank()).toString());
		//System.out.println("PnDescription:showModel:model.getIsNumeric()" + model.getIsNumeric() +" isVisibleMany "+isVisibleMany);
		txtRank.setVisible(model.getIsNumeric() && isVisibleMany);
		iRank.setVisible(model.getIsNumeric() && isVisibleMany);
		sInit.setText(model.getInit());
		sFin.setText(model.getFin());
	}
	
	private void showEmpty() {
		sName.setText("");
		txtNumb.setText("-----");
		sInit.setText("@a0");
		sFin.setText("@zz");
		sMain.setText("");
		sAdd.setText("");
		isNumeric.setSelected(true);
		iRank.setText("2");
		txtRank.setVisible(isVisibleMany);
		iRank.setVisible(isVisibleMany);
		sComm.setText("");
	}
	

	// перевіряє наявність змін та їх коректність  
	// .. змін НЕМАЄ ==> true 
	// .. є некоректні зміни ==> повідомлення + false
	// .. зміни КОРЕКТНІ==> модифікація в БД + відновлення моделі + true
	public boolean testAndSave(){
		boolean res = true;
		// 0->sName, 1->sInit, 2->sFin, 3->isNumeric, 4->iRank, 5->sMain, 6->sAdd, 7->sComm 
		boolean[] modif = new boolean[8];
		String strName, strInit="", strFin="", strMain="", strAdd="", strComm, srank="";
		boolean blnNumeric=true;
		int intRank=0;
		for (int i=0;i<8;i++) modif[i] = false;  // не змінилося жодного елементу
		strName=sName.getText();
		modif[0] = (!strName.equals(model.name));
		if (type.equals("Machine")){
			strInit = sInit.getText(); modif[1] = (!strInit.equals(model.getInit()));
			strFin = sFin.getText(); modif[2] = (!strFin.equals(model.getFin()));
		}
		if(!type.equals("Recursive") && !type.equals("Calculus")&& !type.equals("Computer")){
			blnNumeric = isNumeric.isSelected();
			modif[3] = (blnNumeric!=model.getIsNumeric());
		}
		if(!type.equals("Recursive") && !type.equals("Calculus")){
			srank = iRank.getText();
			if (StringWork.isPosNumber(srank)){
				intRank = new Integer(srank); modif[4]=(intRank!=model.getRank());
			} else modif[4]=true;
		}
		if(!type.equals("Recursive") && !type.equals("Calculus")&& !type.equals("Computer")){
			strMain = sMain.getText(); modif[5] = (!strMain.equals(model.getMain()));
			strAdd = sAdd.getText(); modif[6] = (!strAdd.equals(model.getAdd()));
		}
		strComm=sComm.getText();
		modif[7] = (!strComm.equals(model.descr));
		for(int i=0;i<8;i++) {if (modif[i]) res = false;}
		
		if (!res){
			// Є зміни потрібна перевірка нових значеннь
			ArrayList <String> mess = new ArrayList();  // message about errors. 
			String text="";
			res = true;
		 	if (modif[0]){  //перевіряємо коректність імені !!!!
		 		text = testName(strName);
		 		if(!text.isEmpty()) mess.add(text);
		 	}
		 	if (modif[1]){
		 		if (!StringWork.isState(strInit))mess.add("Формат стану - @STT. Стан \"" + strInit + "\" не коректний !");
		 	}
		 	if (modif[2]){
		 		if (!StringWork.isState(strFin))mess.add("Формат стану - @STT. Стан \"" + strFin + "\" не коректний !");
		 	}
		 	if (modif[3]){
		 		if(blnNumeric && !StringWork.isOnlyAlfa("|#", strMain))
		 			mess.add("Основний алфавіт функції не може включати символи відмінні від | або # ");  
		 	}
		 	if (modif[4]){
		 		if(blnNumeric && !StringWork.isPosNumber(srank))mess.add("Арність - додатнє ціле число ! ");
		 	}
		 	if (modif[5]){
		 		if(blnNumeric && !StringWork.isOnlyAlfa("|#", strMain))
		 			mess.add("Основний алфавіт функції не може включати символи відмінні від | або # ");  
		 	}
		 	if (modif[5] || modif[6]){
		 		text = model.iswfModelAlfa(strMain+strAdd);
		 		if(!text.isEmpty()) mess.add(text);
		 	}
		 	res = (mess.size()==0);
		 	if (res){
		 		// Є зміни КОРЕКТНІ ==> модифікація в БД + відновлення моделі + true
		 		if (modif[0]) model.name = strName;
		 		if (modif[1]) model.setInit(strInit);
		 		if (modif[2]) model.setFin(strFin);
		 		if (modif[3]) model.setIsNumeric(blnNumeric);
		 		if (modif[4]) model.setRank(intRank);
		 		if (modif[5]) model.setMain(strMain);
		 		if (modif[6]) model.setAdd(strAdd);
		 		if (modif[7]) model.descr = strComm;
		 		//db.editModel(type,model);
		 		env.editModel(model);
			} else {
		 		String[] aMess = new String[mess.size()];
		 		for(int i=0; i<mess.size();i++) aMess[i]=mess.get(i);
		 		JOptionPane.showMessageDialog(PnDescription.this,aMess);
		 		showModel();
		 	}
		 	//fMain.setModel(type, model.id);
		}
		return res;
	}
		
	// Класи слухачі 
	class LssName implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else {
				if (testAndSave()) {
					if (type.equals("Machine")) sInit.requestFocus();
					else sMain.requestFocus();
				}	
			}
		}	
	}
	class LssMain implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else {
				if (testAndSave())sAdd.requestFocus();
			}
		}	
	}
	class LssAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (testAndSave())sComm.requestFocus();
		}	
	}
	class LsisNumeric implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model != null){
				if(isNumeric.isSelected()){
					String tempAdd = sAdd.getText(); 
					tempAdd = StringWork.isAlfa("|#", tempAdd);
					tempAdd = tempAdd + StringWork.isAlfa(tempAdd +"|#", sMain.getText());
					sMain.setText("|#");
					sAdd.setText(tempAdd);	
				}
				if (testAndSave()){
					txtRank.setVisible(isNumeric.isSelected());
					iRank.setVisible(isNumeric.isSelected());
					if(isNumeric.isSelected()) iRank.requestFocus(); 
					else sComm.requestFocus();
				}
			} else showEmpty();
		}	
	}
		
	class LsiRank implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else {
				if (testAndSave()){
					if (!type.equals("Computer") && !type.equals("Recursive")) sMain.requestFocus();
					else sComm.requestFocus();
				}
			}
		}	
	}
	class LsComm implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else {testAndSave();}
		}	
	}
		
	class LssInit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else { 
				if (testAndSave())sFin.requestFocus();
			}
		}	
	}
	
	class LssFin implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (model == null) showEmpty();
			else {
				if (testAndSave())sMain.requestFocus();

			}
		}	
	}
	
	private String testName(String name){
		String text="";
		if(!StringWork.isIdentifer(name))
			text = "Ім\"я " + Model.title(type, 3) + " " + name + " - не ідентифікатор!";
		if((text.isEmpty()) && (env.isNameUse(name)))  ///(db.isModel(type,  name)))
			text = Model.title(type, 2) + " з іменем " + name + " вже існує !";
		return text;
	}
}
