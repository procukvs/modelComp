package gui;
import gui.ShowModelButtons.Quit;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import db.*;
import main.*;


public class ShowModels extends JFrame {
	private DbAccess db;
	private String type = "Algorithm";
	private Model model = null;
	
	private JLabel label;
	private ShowModelOne showModel;
	private ShowModelButtons modelButtons;
	
	private ShowTesting sTest;
	
	public ShowModels(DbAccess db){
		super("mAlgorithm");
		this.db = db;
		//  ���������� �������� gui-�������� 
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("����� � ��������");
		menuBar.add(createModelMenu());
		menuBar.add(quit);
		quit.addActionListener(new Quit());
		setJMenuBar(menuBar);
		
		label = new JLabel("�������� ��������� �������");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showModel = new ShowModelOne(db, this);
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		modelButtons = new ShowModelButtons(db,this);
		//===============================
		// ��������� ��
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(showModel, BorderLayout.CENTER);
		getContentPane().add(modelButtons, BorderLayout.SOUTH);
		setVisiblePane(false);
				//label.setVisible(false);
				//showModel.setVisible(false);
				//modelButtons.setVisible(false);
		
		
		//sTest =new ShowTesting();
		//getContentPane().add(sTest, BorderLayout.NORTH);
		
		setSize(800,500);
		//pack();
	}	
	
	private void setVisiblePane(boolean isVisible) {
		label.setVisible(isVisible);
		showModel.setVisible(isVisible);
		modelButtons.setVisible(isVisible);
	}
	
	public void showModel(String type, int id) {
		/*if (type.equals("NoModel")) {
			label.setVisible(false);
			showModel.setVisible(false);
			modelButtons.setVisible(false);
		} else{
			label.setVisible(true);
			showModel.setVisible(true);
			modelButtons.setVisible(true);
		} */
		//System.out.println("ShowModel start ..." + type + "  " + id + " " + this.type);
		setVisiblePane(!type.equals("NoModel"));
		if (!type.equals(this.type)) {
			String text;
			switch(type){
			case "Algorithm" : text = "�������� ��������� �������"; break;
			case "Machine" : text = "Machine"; break;
			default: text = "Not realise " + type + " !";
			}
			label.setText(text);
			this.type = type;
			// set label !!!!!!!!
		}
		if (id == 0) model = null; else model = db.getModel(type, id);
		showModel.setModel(type,model);
		modelButtons.setModel(type,model);
	}
	
	private JMenu createModelMenu () {
		// ��������� ��������� ����, �� ������ ������� �������� ����
		JMenu model= new JMenu("������ ���������");
		JMenuItem algorithm = new JMenuItem(new AlgorithmAction()); // ("Open", new ImageIcon("images\\icon_arrow.gif"));
		// ������� ���� - �������
		//JMenuItem machine = new JMenuItem(new MachineAction()); 	   //(new ExitAction());
		JMenuItem machine = new JMenuItem("Machina Turing"); 
		model.add(algorithm);
		machine.addActionListener(new LsMachine());
		// �����������
		//file.addSeparator();
		model.add(machine);
		return model;
	}
	class AlgorithmAction extends AbstractAction {
		AlgorithmAction() {putValue(NAME,"�������� ��������� �������");}
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Algo visible !!");
		  	//JOptionPane.showMessageDialog(ShowModels.this,"Algorithm..");
			showModel("Algorithm",0);
		}
	}
	class MachineAction extends AbstractAction {
		MachineAction() {putValue(NAME,"������ �������");}
		public void actionPerformed(ActionEvent e) {
			showModel("Machine",0);
			  //	JOptionPane.showMessageDialog(ShowModels.this,"Machine..");
		}
	}
	
	class Quit implements ActionListener  {
		// �������� ��� ������ ---- ��������� ���� �����
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	
	class LsMachine implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
		   JOptionPane.showMessageDialog(ShowModels.this,"Machine.");
		}	
	}

}
