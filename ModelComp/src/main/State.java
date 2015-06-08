package main;

import java.util.ArrayList;

public class State extends Command {
	private String state;
	private int id; // �������� ����� � ��� ����� (���� ����� � id �����) 
	private ArrayList <String> going;
		
	public State(String state, int id, ArrayList <String> going, String txComm) {
		super(txComm); 
		this.state = state; this.id = id; 
		this.going = going;
	}
	public String getState() { return state;}
	public int getId() { return id;}
	public ArrayList <String> getGoing() { return going;};
	public String gettxComm() {return txComm;};
	
	public String show(String alfa) {
		String goSt = "";
		for(int i = 0; i < going.size(); i++){
			if (i>0) goSt = goSt + ", ";
			goSt = goSt + "'" + alfa.charAt(i) + "' -> \"" + going.get(i) + "\"";
		}
		return "\"" + state + "\" -> [" + goSt + "]      '" + txComm;
	}
}
