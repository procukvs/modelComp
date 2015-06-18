package main;

public class Function extends Command {
	String name;
	int rank;
	boolean isConst;
	boolean iswf;
	String txBody;
	String errorText = "";
	public Function(int id, String name, String txBody, String txComm){
		super(id,txComm);
		this.name = name; this.txBody = txBody;
		//this.rank = rank; this.isConst = isConst; 
		rank = -1; isConst = false; iswf = false;
	}
	
	public String toString(){
		return name + ":" + rank + ":" + isConst + ":" + iswf + ":" + txBody;
	}
	
	public String getName () {return name;}
	public int getRank() {return rank;}
	public boolean getisConst () {return isConst;}
	public boolean getiswf () {return iswf;}
	public String gettxBody () {return txBody;}
	
}
