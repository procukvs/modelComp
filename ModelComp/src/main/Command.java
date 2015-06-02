package main;

public class Command {
	public String txComm;
	Command(String txComm) {
		this.txComm = txComm;
	}
	 public String show(){
		 return "Command-show";
	 }
}
