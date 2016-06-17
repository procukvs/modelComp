package model;

import java.util.ArrayList;

import main.StringWork;

public class State extends Command {
	private String state;
	
	private ArrayList <String> going;
	/*
	private int id; // внутр≥шн≥й номер в баз≥ даних (кл€ч разом з id модел≥) 	
	public State(String state, int id, ArrayList <String> going, String txComm) {
		super(txComm); 
		this.state = state; this.id = id; 
		this.going = going;
	}
	public int getId() { return id;}
	*/
	public State(String state, int id, ArrayList <String> going, String txComm) {
		super(id,txComm); 
		this.state = state; 
		this.going = going;
	}
	public String getState() { return state;}
	
	public ArrayList <String> getGoing() { return going;};
	public String gettxComm() {return StringWork.trasferTxComm(txComm);}
	
	public String show(String alfa) {
		String goSt = "";
		for(int i = 0; i < going.size(); i++){
			if (i>0) goSt = goSt + ", ";
			goSt = goSt + "'" + alfa.charAt(i) + "' -> \"" + going.get(i) + "\"";
		}
		return "\"" + state + "\" -> [" + goSt + "]      '" + txComm;
	}
	
	public String output(String alfa) {
		String goSt = "\"" + state + "\" ";
		String move;
		for(int i = 0; i < going.size(); i++){
			move = going.get(i);
			if(!move.isEmpty())
				goSt = goSt + ": \"" + alfa.charAt(i) + "\" -> \"" + move + "\"";
		}
		return goSt + ";";
	}
	
	public ArrayList getSource(Model model) { 
		ArrayList row = new ArrayList();
		Machine mach = (Machine)model;
		String allCh = "_" + mach.main + mach.add + mach.no;
		row.add(this.getState());
		for(int j=0; j < allCh.length(); j++){
			row.add(this.getGoing().get(j));
		}
		row.add(this.gettxComm());
		row.add(this.getId());
		row.add(model.id);
        return row;
	}
		
}
