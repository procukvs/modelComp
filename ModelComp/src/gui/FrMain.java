package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import db.*;
import gui.model.*;
import gui.system.*;
import main.*;

public class FrMain extends JFrame {
	private DbAccess db;
	private String type = "Algorithm";
	private Model model = null;
	private JLabel label;
	private PnModel pModel;
	private PnParTable pParTable;
	private PnModButtons pModButtons;
	private PnParButtons pParButtons;
	
	public FrMain(DbAccess db){
		super("Models of computation");
		this.db = db;
		//  сформувати необхідні gui-елементи 
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("Вийти з програми");
		// створюємо випадаюче меню, що містить звичайні елементи меню
		JMenu mModel= new JMenu("Модель обчислень   ");
		// елемент меню - команда
		JMenuItem computer = new JMenuItem(Model.title("Computer", 1));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  		//("Машини Тьюрінга");
		JMenuItem algorithm = new JMenuItem(Model.title("Algorithm", 1));	//(new AlgorithmAction()); // 
	 	JMenuItem post = new JMenuItem(Model.title("Post", 1));
		JMenuItem rec = new JMenuItem(Model.title("Recursive", 1));
		JMenuItem lambda = new JMenuItem(Model.title("Calculus", 1));
		
		
		mModel.add(computer);
		mModel.add(machine);
		mModel.add(algorithm);
		mModel.add(post);
		mModel.add(rec);
		mModel.add(lambda);
		menuBar.add(mModel);
			
		JMenu mFile= new JMenu("Системні дії   ");
		JMenuItem input = new JMenuItem("Введення моделей з файлу");
		JMenuItem output = new JMenuItem("Виведення моделей в файл");
		
		mFile.add(input);
		mFile.add(output);
	
		menuBar.add(mFile);
		
		JMenu mParameter = new JMenu("Параметри    ");
		JMenuItem state = new JMenuItem("Стан параметрів");
		JMenuItem parameters = new JMenuItem("Встановлення параметрів");
		if(Parameters.getRegime().equals("teacher")){
			mFile.add(state);
			mFile.add(parameters);
		}
		JMenu plafmenu = createPlafMenu(); // Створюємо меню
		menuBar.add(plafmenu);     // Додаємо меню в полосу меню
		menuBar.add(quit);
		setJMenuBar(menuBar);
		
		computer.addActionListener(new LsComputer());
		algorithm.addActionListener(new LsAlgorithm());
		machine.addActionListener(new LsMachine());
		post.addActionListener(new LsPost());
		rec.addActionListener(new LsRecursive());
		lambda.addActionListener(new LsCalculus());
		input.addActionListener(new LsInput());
		output.addActionListener(new LsOutput());
		state.addActionListener(new LsState());
		parameters.addActionListener(new LsParameters());
		quit.addActionListener(new LsQuit());
				
		label = new JLabel("Нормальні алгоритми Маркова");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		pModel = new PnModel(db);          // !!!!!!!!!!!!!!!!!!ref!!!!!!!!!!!!!!!!!!!!
		pModel.setEnv(this);                 // !!!!!!!!!!!!!!!!!!ref!!!!!!!!!!!!!!!!!!!!
		//-------------------------------
		pParTable = new PnParTable (db);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		pModButtons = new PnModButtons(db);                // !!!!!!!!!!!!!!!!!!ref!!!!!!!!!!!!!!!!!!!!
		pModButtons.setEnv(this);                         // !!!!!!!!!!!!!!!!!!ref!!!!!!!!!!!!!!!!!!!!
		//pModButtons.setFiles(showFiles);                     // !!!!!!!!!!!!!!!!!!ref!!!!!!!!!!!!!!!!!!!!
		pParButtons = new PnParButtons(db); 
		pParButtons.setEnv(this, pParTable);
		//===============================
		// розташуємо їх
		//-----------------------------
		Box bxCenter = Box.createVerticalBox();
		bxCenter.add(pModel);
		bxCenter.add(pParTable);
		Box bxSouth = Box.createVerticalBox();
		bxSouth.add(pModButtons);
		bxSouth.add(pParButtons);
		//========================
		
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(bxCenter, BorderLayout.CENTER);
		getContentPane().add(bxSouth, BorderLayout.SOUTH);
		setVisiblePane("NoModel");
		setSize(800,500);
 	   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  	   	addWindowListener(new EndWork());
	}	
	
	private void setVisiblePane( String type) {
		boolean close = type.equals("NoModel");
		boolean files = type.equals("Input") || type.equals("Output") 
				         || type.equals("State") || type.equals("Parameters");
		boolean model = type.equals("Computer") || type.equals("Algorithm")  || type.equals("Machine") 
		         || type.equals("Post") || type.equals("Recursive")  || type.equals("Calculus") ;
		label.setVisible(!close);
		pModel.setVisible(model);
		pParTable.setVisible(files);
		pModButtons.setVisible(model);
		pParButtons.setVisible(files);
	}
	
	private JMenu createPlafMenu() {
		JMenu plafmenu = new JMenu("Зовнішній вигляд");
		UIManager.LookAndFeelInfo[] plafs = 
				UIManager.getInstalledLookAndFeels();
		
		for(int i = 0; i < plafs.length; i++) {
			String plafName = plafs[i].getName();
			final String plafClassName = plafs[i].getClassName();
			
			JMenuItem item = plafmenu.add(new JMenuItem(plafName));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					try {
						UIManager.setLookAndFeel(plafClassName);
						SwingUtilities.updateComponentTreeUI(FrMain.this);
						//showModelButtons.setLookAndFeel(plafClassName);
						//showModelOne.setLookAndFeel(plafClassName);
					}
					catch (Exception ex) { System.err.println(ex);}
				}
			});
		}
		return plafmenu;
	}
		
	public void setModel(String type, int id) {
		//boolean files = type.equals("Input") || type.equals("Output");
		String text = Model.title(type, 1);
		this.type = type;
		//System.out.println("ShowModel start ..." + type + "  " + id + " " + this.type);
		setVisiblePane(type);
		switch (type){
		case "Input": case "Output":
		case "State": case "Parameters":	
			ArrayList fs = null;
			switch (type){
			case "Output": fs = db.getAllModel(); break;
			case "State": fs = db.getStateParameter(); break;
			case "Parameters": fs = db.getAllParameter(); break;	
			}	
			pParTable.showTableFiles(type, fs);
			pParButtons.setModel(type);
			model = null;
			break;
		case "NoModel": break;
		default:
			if (!type.equals(this.type)) {
				// set label !!!!!!!!
				if (text.isEmpty()) text = "Not realise " + type + " !";
			}
			if (id == 0) model = null; else model = db.getModel(type, id);
			pModel.setModel(type,model);
			pModButtons.setModel(type,model);
		}
		label.setText(text);
	/*		
	*/		
	}
		
	class LsComputer implements ActionListener  {
		public void actionPerformed(ActionEvent e) {setModel("Computer",0);	}	
	}
	
	class LsAlgorithm implements ActionListener  {
		public void actionPerformed(ActionEvent e) {setModel("Algorithm",0);	}	
	}
	
	class LsMachine implements ActionListener  {
		public void actionPerformed(ActionEvent e) {setModel("Machine",0);	}	
	}
	
	class LsPost implements ActionListener  {
		public void actionPerformed(ActionEvent e) {setModel("Post",0);	}	
	}
	
	class LsRecursive implements ActionListener  {
		public void actionPerformed(ActionEvent e) {setModel("Recursive",0);	}	
	}
	
	class LsCalculus implements ActionListener {
		public void actionPerformed(ActionEvent e) {setModel("Calculus",0);	}
	}
	
	class LsInput extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			setModel("Input",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"Input..");
		}
	}
	
	class LsOutput extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			setModel("Output",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"Output..");
		}
	}
	
	class LsState extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			setModel("State",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"State..");
		}
	}	

	class LsParameters extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			setModel("Parameters",0);
			//JOptionPane.showMessageDialog(ShowModels.this,"Parameters..");
		}
	}	
	
	class LsQuit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	
	class EndWork extends WindowAdapter {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void  windowClosed(WindowEvent e)  {
	    	db.disConnect();  
            System.exit(0);
		}
	}
	
}
