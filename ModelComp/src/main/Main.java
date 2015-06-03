package main;

import java.awt.event.*;
import javax.swing.*;

import db.*;
import gui.*;

public class Main {
	DbAccess db;
	//!!!!!!!!!!!!!!!!!!!
	ShowModels gui;
	//ShowMenu gui;
	
	Main(){
		db  = new  DbAccess ();
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		gui = new ShowModels(db);
		//gui = new ShowMenu(db);
		System.out.println("Forming GUI");
	    if (db.connectionDb("Model.db")) { 
	    	//System.out.println("Is connection to DB Algo");
	    	// ���� ������������ ������ � �� ��������� �������� ������ opr
	        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        gui.addWindowListener(new EndWork());
	        gui.setVisible(true);
	    } 
	    else System.out.println("No connection to DB Model.db");
	
	    Testing.formMachine();
	}
	
	public static void main(String[] args) {
		// ³��� ��������� � ������ ������� ����.
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
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
