package gui;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import db.*;

public class ShowMenu extends JFrame {
	private DbAccess db;
	private ShowModelAll algo;
	
	public ShowMenu(DbAccess db) {
		super("Computing Models");
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.db = db;
		// створюємо рядок головного меню
		JMenuBar  menuBar = new JMenuBar();
		JButton quit = new JButton("Вийти");
		// додаємо меню, що випадають
		menuBar.add(createModelMenu());
		menuBar.add(quit);
		// встановлюємо рядок головного меню - як меню нашого вікна 
		setJMenuBar(menuBar);
		//setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		algo = new ShowModelAll(db, this);
		
		//getContentPane().add(algo);
		getContentPane().add(algo, BorderLayout.CENTER);
		algo.setVisible(false);
		// виводимо вікно на екран
		setSize(800,500);
		//pack();
		setVisible(true);	
		
		quit.addActionListener(new Quit());
		
		
	}
	//  створюємо меню "Model"
	private JMenu createModelMenu () {
		// створюємо випадаюче меню, що містить звичайні елементи меню
		JMenu model= new JMenu("Model");
		JMenuItem algorithm = new JMenuItem(new AlgorithmAction()); // ("Open", new ImageIcon("images\\icon_arrow.gif"));
		// елемент меню - команда
		JMenuItem machine = new JMenuItem(new MachineAction()); //(new ExitAction());
		model.add(algorithm);
		// розподільник
		//file.addSeparator();
		model.add(machine);
		return model;
	}
	class Quit implements ActionListener  {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void actionPerformed(ActionEvent e) {
		   	db.disConnect();  
            System.exit(0);
		}	
	}
	class AlgorithmAction extends AbstractAction {
		AlgorithmAction() {putValue(NAME,"Algorithm");}
		public void actionPerformed(ActionEvent e) {
			
			algo.setVisible(true);
			//pack();
			System.out.println("Algo visible !!");
		  	//JOptionPane.showMessageDialog(ShowMenu.this,"Algorithm..");
		}
	}
	class MachineAction extends AbstractAction {
		MachineAction() {putValue(NAME,"Machine");}
		public void actionPerformed(ActionEvent e) {
			algo.setVisible(false);
		  	JOptionPane.showMessageDialog(ShowMenu.this,"Machine..");
		}
	}

}
