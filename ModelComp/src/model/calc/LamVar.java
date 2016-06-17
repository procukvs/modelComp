package model.calc;

public class LamVar extends Lambda {
	String name;
	int ind=0;
	int lng=1;
	
	public LamVar (String name, int ind, int lng){
		this.name = name; this.ind = ind; this.lng = lng;
	}
	public String toString() {return "[" + name+ ":" + ind + "]";} 
	public String getName(){return name;}
	public int getInd(){return ind;}
	public int getLng(){return lng;}
	public String toStringFull() {return "(Var " + name+":" +ind + ")";} 
	public String toStringShort(int wh) {
		String txt="";
		if (wh==2) txt = " . ";
		txt = txt+name;
		//System.out.println (" .... LamVar..." + txt + "..wh=.." + wh);
		return txt;
	} 
}
