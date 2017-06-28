package model.calc;

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
	public String toStringShort(LamNames nms, int wh) {
		String txt="", strArg;
		strArg = arg.toStringShort(nms,0);
		if (wh==2) txt = " . ";
		nms.add(name);
		String name1 = nms.head().newName();
		txt = txt + "let " + name1 + " = " + strArg + " in " + body.toStringShort(nms, 0);
		nms.delete();
		return txt;
	} 
	/*
	public String toStringShort(int wh) {
		String txt="";
		if (wh==2) txt = " . ";
		txt = txt + "let " + name + " = " + arg.toStringShort(0) + " in " + body.toStringShort(0);
		return txt;
	} 
	*/
}
