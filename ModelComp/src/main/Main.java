package main;

import java.awt.event.*;

import javax.swing.*;

import db.*;
import gui.*;

public class Main {
	DbAccess db;
	ShowModels gui;
		
	Main(){
		//=====================
		//db  = new  DbAccess ();
		//======================
		db = DbAccess.getDbAccess();
		//====================
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		gui = new ShowModels(db);
		//gui.setListener(gui);
		System.out.println("Forming GUI-- version " + Parameters.getRegime() + "..");
	    if (db.connectionDb("Model.db")) { 
	  	   if (Parameters.getRegime().equals("teacher")) db.setParameters();
	       	// після встановлення звязку з БД створюємо обробник запитів opr
	        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        gui.addWindowListener(new EndWork());
	        gui.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db -- version Teacher..");
	
	   // Testing.testSubstitution();
	   // Testing.test();
	  // Testing.findSubstit(); 
	  //  Testing.testBody();
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			 if(args[0].equals("Teacher")) Parameters.setRegime("teacher");
		}
		// Вікно створюємо в потоці обробки подій.
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// підключення ще ОДНОГО зовнішнього вигляду !!!!
				try {
					UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());
				}
				catch (Exception ex) { System.err.println(ex);}
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
