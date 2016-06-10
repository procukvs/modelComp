package gui.system;

import db.*;
import main.*;
import file.*;
import gui.FrMain;
import gui.eval.DgEval;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PnParButtons extends JPanel {
	private JFileChooser fc = new JFileChooser();
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private FrMain showModels;
	private PnParTable showFiles;
	private String type = "Algorithm";
	//private Model model = null;
	private DgEval showWork; 
	private JTextField nmFile;
	private JButton file ;
	private JButton modify;
	private JButton add;
	private JButton delete;
	private JButton quit;
	private JButton edit ;
	private JButton output;
	private JButton input ;
	//private Box fileBox;
	//private Box buttons;
	private JLabel lSection;
	private Component st1;
  	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//ShowModelButtons(DbAccess db, ShowModels showMain, ShowFiles showFiles){  // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
	public PnParButtons(DbAccess db){	                                       // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		//сформувати необхідні gui-елементи 
		modify = new JButton("Змінити");
		add = new JButton("Нове значення");
		//JButton report = new JButton("Звіти");
		delete = new JButton("Вилучити");
		file = new JButton("Файл ...");
		nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		edit = new JButton("Редагувати коментар");
		output = new JButton("Вивести моделі в файл");
		input = new JButton("Ввести моделі з файлу");
		quit = new JButton("Вийти");
		lSection = new JLabel("base");
	
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showWork = new DgEval(showModels);
		
		this.db = db;
		//this.showModels = showMain;  // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		//this.showFiles = showFiles;  // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		//fileBox = Box.createHorizontalBox();
		add(lSection);
		add(Box.createHorizontalStrut(15));
		add(file);
	    add(nmFile);
		
		st1 = Box.createHorizontalStrut(5);
		add(st1);
		add(output);
		add(Box.createHorizontalStrut(5));
		add(input);
		add(Box.createHorizontalStrut(5));
		//----------------------------------
		//buttons = Box.createHorizontalBox();
		add(modify);
		add(Box.createHorizontalStrut(5));
		add(add);
		add(Box.createHorizontalStrut(5));
		add(delete);
		add(Box.createHorizontalStrut(5));
		add(edit);
		add(Box.createHorizontalStrut(25));				
		add(quit);
		//------------------------------------
		//add(Box.createVerticalStrut(5));
		//add(fileBox);	
		//add(Box.createVerticalStrut(3));
		//add(buttons);	
		//add(Box.createVerticalStrut(5));
		
		// встановити слухачів !!!			
		modify.addActionListener(new Modify());
		add.addActionListener(new AddPar());
		delete.addActionListener(new Delete());
		file.addActionListener(new SetNameFile());
		edit.addActionListener(new Edit());
		output.addActionListener(new ModelOutput());
		input.addActionListener(new ModelInput());
		quit.addActionListener(new Quit());
	}
	
	public void setEnv(FrMain showMain,PnParTable showFiles){   // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		this.showModels = showMain;  
		this.showFiles = showFiles;  // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
	}
	
	public void setFiles(PnParTable showFiles){   // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
		this.showFiles = showFiles;             // !!!!!!!!!! ref !!!!!!!!!!!!!!!!!!!!!!
	}
	public void setModel(String type) {
		boolean isFile = (type.equals("Input")) || (type.equals("Output"));
		boolean isParameter = (type.equals("State")) || (type.equals("Parameters"));
		this.type = type;
		//this.model = model;
		lSection.setText( Parameters.getSection());
		/*
		//==== розташування елементів 
		if (isFile){
			buttons.remove(quit);
			fileBox.add(quit);
		} else {
		  	fileBox.remove(quit);
		  	buttons.add(quit);
		}
		*/
		//====== видимість елементів 
		//if (isParameter){
		//	fileBox.setVisible(false);
		lSection.setVisible(type.equals("Input") && Parameters.getRegime().equals("teacher") );
		file.setVisible(isFile);
		nmFile.setVisible(isFile);
		output.setVisible(type.equals("Output"));
		input.setVisible(type.equals("Input"));
		modify.setVisible(type.equals("State"));
		add.setVisible(type.equals("Parameters"));
		delete.setVisible(type.equals("Parameters"));
		edit.setVisible(type.equals("Parameters"));
		quit.setVisible(true);
		
		// встановити надписи на кнопках
		//modify.setText(Model.title(type, 7));
		//add.setText(Model.title(type, 7) + " на основі");
		//if (isParameter) edit.setText(Model.title(type, 5));
		//else edit.setText("Рoбота з " + Model.title(type, 5));
		output.setText("Вивести " + Model.title(type, 6) + " в файл" );
		input.setText("Ввести " + Model.title(type, 6) + " з файлу");		
		
		//} else{
		//	fileBox.setVisible(true);
		
	
		//	modify.setVisible(!isFile);
		//	add.setVisible(!isFile);
		//	delete.setVisible(!isFile);
		//	edit.setVisible(!isFile && !type.equals("Recursive") && !type.equals("Calculus"));
		//}	
		
		
	
	}
	/*
	public void setLookAndFeel(String className){
		try {
			UIManager.setLookAndFeel(className);
			if(showWork != null) SwingUtilities.updateComponentTreeUI(showWork);
		}
		catch (Exception ex) { System.err.println(ex);}
	}
	*/
	//описуємо класи - слухачі !!!!!!
	class Modify implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			//System.out.println("PParButtons:Add =  "+ type );
			//if (type.equals("State")) {
			int row = showFiles.getSelectedRow();
			String namePar = "", valuePar = "", descPar = "";
			if(row>=0){
				namePar = showFiles.getValue(row, 0);
				valuePar = showFiles.getValue(row, 1);
				descPar = showFiles.getValue(row, 2);
				if (setParameter(namePar, valuePar, descPar)) {
					db.setParameters();
					ArrayList pl = null;
					pl = db.getStateParameter();
					showFiles.showInputModel(pl);
				}  
			} //else System.out.println("...Nothing...Row = " + row);  	
			//}    
		}	
	}
	class AddPar implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			
		//	if (model != null) {
		//		int idModel = model.dbNewAs(); 
		//		showModels.showModel(type, idModel);
		//	}
			//System.out.println("PParButtons:AddAs =  "+type );
			if (type.equals("Parameters")){
				int row = showFiles.getSelectedRow();
				String namePar = "namePar";
				String valuePar = "valuePar";
				String descPar = "description";
				if(row>=0){
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
				}
				if (addParameter(namePar, valuePar, descPar)){
					//System.out.println("....Add New value param..");
					ArrayList pl = null;
					pl = db.getAllParameter();
					showFiles.showInputModel(pl);
				}  
				else System.out.println("...Nothing...");  
			}	
		}	
	}
	class SetNameFile implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			fc.setDialogTitle("Select file");
			fc.setApproveButtonText("Вибрати файл");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int res = fc.showOpenDialog(PnParButtons.this);
			if (res==JFileChooser.APPROVE_OPTION ) 
				
			nmFile.setText(fc.getSelectedFile().getAbsolutePath());
		}	
	}
	
	class ModelOutput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			System.out.println("PParButtons:ModelOutput =  "+ type );
			WorkFile wf= WorkFile.getWorkFile();
			OutputText out = wf.getOut();
			String text = "Файл для виведення не вказано !";
			String name = nmFile.getText();
			if (!name.isEmpty()){
				//if (type.equals("Output")) {
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
				//} 
		//		else {
		//			if (model != null) {
		//				text = model.output(name,out);
		//				if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " виведено в файл " + name + "!";
		//			} 
		//		} 
				
			}
		  JOptionPane.showMessageDialog(PnParButtons.this,text);
		}
	}
	class ModelInput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			System.out.println("PParButtons:ModelInput =  "+ type );
			String text = "Файл для введення не вказано !";
			String name = nmFile.getText();
			Model model = null;
			if (!name.isEmpty()) {
				WorkFile wf = WorkFile.getWorkFile();
				if (type.equals("Input")) {
					if(wf.inputListModel(db, name)) {
						showFiles.showInputModel(wf.getListModel());
					}
					text = wf.getErrorText();
				} 
			//	else {
			//		model = wf.inputModel(name);
			//		if (model != null) {
			//			type = model.getType();
			//			String nameIn = model.name;
						//System.out.println("ShowModelButton:ModelInput " + type + " " + nameIn);
			//			int idModel = db.addModel(type, model);
			//			if (idModel > 0) {
			//				showModels.showModel(type, idModel);
			//				text = Model.title(type, 8) + " " + model.name + " з файлу " + name + "  введено!";
			//			}
			//			else text = Model.title(type, 8)+ " " + nameIn + " з файлу " + name + "  введено, але не збережено в базі даних !";
			//		}
			//		else text = wf.getErrorText();
			//	}	
			}
			JOptionPane.showMessageDialog(PnParButtons.this,text); // text);
		}	
	}
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
			showModels.setModel("NoModel", 0);
		}	
	}
	/*
	class ModelReport implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String str = "abbc";
			String pt = "ce";
			String text = " findFirst(\""+str+"\",\""+pt+"\") = " ;
			JOptionPane.showMessageDialog(PParButtons.this,"ModelReport...");
		}	
	}
	*/
	class Delete implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//System.out.println("PParButtons:Delete =  "+ type );
			//if (type.equals("Parameters")){
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
						JOptionPane.showMessageDialog(PnParButtons.this,
								 "Значення " + text + "- НЕ можна вилучити. \nРозділ " + valuePar + " містить " + cnt + 
								 " моделей.\n Для вилучення цього значення необхідно вилучити з розділу ВСІ моделі!");
					}
					}	
				if (del){
					UIManager.put("OptionPane.yesButtonText", "Так");
					UIManager.put("OptionPane.noButtonText", "Ні");
					int res = JOptionPane.showConfirmDialog(PnParButtons.this,
													"Вилучити значення " + text,"Вилучити ?", JOptionPane.YES_NO_OPTION );
					if (res == JOptionPane.YES_OPTION) {
						ArrayList pl = null;
						db.deleteParameterValue(namePar, valuePar);
						pl = db.getAllParameter();
						showFiles.showInputModel(pl);
					}
				}
				
			} //else System.out.println("...Nothing...Row = " + row);  
			//}
		//	else if (model != null){ 
		//		String text = "Вилучити " + Model.title(type, 6) + " "+ model.name + " з номером " + model.id + " ?";
		//		UIManager.put("OptionPane.yesButtonText", "Так");
		//		UIManager.put("OptionPane.noButtonText", "Ні");
		//		int res = JOptionPane.showConfirmDialog(PParButtons.this,text,"Вилучити ?",JOptionPane.YES_NO_OPTION );
		//		if (res == JOptionPane.YES_OPTION) {
		//			int newId = model.getDbOrder();
		//			if (model.dbDelete()) newId = model.getDbNumber(newId-1);
		//			showModels.showModel(type, newId);
		//		}
		//	}
		}	
	}
	class Edit implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			
			/*
			if (type.equals("State")) {
				int row = showFiles.getSelectedRow();
				String namePar = "", valuePar = "", descPar = "";
				if(row>=0){
					namePar = showFiles.getValue(row, 0);
					valuePar = showFiles.getValue(row, 1);
					descPar = showFiles.getValue(row, 2);
					if (setParameter(namePar, valuePar, descPar)) {
						db.setParameters();
						ArrayList pl = null;
						pl = db.getStateParameter();
						showFiles.showInputModel(pl);
					}  
				} //else System.out.println("...Nothing...Row = " + row);  	
			}    
			else
			*/ 
			//if (type.equals("Parameters")){
			int row = showFiles.getSelectedRow();
			String namePar = "", valuePar = "", descPar = "";
			//System.out.println("PParButtons:ModelWork =  "+ type + " " +row );
			if(row>=0){
				namePar = showFiles.getValue(row, 0);
				valuePar = showFiles.getValue(row, 1);
				descPar = showFiles.getValue(row, 2);
				if (updateParameterDesc(namePar, valuePar, descPar)) {
					ArrayList pl = null;
					pl = db.getAllParameter();
					showFiles.showInputModel(pl);
				}  
			}	
			//}	
		//	else if (model != null){
		//		showWork.setModel(type, model);
	//			showWork.show();
	//		}	
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
		int res = JOptionPane.showOptionDialog(PnParButtons.this, panelPar, "Нове значення параметру",
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
			else JOptionPane.showMessageDialog(PnParButtons.this, "Значення параметру " + name1 + " : " + value1 + " -- ВЖЕ єсть!! ");
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
		for (int i = 0; i < all.size(); i++){
			values[i] = all.get(i);
			if(values[i].equals(value)) j = i;
		}
		UIManager.put("OptionPane.okButtonText", "Так");
		UIManager.put("OptionPane.cancelButtonText", "Вийти");
		String text = "Параметр " + name + " : " + value + " <" + desc + ">";
		Object res = JOptionPane.showInputDialog(PnParButtons.this, 
						text, "Змінити значення параметру", JOptionPane.QUESTION_MESSAGE, null,  values, values[j]);
		if(res != null){
			if (!value.equals((String)res)){
				db.updateStateParameter (name, (String)res);
				rs = true;
			}	
		}
		return rs;
	}
	
	// true => модифікує опис значення параметру
	private boolean updateParameterDesc(String name, String value, String desc) {
		boolean rs = false;	
		String text = "Параметр " + name + " : " + value + " <" + desc + ">";
		Object res = JOptionPane.showInputDialog(PnParButtons.this, 
						text, "Відредагувати опис параметру", JOptionPane.QUESTION_MESSAGE, null,null, desc );
		if(res != null){
			if (!desc.equals((String)res)){
				db.updateParameterDesc (name, value,(String)res);
				rs = true;
			}	
		}
		return rs;
	}	
}
