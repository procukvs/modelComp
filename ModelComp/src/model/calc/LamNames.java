
package model.calc;
import java.util.*;

public class LamNames {
	private LinkedList <LamNBind> names; 
	
	// інформація про вкладені імена 
	public LamNames(){
		names = new LinkedList <LamNBind>();
	}
	
	// findCnt(nm) = -1 ==> not nm in names 
	private int findCnt (String nm){
		int res=-1, i = -1;
		boolean is = false;
		LamNBind lb = null;
		Iterator <LamNBind> it = names.iterator();
		while (it.hasNext() && !is){
			lb = it.next(); i++;
			is= (lb.getName().equals(nm));
		}
		if (is) res = lb.getCnt(); 
		return res;
	} 
	public void add(String nm){
		int cnt = findCnt(nm);
	    names.addFirst(new LamNBind(nm,cnt+1)); 
	}
	public String fname(String nm, int i){
		String res = nm;
		if (i < names.size()) {
			LamNBind lb = names.get(i);
			res = lb.newName();
			// nm може вже бути модифіковане !!!!
			if (nm.equals(lb.getName())) res = lb.newName();
			else System.out.println("model.calc.LamNames.fname:" + nm + "!=" +  lb.getName()  + " in Names (i=" + i + ")" );
		}
		return res;
	}
	public void delete(){
		names.removeFirst(); 
	}
	public LamNBind head(){
		return names.getFirst();
	}
	

}
