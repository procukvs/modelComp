package gui;

import main.*;
import db.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;




public class ShowModelProgram extends JPanel {
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!
	private ShowModels showMain;
	//private ShowModelAll showMain;
	private String type = "Algorithm";
	private Model model = null;
	
	private int idModel = 0;  ///????????????
	private ShowModelTable table;
	private ShowCommandButtons buttons;
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	ShowModelProgram(DbAccess  db, ShowModels owner){
	//ShowModelProgram(DbAccess  db, ShowModelAll owner, ShowMenu frame){
		
		//���������� �������� gui-�������� 
		table = new ShowModelTable(db);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		buttons = new ShowCommandButtons(db,table, owner);	
		//buttons = new ShowCommandButtons(db,table, owner, frame);	
		this.db = db; 
		showMain = owner;
		//=================================
		// ������� ���������
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(SoftBevelBorder.RAISED));
		//add(label, BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		
		
		
		//===============================
	/*	JButton test = new JButton("��������");
		test.addActionListener(new SelectTesting());
		JButton add = new JButton("����");
		add.addActionListener(new RuleAdd());
		JButton edit = new JButton("����������");
		edit.addActionListener(new RuleEdit());
		JButton delete = new JButton("��������");
		delete.addActionListener(new RuleDelete());
		JButton up = new JButton("���������� �����");
		up.addActionListener(new RuleUp());
		JButton down = new JButton("���������� ����");
		down.addActionListener(new RuleDown());
		buttons.add(test);
		buttons.add(add); buttons.add(edit); 
		buttons.add(delete);
		buttons.add(up); buttons.add(down); 
		workRule = new WorkRule(owner); */
	}

	public void setModel(String type, Model model){
		this.type = type;
		this.model = model;
		if (model == null )idModel = 0; else idModel = model.id;
		table.showModel(type, model);
		buttons.setModel(type, model);
		
	}
	
	
}
