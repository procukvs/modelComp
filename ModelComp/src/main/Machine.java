package main;

import java.util.*;
public class Machine extends Model {
	public String main = "|#";
	public String add = "";
	public String no = "";   // ������� �� � main+add ��� ���� �� � ��������� �������� 
	public boolean isNumeric = true;
	public int rank = 1;
	public String init = "@a0";
	public String fin = "@zz";
	//public ArrayList <String> stAll = null;
		
	public Machine(int id, String name) {
		super(id,name);
		//stAll = new ArrayList();
		//stAll.add(new String(init));
		//stAll.add(new String(fin));
	}
	
	/*public boolean isState(String st) {
		boolean go = true;
		int i = 0;
		while ((i < stAll.size()) && go){
			go = !st.equals(stAll.get(i)); i++;
		}
		return !go;
	} */
	
	// ���� ���� � �������� --- �� �������� - ���������Ͳ!
	public void addStateGoing(String st) {
		ArrayList <Move> going = new ArrayList();
		String allCh = "_" + main + add + no;
		for( int i = 0; i < allCh.length(); i++){
			char c = allCh.charAt(i);
			going.add(new Move(c, c, st, '.'));
		}
		insertCommand(new State(st,0,going,"1"));
	}
	
	// ��������� ������� ��� ����� st 
	public int findCommand(String st) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(st.equals(((State)program.get(i)).getState())) cnt = i;
		}
		return cnt;
	}
	
	//
	public void updateCommand(String st, Move mv){
		String allCh = "_" + main + add + no;
		char in = mv.getIn();
		int i = findCommand(st);
		if (i == -1) addStateGoing(st);
		i = findCommand(st);
		if (i >= 0){
			int pos = allCh.indexOf(in);
			if (pos >= 0) {
				((State)(program.get(i))).getGoing().remove(pos);
				((State)(program.get(i))).getGoing().add(pos, mv);
			}
		}
		
	}

	// �������� �������/����� going � ������� ������	
	public void insertCommand(State going) {
		boolean go = true;
		int i = 0;
		int comp = -1;
		String stNew = going.getState();
		State work;
		if (program != null) {
			while ((i < program.size()) && go){
				work =(State)program.get(i);
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
			// ����� ������� ������� ��� ������ �����
			//System.out.println("Is going " + i + " " +  ((Going)program.get(i)).show());
			program.remove(i);
			program.add(i, going);
		}
		else if (comp < 0) {
			// ������ ����, ������ ����� ���, �� ������� � ��������  
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
		maSt = maSt + "  Program = \n";
		if (program != null) {
			for(int i = 0; i < program.size(); i++) {
				maSt = maSt + i + ": " + ((State)program.get(i)).show() + "\n"; 
			}
		}  // else maSt = maSt + "[]";	
		return maSt;
	}	
}
