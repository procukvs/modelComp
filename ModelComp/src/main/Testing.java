package main;

import java.util.*;

public class Testing {
	private boolean multy = true;
	
	public static Machine formMachine(){
		    
	    Machine m = new Machine(5, "Test1");
	    m.add = "a"; m.no = "d";
	    m.init = "@h0"; m.fin = "@z0";
	   // System.out.println(m.show());
	    ((Machine)m).addStateGoing("@s0");
	    ((Machine)m).addStateGoing("@h0"); 
	    ((Machine)m).addStateGoing("@z0");
	    System.out.println(m.show()); 
	    m.updateCommand("@h0", '_', "@s0_>");
	    m.updateCommand("@h0", 'd', "@s0d>");
	    m.updateCommand("@h0", '#', "@s0#>");
	    m.updateCommand("@h0", '|', "@s0|>");
	    m.updateCommand("@h0", 'a', "@s0a>");
	    m.updateCommand("@s0", '_', "@z0_.");
	    m.updateCommand("@s0", 'd', "@s0d>");
	    m.updateCommand("@s0", '#', "@s0#>");
	    m.updateCommand("@s0", '|', "@s0|>");
	    m.updateCommand("@s0", 'a', "@s0a>");
	    System.out.println(m.show()); 
	    return m;
   	}
	
	public static  TreeMap <Character,String > formSubstit(){
		TreeMap <Character,String > m = new TreeMap <Character,String >();
		m.put('R',"abc");
		m.put('S',"ab");
		m.put('P',"");
		return m;
	}
	
	public static String showSubst(TreeMap <Character,String > m) {
		String res = "[";
		for(char k : m.keySet()) {
			if (res.length() > 1) res = res + ",";
			res = res + "\"@" +  k + " \" -> \"" + m.get(k) + "\"";
		}
		return res + "]";
	}
	
	public static String  showSetSubst(ArrayList <TreeMap <Character,String >> set){
		String res = "{";
		if (set != null) {
			for (int i = 0; i < set.size(); i++)
				res = res + "\n" + showSubst(set.get(i));
		}
		return res + "\n}";
	}
	
	public static void testSubstitution() {
		TreeMap <Character,String > mm = formSubstit();
		String str= "a@Rbc@P@Pa@S";
		System.out.println("mm =" + showSubst(mm));
		System.out.println("str =" + str);
		System.out.println("substitution(str,mm) =" + substitutionVar(str,mm));
		
		
	}
	
	public static void findSubstit() {
	   String pat = "@S@P@S@P";
	   String str = "aaaaaa";
	   ArrayList <TreeMap <Character,String >> res = findSubstit(pat,str,null);
	   System.out.println("pat =" + pat);
	   System.out.println("str =" + str);
	   System.out.println("findSubstit(pat,str,null) =" + showSetSubst(res));
	}
	
	
	public static void test(){
		Derive der = new Derive(1, false, "@S@P@S@P", "a@Sbc@P@Pa@S",  "", 5);
		ArrayList <TreeMap <Character,String >> res = der.findFullSet("aaaaaa");
		System.out.println("pat =" + "@S@P@S@P");
		System.out.println("str =" + "aaaaaa");
		System.out.println("findSubstit(pat,str,null) =" + showSetSubst(res)); 
	}
	
	
	public static String substitution(String str, TreeMap <Character,String > sm) {
		String res = "";
		int i =0;
		while (i < str.length()){
			if (str.charAt(i) == '@'){
				i++; res = res + sm.get(str.charAt(i)); 
			} else res = res + str.substring(i,i+1);
			i++;
		}
		return res;
	}
	
	public static String substitutionVar(String str, TreeMap <Character,String > sm) {
		String res = "";
		int i =0;
		while (i < str.length()){
			if (str.charAt(i) == '@'){
				i++; res = res + "[" + str.charAt(i) + sm.get(str.charAt(i)) + "]"; 
			} else res = res + str.substring(i,i+1);
			i++;
		}
		return res;
	}	
	
	public static ArrayList <TreeMap <Character,String >> findSubstit(String pat, String str, TreeMap <Character,String > sm){
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
	
	public static ArrayList <TreeMap <Character,String >> addSetSubstit(ArrayList <TreeMap <Character,String >> init,
																		ArrayList <TreeMap <Character,String >> add) {
		if ((add != null) && (add.size() > 0)) {
			if(init != null) {
				init.addAll(add);	return init;
			} else return add;
		} else return init;
	}
	
	//========================================
	public static void testBody() {
		RecBody w =new  RecBody(2, false);
		RecBody rec = null;
		String str ="";
		String [] in = {"a1","z1","a2",  "i22", "@S(a1,[z1])",
				"@r(z1,a1)", "@R(z1,a1)", "@R(z1,@S(a1,[z1]))", "@M(x1,67)"};
		for (int i = 0; i < in.length; i++){
			System.out.println(": " + in[i] + " --> ");
			rec = w.analysRecBody(in[i]);
			if (rec == null) str = w.getErrorText();
			else str = " < " + rec.rank + " : " + rec.isConst + " > " + rec.toString();
			System.out.println( " --> " + str);
		}
	
	}
	
	
}
