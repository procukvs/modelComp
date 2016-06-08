package main;

import java.awt.event.*;
import javax.swing.*;

import db.*;
import gui.*;



public class Main {
	private DbAccess db;
	private FrMain fMain;
		
	Main(){
		db = DbAccess.getDbAccess();
		//====================
		System.out.println("Forming GUI-- version 3.1.1: " + Parameters.getRegime() + "..");
	    if (db.connectionDb("Model.db")) { 
	    	if (Parameters.getRegime().equals("teacher")) db.setParameters();
			fMain = new FrMain(db);    	 
	  	   	fMain.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db -- version Teacher..");
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
