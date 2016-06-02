package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import main.*;

public class ShowSteps extends JPanel {
	ModelTable dbm;
	//===========================
	
	JLabel header;
	JTable table;
	JCheckBox cInter;
	JLabel lInter;
	ShowGroup showGroup;
	private int group=1;
	private boolean internal = true;
	//JButton bQuit;
	ArrayList dataIn = new ArrayList();
	private String type = "Algorithm";
	private Algorithm algo = null;
	private Model model; 
	private ArrayList sl;
	
	ShowSteps(){
		//super("mTest.");
		//setModal(true);	
		setLayout(new BorderLayout());
		//setBorder(new LineBorder(Color.green,5));
		//setBorder(new BevelBorder(BevelBorder.LOWERED));
		//setBorder(new TitledBorder("Послідовність підстановок алгоритму"));
		String[][] inform = {
				{"Крок","I","N"},
				{"№ підст.","I","N"},
				{"До підстановки","S","N"},
				{"Пiсля підстановки","S","N"}
		};
	
		
		dbm = new ModelTable(false);
		table = new JTable(dbm);

		
	
			
		dbm.setInitialModel(inform);
		JScrollPane forTable = new JScrollPane(table);
		
		TableColumn column = null;
		int[] widthCol = {100,75,300,300};
	    for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }     

		//workCol();
		
		showGroup = new ShowGroup(this);
		showGroup.setVisible(false);
		cInter = new JCheckBox();
		lInter = new JLabel("Внутрішнє представлення");
		//bQuit = new JButton("Вийти");
		
		header = new JLabel("Послідовність підстановок алгоритму");
		header.setHorizontalAlignment(header.CENTER);
		//Container contentPane = getContentPane();
		Box control = Box.createHorizontalBox();
		control.add(showGroup);
		control.add(Box.createGlue());
		control.add(cInter);
		control.add(lInter);
		control.add(Box.createHorizontalStrut(10));
		//control.add(bQuit);
		control.add(Box.createHorizontalStrut(10));
		
		add(header,BorderLayout.NORTH);
		add(forTable,BorderLayout.CENTER);
		add(control,BorderLayout.SOUTH);
		
		//pack();
		//setSize(775,400);
		
		//bQuit.addActionListener(new TestQuit());
		cInter.addActionListener(new TestInter());
		showGroup.th.addActionListener(new TestTh());
		showGroup.all.addActionListener(new TestAll());
		showGroup.step.addActionListener(new TestStep());
	}
	
	public void setShowSteps(String type,  Model model, ArrayList sl){
		ArrayList ds = null;
		int cnt = 0;
		boolean visible = false;
		this.model = model;
		this.type = type;
		this.sl = sl;
		group = 1;
		//cInter.setSelected(model.getIsNumeric());
		//cInter.setVisible(model.getIsNumeric());
		//lInter.setVisible(model.getIsNumeric());
		if(type.equals("Algorithm")  || type.equals("Post")) visible = model.getIsNumeric();
		cInter.setSelected(type.equals("Algorithm"));
		internal = type.equals("Algorithm");
		cInter.setVisible(visible);
		lInter.setVisible(visible);
		showGroup.setVisible(type.equals("Post"));
		header.setText(Model.title(type, 13));
		ds = model.getStepSource(sl, type.equals("Algorithm"));
		dbm.setDataSource(null);
								//System.out.println("showStep....panel" + type + ".." + sl.size());
		dbm.setInitialModel(findInform(type));
		dbm.fireTableStructureChanged();
		setColumnWidth();
		if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);	
		if(type.equals("Post")) {
			if ((ds != null) && (ds.size() > 0)) cnt = ds.size();
			showGroup.lCnt.setText(" " + cnt + " ");
		}
	}
	
	
	
	
/*	
	public void setShowSteps2(String type, Model model, ArrayList subList) {
		this.model = model;
		this.type = type;
		algo = (Algorithm) model;
		cInter.setSelected(algo.isNumeric);
		cInter.setVisible(algo.isNumeric);
		lInter.setVisible(algo.isNumeric);
		sl = subList;
		formDataSource(true);
															//dbm.fireTableStructureChanged();
		dbm.setDataSource(dataIn);   // dbm.setDataSource(getStepSource(sl, true));
	}
	
	private void formDataSource(boolean internal) {
		Substitution sb;
		dataIn.clear();
		for(int i = 0; i < sl.size(); i++ ){
			sb = (Substitution)sl.get(i);
			ArrayList row = new ArrayList();
			row.add(i+1);
			row.add(sb.rule);
			if (internal) row.add(algo.extractPrev( sb));
			else row.add(StringWork.transNumeric(algo.extractPrev( sb)));
			if (internal) row.add(algo.extract(sb));
			else row.add(StringWork.transNumeric(algo.extract( sb)));
			dataIn.add(row);
		}
	}
	*/
	class TestInter implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//formDataSource(cInter.isSelected());
			//dbm.setDataSource(dataIn);
			internal = cInter.isSelected();
			if (type.equals("Algorithm"))
				dbm.setDataSource(model.getStepSource(sl, cInter.isSelected()));
			else setTablePost(internal,group);		
			/*ArrayList ds = null;
			ds = model.getStepSource(sl, cInter.isSelected());
			JOptionPane.showMessageDialog(ShowSteps.this,"testInter " + cInter.isSelected() +" " + (sl== null) + " ds " + ds.size());
			dbm.setDataSource(null);
			dbm.setInitialModel(findInform(type));
			dbm.fireTableStructureChanged();
			setColumnWidth();
			if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);	*/
		}	
	}
	class TestTh implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(ShowSteps.this,"Всі теореми");
			group = 1;
			setTablePost(internal,1);
		}	
	} 
	class TestAll implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			group = 2;
			setTablePost(internal,2);
			/*
			ArrayList ds = null;
			String[][] info = new String[][]{	{"Крок","I","N"},	{"Виведено","S","N"},
												{"Теорема","B","N"}, {"Попередньо виведене","S","N"}	}; 
			//JOptionPane.showMessageDialog(ShowSteps.this,"Всі виведені");
			header.setText("Всі виводимі слова");
			ds = model.getStepSource(sl, true,2);
			dbm.setDataSource(null);
			dbm.setInitialModel(info);
			dbm.fireTableStructureChanged();
			setColumnWidth1(new int[]{25,260,30,460});
			if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);
			*/	
		}	
	} 
	class TestStep implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			group = 3;
			setTablePost(internal,3);
		}	
	} 
	
	private void setTablePost(boolean inter, int var){
		ArrayList ds = null;
		String[][] info;
		int [] w = null;
		String hd;
		int cnt = 0;
		switch(var){
		case 2:
			hd = "Всі виводимі слова";
			info = (inter?
					new String[][]{	{"Крок","I","N"},	{"Виведено","S","N"},
					{"Теорема","B","N"}, {"Внутрішнє представлення","S","N"}} :
					new String[][]{	{"Крок","I","N"},	{"Виведено","S","N"},
						{"Теорема","B","N"}, {"Попередньо виведене","S","N"}}); 
			w = new int[]{25,260,30,460};
			break;
		case 3:
			hd = "Всі виведені (покроково)";
			info = (inter?
					new String[][]{	{"Крок","I","N"}, {"Правило","I","N"},{"Виведено","S","N"},
					{"Теорема","B","N"}, {"Внутрішнє представлення","S","N"}} :
					new String[][]{	{"Крок","I","N"}, {"Правило","I","N"}, {"Виведено","S","N"},
					{"Теорема","B","N"}, {"Попередньо виведене","S","N"}	});
			w = new int[]{25,20, 250,30,450};
			break;
		default:
			hd = "Всі теореми";
			info = (inter?
					new String[][]{	{"Крок","I","N"},	{"Виведено","S","N"},
									{"Внутрішнє представлення","S","N"}} :
					new String[][]{{"Крок","I","N"},	{"Виведено","S","N"},
									{"Попередньо виведене","S","N"}	});
			w = new int[]{25,275,475};
		}
		header.setText(hd);
		ds = model.getStepSource(sl, inter, var);
		dbm.setDataSource(null);
		dbm.setInitialModel(info);
		dbm.fireTableStructureChanged();
		TableColumn column = null;
		for (int i = 0; i < w.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(w[i]); 
	    } 
		if ((ds != null) && (ds.size() > 0)) {
			dbm.setDataSource(ds);
			cnt = ds.size();
		}
		showGroup.lCnt.setText(" " + cnt + " ");
	}
	
	/*
	class TestQuit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(WorkAlgorithm.this,"Quit");
			hide();
		}	
	} */
	// інформація про колонки = назва + тип + редагуємість	
	private String[][] findInform(String type){
		String[][] info = null;
		switch(type){
		case "Algorithm" : 
			info = new String[][]{
					{"Крок","I","N"},
					{"№ підст.","I","N"},
					{"До підстановки","S","N"},
					{"Пiсля підстановки","S","N"}
				}; break;
		case "Machine": 
			info = new String[][]{
					{"Крок","I","N"},
					{"Конфігурація","S","N"},
					{"Скорочене представлення конфігурації","S","N"}
				}; break;
		case "Post":
			info = new String[][]{
					{"Крок","I","N"},
					{"Виведено","S","N"},
					{"Попередньо виведене","S","N"}
				}; break;
		case "Calculus" : 
			info = new String[][]{
					{"Крок","I","N"},
					{"Тип","S","N"},
					{"Операнди редукції","S","N"},
					{"Результат","S","N"}
				}; break;		
			
		case "Computer":
			
			int cnt = ((Computer)model).evalMaxReg() +3;
			info = new String[cnt][3];
			info[0][0] = "Крок"; info[0][1] = "I";  info[0][2] = "N"; 
			info[1][0] = "№Команди"; info[1][1] = "I";  info[1][2] = "N"; 
			info[2][0] = "Команда"; info[2][1] = "S";  info[2][2] = "N"; 
			for(int i = 3; i < cnt; i++){
				info[i][0] = "r"+(i-2); info[i][1] = "I";  info[i][2] = "N"; 
			}
			break;
		default: 	info = new String[][]{
				{"Крок","I","N"},
				{"№ підст.","I","N"},
				{"До підстановки","S","N"},
				{"Пiсля підстановки","S","N"}
			};
			
		}
		return info;
	}
	
	private void setColumnWidth() {
		TableColumn column = null;
		int[] widthCol = findWidth(); //{10,100,100,10,400,10};
	    for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }     
	}
	
	private void setColumnWidth1(int[] widthCol ) {
		TableColumn column = null;
		for (int i = 0; i < widthCol.length; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(widthCol[i]); 
	    }     
	}
	
	private int[] findWidth(){
		int [] w = null;
		switch(type){
		case "Algorithm" : w = new int[]{100,75,300,300}; break;
		case "Calculus" : w = new int[]{25,50,325,375}; break;
		case "Machine":  w = new int[]{75,350,350}; break;
		case "Post":  w = new int[]{25,275,475}; break;
		case "Computer":
			//int cnt = ((ArrayList)(sl.get(0))).size();
			int cnt = ((Computer)model).evalMaxReg() +3;
			w = new int[cnt];
			for(int i=0; i<cnt; i++) w[i]=25;
			w[2]=40;
			break;
		default:  w = new int[]{100,75,300,300}; 
		}
		return w;
	}
	

}
