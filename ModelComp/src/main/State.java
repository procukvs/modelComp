package main;

import java.util.ArrayList;

public class State extends Command {
	private String state;
	private int id; // внутр≥шн≥й номер в баз≥ даних (кл€ч разом з id модел≥) 
	private ArrayList <Move> going;
		
	public State(String state, int id, ArrayList <Move> going, String txComm) {
		super(txComm); 
		this.state = state; this.id = id; 
		this.going = going;
	}
	public String getState() { return state;}
	public int getId() { return id;}
	public ArrayList <Move> getGoing() { return going;};
	
	public String show() {
		String goSt = "";
		for(int i = 0; i < going.size(); i++){
			if (i>0) goSt = goSt + ", ";
			goSt = goSt + going.get(i).show();
		}
		return "\"" + state + "\" -> [" + goSt + "]";
	}
}
