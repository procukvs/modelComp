package main;

import java.util.ArrayList;

public class Command {
	private int id; // внутр≥шн≥й номер в баз≥ даних (кл€ч разом з id модел≥) 
	public String txComm;
	Command(String txComm) {
		this.txComm = txComm;
		id = 0;
	}
	Command(int id, String txComm) {
		this.txComm = txComm;
		this.id = id;
	}
	public String output(){
		 return "Command-show";
	}
	public String show(String st){
		 return "Command-show";
	}
	public int getId() { return id;}
	public String gettxComm() {return txComm;}
	
	public ArrayList getSource(Model model) { return null; }
	public String getName() {return "----";}  // Recursive, Calculus
	

}
