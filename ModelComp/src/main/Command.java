package main;

public class Command {
	private int id; // ��������� ����� � ��� ����� (���� ����� � id �����) 
	public String txComm;
	Command(String txComm) {
		this.txComm = txComm;
		id = 0;
	}
	Command(int id, String txComm) {
		this.txComm = txComm;
		this.id = id;
	}
	/*public String show(){
		 return "Command-show";
	}*/
	public String show(String st){
		 return "Command-show";
	}
	public int getId() { return id;}
	public String gettxComm() {return txComm;};

}
