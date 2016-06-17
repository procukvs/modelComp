package main;

import java.awt.event.*;
import javax.swing.*;

import db.*;
import gui.*;
import model.AllModels;

public class Main {
	private static DbAccess db;
	private FrMain fMain;
	private static AllModels env=null;
	// Cтворює всі об"єкти, відкриває (ініціалізує) ресурси
	// Запускає gui
	// Закінсує роботи (метод andWork)
	
	Main(){
		db = DbAccess.getDbAccess();
		//====================
		
	    if (db.connectionDb("Model.db")) { 
	    	if (Parameters.getRegime().equals("teacher")) db.setParameters();
	    	System.out.println("Forming GUI-- " + "version " + Parameters.getVersion() + ": " + Parameters.getRegime() + "..");
	    	// Cтворює всі об"єкти, відкриває (ініціалізує) ресурси
			fMain = new FrMain(db); 
			env = AllModels.getAllModels(db, fMain); 
			fMain.setEnv(db,env);
			// Запускає gui
			fMain.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db -- version Teacher..");
	}
	
	
	
	public static void endWork(){
	   // закриває всі ресурси і закінчує роботу
	   	db.disConnect();  
        System.exit(0);
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

	
/*	
	private WShowModels showModels;
	Main(){
		showModels = new WShowModels();
	  	showModels.setVisible(true);
	}

	public static void main(String[] args) {
		System.out.println("Forming GUI-- testing WindowBuilder ..");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
					System.out.println("...Test1...");			
				} catch (Exception e) {
					e.printStackTrace();
				}
				new Main();
			}
		});
	}
*/
}
