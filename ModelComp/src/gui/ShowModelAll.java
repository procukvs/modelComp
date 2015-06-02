package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import javax.swing.*;
import db.*;
import main.*;

public class ShowModelAll extends JPanel {
	private DbAccess db;
	private String type = "Algorithm";
	private Model model = null;
	
	private JLabel label;
	private ShowModelOne showModel;
	private ShowModelButtons modelButtons;
	//public ShowMenu mainMenu = null;
	
	public ShowModelAll(DbAccess db, ShowMenu owner){
		//super("mAlgorithm");
		this.db = db;
		//  сформувати необхідні gui-елементи 
		label = new JLabel("Нормальні алгоритми Маркова");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		//showModel = new ShowModelOne(db, this, owner);
		//-------------------------------
	   	//modelButtons = new ShowModelButtons(db,this,owner);
		//===============================
		// розташуємо їх
		add(label, BorderLayout.NORTH);
		add(showModel, BorderLayout.CENTER);
		add(modelButtons, BorderLayout.SOUTH);
		//setSize(800,500);
		owner.pack();
	}	
	
	public void showModel(String type, int id) {
		if (id == 0) model = null; else model = db.getModel(type, id);
		if (!type.equals(this.type)) {
			// set label !!!!!!!!
		}
		showModel.setModel(type,model);
		modelButtons.setModel(type,model);
	}

}
