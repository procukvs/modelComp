package main;
import java.util.*;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList program;
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
}
