package main;

import java.util.*;

import javax.swing.tree.*;

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
	
	public boolean usingName(String name) { return g.usingName(name);}
	
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
	
	public int eval(int[] arg, Recursive set){
		int res = 0;
		int resg; 
		boolean go = true;
		if (set.getNoUndef()){
			int k = arg.length;
			set.stepEval();
			int[] argG = new int[k + 1];
			for( int i = 0; i < k; i++) argG[i] = arg[i];
			argG[k] = 0;
			resg = g.eval(argG, set);
			while (go && (resg !=0) && set.getNoUndef()){
				res++; argG[k] = res;
				go = (res <= max);
				if (go) resg = g.eval(argG, set);
			}
			if (!go) set.setUndef(this.toString());
		}
		return res;
	}
	
	
	public void formTree(DefaultMutableTreeNode root) { 
		//DefaultMutableTreeNode gt = g.formTree();
		DefaultMutableTreeNode base = new DefaultMutableTreeNode(toString(),true);
		g.formTree(base);
		//System.out.println(this.toString());
		root.add(base);
	}
	public String test(int[] arg, Recursive set, DefaultMutableTreeNode root){
		String res = "..";
		int resg; 
		int ires = 0;
		boolean go = true;
		if (set.getNoUndef()){
			int k = arg.length;
			String sBase = this.toString() + "<" + StringWork.argString(arg) + ">=";
			DefaultMutableTreeNode base = new DefaultMutableTreeNode(sBase,true);
			set.stepEval();
			int[] argG = new int[k + 1];
			for( int i = 0; i < k; i++) argG[i] = arg[i];
			argG[k] = ires;
			resg = g.eval(argG, set);
			while (go && (resg !=0) && set.getNoUndef()){
				ires++; argG[k] = ires;
				go = (ires <= max);
				if (go){
					res = g.test(argG, set, base);
					if(set.getNoUndef()) resg = new Integer(res);
				}
			}
			if (!go) set.setUndef(this.toString());
			if(set.getNoUndef()) res = ""+ ires; 
			base.setUserObject(sBase+res); 
			root.add(base);
		}
		return res;
	}
}
