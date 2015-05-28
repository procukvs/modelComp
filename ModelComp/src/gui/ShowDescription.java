package gui;

import main.*;
import db.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ShowDescription extends JPanel {
	private DbAccess db;
	private Model model;
	
	private JTextField sName;
	private JLabel txtNumb;
	private JCheckBox isNumeric;
	private JLabel txtRank;
	private JTextField iRank;
	private JTextField sMain;
	private JTextField sAdd;
	private JTextField sComm;
	
	ShowDescription(DbAccess db){
		//���������� �������� gui-�������� 
		JLabel txtAlgo = new JLabel("�������� ");
		sName = new JTextField(10);
		sName.setMaximumSize(new Dimension(30,100));
		txtNumb = new JLabel("--------");
		JLabel txtNumeric = new JLabel("������� ");
		isNumeric = new JCheckBox();
		isNumeric.setSelected(true);
		txtRank = new JLabel("������");
		iRank = new JTextField(2);
		iRank.setMaximumSize(new Dimension(3,100));
		iRank.setText("2");
		JLabel txtMain = new JLabel("������ ��������");
		sMain = new JTextField(10); 
		sMain.setMaximumSize(new Dimension(20,100));
		JLabel txtAdd = new JLabel("������ ����������");
		sAdd = new JTextField(10); 
		sAdd.setMaximumSize(new Dimension(20,100));
		JLabel txtComm = new JLabel("���� ���������");
		sComm = new JTextField(50); 
		sComm.setMaximumSize(new Dimension(100,100));
		
		this.db = db;
		//=================================
		// ������� ���������
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
		// ���������� �������� !!!			
		sName.addActionListener(new LsName());
		sMain.addActionListener(new LsMain());
		sAdd.addActionListener(new LsAdd());
		isNumeric.addActionListener(new LisNumeric());
		iRank.addActionListener(new LiRank());
		sComm.addActionListener(new LsComm());
		
	}
	
	// ����� ������� 
	class LsName implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			/*String name = sName.getText();
			String text="";
			//System.out.println(".." + com + "..");
			if (model == null) showEmpty();
			else {
				if(!name.equals(model.nmAlgo)) {
					if(!sw.isIdentifer(name)) text = "��\"� ��������� " + name + " - �� �������������!";
					if((text.isEmpty()) && (db.isModel(name)))
						text = "�������� � ������ " + name + " ��� ���� !";
					if(text.isEmpty()) {
						model.nmAlgo = name;
						db.editAlgorithm(model);
					} else {
						JOptionPane.showMessageDialog(ShowDescription.this,text);		
						showModel();
					}
				}	
			}*/
			JOptionPane.showMessageDialog(ShowDescription.this,"sName....");
		}	
	}
		class LsMain implements ActionListener  {
			public void actionPerformed(ActionEvent event){
			/*	String com = sMain.getText();
				String text;
				System.out.println(".." + com + "..");
				if (model == null) showEmpty();
				else {
					if (!com.equals(model.main)) {
						if (model.isNumeric) {
							text = "�������� ������ � ������� ������ |# !";
							sMain.setText("|#");
							JOptionPane.showMessageDialog(ShowDescription.this,text);	
						} 
						model.main = sMain.getText();
						db.editAlgorithm(model);
						//text = db.testingRules(model.algo, com + model.add);
						text = model.testingRules();
						if (!text.isEmpty())
							JOptionPane.showMessageDialog(ShowDescription.this,text);
					//	sAdd.requestFocus();
					} 
					sAdd.requestFocus();
				}*/
				JOptionPane.showMessageDialog(ShowDescription.this,"sMain....");
			}	
		}
		class LsAdd implements ActionListener  {
			public void actionPerformed(ActionEvent event){
			/*	String com = sAdd.getText();
				//System.out.println(".." + com + "..");
				if (model == null) showEmpty();
				else {
					if (!com.equals(model.add)) {
						// String text = "������� �� " + com + " �� �� ������� � abc ." + sw.isAlfa("abc", com) +".";
					    //String text = db.testingRules(model.algo, model.main + com);
					    model.add = com;
					    db.editAlgorithm(model);
					    String text = model.testingRules();
					    if (!text.isEmpty())
					    	 JOptionPane.showMessageDialog(ShowDescription.this,text);
					}
					sComm.requestFocus();
					
				}*/
				JOptionPane.showMessageDialog(ShowDescription.this,"sAdd....");
			}	
		}
		class LisNumeric implements ActionListener  {
			public void actionPerformed(ActionEvent event){
			/*	Boolean isNew;
				String text = "";
				//System.out.println(".." + com + "..");
				if (model != null){
					isNew = isNumeric.isSelected();
					if(isNew){
						if(!model.isNumeric){
							// ������� ������ �������� !!!!!
							model.isNumeric = true;
							txtRank.setVisible(true);
							iRank.setVisible(true);
							model.main = "|#";
							sMain.setText("|#");
							db.editAlgorithm(model);
							//text = db.testingRules(model.algo, model.main+model.add);
							text = model.testingRules();
							if (!text.isEmpty())
								JOptionPane.showMessageDialog(ShowDescription.this,text);
						
						}
						
					} else{
						if(model.isNumeric){
							// ������� ������ �� ��������!!
							model.isNumeric = false;
							txtRank.setVisible(false);
							iRank.setVisible(false);
							db.editAlgorithm(model);
						}
					}
				} else showEmpty();
				*/
				JOptionPane.showMessageDialog(ShowDescription.this,"isNumeric....");
			}	
		}
		
		class LiRank implements ActionListener  {
			public void actionPerformed(ActionEvent event){
			/*	String srank = iRank.getText();
				int rank = 1;
				String text;
				//System.out.println(".." + com + "..");
				if (model == null) showEmpty();
				else {
					if(sw.isPosNumber(srank)){
						rank = new Integer(srank); 
						if (model.rank != rank) {
							model.rank = rank;
							db.editAlgorithm(model);
						}
					} else{
						text = "������ - ������ ���� ����� ! ";
						JOptionPane.showMessageDialog(ShowDescription.this,text);
						showModel();
					}
				}*/
				JOptionPane.showMessageDialog(ShowDescription.this,"iRank....");
			}	
		}
		class LsComm implements ActionListener  {
			public void actionPerformed(ActionEvent event){
				/*String com = sComm.getText();
				//System.out.println(".." + com + "..");
				if (model == null) showEmpty();
				else if (!com.equals(model.txComm)) {
						model.txComm = com;
						db.editAlgorithm(model);
					    //String text = "������� ����������  " + r + " � " + all + " ����������  ��������� " + model.nmAlgo +".";
					    //JOptionPane.showMessageDialog(ShowProgram.this,text);
				} */
				JOptionPane.showMessageDialog(ShowDescription.this,"sComm....");
			}	
		}
		
		public void setModel(Model model) {
		    this.model = model;	
		    if (model == null) showEmpty( );
		    else showModel();
		}
		
		private void showModel() {
			Algorithm algo = (Algorithm)model;
			sName.setText(algo.name);
			txtNumb.setText("����� " + algo.id);
			sMain.setText(algo.main);
			sAdd.setText(algo.add);
			isNumeric.setSelected(algo.isNumeric);
			iRank.setText(((Integer)algo.rank).toString());
			txtRank.setVisible(algo.isNumeric);
			iRank.setVisible(algo.isNumeric);
			sComm.setText(algo.descr);
		}
		private void showEmpty() {
			sName.setText("");
			txtNumb.setText("-----");
			sMain.setText("");
			sAdd.setText("");
			isNumeric.setSelected(true);
			iRank.setText("2");
			txtRank.setVisible(true);
			iRank.setVisible(true);
			sComm.setText("");
		}
	
}
