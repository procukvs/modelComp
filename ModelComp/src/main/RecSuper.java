package main;

import java.util.*;

public class RecSuper extends RecBody {
	RecBody f;
	ArrayList <RecBody> af;
	public RecSuper(RecBody f, ArrayList <RecBody> af) {
		super(((af == null) || (af.size() == 0) ? 1 : af.get(0).rank), 
				f.toString().equals("a1") && (af != null) && (af.size() == 1) && af.get(0).isConst);
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
}
