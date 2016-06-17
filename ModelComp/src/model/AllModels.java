package model;

import java.util.*;


import db.*;
import file.OutputText;
import file.WorkFile;
import gui.*;
import main.*;

public class AllModels {
    // клас для інтерфейсу gui і всіх моделей
	//
	// realizy singleton !!!
	
	private static AllModels all;
	private String section= "";
	private String type = "";
	private ArrayList <Pair> list = null;
	private int pos = 0;
	private Model model = null;
	
	private DbAccess db;
	private FrMain fMain;
	
	private AllModels(DbAccess db, FrMain fMain){ 
		this.db= db; this.fMain = fMain;	
	}
	public static AllModels getAllModels(DbAccess db, FrMain fMain) {
		if(all == null) all = new AllModels(db, fMain);
		return all;
	}
	
	public String getSection() {return section;}
	public String getType() {return type;}
	public int getPos() {return pos;}
	public Model getModel() {return model;} 
	public int getCntModel() {return list.size();}
	
	
	public void set(String type){
		this.type = type;
		section = Parameters.getSection();
		setAll(0);
	}
	public ArrayList <String> getAllModel(){
		ArrayList <String> allNm = new ArrayList <String> ();
		for(int i=0; i<list.size();i++) 
			allNm.add(new String(list.get(i).getName()));
		return allNm;
	}
	public ArrayList <String> getAllNameFunction(String nmSet){
		return db.getAllNameFunction(type, nmSet);
	}
	public void getFirst(){
		pos=0;	model=null;
		if (list.size()> 0){
			pos=1;	model = db.getModel(type, list.get(0).getId());
		} 
		fMain.show(this);
	}
	public void getLast(){
		pos=0;	model=null;
		if (list.size()> 0){
			pos=list.size();	model = db.getModel(type, list.get(pos-1).getId());
		} 
		fMain.show(this);
	}
	public void getPrev(){
		if (pos>1){
			pos=pos-1;	model = db.getModel(type, list.get(pos-1).getId());
			fMain.show(this);
		} 
	}
	public void getNext(){
		if ((list.size()> 0) && (pos<list.size())){
			pos=pos+1;	model = db.getModel(type, list.get(pos-1).getId());
			fMain.show(this);
		} 
	}
	public void newModel(){
		//int idM = model.dbNew(type);
		int idM = db.newModel(type);
		if (idM!=0) setAll(idM);
	}
	public void newModelAs(){
		//int idM = model.dbNewAs();
		int idM = db.newModelAs(type, model);
		if (idM!=0) setAll(idM);
	}
	public void deleteModel(){
		//model.dbDelete();
		db.deleteModel(type, model);
		list = db.getInfModels(type, section);
		if(list.size()> 0) {	
			if (pos>1)pos--;
		} else pos=0;
		if (pos==0) model=null; 
		else model = db.getModel(type, list.get(pos-1).getId());  
		fMain.show(this);
	}
	public void editModel(Model edModel){
		db.editModel(type, edModel);
		setAll(model.id);
	}
	public void newCommand(Command command){
		db.newCommand(type, model, command);
		setAll(model.id);
	}
	public void editCommand(int id, Command command){
		db.editCommand(type, model, id, command);
		setAll(model.id);
	}
	public void deleteCommand(int id, Command command){
		db.deleteCommand(type, model.id, id, command);
		setAll(model.id);
	}
	public void moveUpCommand(int row){
		db.moveUp(type, model, row);
		setAll(model.id);
	}
	public void moveDownCommand(int row){
		db.moveDown(type, model, row);
		setAll(model.id);
	}
	public void renameStateMachine(String name, String name1){
		model.dbRenameState(name, name1);
		setAll(model.id);
	}
	public String insertModel(int row, String nmModel, String nmFunction){
		String text = "";
		switch(type){
		case "Computer" : text = model.dbInsertModel(row, nmModel);  break;
		case "Machine"  : text = model.dbInsertModel(nmModel);  break;
		case "Recursive":  
		case "Calculus" : text = model.dbInsertModel(row, nmModel, nmFunction); break;
		default: break;
		}	
		if (text.isEmpty()) setAll(model.id);
		return text;
	}
	
	
	public String inputModel(String nmFile){
		String text = "";
		WorkFile wf = WorkFile.getWorkFile();
		model = wf.inputModel(nmFile);
		if (model != null) {
			type = model.getType();
			String nameIn = model.name;
			//System.out.println("ShowModelButton:ModelInput " + type + " " + nameIn);
			int idModel = db.addModel(type, model);
			if (idModel > 0) {
				fMain.setModel(type, idModel);
				text = Model.title(type, 8) + " " + model.name + " з файлу " + nmFile + "  введено!";
			}
			else text = Model.title(type, 8)+ " " + nameIn + " з файлу " + nmFile+ "  введено, але не збережено в базі даних !";
			setAll(idModel);
		}
		else text = wf.getErrorText();
		return text;
	}
	
	public String outputModel(String nmFile){
		String text = "";
		WorkFile wf= WorkFile.getWorkFile();
		OutputText out = wf.getOut();
		text = model.output(nmFile,out);
		if(text.isEmpty()) text = Model.title(type, 8) + " " + model.name + " виведено в файл " + nmFile + "!";
		return text;
	}
	
	public boolean isNameUse(String name){
		boolean res=false;
		for(int i =0; i<list.size();i++)
			{ if(list.get(i).getName().equals(name)) res=true;}
		return res;
	}
	
	private void setAll(int id){
		list = db.getInfModels(type, section);
		pos = 0; model = null;
		if (id > 0){
			pos = findPosition(id); model = db.getModel(type, id);
		}
		fMain.show(this);
	}
	private int findPosition(int id){
		int pos=0;
		for(int i=0;i<list.size();i++){
			if(list.get(i).getId()==id)pos=i+1;
		}
		return pos;
	}
	
}
