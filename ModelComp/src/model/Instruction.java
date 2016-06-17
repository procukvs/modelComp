package model;

import java.util.ArrayList;

public class Instruction extends Command {
	private int num = 1;
	private String cod = "Z";
	private int reg1 = 1;
	private int reg2 = 1;
	private int next = 1;
	
	public Instruction(int num, String cod, int reg1, int reg2,  int next, String txComm, int id) {
		super(id,txComm);
		this.num = num; this.cod = cod; 
		this.reg1 = reg1; this.reg2 = reg2; this.next = next;
	}

	public int getNum() {return num;}
	public String getCod() {return cod;}
	public int getReg1() {return reg1;}
	public int getReg2() {return reg2;}
	public int getNext() {return next;}
	
	public String toCommand(){
		String ins = "";
		switch (cod){
		case "J" : ins = "," + next; 
		case "T" : ins = "," + reg2 + ins; 
		case "Z" :
		case "S" : ins = reg1 + ins; 
		}
		return cod + "(" + ins + ")";
	}
	
	 public String show(String dummy){
	      String s = ""; 
	      s = this.toCommand();
	      if (txComm.length()>0) s = s +  " '" + txComm;
	      return s;
	 }
	 public int evalMaxReg(){
		 if(cod.equals("Z") || cod.equals("S")) return reg1;
		 else return ((reg1>reg2)?reg1:reg2); 
	}
	 
	public ArrayList getSource(Model model) { 
		ArrayList row = new ArrayList();
		row.add(this.getNum());
		row.add("  " + this.toCommand());
		row.add(this.gettxComm());
		row.add(this.getId());;
		row.add(model.id);
        return row;
	}
		
}
