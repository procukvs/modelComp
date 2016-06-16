package main;

import java.util.ArrayList;
import java.util.TreeMap;

public class Derive extends Command {
	private int num = 1;
	private boolean isAxiom = false;
	private String sLeft = "";
	private String sRigth = "";
	
	//================================
	private boolean isMulty = true;  // допустимі в лівій частині повторення змінних
	private static String varChar = "RSTUVWXYZ";
	
	
	//private String txComm = "";
    public String show(String dummy){
      String s = ""; 
     // if (isAxiom) s = "Аксіома "; 
     // else s = "" + sLeft + " ->"; 
      if (isAxiom) s = "..."; else  s = sLeft + " ->" ;
      s = s + " " + sRigth;
      if (txComm.length()>0) s = s +  " '" + txComm;
      return s;
    }
    public Derive(int num, boolean isAxiom, String sLeft, String sRigth,  String txComm, int id) {
    	super(id,txComm);
    	this.num = num; this.isAxiom = isAxiom; 
    	this.sLeft = sLeft; this.sRigth = sRigth;
    }
    public int getNum() {return num;}
    public boolean getisAxiom() {return isAxiom;}
    public String getsLeft() {return sLeft;}
    public String getsRigth() {return sRigth;}
   // public String gettxComm() {return txComm;}
   // public int getId() { return id;}
    
    public String output() {
    	String wr = "";
		if (!isAxiom) wr = "  \"" + sLeft + "\" -> "; else wr = "  ";
		wr = wr + "\"" + sRigth + "\";";
    	return wr;
   }
   
   public ArrayList <String>  iswfCommand(Post post){
	   ArrayList <String> mes =  new ArrayList <String>();
	   mes.addAll(post.iswfNum("" + num));
	   if (!isAxiom){
		   mes.addAll(post.iswfLeft(sLeft));
		   mes.addAll(post.iswfRigth(sLeft,sRigth));
	   } else mes.addAll(post.iswfAxiom(sRigth));
	   return mes;
   }
   
   public ArrayList getSource(Model model) { 
		ArrayList row = new ArrayList();
		row.add(this.getNum());
		row.add(this.getisAxiom());
		row.add(this.getsLeft());
		row.add(this.getsRigth());
		row.add(this.gettxComm());
		row.add(this.getId());
		row.add(model.id);
       return row;
	}
	
   
   // застосування правила виводу num до слова str на кроці step
   public ArrayList <FullSubstitution> extend(String str, String main, int step){
	   ArrayList <FullSubstitution> res = new ArrayList <FullSubstitution>();
	   ArrayList <TreeMap <Character,String >> sms = findSubstit(sLeft,str, null);
	   if (sms != null) {
		   for (int i = 0; i < sms.size(); i++){
			   TreeMap <Character,String > sm = sms.get(i); 
			   String st = StringWork.substitution(sRigth,sm);
			   boolean isTh = StringWork.isOnlyAlfa(main, st);
			  // System.out.println("main=" + main + " st=" + st + "..." + isTh);
			   res.add(new FullSubstitution(st, new Substitution(num, step, StringWork.substitutionVar(sLeft, sm)), isTh, true));
		   }
	   }
	   return res;
   }
   
   public ArrayList <TreeMap <Character,String >> findFullSet(String str){
	   return findSubstit(sLeft,str, null);
   }
   
   private  ArrayList <TreeMap <Character,String >> findSubstit(String pat, String str, TreeMap <Character,String > sm){
		if (pat.isEmpty()){
			if (str.isEmpty()) {
				ArrayList <TreeMap <Character,String >> res = new ArrayList <TreeMap <Character,String>>();
				res.add(sm);
				return res;
			}
			else return null;
		} else {
			if (pat.charAt(0) != '@'){
				if ((!str.isEmpty()) && (str.charAt(0) == pat.charAt(0))) 
					return findSubstit(pat.substring(1), str.substring(1), sm);
				else return null;
			} else {
				if ((sm != null) && (sm.containsKey(pat.charAt(1))) ){
					String sub = sm.get(pat.charAt(1));
					if ((str.length() >= sub.length()) &&(sub.equals(str.substring(0,sub.length())))) {
						//String str1 = (str.length())
						return findSubstit(pat.substring(2),str.substring(sub.length()), sm);
					} else return null;
				} else {
					ArrayList <TreeMap <Character,String >> res = new ArrayList <TreeMap <Character,String>>();
					for (int i=0; i <= str.length(); i++){
						TreeMap <Character,String > sm1;
						if (sm == null) sm1 = new TreeMap <Character,String > ();
						else sm1 =  (TreeMap <Character,String >) sm.clone();
						sm1.put(pat.charAt(1), str.substring(0,i));
						res = addSetSubstit(res, findSubstit(pat.substring(2), str.substring(i), sm1)); 
					}
					return res;
				}	
			}
		}
	}
	
	private ArrayList <TreeMap <Character,String >> addSetSubstit(ArrayList <TreeMap <Character,String >> init,
																		ArrayList <TreeMap <Character,String >> add) {
		if ((add != null) && (add.size() > 0)) {
			if(init != null) {
				init.addAll(add);	return init;
			} else return add;
		} else return init;
	} 
   
}
