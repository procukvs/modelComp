package gui;

import db.*;
import main.*;
import file.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

//import javax.swing.border.*;
import javax.swing.*;

public class ShowModelButtons extends JPanel {
	JFileChooser fc = new JFileChooser();
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	private ShowFiles showFiles;
	private String type = "Algorithm";
	private Model model = null;
	ShowWork showWork; 
	private JTextField nmFile;
	
	private JButton add;
	private JButton addBase;
	private JButton delete;
	private JButton quit;
	private JButton work ;
	private JButton output;
	private JButton input ;
	private Box fileBox;
	private Box buttons;
  	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowModelButtons(DbAccess db, ShowModels showMain, ShowFiles showFiles){
	//ShowModelButtons(DbAccess db, ShowModelAll showMain, ShowMenu frame){
		//���������� �������� gui-�������� 
		add = new JButton("�����");
		addBase = new JButton("����� �� �����");
		JButton report = new JButton("����");
		delete = new JButton("��������");
		JButton file = new JButton("���� ...");
		nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		work = new JButton("�o���� � ����������");
		output = new JButton("������� �������� � ����");
		input = new JButton("������ �������� � �����");
		quit = new JButton("�����");
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showWork = new ShowWork(showMain);
		//showWork = new ShowWork(frame);
		
		this.db = db;
		this.showMain = showMain;
		this.showFiles = showFiles;
		
		//=================================
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		fileBox = Box.createHorizontalBox();
		fileBox.add(file);
	    fileBox.add(nmFile);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(output);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(input);
		fileBox.add(Box.createHorizontalStrut(5));
		//----------------------------------
		buttons = Box.createHorizontalBox();
		buttons.add(add);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(addBase);
		buttons.add(Box.createHorizontalStrut(5));
		//buttons.add(report);
		//buttons.add(Box.createHorizontalStrut(5));
		buttons.add(delete);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(work);
		buttons.add(Box.createHorizontalStrut(25));				
		buttons.add(quit);
		//------------------------------------
		add(Box.createVerticalStrut(5));
		add(fileBox);	
		add(Box.createVerticalStrut(3));
		add(buttons);	
		add(Box.createVerticalStrut(5));
				//Box mainBox = Box.createVerticalBox();
				//mainBox.add(box1);
				//mainBox.add(box2);
			//	buttons.add(mainBox);
		
		// ���������� �������� !!!			
		add.addActionListener(new Add());
		addBase.addActionListener(new AddAs());
		//	report.addActionListener(new ModelReport());
		delete.addActionListener(new ModelDelete());
		file.addActionListener(new SetNameFile());
		work.addActionListener(new ModelWork());
		output.addActionListener(new ModelOutput());
		input.addActionListener(new ModelInput());
		quit.addActionListener(new Quit());
	}
	
	public void setModel(String type, Model model) {
		boolean isFile = (type.equals("Input")) || (type.equals("Output"));
		this.type = type;
		this.model = model;
		//==== ������������ �������� 
		if (isFile){
			buttons.remove(quit);
			fileBox.add(quit);
		} else{
			fileBox.remove(quit);
			buttons.add(quit);		
		}
		//====== �������� �������� 
		output.setVisible(!type.equals("Input"));
		input.setVisible(!type.equals("Output"));
		add.setVisible(!isFile);
		addBase.setVisible(!isFile);
		delete.setVisible(!isFile);
		work.setVisible(!isFile && !type.equals("Recursive"));
		//quit.setVisible(!isFile);
		
		// ���������� ������� �� �������
		add.setText(Model.title(type, 7));
		addBase.setText(Model.title(type, 7) + " �� �����");
		work.setText("�o���� � " + Model.title(type, 5));
		output.setText("������� " + Model.title(type, 6) + " � ����" );
		input.setText("������ " + Model.title(type, 6) + " � �����");
	
	}
	
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			if(showWork != null) SwingUtilities.updateComponentTreeUI(showWork);
			//ShowModels.this.pack();
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	//������� ����� - ������� !!!!!!
	class Add implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			int idModel = Model.dbNew(type);  // db.newModel(type); 
			showMain.showModel(type, idModel);
		   	//JOptionPane.showMessageDialog(ShowModelButtons.this,"Add..");
		}	
	}
	class AddAs implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			if (model != null) {
				int idModel = model.dbNewAs(); //db.newModelAs(type, model); 
				showMain.showModel(type, idModel);
			}
		   	//JOptionPane.showMessageDialog(ShowModelButtons.this,"AddAs..");
		}	
	}
	class SetNameFile implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			                               //UIManager.put("JFileChooser.cancelButtonText", "+++");
			fc.setDialogTitle("Select file");
			fc.setApproveButtonText("������� ����");
			                              //fc.setApproveButtonToolTipText("++++");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int res = fc.showOpenDialog(ShowModelButtons.this);
			if (res==JFileChooser.APPROVE_OPTION ) 
				
			nmFile.setText(fc.getSelectedFile().getAbsolutePath());
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"SetNameFile..." ); // fc.getSelectedFile());
		}	
	}
	
	class ModelOutput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//Algorithm model;
			//model = showModel.getAlgorithm();
			WorkFile wf= WorkFile.getWorkFile();
			OutputText out = wf.getOut();
			String text = "���� ��� ��������� �� ������� !";
			String name = nmFile.getText();
			if (!name.isEmpty()){
				if (type.equals("Output")) {
					ArrayList ml = showFiles.getOutputModel();
					ArrayList row;
					Model outModel = null;
					int i = 0;
					if ((ml !=null) && (ml.size() > 0)){
						if(out.open(name)) {
							text = "";
							while((i < ml.size()) && (text.isEmpty())){
								row = (ArrayList)ml.get(i);
								outModel = db.getModel((String)row.get(0), (int)row.get(1));
								if (i > 0)  out.output("");
								text = outModel.output(out);
								i++;
							}
							out.close();
							if (text.isEmpty()) text = "�������� " + i + " ������� � ���� " + name + ".";
						} else text = "Not open output file " + name + "!"; 	
					} else text = "�� ������� ����� ����� ��� ������ !";
				} else {
					if (model != null) {
						text = model.output(name,out);
						if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " �������� � ���� " + name + "!";
						/*
						if(out.open(name)) {
							text = model.output(out);
							out.close();
							if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " �������� � ���� " + name + "!";
						} else text = "Not open output file " + name + "!";
						*/ 		
					} 
				} 
				
			}
			if (type.equals("Output") || (model != null) )  JOptionPane.showMessageDialog(ShowModelButtons.this,text);
		}
	}
	class ModelInput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "���� ��� �������� �� ������� !";
			String name = nmFile.getText();
			//Algorithm model;
			Model model = null;
			if (!name.isEmpty()) {
				WorkFile wf = WorkFile.getWorkFile();
				if (type.equals("Input")) {
					if(wf.inputListModel(db, name)) {
						showFiles.showInputModel(wf.getListModel());
					}
					text = wf.getErrorText();
					//JOptionPane.showMessageDialog(ShowModelButtons.this,text);
					//text = " Input  models form file !!!";
				} else {
				
					//text = "������ ������ � ����� " 
					//		+ fc.getSelectedFile().getAbsolutePath() + "...";
				
					//model = wf.inputAlgorithm(name);
					model = wf.inputModel(name);
					if (model != null) {
						type = model.getType();
						String nameIn = model.name;
						int idModel = db.addModel(type, model);
						if (idModel > 0) {
							showMain.showModel(type, idModel);
							text = Model.title(type, 8) + " " + model.name + " � ����� " + name + "  �������!";
						}
						else text = Model.title(type, 8)+ " " + nameIn + " � ����� " + name + "  �������, ��� �� ��������� � ��� ����� !";
					}
					else text = wf.getErrorText();
				}	
			}
			JOptionPane.showMessageDialog(ShowModelButtons.this,text); // text);
		}	
	}
	class Quit implements ActionListener  {
		// �������� ��� ������ ---- ��������� ���� �����
		public void actionPerformed(ActionEvent e) {
			showMain.showModel("NoModel", 0);
		}	
	}
	class ModelReport implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String str = "abbc";
			String pt = "ce";
			String text = " findFirst(\""+str+"\",\""+pt+"\") = " ;
			//JOptionPane.showMessageDialog(ShowModel.this,text + StringWork.findFirst(str,pt));
			JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelReport...");
		}	
	}
	class ModelDelete implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (model != null){ 
				String text = "�������� " + Model.title(type, 6) + " "+ model.name + " � ������� " + model.id + " ?";
				UIManager.put("OptionPane.yesButtonText", "���");
				UIManager.put("OptionPane.noButtonText", "ͳ");
				int res = JOptionPane.showConfirmDialog(ShowModelButtons.this,text,"�������� ?",JOptionPane.YES_NO_OPTION );
				if (res == JOptionPane.YES_OPTION) {
						//int newId = db.getOrder(type, model.id);
						//db.deleteModel(type, model);
						//newId = db.getNumber(type, newId-1);
					int newId = model.getDbOrder();
					if (model.dbDelete()) newId = model.getDbNumber(newId-1);
					showMain.showModel(type, newId);
				}
			}
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelDelete...");
		}	
	}
	class ModelWork implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (model != null){
				//System.out.println(model.getDbOrder());
				showWork.setModel(type, model);
				showWork.show();
			}	
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelWork...");		
		}
	}	


}
