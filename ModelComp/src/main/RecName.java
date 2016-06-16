package main;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class RecName extends RecBody {
	String name;
	/*
	public RecName(String name, int rank, boolean isConst) {
		super(rank,isConst);
		this.name = name;
	}
	*/
	public RecName(String name) {
		this.name = name;
	}
	
	public String toString(){return name ;} 
	
	public String toTest(){return "<Name:" + rank + ":" + name + ">";} 
	
	public boolean usingName(String name1) { return name1.equals(name);}
	
	public String setRank(String init, HashMap <String, RecBody> map) {
		String st = "";
		if (rank == 0) {
			if(init.equals(name)) st = "Функція "+ name + " викликає саму себе."; 
			else st = "Не встановлено арність функції " + name + ".";
		} else if (rank == -1){ 
			rank = 0;
			if (map.containsKey(name)) {
				RecBody bd = map.get(name);
				if(bd != null) {
					if (bd.rank == -1) st = bd.setRank(init, map);
					rank = bd.rank;
				} else st = "Функція " + name + " містить синтаксичні помилки.";
			} else st = "Не знайдено функцію " + name + "."; 
			//System.out.println("RecName- end" + name + " :rank =  " + rank );
		} 
		return st;
	}
	public boolean isConst(HashMap <String, RecBody> map) {
		return map.get(name).isConst(map);
	}
	public String iswf(HashMap <String, RecBody> map) {
		return "";
	}
	public void setIswf(HashMap <String, RecBody> map) {
		String st="";
		if (map.containsKey(name)) {
			RecBody bd = map.get(name);
			if(bd != null) st = bd.iswf(map);
			else st = "Функція " + name + " містить синтаксичні помилки.";
		} else st = "Не знайдено функцію " + name + "."; 
	}
	
	public int eval(int[] arg, Recursive set){
		int res = 0;
		if (set.getNoUndef()){
			int i = set.findCommand(name);
			if (i>=0){
				RecBody bd = ((Function)set.program.get(i)).getBody();
				res = bd.eval(arg, set);
			} else System.out.println("RecName=eval: not find body function " + name + ". Set = " + set.toString());
			//res = set.map.get(name).eval(arg, set);
		}
		return res;
	}
	
	public void formTree(DefaultMutableTreeNode root) { 
		//System.out.println(this.toString());
		root.add( new DefaultMutableTreeNode(name,false));
	}
	public String test(int[] arg, Recursive set, DefaultMutableTreeNode root){
		String res = "..";
		if (set.getNoUndef()){
			String sBase = name + "<" + StringWork.argString(arg) + ">=" ;
			DefaultMutableTreeNode base = new DefaultMutableTreeNode(sBase,true);
			int i = set.findCommand(name);
			if (i>=0){
				RecBody bd = ((Function)set.program.get(i)).getBody();
				res = bd.test(arg, set,base);
			} else System.out.println("RecName=eval: not find body function " + name + ". Set = " + set.toString());
			//res = set.map.get(name).test(arg, set,base);
			base.setUserObject(sBase+res); 
			root.add(base);
		}
		return res;
	}
	
}
