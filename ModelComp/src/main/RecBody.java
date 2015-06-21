package main;

import java.util.*;
import javax.swing.tree.*;


public class RecBody {
	int rank = -1;
	/*
	boolean isConst;
	public boolean iswf;
	public RecBody(int rank, boolean isConst) {
		this.rank = rank; this.isConst = isConst;
		iswf = false;
	}
	*/
	public String toString() {return "";}
	public String toTest() {return "";}
	
	public int eval(int[] arg, Recursive set){
		return 0;
	}
	public RecBody fromString(String text){
		return null;
	}
	
	public String setRank(String name, HashMap <String, RecBody> map) {return "";}
	public void setIswf(HashMap <String, RecBody> map) {}
	public boolean isConst(HashMap <String, RecBody> map) {return false;};
	public String iswf(HashMap <String, RecBody> map) {return "";}
	
	public void formTree(DefaultMutableTreeNode  root) { }
	public int test(int[] arg, Recursive set, DefaultMutableTreeNode  root){
		return 0;
	}
}

