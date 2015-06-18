package main;

import java.util.*;

public class RecSuper extends RecBody {
	RecBody f;
	ArrayList <RecBody> af;
	public RecSuper(RecBody f, ArrayList <RecBody> af) {
		//super(((af == null) || (af.size() == 0) || (af.get(0).rank <= 0))  ? -1 : af.get(0).rank , 
		//		f.toString().equals("a1") && (af != null) && (af.size() == 1) && af.get(0).isConst);
		this.f = f; this.af = af;
	}
	
	public String toString(){
		String st = "";
		if (af != null){
			for(int i = 0; i < af.size(); i++){
				if (i > 0) st = st + ",";
				st = st + af.get(i).toString();
			}
		}
		return "@S(" + f.toString() + ",[" + st + "])";
	} 
	
	public String toTest(){
		String st = "";
		if (af != null){
			for(int i = 0; i < af.size(); i++){
				if (i > 0) st = st + ",";
				st = st + af.get(i).toTest();
			}
		}
		return "<Super:" + rank + ":" + f.toTest() + ",[" + st + "]>";
	} 
	
	
	public String setRank(String init, HashMap <String, RecBody> map) {
		String st = "";
		if((af != null) && (af.size() > 0)) {
			for (int i = 0; i < af.size(); i++) {
				String st1 = af.get(i).setRank(init, map);
				if (i == 0) {
					rank = af.get(i).rank; st = st1;
				}
			}
		} else {
			rank = 0; st = "В операції суперпозиції порожній список аргументів.";
		}
		if (st.isEmpty()) st = f.setRank(init, map);
		return st;
	}
	public boolean isConst(HashMap <String, RecBody> map) {
		return f.toString().equals("a1") && 
				(af != null) && (af.size() == 1) && af.get(0).isConst(map);
	}
	public String iswf(HashMap <String, RecBody> map) {
		String st = "";
		if ((af != null) && (af.size() > 0)) {
			int r = af.get(0).rank;
			int i = 0;
			if (r > 0) {
				while ((i<af.size()) && st.isEmpty()){
					RecBody ra = af.get(i);
					if (ra.rank == r) st = ra.iswf(map);
					else st = "Невірна арність " + ra.rank + " у " + i + " аргумента суперпозиції."; 
					i++;
				}
			}
			else st = "Невірна арність " + r + " у першого аргумента суперпозиції."; 
		} else st = "Немає аргументів у суперпозиції.";	
		if (st.isEmpty()) st = f.iswf(map);
		if (st.isEmpty() && (f.rank != af.size())) st = "Арність функції " + f.rank + " не дорівнює кількості аргументів.";
		return st;
	}
}
