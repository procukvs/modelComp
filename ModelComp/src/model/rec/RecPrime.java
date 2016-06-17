package model.rec;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import main.StringWork;

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
	
	public boolean usingName(String name) {
		return g.usingName(name) || h.usingName(name);
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
	
	public int eval(int[] arg, Recursive set){
		int res = 0;
		if (set.getNoUndef()){
			int k = arg.length;
			int[] argG = new int[(k==1?1:k-1)];
			int[] argH = new int[k+1];
			set.stepEval();
			int y = arg[k - 1];
			for(int i = 0; i < (k-1); i++){
				argG[i] = arg[i]; argH[i] = arg[i];
			}
			if(k==1) argG[0] = arg[0];
			res = g.eval(argG, set);
			for(int i = 0; i < y; i++){
				argH[k-1] = i; argH[k] = res;
				res = h.eval(argH, set);
			}
		}
		return res;
	}	
	
	public void formTree(DefaultMutableTreeNode root) { 
		DefaultMutableTreeNode base = new DefaultMutableTreeNode(this.toString(),true);
		//System.out.println(this.toString());
		g.formTree(base);
		h.formTree(base);
		root.add(base);
	}
	public String test(int[] arg, Recursive set, DefaultMutableTreeNode root){
		String res = "..";
		String sBase = this.toString() + "<" + StringWork.argString(arg) + ">=";
		DefaultMutableTreeNode base = new DefaultMutableTreeNode(sBase,true);
		if (set.getNoUndef()){
			int k = arg.length;
			int[] argG = new int[(k==1?1:k-1)];
			int[] argH = new int[k+1];
			set.stepEval();
			if(set.getNoUndef()){ 
				int y = arg[k - 1];
				for(int i = 0; i < (k-1); i++){
					argG[i] = arg[i]; argH[i] = arg[i];
				}
				if(k==1) argG[0] = arg[0];
				res = g.test(argG, set,base);
				for(int i = 0; i < y; i++){
					if (!res.equals("..")){
						argH[k-1] = i; argH[k] = new Integer(res);
						res = h.test(argH, set, base);
					}	
				}
			}	
			base.setUserObject(sBase+res); 
		}
		root.add(base);
		return res;
	}	
}
