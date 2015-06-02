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
		//setBorder(new TitledBorder("����������� ���������� ���������"));
		String[][] inform = {
				{"����","I","N"},
				{"� ����.","I","N"},
				{"�� ����������","S","N"},
				{"�i��� ����������","S","N"}
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
		
		
		cInter = new JCheckBox();
		lInter = new JLabel("������� �������������");
		//bQuit = new JButton("�����");
		
		JLabel header = new JLabel("����������� ���������� ���������");
		header.setHorizontalAlignment(header.CENTER);
		//Container contentPane = getContentPane();
		Box control = Box.createHorizontalBox();
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
	
	public void setShowSteps(String type, Model model, ArrayList subList) {
		this.model = model;
		this.type = type;
		algo = (Algorithm) model;
		cInter.setSelected(algo.isNumeric);
		cInter.setVisible(algo.isNumeric);
		lInter.setVisible(algo.isNumeric);
		sl = subList;
		formDataSource(true);
		//dbm.fireTableStructureChanged();
		dbm.setDataSource(dataIn);
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
	
	class TestInter implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			formDataSource(cInter.isSelected());
			dbm.setDataSource(dataIn);
		}	
	}
	/*
	class TestQuit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//JOptionPane.showMessageDialog(WorkAlgorithm.this,"Quit");
			hide();
		}	
	} */
	

}
