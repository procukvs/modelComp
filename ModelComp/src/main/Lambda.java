package main;

import java.util.HashMap;

public class Lambda {
	int rank = -1;
	public String toString() {return "";}
	public String toTest() {return "";}
	
	public int eval(int[] arg, Calculus set){
		return 0;
	}
	public Lambda fromString(String text){
		return null;
	}
	public String toStringFull() {return "";} 
	public String toStringShort(int wh) {
		// wh=2 --> in Abs-body , wh=1 --> in App fun, wh=0 --> else    
		return "";
	} 
	public String getName(){return "";}
	public Lambda getArg(){return null;}
	public Lambda getBody(){return null;}
	public int getInd(){return (-1);}
	public int getLng(){return (-1);}
	/*
	public String setRank(String name, HashMap <String, Lambda> map) {return "";}
	public boolean isConst(HashMap <String, Lambda> map) {return false;}
	public String iswf(HashMap <String, Lambda> map) {return "";}
	*/
}
