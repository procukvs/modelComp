package main;

public class Function extends Command {
	String name;
	int rank;
	boolean isConst;
	boolean iswf;
	String txBody;
	public Function(int id, String name, int rank, boolean isConst, String txBody, String txComm){
		super(id,txComm);
		this.name = name; this.txBody = txBody;
		this.rank = rank; this.isConst = isConst; iswf = false;
	}
	
}
