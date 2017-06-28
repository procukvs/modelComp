package model.calc;

public class LamNmb extends Lambda {
	String name;
	int val=0;
	
	public LamNmb (String name, int val){
		this.name = name; this.val = val;
	}
	public String toString() {return name;} 
	public String getName(){return name;}
	public int getInd(){return val;}
	public String toStringFull() {return "(Nmb " + name + ")";} 
	public String toStringShort(LamNames nms,int wh) {
		String txt="";
		if (wh==2) txt = " . ";
		txt = txt+name;
		//System.out.println (" .... LamNmb..." + txt + "..wh=.." + wh);
		return txt;
	} 

}
