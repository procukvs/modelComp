package gui;

import main.*;
import db.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ShowDescription extends JPanel {
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	//private ShowModelAll showMain;
	private String type = "Algorithm";
	private Model model;
	//private Algorithm algo; 
	//private Machine mach;
	
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
	
	
	ShowDescription(boolean isEdit){
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
		sName.setEnabled(isEdit);
		sMain.setEnabled(isEdit);
		sAdd.setEnabled(isEdit);
		isNumeric.setEnabled(isEdit);
		iRank.setEnabled(isEdit);
		sComm.setEnabled(isEdit);
		
		sInit.setEnabled(isEdit);
		sFin.setEnabled(isEdit);
		txtInit.setVisible(false);
		sInit.setVisible(false);
		txtFin.setVisible(false);
		sFin.setVisible(false);
	}
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void setLink(DbAccess db, ShowModels owner) {
	//public void setLink(DbAccess db, ShowModelAll owner) {
		this.db = db;
		showMain = owner;		
	}
	
	public void setModel(String type,Model model) {
		boolean isVisible = type.equals("Machine");
		boolean isComputer = type.equals("Computer");
		boolean isVisibleMany = !type.equals("Recursive");
		this.type = type;
	    this.model = model;
	  	txtAlgo.setText(Model.title(type, 2)); 
		txtComm.setText("Опис " + Model.title(type, 3));
	    if (model == null) showEmpty( );
	    else showModel();
	    
	    txtMain.setVisible(isVisibleMany && !isComputer);
	    sMain.setVisible(isVisibleMany && !isComputer);
	    txtAdd.setVisible(isVisibleMany && !isComputer);
	    sAdd.setVisible(isVisibleMany && !isComputer);
	    txtNumeric.setVisible(isVisibleMany && !isComputer);
	    isNumeric.setVisible(isVisibleMany && !isComputer);
	    //if (model == null) txtRank.setVisible(isVisibleMany);
	    //else 	txtRank.setVisible(isVisibleMany && model.getIsNumeric());
	   // iRank.setVisible(isVisibleMany);
	    
		txtInit.setVisible(isVisible);
		sInit.setVisible(isVisible);
		txtFin.setVisible(isVisible);
		sFin.setVisible(isVisible);
	}
	private void showModel() {
		sName.setText(model.name);
		txtNumb.setText("Номер " + model.id);
		sComm.setText(model.descr);
		sMain.setText(model.getMain());
		sAdd.setText(model.getAdd());
		isNumeric.setSelected(model.getIsNumeric());
		iRank.setText(((Integer)model.getRank()).toString());
		txtRank.setVisible(model.getIsNumeric());
		iRank.setVisible(model.getIsNumeric());
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
		txtRank.setVisible(true);
		iRank.setVisible(true);
		sComm.setText("");
	}
	
	
	// Класи слухачі 
	class LssName implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String name = sName.getText();
			String text="";
			boolean go = true;
			//System.out.println(".." + com + "..");
			if (model == null) showEmpty();
			else {
				if(!name.equals(model.name)) {
					if(!StringWork.isIdentifer(name))
						text = "Ім\"я " + Model.title(type, 3) + " " + name + " - не ідентифікатор!";
					if((text.isEmpty()) && (db.isModel(type,  name)))
						text = Model.title(type, 2) + " з іменем " + name + " вже існує !";
					if(text.isEmpty()) {
						model.name = name;
						db.editModel(type,model);
						//System.out.println("name=" + name + " model.name=" + model.name);
						showMain.showModel(type, model.id);
						if (type.equals("Machine")) sInit.requestFocus();
						else sMain.requestFocus();
					} else {
						JOptionPane.showMessageDialog(ShowDescription.this,text);		
						showModel();
						go = false;
					}
				}	
				if (go) {
					//db.editModel(type,model);
					//showMain.showModel(type, model.id);
					if (type.equals("Machine")) sInit.requestFocus();
					else sMain.requestFocus();
				}	
			}
			
			//JOptionPane.showMessageDialog(ShowDescription.this,"sName....");
		}	
	}
	class LssMain implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String com = sMain.getText();
			String text;
			//System.out.println(".." + com + "..");
			if (model == null) showEmpty();
			else {
				/*
				boolean isNum = true;
				String main = "|#";
				switch(type){
				case "Algorithm": isNum = algo.isNumeric; main = algo.main; break;
				case "Machine": isNum = mach.isNumeric; main = mach.main;break;
				}
				*/
				if (!com.equals(model.getMain())) {
					if (model.getIsNumeric()) {
						text = "Основний алфавіт у функції завжди |# !";
						sAdd.setText(StringWork.unionAlfa(sAdd.getText(), com));
						sMain.setText("|#");
						JOptionPane.showMessageDialog(ShowDescription.this,text);	
					}
					model.setMain(sMain.getText());
					model.setAdd(sAdd.getText());
					db.editModel(type,model);
					showMain.showModel(type, model.id);
					//System.out.println(".Set alfa." + com + "..");
					String[] text1 = model.iswfModel();
					/*
					String[] text1 = {""};
					switch(type){
					case "Algorithm":
						algo.main = sMain.getText();
						algo.add = sAdd.getText();
						db.editModel(type,algo);
						text1 = algo.testingRules();
						break;
					case "Machine":
						mach.main = sMain.getText();
						mach.add = sAdd.getText();
						db.editModel(type,mach);
						text1 = mach.testingRules();
						break;	
					} */	
					if (text1 != null)
						JOptionPane.showMessageDialog(ShowDescription.this,text1);
				} 
				sAdd.requestFocus();
			}
			//JOptionPane.showMessageDialog(ShowDescription.this,"sMain....");
		}	
	}
	class LssAdd implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String com = sAdd.getText();
			if (model == null) showEmpty();
			else {
				//Algorithm algo = (Algorithm)model;
				if (!com.equals(model.getAdd())) {
				    model.setAdd(com);
					db.editModel(type,model);
					showMain.showModel(type, model.id);
				    String[] text= model.iswfModel();
				   // System.out.println(type + ".." + text[0] + "..");
				    if (text != null)
				    	 JOptionPane.showMessageDialog(ShowDescription.this,text);
				}
				sComm.requestFocus();
			}
			//JOptionPane.showMessageDialog(ShowDescription.this,"sAdd....");
		}	
	}
	class LsisNumeric implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			Boolean isNew;
			if (model != null){
				//Algorithm algo = (Algorithm)model;
				isNew = isNumeric.isSelected();
				if(isNew){
					if(!model.getIsNumeric()){
						// зробили модель функцією !!!!!
						model.setIsNumeric(true);
						txtRank.setVisible(true);
						iRank.setVisible(true);
						//algo.add = algo.add + StringWork.isAlfa(algo.add +"|#", algo.main);
						model.setAdd(StringWork.isAlfa("|#", model.getAdd()));
						model.setAdd(model.getAdd() + StringWork.isAlfa(model.getAdd() +"|#", model.getMain()));;
						model.setMain("|#");
						sMain.setText("|#");
						sAdd.setText(model.getAdd());
						db.editModel(type,model);
						showMain.showModel(type, model.id);
						//text = db.testingRules(model.algo, model.main+model.add);
						String[] text= model.iswfModel();
						if (text != null)
							JOptionPane.showMessageDialog(ShowDescription.this,text);
					}
				} else{
					if(model.getIsNumeric()){
						// зробили модель НЕ функцією!!
						model.setIsNumeric(false);
						txtRank.setVisible(false);
						iRank.setVisible(false);
						db.editModel(type,model);
						//showMain.showModel(type, model.id);
					}
				}
				sComm.requestFocus();
			} else showEmpty();
			//JOptionPane.showMessageDialog(ShowDescription.this,"isNumeric....");
		}	
	}
		
	class LsiRank implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String srank = iRank.getText();
			int rank = 1;
			String text;
			//System.out.println(".." + com + "..");
			if (model == null) showEmpty();
			else {
				//Algorithm algo = (Algorithm)model;
				if(StringWork.isPosNumber(srank)){
					rank = new Integer(srank); 
					if (model.getRank() != rank) {
						model.setRank(rank);
						db.editModel(type,model);
						//showMain.showModel(type, model.id);
					}
				} else{
					text = "Арність - додатнє ціле число ! ";
					JOptionPane.showMessageDialog(ShowDescription.this,text);
					showModel();
				}
			}
			//JOptionPane.showMessageDialog(ShowDescription.this,"iRank....");
		}	
	}
	class LsComm implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String com = sComm.getText();
			//System.out.println(".." + com + "..");
			if (model == null) showEmpty();
			else if (!com.equals(model.descr)) {
					model.descr = com;
					db.editModel(type,model);
					//showMain.showModel(type, model.id);
			} 
			//JOptionPane.showMessageDialog(ShowDescription.this,"sComm....");
		}	
	}
		
	class LssInit implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String init = sInit.getText();
			String text="";
			if (model == null) showEmpty();
			else {
				if (!init.equals(model.getInit())) {
					if (StringWork.isState(init)){
						model.setInit(init);
						db.editModel(type,model);
					}
					else  text = "Формат стану - @STT. Стан \"" + init + "\" не коректний !";
				}
				if (!text.isEmpty()){
					JOptionPane.showMessageDialog(ShowDescription.this, text); 
					showModel();
				}
				else sFin.requestFocus();
			}
		}	
	}
	
	class LssFin implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			String fin = sFin.getText();
			String text="";
			if (model == null) showEmpty();
			else {
				if (!fin.equals(model.getFin())) {
					if (StringWork.isState(fin)){
						model.setFin(fin);
						db.editModel(type,model);
					}
					else  text = "Формат стану - @STT. Стан \"" + fin + "\" не коректний !";
				}
				if (!text.isEmpty()){
					JOptionPane.showMessageDialog(ShowDescription.this, text); 
					showModel();
				}
				else sMain.requestFocus();
			}
		}	
	}

	
		/*
		switch (type) {
		case "Algorithm" :
			algo = (Algorithm)model;
			sMain.setText(algo.main);
			sAdd.setText(algo.add);
			isNumeric.setSelected(algo.isNumeric);
			iRank.setText(((Integer)algo.rank).toString());
			txtRank.setVisible(algo.isNumeric);
			iRank.setVisible(algo.isNumeric);
			
			break;
		case "Machine" : 
			mach = (Machine)model;
			sInit.setText(mach.init);
			sFin.setText(mach.fin);
			sMain.setText(mach.main);
			sAdd.setText(mach.add);
			isNumeric.setSelected(mach.isNumeric);
			iRank.setText(((Integer)mach.rank).toString());
			txtRank.setVisible(mach.isNumeric);
			iRank.setVisible(mach.isNumeric);
			break;
		} */
		

	
}
