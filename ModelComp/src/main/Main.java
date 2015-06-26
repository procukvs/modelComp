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
		System.out.println("Forming GUI");
	    if (db.connectionDb("Model.db")) { 
	       	// ���� ������������ ������ � �� ��������� �������� ������ opr
	        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        gui.addWindowListener(new EndWork());
	        gui.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db");
	
	   // Testing.testSubstitution();
	   // Testing.test();
	  // Testing.findSubstit(); 
	  //  Testing.testBody();
	}
	
	public static void main(String[] args) {
		// ³��� ��������� � ������ ������� ����.
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// ���������� �� ������ ���������� ������� !!!!
				try {
					UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());
				}
				catch (Exception ex) { System.err.println(ex);}
				new Main();
			}
		});
	}
	class EndWork extends WindowAdapter {
		// �������� ��� ������ ---- ��������� ���� �����
		public void  windowClosed(WindowEvent e)  {
	    	db.disConnect();  
            System.exit(0);
		}
	}
	
}
