package model.calc;

import java.util.ArrayList;

import model.Command;
import model.Model;

public class LambdaDecl extends Command {
	String name;
	int num;
	boolean iswf;
	Lambda body;
	String txBody;
	String errorText = "";
	public LambdaDecl(int id, int num, String name, String txBody, String txComm){
		
		super(id,txComm);
		//System.out.println("LambdaDecl:new " + id + " "+ num + " " + name );
		this.num = num; this.name = name; this.txBody = txBody;
		iswf = false;body = null;
	}
	
	public String toString(){
		return name + ":" + iswf + ":" + txBody + ":" + body.toString();
	}
	
	public String show(String st1){
		String st = name + ":"  + txBody;
		if (!txComm.isEmpty()) st = st + "\n  '" + txComm;
		if(!errorText.isEmpty()) st = st + "\n  " + errorText;
		if (body != null) st = st + "\n " + body.toString();
		return st ;
	}
	
	public String getName () {return name;}
	public int getNum() {return num;}
	//public boolean getisConst () {return isConst;}
	public boolean getiswf () {return iswf;}
	public String gettxBody () {return txBody;}
	public String geterrorText () {return errorText;}
	public void settxBody(String txBody){ this.txBody =txBody;}
	public void setBody(Lambda body){ this.body =body;}
	public Lambda getBody() {return body;}
	
	public ArrayList getSource(Model model) { 
		ArrayList row = new ArrayList();
		row.add(this.getNum());
		row.add(this.getName());
		row.add(this.getiswf());
		row.add(this.gettxBody());
		row.add(this.gettxComm());
		row.add(this.getId());
		row.add(model.id);
        return row;
	}
	
}
