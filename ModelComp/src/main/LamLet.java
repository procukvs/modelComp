package main;

public class LamLet extends Lambda {
	String name;
	Lambda arg;
	Lambda body;
	
	public LamLet (String name, Lambda arg, Lambda body){
		this.name = name; this.arg = arg; this.body = body;
	}
	public String toString() {return "(let" + name + "=" + arg.toString() + " in " + body.toString() + ")";} 
	public String getName(){return name;}
	public Lambda getArg(){return arg;}
	public Lambda getBody(){return body;}
	public String toStringFull() {return "(Let " + name + "=" + arg.toStringFull() + " in " + body.toStringFull() + ")";}
	public String toStringShort(int wh) {
		String txt="";
		if (wh==2) txt = " . ";
		txt = txt + "let " + name + " = " + arg.toStringShort(0) + " in " + body.toStringShort(0);
		return txt;
	} 
}
