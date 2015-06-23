package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import db.*;
import main.*;


public class ShowModels extends JFrame {
	private DbAccess db;
	private String type = "Algorithm";
	private Model model = null;
	private JLabel label;
	private ShowModelOne showModel;
	private ShowFiles showFiles;
	private ShowModelButtons modelButtons;
	//private Box mainBox ;
	
	private ShowTesting sTest;
	
	public ShowModels(DbAccess db){
		super("Models of computation");
		this.db = db;
		//  ���������� �������� gui-�������� 
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("����� � ��������");
		// ��������� ��������� ����, �� ������ ������� �������� ����
		JMenu mModel= new JMenu("������ ���������   ");
		// ������� ���� - �������
		JMenuItem computer = new JMenuItem(Model.title("Computer", 1));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  		//("������ �������");
		JMenuItem algorithm = new JMenuItem(Model.title("Algorithm", 1));	//(new AlgorithmAction()); // 
	 	JMenuItem post = new JMenuItem(Model.title("Post", 1));
		JMenuItem rec = new JMenuItem(Model.title("Recursive", 1));
		
		mModel.add(computer);
		mModel.add(machine);
		mModel.add(algorithm);
		mModel.add(post);
		mModel.add(rec);
		menuBar.add(mModel);
		//menuBar.add(createModelMenu());
		
		JMenu mFile= new JMenu("������ � ������    ");
		JMenuItem input = new JMenuItem("�������� ������� � �����");
		JMenuItem output = new JMenuItem("��������� ������� � ����");
		
		mFile.add(input);
		mFile.add(output);
		menuBar.add(mFile);
		
		menuBar.add(quit);
		setJMenuBar(menuBar);
		
		computer.addActionListener(new LsComputer());
		algorithm.addActionListener(new LsAlgorithm());
		machine.addActionListener(new LsMachine());
		post.addActionListener(new LsPost());
		rec.addActionListener(new LsRecursive());
		input.addActionListener(new LsInput());
		output.addActionListener(new LsOutput());
		quit.addActionListener(new LsQuit());
				
		label = new JLabel("�������� ��������� �������");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showModel = new ShowModelOne(db, this);
		//-------------------------------
		showFiles = new ShowFiles (db);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		modelButtons = new ShowModelButtons(db,this);
		//===============================
		// ��������� ��
		//-----------------------------
		Box mainBox = Box.createVerticalBox();
		mainBox.add(showModel);
		mainBox.add(showFiles);
		//========================
		
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(mainBox, BorderLayout.CENTER);
		getContentPane().add(modelButtons, BorderLayout.SOUTH);
		setVisiblePane("NoModel");
		setSize(800,500);
		//pack();
	}	
	
	private void setVisiblePane(String type) {
		boolean close = type.equals("NoModel");
		boolean files = type.equals("Input") || type.equals("Output");
		label.setVisible(!close);
		showModel.setVisible(!close && !files);
		showFiles.setVisible(!close && files);
		modelButtons.setVisible(!close);
	}
	
	public void showModel(String type, int id) {
		boolean files = type.equals("Input") || type.equals("Output");
		String text = Model.title(type, 1);
		this.type = type;
		//System.out.println("ShowModel start ..." + type + "  " + id + " " + this.type);
		setVisiblePane(type);
		switch (type){
		case "Input": case "Output":
			ArrayList fs = null;
			if (type.equals("Output")) fs = db.getAllModel();
			showFiles.showTableFiles(type, fs);
			modelButtons.setModel(type,model);
			model = null;
			break;
		case "NoModel": break;
		default:
			if (!type.equals(this.type)) {
				// set label !!!!!!!!
				if (text.isEmpty()) text = "Not realise " + type + " !";
			}
			if (id == 0) model = null; else model = db.getModel(type, id);
			//System.out.println("showModelstart ..." + type + "  " + id );
			//if (model != null )	System.out.println(model.show());
			showModel.setModel(type,model);
			modelButtons.setModel(type,model);
		}
		label.setText(text);
	/*		
	*/		
			
	}
	
	class LsComputer implements ActionListener  {
		public void actionPerformed(ActionEvent e) {showModel("Computer",0);	}	
	}
	
	class LsAlgorithm implements ActionListener  {
		public void actionPerformed(ActionEvent e) {showModel("Algorithm",0);	}	
	}
	
	class LsMachine implements ActionListener  {
		public void actionPerformed(ActionEvent e) {showModel("Machine",0);	}	
	}
	
	class LsPost implements ActionListener  {
		public void actionPerformed(ActionEvent e) {showModel("Post",0);	}	
	}
	
	class LsRecursive implements ActionListener  {
		public void actionPerformed(ActionEvent e) {showModel("Recursive",0);	}	
	}
	
	class LsInput extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			showModel("Input",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"Input..");
		}
	}
	
	class LsOutput extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			showModel("Output",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"Output..");
		}
	}
	
	class LsQuit implements ActionListener  {
		// �������� ��� ������ ---- ��������� ���� �����
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	
	/*
	 private JMenu createModelMenu () {
		// ��������� ��������� ����, �� ������ ������� �������� ����
		JMenu model= new JMenu("������ ���������");
		// ������� ���� - �������
		JMenuItem algorithm = new JMenuItem(new AlgorithmAction()); // ("Open", new ImageIcon("images\\icon_arrow.gif"));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  //("������ �������"); 
		machine.addActionListener(new LsMachine());
		
		model.add(algorithm);
		model.add(machine);
		return model;
		// �����������
		//file.addSeparator();
	}
	 class AlgorithmAction extends AbstractAction {
		AlgorithmAction() {putValue(NAME,Model.title("Algorithm", 1));}   //"�������� ��������� �������"
		public void actionPerformed(ActionEvent e) {
			showModel("Algorithm",0);
		}
	} 
	  
	  class MachineAction extends AbstractAction {
		MachineAction() {putValue(NAME,Model.title("Machine", 1));}  // "������ �������"
		public void actionPerformed(ActionEvent e) {
			showModel("Machine",0);
			  //	JOptionPane.showMessageDialog(ShowModels.this,"Machine..");
		}
	} */
	
	


}
