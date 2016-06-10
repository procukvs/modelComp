package gui.model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import db.*;
import gui.*;

import main.*;


public class PnModel extends JPanel {
	private DbAccess db;
	//!!!!!!!!!!!!!!!!!!!!!!!
	private FrMain fMain;
	private String type = "Algorithm";
	
	private Model model = null;
	private int idModel = 0;  ///????????????
	// завжди виділяється одна модель із списку моделей !!!
	// порядок в списку визначається в БД 
		
	private PnDescription pDescription;
	private PnComTable pComTable;
	private PnComButtons pComButtons;
	
	
	//!!!!!!!!!!!!!!!!!!!!!
	public PnModel(DbAccess  db){	// !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		//сформувати необхідні gui-елементи 
		pDescription = new PnDescription(true);
		pComTable = new PnComTable(db);
		pComButtons = new PnComButtons(db); // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		this.db = db; 
		//=================================
		// формуємо розміщення
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());
		//-----------------------------
		add(pDescription, BorderLayout.NORTH);
		add(pComTable, BorderLayout.CENTER);
		add(pComButtons, BorderLayout.SOUTH);
	}	
	
	public void setEnv(FrMain owner){     // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		fMain = owner;                        // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		pDescription.setEnv(db, fMain);       // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		pComTable.setEnv(pComButtons);             // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!
		pComButtons.setEnv(fMain,pDescription,  pComTable); 
	}                                            // !!!!!!!!!!!!!ref!!!!!!!!!!!!!!!  
	
	public void setModel(String type, Model model) {
		this.model = model;
		this.type = type;
		
	   	pDescription.setModel(type, model);
		if (model == null )idModel = 0; else idModel = model.id;
		pComTable.setModel(type, model);
       	pComButtons.setModel(type, model);
   	}
	
	public void setLookAndFeel(String className){
		//program.setLookAndFeel(className);
	}

}
