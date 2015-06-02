package main;

import java.util.*;

public class Going extends Command  {
	private String state;
	private ArrayList <Move> going;
		
	public Going(String state, ArrayList <Move> going, String txComm) {
		super(txComm);
		this.state = state; this.going = going;
	}
	public String getState() { return state;}
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
