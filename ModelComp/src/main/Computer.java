package main;

import java.util.ArrayList;

import db.DbAccess;

public class Computer extends Model {
	public int rank = 1;
	
	public Computer(int id, String name) {
		super(id,name);
	}
	
	//-------------------------
	public int getRank() {return rank;}
	public String getType() {return "Computer";}
	//----------------------------------------------
	
	//-----work DB ------- 
	
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		State inst;
		Instruction com;
		if (program != null) {
			for (int i = 0; i < program.size(); i++){
				row = new ArrayList();
				com = (Instruction)program.get(i);
				row.add(com.getNum());
				row.add("  " + com.toCommand());
				row.add(com.gettxComm());
				row.add(com.getId());
				row.add(idModel);
				data.add(row);
			} 
		}
        return data;
	}
	public ArrayList <String> iswfNum(String num) {
		ArrayList <String> mes = new ArrayList <String>();
		int numI = -1;
		if (StringWork.isPosNumber(num)) numI = new Integer(num);
		if((numI < 0) || (numI > program.size()+1)) 
			mes.add("E:Порядковий номер команди повинен бути не меньше 1 і не більше " + (program.size() + 1) + ".");
		return mes;
	}	
	
	public String testPart(String cod, int i, String part) {
		String text = "";
		String[] what = {"Регістр 1","Регістр 2","Номер наступної команди"};
		boolean test = true;
		switch(cod){
		case "S": case "Z": test = (i==1); break;
		case "T": test = (i < 3); break;
		}
		if (test){
			if(!StringWork.isPosNumber(part))
				text = what[i-1] + " " + part + " - недодатнє число.";
		}
		return text;		
	}
	
	public String dbInsertModel(int where, String nmInsert) {
		return DbAccess.getDbComputer().insertComputer(this.id, where, nmInsert, this.findMaxNumber());
	}	
}
