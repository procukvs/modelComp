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
		//сформувати необхідні gui-елементи 
		add = new JButton("Новий");
		addBase = new JButton("Новий на основі");
		JButton report = new JButton("Звіти");
		delete = new JButton("Вилучити");
		JButton file = new JButton("Файл ...");
		nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		work = new JButton("Рoбота з алгоритмом");
		output = new JButton("Вивести алгоритм в файл");
		input = new JButton("Ввести алгоритм з файлу");
		quit = new JButton("Вийти");
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showWork = new ShowWork(showMain);
		//showWork = new ShowWork(frame);
		
		this.db = db;
		this.showMain = showMain;
		this.showFiles = showFiles;
		
		//=================================
		// формуємо розміщення
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
		
		// встановити слухачів !!!			
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
		//==== розташування елементів 
		if (isFile){
			buttons.remove(quit);
			fileBox.add(quit);
		} else{
			fileBox.remove(quit);
			buttons.add(quit);		
		}
		//====== видимість елементів 
		output.setVisible(!type.equals("Input"));
		input.setVisible(!type.equals("Output"));
		add.setVisible(!isFile);
		addBase.setVisible(!isFile);
		delete.setVisible(!isFile);
		work.setVisible(!isFile && !type.equals("Recursive"));
		//quit.setVisible(!isFile);
		
		// встановити надписи на кнопках
		add.setText(Model.title(type, 7));
		addBase.setText(Model.title(type, 7) + " на основі");
		work.setText("Рoбота з " + Model.title(type, 5));
		output.setText("Вивести " + Model.title(type, 6) + " в файл" );
		input.setText("Ввести " + Model.title(type, 6) + " з файлу");
	
	}
	
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			if(showWork != null) SwingUtilities.updateComponentTreeUI(showWork);
			//ShowModels.this.pack();
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	
	//описуємо класи - слухачі !!!!!!
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
			fc.setApproveButtonText("Вибрати файл");
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
			String text = "Файл для виведення не вказано !";
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
							if (text.isEmpty()) text = "Виведено " + i + " моделей в файл " + name + ".";
						} else text = "Not open output file " + name + "!"; 	
					} else text = "Не вказано жодної моделі для виводу !";
				} else {
					if (model != null) {
						text = model.output(name,out);
						if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " виведено в файл " + name + "!";
						/*
						if(out.open(name)) {
							text = model.output(out);
							out.close();
							if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " виведено в файл " + name + "!";
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
			String text = "Файл для введення не вказано !";
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
				
					//text = "Ввести модель з файлу " 
					//		+ fc.getSelectedFile().getAbsolutePath() + "...";
				
					//model = wf.inputAlgorithm(name);
					model = wf.inputModel(name);
					if (model != null) {
						type = model.getType();
						String nameIn = model.name;
						int idModel = db.addModel(type, model);
						if (idModel > 0) {
							showMain.showModel(type, idModel);
							text = Model.title(type, 8) + " " + model.name + " з файлу " + name + "  введено!";
						}
						else text = Model.title(type, 8)+ " " + nameIn + " з файлу " + name + "  введено, але не збережено в базі даних !";
					}
					else text = wf.getErrorText();
				}	
			}
			JOptionPane.showMessageDialog(ShowModelButtons.this,text); // text);
		}	
	}
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
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
				String text = "Вилучити " + Model.title(type, 6) + " "+ model.name + " з номером " + model.id + " ?";
				UIManager.put("OptionPane.yesButtonText", "Так");
				UIManager.put("OptionPane.noButtonText", "Ні");
				int res = JOptionPane.showConfirmDialog(ShowModelButtons.this,text,"Вилучити ?",JOptionPane.YES_NO_OPTION );
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
