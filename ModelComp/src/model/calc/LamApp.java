package model.calc;

public class LamApp extends Lambda {
	Lambda body;
	Lambda arg;
	
	public LamApp (Lambda body, Lambda arg){
		this.body = body; this.arg = arg;
	}
	public String toString() {return "(" + body.toString() + " " + arg.toString() + ")";} 
	public Lambda getArg(){return arg;}
	public Lambda getBody(){return body;}
	public String toStringFull() {return "(App " + body.toStringFull() + " " + arg.toStringFull() + ")";}
	public String toStringShort(LamNames nms,int wh) {
		// wh=2 --> in Abs-body , wh=1 --> in App fun, wh=0 --> else
		String txt="";
		if (wh==1){
			txt = body.toStringShort(nms, 1) + " " + arg.toStringShort(nms, 0);
		} else{
			if (wh==2) txt = " . ";
			txt = txt + "(" + body.toStringShort(nms, 1) + " " + arg.toStringShort(nms, 0) + ")";
		}	
		//System.out.println (" .... LampApp..." + txt + "..wh=.." + wh);
		return txt;
	} 
	/*
	public String toStringShort(int wh) {
		// wh=2 --> in Abs-body , wh=1 --> in App fun, wh=0 --> else
		String txt="";
		if (wh==1){
			txt = body.toStringShort(1) + " " + arg.toStringShort(0);
		} else{
			if (wh==2) txt = " . ";
			txt = txt + "(" + body.toStringShort(1) + " " + arg.toStringShort(0) + ")";
		}	
		//System.out.println (" .... LampApp..." + txt + "..wh=.." + wh);
		return txt;
	} 
	*/
}
