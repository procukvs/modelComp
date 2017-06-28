package model.calc;

public class LamStep {
	// Reduction or Result evaluation Lambda
	private String what;    // End, Var, App, Let, Nmb, Err 
	private String name;
	private Lambda[] term = new Lambda[3];
	private String strArg = "", tmp1;
 
	public LamStep (LamNames nms, String what, String name, Lambda res, Lambda arg, Lambda body){
		// what = "App" what = "Let"
		this.what = what; this.name = name;
		this.term[0]=res; this.term[1]=arg; this.term[2]=body; 
		
		nms.add(name);
		String name1 = nms.head().newName();
		tmp1 = term[2].toStringShort(nms, 0);
		nms.delete();
		strArg = "[" + name1 + "->" + term[1].toStringShort(nms, 0) + "] " + tmp1;
	}
	public LamStep (LamNames nms,  String name, Lambda res, Lambda arg, int ind){
		// what = "Var" 
		this.what = "Var"; this.name = name;
		this.term[0]=res; this.term[1]=arg; this.term[2]=null; 
		//strArg = nms.fname(name, ind) + " => " + term[1].toStringShort(nms, 0);
		strArg = nms.fname(name, ind) + " => " + term[1].toStringShort(new LamNames(), 0);  //****
	}
	public LamStep (LamNames nms, String name, Lambda res, Lambda arg){
		// what = "Nmb"
		this.what = "Nmb"; this.name = name;
		this.term[0]=res; this.term[1]=arg; this.term[2]=null; 
		//strArg = name + " => " + term[1].toStringShort(nms, 0);
		strArg = name + " => " + term[1].toStringShort(new LamNames(), 0);                 //****
	}
	public LamStep (String name, Lambda res){
		this.what = "End"; this.name = name;
		this.term[0]=res; this.term[1]=null; this.term[2]=null; 
		strArg = name; 
	}
	public LamStep (String res){
		this.what = "Err"; this.name = res;
		this.term[0]=null; this.term[1]=null; this.term[2]=null; 
		strArg = name;
	}
	public String getWhat(){return what;}
	public String getName(){return name;}
	public Lambda[] getTerm(){return term;}
	public void modify(Lambda res){
		this.term[0]=res;
	}
	public String toString(){
		String txt="";
		switch (what){
	    case "End": txt = name + "==>" + term[0].toString(); break;
	    case "Var": txt = name + ":" + term[1].toString() + "=>" + term[0].toString(); break;
	    case "App": txt = "<" + name + "->" + term[1].toString() + ">" + term[2].toString()+ "=>" + term[0].toString();  break;
	    case "Let": txt = "let " + name + "=" + term[1].toString() + " in " + term[2].toString()+ "=>" + term[0].toString();  break;
	    case "Nmb": txt = name + ":" + term[1].toString() + "=>" + term[0].toString(); break;
	    case "Err": txt = name; break;
	    default:
		}
		return what + ".." + txt;		
	}
	public String takeArgs(){ return strArg;}
	/*
	public String takeArgs(){
		String txt="";
		switch (what){
	    case "End": txt = name ; break;
	    case "Var": txt = name + " => " + term[1].toStringShort(0); break;
	    case "App": txt = "[" + name + "->" + term[1].toStringShort(0) + "] " + term[2].toStringShort(0);  break;
	    case "Let": txt = "let " + name + " = " + term[1].toStringShort(0) + " in " + term[2].toStringShort(0);  break;
	    case "Nmb": txt = name + " => " + term[1].toStringShort(0); break;
	    case "Err": txt = name; break;
	    default:
		}
		return txt;
	}
	*/
}
