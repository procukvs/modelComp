package gui.model;

import db.*;
import main.*;
import file.*;
import gui.*;
import gui.eval.*;
import gui.system.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PnModButtons extends JPanel {
	private JFileChooser fc = new JFileChooser();
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private FrMain fMain;
	private String type = "Algorithm";
	private Model model = null;
	private DgEval dEval; 
	private JTextField nmFile;
	
	private JButton add;
	private JButton addBase;
	private JButton delete;
	private JButton quit;
	private JButton work ;
	private JButton output;
	private JButton input ;
	//private JLabel lSection;
	private JLabel selection;
	private JLabel section;
	private JLabel version;
	
	private int selected = 0;
	private int r = 0;
  	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public PnModButtons(DbAccess db){	                                       // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		//���������� �������� gui-�������� 
		selection = new JLabel("  0:0  ");  
		JButton first = new JButton("|<");
		first.setMaximumSize(new Dimension(20,20));
		JButton prev = new JButton("<");
		prev.setMaximumSize(new Dimension(20,20));
		JButton next = new JButton(">");
		next.setMaximumSize(new Dimension(20,20));
		JButton last = new JButton(">|");
		last.setMaximumSize(new Dimension(20,20));
		version = new JLabel("2.3");  
		section = new JLabel(""); 
		add = new JButton("�����");
		addBase = new JButton("����� �� �����");
		//JButton report = new JButton("����");
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
		dEval = new DgEval(fMain);
		
		this.db = db;
			
		//=================================
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		Box select = Box.createHorizontalBox();
		select.add(Box.createHorizontalStrut(5));
		select.add(first); select.add(prev); 
		select.add(selection);
		select.add(next); 
		select.add(last);
		select.add(Box.createHorizontalGlue());
		if(Parameters.getRegime().equals("teacher")){
		  select.add(section);
		  select.add(Box.createHorizontalStrut(15));
		  section.setText(Parameters.getSection());
		} 
		select.add(version);
		Box fileBox = Box.createHorizontalBox();
		fileBox.add(Box.createHorizontalStrut(15));
		fileBox.add(file);
	    fileBox.add(nmFile);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(output);
		fileBox.add(Box.createHorizontalStrut(5));
		fileBox.add(input);
		fileBox.add(Box.createHorizontalStrut(5));
		//----------------------------------
		Box buttons = Box.createHorizontalBox();
		buttons.add(add);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(addBase);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(delete);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(work);
		buttons.add(Box.createHorizontalStrut(25));				
		buttons.add(quit);
		//------------------------------------
		//add(Box.createVerticalStrut(5));
		add(select);
		add(Box.createVerticalStrut(5));
		add(fileBox);
		add(Box.createVerticalStrut(3));
		add(buttons);	
		add(Box.createVerticalStrut(5));
		
		// ���������� �������� !!!	
		first.addActionListener(new SelectFirst());
		prev.addActionListener(new SelectPrev());
		next.addActionListener(new SelectNext());
		last.addActionListener(new SelectLast());
		add.addActionListener(new Add());
		addBase.addActionListener(new AddAs());
		delete.addActionListener(new ModelDelete());
		file.addActionListener(new SetNameFile());
		work.addActionListener(new ModelWork());
		output.addActionListener(new ModelOutput());
		input.addActionListener(new ModelInput());
		quit.addActionListener(new Quit());
	}
	
	public void setEnv(FrMain fMain){   // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		this.fMain = fMain;
	}

	public void setModel(String type, Model model) {
		this.type = type;
		this.model = model;
		if (model == null) selected = 0; else selected = db.getOrder(type, model.name);
		r = db.getModelCount(type);
    	//System.out.println("setModel  " + selected + "  cnt = " + r);
    	if (selected > r) selected = r;
    	selection.setText(selected + " : " + r);
    	if(Parameters.getRegime().equals("teacher")) section.setText(Parameters.getSection());
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
			if(dEval != null) SwingUtilities.updateComponentTreeUI(dEval);
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	//������� ����� - ������� !!!!!!
	class SelectFirst implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			r = db.getModelCount(type);
			//System.out.println("SelectFirst  " + r);
			selected = 1;
			if (r==0){ 
				selected = 0; 
				fMain.setModel(type, 0);
			} else fMain.setModel(type, db.getNumber(type, selected));
		}	
	}
	class SelectPrev implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if (selected > 1) {
				selected--;
				fMain.setModel(type, db.getNumber(type, selected));
			}
		}	
	}
	class SelectNext implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			if ((db.getModelCount(type)> selected) && (selected > 0)){ 
				selected++;
				fMain.setModel(type, db.getNumber(type, selected));
			}
		}	
	}
	class SelectLast implements ActionListener  {
		public void actionPerformed(ActionEvent event){
			r = db.getModelCount(type);
			selected = r;
			if (r==0) fMain.setModel(type, 0);
			 else fMain.setModel(type, db.getNumber(type, selected));
		}	
	}
	
	class Add implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			int idModel = Model.dbNew(type);  
			fMain.setModel(type, idModel);
		}	
	}
	
	class AddAs implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (model != null) {
				int idModel = model.dbNewAs(); 
				fMain.setModel(type, idModel);
			}
		}	
	}
	
	class SetNameFile implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			fc.setDialogTitle("Select file");
			fc.setApproveButtonText("������� ����");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int res = fc.showOpenDialog(PnModButtons.this);
			if (res==JFileChooser.APPROVE_OPTION ) 
				
			nmFile.setText(fc.getSelectedFile().getAbsolutePath());
		}	
	}
	
	class ModelOutput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			WorkFile wf= WorkFile.getWorkFile();
			OutputText out = wf.getOut();
			String text = "���� ��� ��������� �� ������� !";
			String name = nmFile.getText();
			if (!name.isEmpty()){
				if (model != null) {
					text = model.output(name,out);
					if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " �������� � ���� " + name + "!";
				} 
			}
			if ((model != null) )  JOptionPane.showMessageDialog(PnModButtons.this,text);
		}
	}
	
	class ModelInput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "���� ��� �������� �� ������� !";
			String name = nmFile.getText();
			Model model = null;
			if (!name.isEmpty()) {
				WorkFile wf = WorkFile.getWorkFile();
				model = wf.inputModel(name);
				if (model != null) {
					type = model.getType();
					String nameIn = model.name;
					//System.out.println("ShowModelButton:ModelInput " + type + " " + nameIn);
					int idModel = db.addModel(type, model);
					if (idModel > 0) {
						fMain.setModel(type, idModel);
						text = Model.title(type, 8) + " " + model.name + " � ����� " + name + "  �������!";
					}
					else text = Model.title(type, 8)+ " " + nameIn + " � ����� " + name + "  �������, ��� �� ��������� � ��� ����� !";
				}
				else text = wf.getErrorText();
			}
			JOptionPane.showMessageDialog(PnModButtons.this,text); // text);
		}	
	}
	class Quit implements ActionListener  {
		// �������� ��� ������ ---- ��������� ���� �����
		public void actionPerformed(ActionEvent e) {
			fMain.setModel("NoModel", 0);
		}	
	}
	
	class ModelDelete implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (model != null){ 
				String text = "�������� " + Model.title(type, 6) + " "+ model.name + " � ������� " + model.id + " ?";
				UIManager.put("OptionPane.yesButtonText", "���");
				UIManager.put("OptionPane.noButtonText", "ͳ");
				int res = JOptionPane.showConfirmDialog(PnModButtons.this,text,"�������� ?",JOptionPane.YES_NO_OPTION );
				if (res == JOptionPane.YES_OPTION) {
					int newId = model.getDbOrder();
					if (model.dbDelete()) newId = model.getDbNumber(newId-1);
					fMain.setModel(type, newId);
				}
			}
		}	
	}
	
	class ModelWork implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (model != null){
				dEval.setModel(type, model);
				dEval.show();
			}		
		}
	}

}
