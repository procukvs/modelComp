package main;

import java.util.*;
public class Machine extends Model {
	public String main = "|#";
	public String add = "";
	public String no = "";   // Символи не з main+add для яких ЩЕ є визначені переходи 
	public boolean isNumeric = true;
	public int rank = 1;
	public String init = "@a0";
	public String fin = "@zz";
	public ArrayList <String> stAll = null;
		
	public Machine(int id, String name) {
		super(id,name);
		stAll = new ArrayList();
		stAll.add(new String(init));
		stAll.add(new String(fin));
	}
	
	public boolean isState(String st) {
		boolean go = true;
		int i = 0;
		while ((i < stAll.size()) && go){
			go = !st.equals(stAll.get(i)); i++;
		}
		return !go;
	}
	
	// додає стан в програму --- всі переходи - НЕВИЗНАЧЕНІ!
	public void addStateGoing(String st) {
		ArrayList <Move> going = new ArrayList();
		String allCh = "_" + main + add + no;
		for( int i = 0; i < allCh.length(); i++){
			char c = allCh.charAt(i);
			going.add(new Move(c, c, st, '.'));
		}
		insertCommand(new Going(st,going,"1"));
	}

	// вставляє команду/змінює going в програмі машини	
	public void insertCommand(Going going) {
		boolean go = true;
		int i = 0;
		int comp = -1;
		String stNew = going.getState();
		Going work;
		if (program != null) {
			while ((i < program.size()) && go){
				work =(Going)program.get(i);
				comp = work.getState().compareTo(stNew);
				//System.out.println( comp + " ; " +  work.getState() + " ? " + stNew);
				if (comp < 0) i++; else go = false;
			}
		}	
		System.out.println("Add going " + going.show());
		if ((program == null) || (program.size() == 0)) {
			//System.out.println("Add in empty list ");
			if (program == null) program = new ArrayList();
			program.add(going);
		}
		else if (comp == 0){
			//System.out.println("Is going " + i + " " +  ((Going)program.get(i)).show());
			program.remove(i);
			program.add(i, going);
		}
		else if (comp < 0) {
			//System.out.println("All lest: max =  " + i + " " + 
			//			((Going)program.get(program.size()-1)).show());
			program.add(going);
		}
		else {
			//System.out.println("Next bigger: max =  " + i + " " + ((Going)program.get(i)).show());
			program.add(i, going);
		}
	}
	
	public String show() {
		String maSt = "";
		maSt = "Machine " + name + " (" + id + ") \n";
		maSt = maSt + "  Alphabets: <" + main + "><" + add + "><" + no + ">\n";
		maSt = maSt + "  States: init: " + init + " fin : " + fin +  " \n";
		maSt = maSt + "  Program = \n ";
		if (program != null) {
			for(int i = 0; i < program.size(); i++) {
				maSt = maSt + "  " + i + ": " + ((Going)program.get(i)).show() + "\n"; 
			}
		}  // else maSt = maSt + "[]";	
		return maSt;
	}	
}
