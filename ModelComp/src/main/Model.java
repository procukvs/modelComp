package main;
import java.util.*;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList program;
	
	private static String[][] titles = { 
			{"Algorithm", "Нормальні алгоритми Маркова","Алгоритм","алгоритму","Підстановки алгоритму",
										"алгоритмом","алгоритм","Новий", "Алгоритм"}, 
			{"Machine", "Машини Тьюрінга","Машина","машини","Таблиця переходів",
										"машиною", "машину","Нова", "Машину"} };
				
	Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
	
	
	
	public ArrayList getDataSource(int idModel) {
		return null;
	}
	
	public String[] testingRules(){
		return null;
	}
	
	public ArrayList eval(String str, int nodef){
		return null;
	}
	
	public String show(){
		 return "Model-show";
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
}
