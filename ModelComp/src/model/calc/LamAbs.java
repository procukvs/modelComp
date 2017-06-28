package model.calc;

public class LamAbs extends Lambda {
	String name;
	Lambda body;
	
	public LamAbs (String name, Lambda body){
		this.name = name; this.body = body;
	}
	public String toString() {return "(\\" + name + "." + body.toString() + ")";} 
	public String getName(){return name;}
	public Lambda getBody(){return body;}
	public String toStringFull() {return "(Abs " + name + "." + body.toStringFull() + ")";} 
	public String toStringShort(LamNames nms,int wh) {
		// wh=2 --> in Abs-body , wh=1 --> in App fun, wh=0 --> else  
		String txt="";
        nms.add(name);
		String name1 = nms.head().newName();
		if(wh==2){
			txt = " " + name1 + body.toStringShort(nms, wh);
		} else{
			txt = "(\\" + name1 + body.toStringShort(nms, 2) + ")";
		}
		nms.delete();
		return txt;
	} 
	/*
	public String toStringShort(int wh) {
		// wh=2 --> in Abs-body , wh=1 --> in App fun, wh=0 --> else  
		String txt="";
		if(wh==2){
			txt = " " + name + body.toStringShort(wh);
		} else{
			txt = "(\\" + name + body.toStringShort(2) + ")";
		}
		return txt;
	} 
	*/
}
