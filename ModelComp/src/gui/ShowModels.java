package gui;

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
		super("Models of computation");
		this.db = db;
		//  сформувати необхідні gui-елементи 
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("Вийти з програми");
		// створюємо випадаюче меню, що містить звичайні елементи меню
		JMenu mModel= new JMenu("Модель обчислень");
		// елемент меню - команда
		JMenuItem computer = new JMenuItem(Model.title("Computer", 1));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  		//("Машини Тьюрінга");
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
		menuBar.add(quit);
		setJMenuBar(menuBar);
		
		computer.addActionListener(new LsComputer());
		algorithm.addActionListener(new LsAlgorithm());
		machine.addActionListener(new LsMachine());
		post.addActionListener(new LsPost());
		rec.addActionListener(new LsRecursive());
		quit.addActionListener(new LsQuit());
				
		label = new JLabel("Нормальні алгоритми Маркова");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		showModel = new ShowModelOne(db, this);
		//-------------------------------
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		modelButtons = new ShowModelButtons(db,this);
		//===============================
		// розташуємо їх
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(showModel, BorderLayout.CENTER);
		getContentPane().add(modelButtons, BorderLayout.SOUTH);
		setVisiblePane(false);
		setSize(800,500);
		//pack();
	}	
	
	private void setVisiblePane(boolean isVisible) {
		label.setVisible(isVisible);
		showModel.setVisible(isVisible);
		modelButtons.setVisible(isVisible);
	}
	
	public void showModel(String type, int id) {
		//System.out.println("ShowModel start ..." + type + "  " + id + " " + this.type);
		setVisiblePane(!type.equals("NoModel"));
		if (!type.equals(this.type)) {
			// set label !!!!!!!!
			String text = Model.title(type, 1);
			if (text.isEmpty()) text = "Not realise " + type + " !";
			label.setText(text);
			this.type = type;
		}
		if (id == 0) model = null; else model = db.getModel(type, id);
		//System.out.println("showModelstart ..." + type + "  " + id );
		//if (model != null )	System.out.println(model.show());
		showModel.setModel(type,model);
		modelButtons.setModel(type,model);
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
	
	class LsQuit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	
	/*
	 private JMenu createModelMenu () {
		// створюємо випадаюче меню, що містить звичайні елементи меню
		JMenu model= new JMenu("Модель обчислень");
		// елемент меню - команда
		JMenuItem algorithm = new JMenuItem(new AlgorithmAction()); // ("Open", new ImageIcon("images\\icon_arrow.gif"));
		JMenuItem machine = new JMenuItem(Model.title("Machine", 1));  //("Машини Тьюрінга"); 
		machine.addActionListener(new LsMachine());
		
		model.add(algorithm);
		model.add(machine);
		return model;
		// розподільник
		//file.addSeparator();
	}
	 class AlgorithmAction extends AbstractAction {
		AlgorithmAction() {putValue(NAME,Model.title("Algorithm", 1));}   //"Нормальні алгоритми Маркова"
		public void actionPerformed(ActionEvent e) {
			showModel("Algorithm",0);
		}
	} 
	  
	  class MachineAction extends AbstractAction {
		MachineAction() {putValue(NAME,Model.title("Machine", 1));}  // "Машини Тьюрінга"
		public void actionPerformed(ActionEvent e) {
			showModel("Machine",0);
			  //	JOptionPane.showMessageDialog(ShowModels.this,"Machine..");
		}
	} */
	
	


}
