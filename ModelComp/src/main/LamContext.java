package main;

import java.util.*;

public class LamContext {
	private LinkedList <LamBinding> ctx; 
	public LamContext(){
		ctx = new LinkedList <LamBinding>();
	}
	private LamContext( LinkedList <LamBinding> base){
		ctx = new LinkedList <LamBinding>(base);
	}
	public int findIndexP (String nm){
		int res=-1, i = -1;
		boolean is = false;
		Iterator <LamBinding> it = ctx.iterator();
		while (it.hasNext() && !is){
			LamBinding lb = it.next(); i++;
			is= (lb.getName().equals(nm));
		}
		if (is) res = i; 
		return res;
	}
	public void add(LamBinding nt){
		ctx.addFirst(nt); 
	}
	public void delete(){
		ctx.removeFirst(); 
	}
	public int lng(){
		return ctx.size();
	}
	public LamContext copy(){
		return new LamContext(ctx); 
	}
	public Lambda findBinding(int i) {return ctx.get(i).getBody();}
	public int lngContext() {return ctx.size();}
	public LamBinding getLamBinding(int i){
		LamBinding lb = null;
		if ((i>=0) && (i<ctx.size())) lb = ctx.get(i);
		return lb;
	}
	public String toStringShort(){
		String txt = "[";
		for(int i=0;i<ctx.size();i++ ){
			if(i>0) txt = txt + ","; 
			txt = txt + ctx.get(i).getName();
		}
		return txt + "]";
	}
}
