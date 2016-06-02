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
	private JFileChooser fc = new JFileChooser();
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showModels;
	private ShowFiles showFiles;
	private String type = "Algorithm";
	private Model model = null;
	private ShowWork showWork; 
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
	private JLabel lSection;
	//private JLabel lVersion;
	//private JComboBox section;
	//private String[] sectSet = {"base"};
  	
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
		lSection = new JLabel("base");
		//lVersion = new JLabel("0.1");
		//section = new JComboBox();
		//section.setPrototypeDisplayValue("Family Ivanov");

		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showWork = new ShowWork(showMain);
		//showWork = new ShowWork(frame);
		
		this.db = db;
		this.showModels = showMain;
		this.showFiles = showFiles;
		
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		fileBox = Box.createHorizontalBox();
		//fileBox.add(lVersion);
		fileBox.add(lSection);
		fileBox.add(Box.createHorizontalStrut(15));
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
		boolean isParameter = (type.equals("State")) || (type.equals("Parameters"));
		this.type = type;
		this.model = model;
		lSection.setText( Parameters.getSection());
		//==== розташування елементів 
		if (isFile){
			buttons.remove(quit);
			fileBox.add(quit);
		} else {
		  	fileBox.remove(quit);
		  	buttons.add(quit);
		  	//if (isParameter) buttons.remove(addBase);
		  	//else buttons.add(addBase);
		}
		//====== видимість елементів 
		if (isParameter){
			fileBox.setVisible(false);
			work.setVisible(true);
			quit.setVisible(true);
			add.setVisible(type.equals("Parameters"));
			addBase.setVisible(false);
			delete.setVisible(type.equals("Parameters"));
		} else{
			fileBox.setVisible(true);
			lSection.setVisible(type.equals("Input") && Parameters.getRegime().equals("teacher") );
			output.setVisible(!type.equals("Input"));
			input.setVisible(!type.equals("Output"));
			add.setVisible(!isFile);
			addBase.setVisible(!isFile);
			delete.setVisible(!isFile);
			work.setVisible(!isFile && !type.equals("Recursive") && !type.equals("Calculus"));
			//quit.setVisible(!isFile);
		}	
		
		// встановити надписи на кнопках
		add.setText(Model.title(type, 7));
		addBase.setText(Model.title(type, 7) + " на основі");
		if (isParameter) work.setText(Model.title(type, 5));
		else work.setText("Рoбота з " + Model.title(type, 5));
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
			if (type.equals("Parameters")){
					//JOptionPane.showMessageDialog(ShowModelButtons.this,"Parameters...New");
				int row = showFiles.getSelectedRow();
				String namePar = "namePar";
				String valuePar = "valuePar";
				String descPar = "description";
				//System.out.println(" Row = " + row);
				if(row>=0){
					//System.out.println(" 1: " + showFiles.getValue(row, 0) + " 2: " + 
				    //                      showFiles.getValue(row, 1) + " 3: " + showFiles.getValue(row, 2));
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
				}
				if (addParameter(namePar, valuePar, descPar)){
					System.out.println("....Add New value param..");
					ArrayList pl = null;
					pl = db.getAllParameter();
					showFiles.showInputModel(pl);
				}  
				else System.out.println("...Nothing...");  
			}	
			else { 
				int idModel = Model.dbNew(type);  // db.newModel(type); 
				showModels.showModel(type, idModel);
				//JOptionPane.showMessageDialog(ShowModelButtons.this,"Add..");
			}	
		}	
	}
	class AddAs implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			if (model != null) {
				int idModel = model.dbNewAs(); //db.newModelAs(type, model); 
				showModels.showModel(type, idModel);
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
							showModels.showModel(type, idModel);
							text = Model.title(type, 8) + " " + model.name + " з файлу " + name + "  введено!";
						}
						else text = Model.title(type, 8)+ " " + nameIn + " з файлу " + name + "  введено, але не збережено в базі даних !";
					}
					else text = wf.getErrorText();
				}	
			}
			//System.out.println(" Section = " + section.getSelectedItem());
			JOptionPane.showMessageDialog(ShowModelButtons.this,text); // text);
		}	
	}
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
			showModels.showModel("NoModel", 0);
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
			if (type.equals("Parameters")){
				int row = showFiles.getSelectedRow();
				boolean del = true;
				String namePar = "", valuePar = "", descPar = "";
				if(row>=0){
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
					String text = " параметру " + namePar + " : " + valuePar + " <" + descPar + ">";
					if (namePar.equals("Section")){
						int cnt = db.getSectionCount(valuePar);
						if (cnt>0){
							del = false;
							JOptionPane.showMessageDialog(ShowModelButtons.this,
									 "Значення " + text + "- НЕ можна вилучити. \nРозділ " + valuePar + " містить " + cnt + 
									 " моделей.\n Для вилучення цього значення необхідно вилучити з розділу ВСІ моделі!");
						}
					}	
					if (del){
						UIManager.put("OptionPane.yesButtonText", "Так");
						UIManager.put("OptionPane.noButtonText", "Ні");
						int res = JOptionPane.showConfirmDialog(ShowModelButtons.this,
														"Вилучити значення " + text,"Вилучити ?", JOptionPane.YES_NO_OPTION );
						if (res == JOptionPane.YES_OPTION) {
							ArrayList pl = null;
							db.deleteParameterValue(namePar, valuePar);
							pl = db.getAllParameter();
							showFiles.showInputModel(pl);
							//JOptionPane.showMessageDialog(ShowModelButtons.this,"Parameters...Delete");
						}
					}
					
				} //else System.out.println("...Nothing...Row = " + row);  
				/*
				ArrayList sl = db.getParameterValue("Section");
				for(int i = 0; i < sl.size(); i ++){
					System.out.println("Section: " + (String)sl.get(i) + ".." + db.getSectionCount((String)sl.get(i)) + "..");  
				}
				JOptionPane.showMessageDialog(ShowModelButtons.this,"Parameters...Delete");
				*/	
			}
			else if (model != null){ 
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
					showModels.showModel(type, newId);
				}
			}
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelDelete...");
		}	
	}
	class ModelWork implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if (type.equals("State")) {
				int row = showFiles.getSelectedRow();
				String namePar = "", valuePar = "", descPar = "";
				if(row>=0){
					//System.out.println(" 1: " + showFiles.getValue(row, 0) + " 2: " + 
					//                      showFiles.getValue(row, 1) + " 3: " + showFiles.getValue(row, 2));
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
					//System.out.println("....Update param.." + namePar + " " + valuePar);
					if (setParameter(namePar, valuePar, descPar)) {
						//System.out.println("....Update param..");
						db.setParameters();
						ArrayList pl = null;
						pl = db.getStateParameter();
						showFiles.showInputModel(pl);
					}  
					//else System.out.println("...Nothing...");  
				} //else System.out.println("...Nothing...Row = " + row);  	
				//JOptionPane.showMessageDialog(ShowModelButtons.this,"State...Update");
			}    
			else if (type.equals("Parameters")){
				int row = showFiles.getSelectedRow();
				String namePar = "", valuePar = "", descPar = "";
				if(row>=0){
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
					//System.out.println("....Update param.." + namePar + " " + valuePar);
					if (updateParameterDesc(namePar, valuePar, descPar)) {
						//System.out.println("....Update desc..");
						ArrayList pl = null;
						pl = db.getAllParameter();
						showFiles.showInputModel(pl);
					}  
				     //boolean updateParameterDesc(String name, String value, String desc)	
				}	
				//JOptionPane.showMessageDialog(ShowModelButtons.this,"Parameters...Edit");
			}	
			else if (model != null){
				//System.out.println(model.getDbOrder());
				showWork.setModel(type, model);
				showWork.show();
			}	
			//JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelWork...");		
		}
	}	

	// true => додає нове значення деякого параметру в список всіх значень параметрів
	private boolean addParameter(String name, String value, String desc) {
		boolean rs = false;
		// формуємо Діалогове вікно "Нове значення параметру"
		//  ..... компоненти діалогового вікна
		String[] textButtons = {"Зберегти", "Вийти"};
		JPanel panelPar = new JPanel();
		JTextField namePar = new JTextField(6);
		JTextField valuePar = new JTextField(6);
		JTextField descPar = new JTextField(20);
		panelPar.add(new JLabel("Назва"));
	    panelPar.add(namePar);
		panelPar.add(new JLabel("Значення"));
		panelPar.add(valuePar);
		panelPar.add(new JLabel("Коментар"));
		panelPar.add(descPar);
		namePar.setText(name);
		valuePar.setText(value);
		descPar.setText(desc);
		//  ... відкриття вікна
		int res = JOptionPane.showOptionDialog(ShowModelButtons.this, panelPar, "Нове значення параметру",
				 		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, textButtons, null);
		//  ... обробка результату встановлення нового значення параметру
		if (res == JOptionPane.OK_OPTION){
			String name1 = namePar.getText();
			String value1 = valuePar.getText();
			String desc1 = descPar.getText();
			if (!db.isParameterValue(name1, value1)){
				db.setParameterValue(name1, value1, desc1);
				rs = true;
			}
			else JOptionPane.showMessageDialog(ShowModelButtons.this, "Значення параметру " + name1 + " : " + value1 + " -- ВЖЕ єсть!! ");
			
			/*
			if (!((Machine)model).isState(name1)) {
				//db.moveUp(type, model.id, row);
				//showMain.showModel(type, model.id);
				//table.showPrevRow(false);
				model.dbRenameState(name, name1);
				showMain.showModel(type, model.id);
				table.showRow(false,model.findCommand(name1)+1);
				//JOptionPane.showMessageDialog(ShowCommandButtons.this, "Rename.." + name + " -> " + name1);
			}
			else text = "Програма машини " + model.name + " вже використовує стан " + name1 + " !";
			*/
		}
		return rs;
	}
	
	// true => змінює коментар desc у значення параметру (name+value)
	private boolean setParameter(String name, String value, String desc){
     	boolean rs = false;
		ArrayList <String> all = db.getParameterValue(name);
		int j;
		String[] values = new String [all.size()];
		j = 0;
		//System.out.println("..values ..." + all.size());  
		for (int i = 0; i < all.size(); i++){
			values[i] = all.get(i);
			if(values[i].equals(value)) j = i;
		}
		UIManager.put("OptionPane.okButtonText", "Так");
		UIManager.put("OptionPane.cancelButtonText", "Вийти");
		String text = "Параметр " + name + " : " + value + " <" + desc + ">";
		Object res = JOptionPane.showInputDialog(ShowModelButtons.this, 
						text, "Змінити значення параметру", JOptionPane.QUESTION_MESSAGE, null,  values, values[j]);
		if(res != null){
			if (!value.equals((String)res)){
				db.updateStateParameter (name, (String)res);
				rs = true;
				//JOptionPane.showMessageDialog(ShowModelButtons.this,"Нове значення параметру " + (String)res);
			}	
			//else JOptionPane.showMessageDialog(ShowModelButtons.this,"Значення параметру не Змінено " + text ); 
		}
		return rs;
	}
	
	// true => модифікує опис значення параметру
	private boolean updateParameterDesc(String name, String value, String desc) {
		boolean rs = false;	
		String text = "Параметр " + name + " : " + value + " <" + desc + ">";
		Object res = JOptionPane.showInputDialog(ShowModelButtons.this, 
						text, "Відредагувати опис параметру", JOptionPane.QUESTION_MESSAGE, null,null, desc );
		if(res != null){
			if (!desc.equals((String)res)){
				//db.updateStateParameter (name, (String)res);
				db.updateParameterDesc (name, value,(String)res);
				rs = true;
				//JOptionPane.showMessageDialog(ShowModelButtons.this,"Новий опис параметру " + (String)res);
			}	
			//else JOptionPane.showMessageDialog(ShowModelButtons.this,"Опис параметру не Змінено " + text ); 
		}
		return rs;
	}	
}