package main;
import java.util.*;

import db.*;
import file.*;
import gui.ShowForm;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList program;
	
	//-----------------------------------------------
	public String getMain() {return "";}
	public String getAdd() {return "";}
	public String getAllChar() {return ""; }
	public String getNo() {return "";}
	public boolean getIsNumeric() {return true;}
	public int getRank() {return 2;}
	public String getInit() {return "";}
	public String getFin() {return "";}
	public String getType() {return "Algorithm";}     // "Model"
	public void setMain(String main) { }
	public void setAdd(String add) { }
	public void setIsNumeric(boolean isNumeric) { }
	public void setRank(int rank){}
	public void setInit(String init) { }
	public void setFin(String fin) { }
	//----------------------------------------------	
	
	//-----work DB ------- 
	public boolean dbDelete(){ return false;} //
	public int getDbOrder(){
		//System.out.println(getTypeModel());
		return DbAccess.getDbAccess().getOrder(getTypeModel(), name); 
	}
	public int getDbNumber(int id){
		//System.out.println(getTypeModel());
		return DbAccess.getDbAccess().getNumber(getTypeModel(), id); 
	}
	static public int dbNew(String type){
		return DbAccess.getDbAccess().newModel(type);
	}
	public int dbNewAs() { return 1;}
	public void dbRenameState(String in,String out) {}
	public String dbInsertModel(String nmInsert) { return "";}
	
	
	private static String[][] titles = { 
			{"Algorithm", "Нормальні алгоритми Маркова","Алгоритм","алгоритму","Підстановки алгоритму",	"алгоритмом",
								"алгоритм","Новий", "Алгоритм", "Нова","Підстановка",
								"підстановку","нормальним алгоритмом"}, 
			{"Machine", "Машини Тюрінга","Машина","машини","Програма машини (Таблиця переходів)", "машиною", 
								"машину","Нова", "Машину", "Новий","Стан",
								"стан", "машиною Тюрінга"},
			{"Post", "Системи Поста","Система","системи","Аксіоми та правила виводу системи", "системою", 
								"систему","Нова", "Систему", "Нове","Аксіома/правило виводу",
								"аксіому/правило виводу", "системою Поста"} };
				
	Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
	
	public String takeResult(ArrayList sl, int nodef) { return "";}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal) {return null;}
	
	public ArrayList getDataSource(int idModel) {
		return null;
	}
	
	public String[] iswfModel(){
		return null;
	}
	
	public ArrayList eval(String str, int nodef){
		return null;
	}
	
	public ArrayList eval(ShowForm showForm, int step){
		return null;
	}
	
	public String show(){
		 return "Model-show";
	}
	
	public String output(String name, OutputText out) {return "outputModel";};
	
	static public String title(String type, int num) {
		String rs = "";
		int j = -1;
		for(int i = 0; i < titles.length; i++ ) {
			if (type.equals(titles[i][0])) j = i;
		}
		if (j >= 0) rs = titles[j][num];
		return rs;
	}
	
	// знаходить максимальний номер команди в програмi
	public int findMaxNumber() {
		return program.size();
	}
	// знаходить  порядковий номер команди за іі номером-ключом в БД id
	public int findCommand(int id) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(id == (((Command)program.get(i)).getId())) cnt = i;
		}
		return cnt;
	}
	
	public int findCommand(String key) {return 0;}
	
	private String getTypeModel(){
		return this.getClass().getName().substring(5);
	}
	
}
