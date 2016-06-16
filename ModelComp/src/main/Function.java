package main;

import java.util.ArrayList;

public class Function extends Command {
	String name;
	int rank;
	boolean isConst;
	boolean iswf;
	RecBody body;
	String txBody;                                            /////////////New
	String errorText = "";
	public Function(int id, String name, String txBody, String txComm){
		super(id,txComm);
		this.name = name; this.txBody = txBody;
		//this.rank = rank; this.isConst = isConst; 
		rank = -1; isConst = false; iswf = false; 
		body = null;                                          /////////////////// New
	}
	public void setBody(RecBody body){ this.body =body;}     //////////////New
	public RecBody getBody(){return body;}                    //////////////New
	
	public ArrayList getSource(int idModel) { 
		ArrayList row = new ArrayList();
		row.add(this.getName());
		row.add(this.getRank());
		row.add(this.getisConst());
		row.add(this.getiswf());
		row.add(this.gettxBody());
		row.add(this.gettxComm());
		row.add(this.getId());
		row.add(idModel);
        return row;
	}
	
	
	
	public String toString(){
		return name + ":" + rank + ":" + isConst + ":" + iswf + ":" + txBody;
	}
	
	public String show(String st1){
		String st = name + ":" + rank + ":" + txBody;
		if (!txComm.isEmpty()) st = st + "\n  '" + txComm;
		if(!errorText.isEmpty()) st = st + "\n  " + errorText;
		return st ;
	}
	
	public String getName () {return name;}
	public int getRank() {return rank;}
	public boolean getisConst () {return isConst;}
	public boolean getiswf () {return iswf;}
	public String gettxBody () {return txBody;}
	public String geterrorText () {return errorText;}
	public void settxBody(String txBody){ this.txBody =txBody;}
	
	
}
