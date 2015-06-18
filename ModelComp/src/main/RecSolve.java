package main;

import java.util.HashMap;

public class RecSolve extends RecBody {
	RecBody g;
	int max;
	public RecSolve(RecBody g, int max){
		//super((g.rank>0?g.rank-1:-1),false);
		this.g =g; this.max = max;
	}
	
	public String toString(){
		return "@M(" + g.toString() + "," + max + ")";
	} 
	
	public String toTest(){
		return "<Solve:" + rank + ":" +  g.toTest() + "," + max + ">";
	} 
	
	public String setRank(String init, HashMap <String, RecBody> map) {
		String st = g.setRank(init, map);
		if(g.rank==0) rank = 0; else rank = g.rank-1;
		return st;
	}
	public String iswf(HashMap <String, RecBody> map) {
		String st =  g.iswf(map);
		if (st.isEmpty() && (g.rank <= 1)) st = "Невірна арність " + g.rank + " у функції операції мінімізації.";
		return st;		
	}
}
