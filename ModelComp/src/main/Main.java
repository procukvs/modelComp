package main;

import java.awt.event.WindowAdapter;
import java.awt.event.*;
import javax.swing.*;

import db.*;
import gui.*;

public class Main {
	DbAccess db;
	ShowModels gui;
	
	Main(){
		db  = new  DbAccess ();
		gui = new ShowModels(db);
		System.out.println("Forming GUI");
	    if (db.connectionDb("Model.db")) { 
	    	//System.out.println("Is connection to DB Algo");
	    	// після встановлення звязку з БД створюємо обробник запитів opr
	        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        gui.addWindowListener(new EndWork());
	        gui.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db");
	}
	
	public static void main(String[] args) {
		// Вікно створюємо в потоці обробки подій.
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				new Main();
			}
		});
	}
	class EndWork extends WindowAdapter {
		// закінчуємо всю роботу ---- закриваємо базу даних
		public void  windowClosed(WindowEvent e)  {
	    	db.disConnect();  
            System.exit(0);
		}
	}
	
}
