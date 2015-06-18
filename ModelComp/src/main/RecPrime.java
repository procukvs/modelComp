package main;

import java.util.HashMap;

public class RecPrime extends RecBody {
	RecBody g,h;
	public RecPrime(RecBody g, RecBody h){
		this.g = g; this.h = h;
	}

	public String toString(){
		return "@R(" + g.toString() + "," + h.toString() + ")";
	} 
	
	public String toTest(){
		return "<Prime:" + rank + ":" + g.toTest() + "," + h.toTest() + ">";
	} 
	
	public String setRank(String init, HashMap <String, RecBody> map) {
		String st = h.setRank(init, map);
		if(!st.isEmpty())rank = 0; else rank = h.rank-1;
		g.setRank(init,map);
		return st;
	}
	
	public String iswf(HashMap <String, RecBody> map) {
		String st = g.iswf(map);
		if (st.isEmpty()) st = h.iswf(map);
		if (st.isEmpty()) {
			if ( (g.rank+2 != h.rank) && (!g.isConst(map) || (h.rank != 2)))
				st = "Невідповідність арностей у функцій операції примітивної рекурсії.";
		}
		return st;
	}
}
