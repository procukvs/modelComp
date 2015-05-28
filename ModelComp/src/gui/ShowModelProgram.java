package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import db.*;


public class ShowModelProgram extends JPanel {
	DbAccess db;
	
	ShowModelProgram(DbAccess  db, Frame owner){
		
		//сформувати необхідні gui-елементи 
		//JLabel label = new JLabel("Підстановки алгоритма");
		//label.setHorizontalAlignment(label.CENTER);
		//JButton table = new JButton("This is place for table");
		ShowCommandButtons buttons = new ShowCommandButtons(db);
		ShowModelTable table = new ShowModelTable(db,owner);
		//JPanel buttons = new JPanel();
		
		this.db = db; 
		//=================================
		// формуємо розміщення
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(SoftBevelBorder.RAISED));
		//add(label, BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		
		
		
		//===============================
	/*	JButton test = new JButton("Перевірка");
		test.addActionListener(new SelectTesting());
		JButton add = new JButton("Нова");
		add.addActionListener(new RuleAdd());
		JButton edit = new JButton("Редагувати");
		edit.addActionListener(new RuleEdit());
		JButton delete = new JButton("Вилучити");
		delete.addActionListener(new RuleDelete());
		JButton up = new JButton("Перемістити вверх");
		up.addActionListener(new RuleUp());
		JButton down = new JButton("Перемістити вниз");
		down.addActionListener(new RuleDown());
		buttons.add(test);
		buttons.add(add); buttons.add(edit); 
		buttons.add(delete);
		buttons.add(up); buttons.add(down); 
		workRule = new WorkRule(owner); */
	}


}
