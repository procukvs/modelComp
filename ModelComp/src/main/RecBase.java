package main;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class RecBase extends RecBody {
	String name;
	int b=0;
	int wh=0;
	public RecBase (String name){
		this.name = name;
		switch(name.charAt(0)){
		case 'i': b = name.charAt(1)-'0';
				wh = name.charAt(2)-'0'; break;
		case 'a': wh = 1;
		}
	}
	public String setRank(String init, HashMap <String, RecBody> map) {
		if (b == 0) rank = 1; else rank = b;
 		return "";
	}	
	public boolean isConst(HashMap <String, RecBody> map) {	return (b==0) && (wh==0);}
	public String iswf(HashMap <String, RecBody> map) {
		String st = "";
		if ((b>0) &&(!(b>=wh))) st = "Функція селекції " + name + " - невірна.";
		return st;
	}
	public String toString() {return name;} 
	public String toTest(){return "<Base:" + rank + ":" + name + ">";} 
	
	public DefaultMutableTreeNode formTree() { 
		return new DefaultMutableTreeNode(name,false);
	}
}
