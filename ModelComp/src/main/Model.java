package main;
import java.util.*;

import db.*;
import file.*;
import gui.*;

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
		//повертає id нової моделі в БД
		return DbAccess.getDbAccess().newModel(type);
	}
	public int dbNewAs() { return 1;}
	public void dbRenameState(String in,String out) {}
	public String dbInsertModel(String nmInsert) { return "";}
	public String dbInsertModel(int were, String nmInsert) { return "";}
	
	
	private static String[][] titles = { 
			{"Algorithm", "Нормальні алгоритми Маркова","Алгоритм","алгоритму","Підстановки алгоритму",	"алгоритмом",
								"алгоритм","Новий", "Алгоритм", "Нова","Підстановка",
								"підстановку","нормальним алгоритмом","Послідовність підстановок алгоритму"}, 
			{"Machine", "Машини Тюрінга","Машина","машини","Програма машини (Таблиця переходів)", "машиною", 
								"машину","Нова", "Машину", "Новий","Стан",
								"стан", "машиною Тюрінга","Послідовність конфігурацій машини"},
			{"Post", "Системи Поста","Система","системи","Аксіоми та правила виводу системи", "системою", 
								"систему","Нова", "Систему", "Нове","Аксіома/правило виводу",
								"аксіому/правило виводу", "системою Поста","Всі теореми"},
			{"Recursive", "Частково-рекурсивні функції","Набір функцій","набору","Функції набору", "набором", 
									"набір","Новий", "Набір", "Нова","Функція",
									"функцію", "набором функцій","Функції набору"},
			{"Calculus", "Лямбда-вирази","Набір виразів","набору","Вирази набору", "набором", 
										"набір","Новий", "Набір", "Новий","Вираз",
										"вираз", "набором виразів","Вирази набору"},			
			{"Computer", "Машини з необмеженими регістрами","Машина","машини","Програма", "машиною", 
									"машину","Нова", "Машину", "Нова","Команда",
									"команду", "машиною","Послідовність конфігурацій машини"},
			{"Input", "Введення моделей з файлу","Input2","Input3","Input4", "Input5",
									"моделі","Input7", "Input8", "Input9","Input10",
									"Input11", "Input12","Input13"},
			{"Output", "Виведення моделей в файл","Output2","Output3","Output4", "Output5", 
									"моделі","Output7", "Output8", "Outpu9t","Output10",
									"Output11", "Output12","Output13"},
     		{"State", "Стан параметрів","State2","State3","State4", "Змінити", 
	    							"State6","State7", "State8", "State9","State10",
									"State11", "State12","State13"},
		    {"Parameters", "Встановлення параметрів","Parameters2","Parameters3","Parameters4", "Редагувати коментар", 
									"Parameters6","Нове значення параметру", "Parameters8", "Parameters9","Parameters10",
									"Parameters11", "Parameters12","Parameters13"}									
								};
				
	Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
	
	public String takeResult(ArrayList sl, int nodef) { return "";}
	
	public int takeCountStep(ArrayList sl, int nodef) { return 0;}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal, int var) {return null;}
	public ArrayList getStepSource(ArrayList sl, boolean internal) {return null;}
	
	public ArrayList getDataSource(int idModel) {
		return null;
	}
	
	public String[] iswfModel(){
		return null;
	}
	
	// перевіряє, що в командах моделі використовуються лише символи обєднаного алфавіту 
	public String iswfModelAlfa(String alfa){return "";	}
	
	public ArrayList eval(String str, int nodef){
		return null;
	}
	
	public ArrayList eval(int[] arg, int nodef){
		return null;
	}
	
	public ArrayList eval(String str, int nodef, boolean isStep){
		return null;
	}
	
	public String show(){
		 return "Model-show";
	}
	
	public String output(OutputText out) {return "outputModel";}
	
	public String output(String name, OutputText out) {
		String res = "";
		if(out.open(name)) {
			res = this.output(out);
			out.close();
		} else res = "Not open output file " + name + "!"; 
		return res;
	}
	
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
	public int findMaxNumber(){
		int cnt = 0;
		int num;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ ){
				num = ((Command)program.get(i)).getId();
				if(cnt < num) cnt = num;
			}	
		}
		return cnt;
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
