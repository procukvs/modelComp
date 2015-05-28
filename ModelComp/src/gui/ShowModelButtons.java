package gui;

import db.*;

import java.awt.*;
import java.awt.event.*;
//import javax.swing.border.*;
import javax.swing.*;

public class ShowModelButtons extends JPanel {
	DbAccess db;
	
	ShowModelButtons(DbAccess db){
		//сформувати необхідні gui-елементи 
		JButton add = new JButton("Новий");
		JButton addBase = new JButton("Новий на основі");
		JButton report = new JButton("Звіти");
		JButton delete = new JButton("Вилучити");
		JButton file = new JButton("Файл ...");
		JTextField nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		JButton work = new JButton("Рoбота з алгоритмом");
		JButton output = new JButton("Вивести алгоритм в файл");
		JButton input = new JButton("Ввести алгоритм з файлу");
		JButton quit = new JButton("Вийти");
		
		this.db = db;
		//=================================
		// формуємо розміщення
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
	
	//описуємо класи - слухачі !!!!!!
	class Add implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
			//Algorithm newModel = db.newAlgorithm(); 
		   	//if (newModel != null) showModel.showLast();
		   	JOptionPane.showMessageDialog(ShowModelButtons.this,"Add..");
		}	
	}
	class AddAs implements ActionListener  {
		// 
		public void actionPerformed(ActionEvent e) {
		//	Algorithm model = showModel.getAlgorithm();
		//	if (model != null) {
		//		Algorithm newModel = db.newAlgorithmAs(model); 
		//		if (newModel != null) showModel.showLast();
		//	}
		   	JOptionPane.showMessageDialog(ShowModelButtons.this,"AddAs..");
		}	
	}
	class SetNameFile implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			                               //UIManager.put("JFileChooser.cancelButtonText", "+++");
			//fc.setDialogTitle("Select file");
			//fc.setApproveButtonText("Вибрати файл");
			                              //fc.setApproveButtonToolTipText("++++");
			//fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//int res = fc.showOpenDialog(ShowModel.this);
			//if (res==JFileChooser.APPROVE_OPTION ) 
				
			//	nmFile.setText(fc.getSelectedFile().getAbsolutePath());
			JOptionPane.showMessageDialog(ShowModelButtons.this,"SetNameFile..." ); // fc.getSelectedFile());
		}	
	}
	
	class ModelOutput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//Algorithm model;
			//model = showModel.getAlgorithm();
			String text = "Файл для виведення не вказано !";
			//if (model != null) {
			//	String name = nmFile.getText();
			//	if (!name.isEmpty()){
					//text = "Вивести модель " + model.nmAlgo + 
					//		" в файл " + fc.getSelectedFile().getAbsolutePath() + "...";
					//String[] wr = new String[model.asub.size()];
					//Rule r ; 
					//for (int i = 0; i < model.asub.size(); i++ ) {
					//	r = (Rule)model.asub.get(i);
					//	wr[i] = r.getsLeft() + "->" + r.getsRigth();
					//}
			//		WorkFile wf = new WorkFile();
			//		text = wf.outputAlgorithm(name,model);
			//		if(text.isEmpty()) text = "Модель " + model.nmAlgo + " виведено в файл " + name + "!";
			//	}	
			//	JOptionPane.showMessageDialog(ShowModel.this,text);
			//}
			JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelOutput...");
		}	
	}
	class ModelInput implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "Файл для введення не вказано !";
		//	String name = nmFile.getText();
		//	Algorithm model;
		//	if (!name.isEmpty()) {
				//text = "Ввести модель з файлу " 
				//		+ fc.getSelectedFile().getAbsolutePath() + "...";
		//		WorkFile wf = new WorkFile();
		//		model = wf.inputAlgorithm(name);
		//		if (model != null) {
		//			String nameIn = model.nmAlgo;
		//			model = db.addAlgorithm(model);
		//			if (model != null) {
		//				showModel.showLast();
		//				text = "Алгоритм " + model.nmAlgo + " з файлу " + name + "  введено!";
		//			}
		//			else text = "Алгоритм " + nameIn + " з файлу " + name + "  введено, але не збережено в базі даних !";
		//		}
		//		else text = wf.getErrorText();
		//	}	
			JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelInput..."); // text);
		}	
	}
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
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
			///Algorithm model = showModel.getAlgorithm();
			//if (model != null){ 
			//	String text = "Вилучити алгоритм "+ model.nmAlgo + " з номером " + model.algo + " ?";
				UIManager.put("OptionPane.yesButtonText", "Так");
				UIManager.put("OptionPane.noButtonText", "Ні");
			//	int res = JOptionPane.showConfirmDialog(ShowModel.this,text,"Вилучити ?",JOptionPane.YES_NO_OPTION );
			//	if (res == JOptionPane.YES_OPTION) {
			//		db.deleteAlgorithm(model.algo);
			//		showModel.showPrev();
				
			//	}
			//}
			JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelDelete...");
		}	
	}
	class ModelWork implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
		//	Algorithm model = showModel.getAlgorithm();
		//	if (model != null){
		//		workAlgo.setModel(model);
		//		workAlgo.show();
				//(showModel.work).show();
			
		//	}	
			JOptionPane.showMessageDialog(ShowModelButtons.this,"ModelWork...");		
		}	
	}	

}
