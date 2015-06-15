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
	
	JTable table;
	JCheckBox cInter;
	JLabel lInter;
	private ShowGroup showGroup;
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
		
		JLabel header = new JLabel("Послідовність підстановок алгоритму");
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
	}
	
	public void setShowSteps(String type,  Model model, ArrayList sl){
		ArrayList ds = null;
		boolean visible = false;
		this.model = model;
		this.type = type;
		this.sl = sl;
		//cInter.setSelected(model.getIsNumeric());
		//cInter.setVisible(model.getIsNumeric());
		//lInter.setVisible(model.getIsNumeric());
		if(type.equals("Algorithm")) visible = model.getIsNumeric();
		cInter.setSelected(visible);
		cInter.setVisible(visible);
		lInter.setVisible(visible);
		ds = model.getStepSource(sl, true);
		dbm.setDataSource(null);
		dbm.setInitialModel(findInform(type));
		dbm.fireTableStructureChanged();
		setColumnWidth();
		if ((ds != null) && (ds.size() > 0)) dbm.setDataSource(ds);	
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
			dbm.setDataSource(model.getStepSource(sl, cInter.isSelected()));
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
	
	private int[] findWidth(){
		int [] w = null;
		switch(type){
		case "Algorithm" : w = new int[]{100,75,300,300}; break;
		case "Machine":  w = new int[]{75,350,350}; break;
		default:  w = new int[]{100,75,300,300}; 
		}
		return w;
	}
	

}
