package gui;

import db.*;
import main.*;
import file.*;

import java.awt.*;
import java.awt.event.*;

//import javax.swing.border.*;
import javax.swing.*;

public class ShowModelButtons extends JPanel {
	JFileChooser fc = new JFileChooser();
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	//private ShowModelAll showMain;
	private String type = "Algorithm";
	private Model model = null;
	ShowWork showWork; 
	private JTextField nmFile;
	
	private JButton add;
	private JButton addBase;
	private JButton work ;
	private JButton output;
	private JButton input ;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowModelButtons(DbAccess db, ShowModels showMain){
	//ShowModelButtons(DbAccess db, ShowModelAll showMain, ShowMenu frame){
		//���������� �������� gui-�������� 
		add = new JButton("�����");
		addBase = new JButton("����� �� �����");
		JButton report = new JButton("����");
		JButton delete = new JButton("��������");
		JButton file = new JButton("���� ...");
		nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		work = new JButton("�o���� � ����������");
		output = new JButton("������� �������� � ����");
		input = new JButton("������ �������� � �����");
		JButton quit = new JButton("�����");
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showWork = new ShowWork(showMain);
		//showWork = new ShowWork(frame);
		
		this.db = db;
		this.showMain = showMain;
		
		//=================================
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Box fileBox = Box.createHorizontalBox();
		fileBox.add(file);
	    fileBox.add(nmFile);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(output);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(input);
		//----------------------------------
		Box buttons = Box.createHorizontalBox();
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
		this.type = type;
		this.model = model; 
		// ���������� ������� �� �������
		add.setText(Model.title(type, 7));
		addBase.setText(Model.title(type, 7) + " �� �����");
		work.setText("�o���� � " + Model.title(type, 5));
		output.setText("������� " + Model.title(type, 6) + " � ����" );
		input.setText("������ " + Model.title(type, 6) + " � �����");
	}
	
	//������� ����� - ������� !!!!!!
	class Add implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			int idModel = db.newModel(type); 
			showMain.showModel(type, idModel);
		   	//JOptionPane.showMessageDialog(ShowModelButtons.this,"Add..");
		}	
	}
	class AddAs implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			if (model != null) {
				int idModel = db.newModelAs(type, model); 
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
			String text = "���� ��� ��������� �� ������� !";
			if (model != null) {
				String name = nmFile.getText();
				if (!name.isEmpty()){
					//text = "������� ������ " + model.nmAlgo + 
					//		" � ���� " + fc.getSelectedFile().getAbsolutePath() + "...";
					//String[] wr = new String[model.asub.size()];
					//Rule r ; 
					//for (int i = 0; i < model.asub.size(); i++ ) {
					//	r = (Rule)model.asub.get(i);
					//	wr[i] = r.getsLeft() + "->" + r.getsRigth();
					//}
					WorkFile wf = new WorkFile();
					text = model.output(name, (wf.getOut()));
					//text = wf.outputAlgorithm(name,(Algorithm)model);
					if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " �������� � ���� " + name + "!";
				}	
				JOptionPane.showMessageDialog(ShowModelButtons.this,text);
			}
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelOutput...");
		}	
	}
	class ModelInput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "���� ��� �������� �� ������� !";
			String name = nmFile.getText();
			//Algorithm model;
			Model model;
			if (!name.isEmpty()) {
				//text = "������ ������ � ����� " 
				//		+ fc.getSelectedFile().getAbsolutePath() + "...";
				WorkFile wf = new WorkFile();
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
				JOptionPane.showMessageDialog(ShowModelButtons.this,text + " ??");
			}	
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelInput..."); // text);
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
					model.dbDelete();
					newId = model.getDbNumber(newId-1);
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
