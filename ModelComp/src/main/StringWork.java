package main;

import java.util.regex.Pattern;
import java.util.*;

public class StringWork {
	public boolean isNumber(String s) {
		int l = s.length();
		int i;
		boolean good = true;
		i = 0;
		while((good) && (i<l)){
			good = isNumber(s.charAt(i));
			i++;
		}
		return good;
	}
	public boolean isNumber(char ch){
		return (ch == '0') || (ch == '1') || (ch == '2') || 
				(ch == '3') || (ch == '4') || (ch == '5') ||
				(ch == '6') || (ch == '7') || (ch == '8') || (ch == '9'); 
	}
	
	public static boolean isPosNumber(String s) {
		//Pattern pnum = Pattern.compile("tt");
		return Pattern.matches("[0-9]*[1-9][0-9]*", s);
	}
	
	public static boolean isNatur(String s) {
		return Pattern.matches("[0-9]+", s);
	}
	
	public static boolean isAlfa(char ch) {
		return Pattern.matches("[a-zA-Z]", new String("" + ch));
	}

	public static boolean isIden(char ch) {
		return Pattern.matches("[a-zA-Z_0-9]", new String("" + ch));
	}
	
	public static boolean isDigit(char ch) {
		return Pattern.matches("[0-9]", new String("" + ch));
	}
	
	public static boolean isSelect(String s) {
		return Pattern.matches("i[1-9][1-9]", s); // && (s.charAt(1) >= s.charAt(2));
	}
	
	public static boolean isIdentifer(String s) {
		//Pattern pnum = Pattern.compile("tt");
		return Pattern.matches("[a-zA-Z][0-9a-zA-Z_]*", s);
	}
	

	public static boolean isPeano(String s) {
		return Pattern.matches("[|]*", s);
	}
	
	public static boolean isMove(String s) {
		//Pattern pnum = Pattern.compile("tt");
		return Pattern.matches("@[0-9a-zA-Z][0-9a-zA-Z][^ ][<>.]", s);
	}
	public static boolean isState(String s) {
		//Pattern pnum = Pattern.compile("tt");
		return Pattern.matches("@[0-9a-zA-Z][0-9a-zA-Z]", s);
	}
	
	public static boolean isOnlyAlfa(String alfa, String s) {
		//Pattern pnum = Pattern.compile("tt");
		return Pattern.matches("[" + alfa+"]*", s);
	}
	
	
	public static String isAlfa(String alfa, String s) {
	// вибирає з s всі РІЗНІ символи, що не водять в алфавіт alfa
		String noAlfa = "";
		String one;
		for (int i = 0; i < s.length(); i++){
			one = s.substring(i,i+1);
			//System.out.println("alfa=" + alfa+ " one=" + one + " patern=" + "[^"+alfa+"]");
			if ((alfa.isEmpty()) || (Pattern.matches("[^"+alfa+"]",one))) {
				if ((noAlfa.isEmpty()) ||(Pattern.matches("[^"+ noAlfa + "]",one))) noAlfa = noAlfa + one;
			}
		}
		return noAlfa;
	}
	
	public static String unionAlfa(String alfa, String beta){
		return alfa + isAlfa(alfa,beta);
	}
	
	public static String extract(String s, String what) {
		String alfa = "";
		String var = "";
		int i = 0;
		while(i < s.length()){
			if(s.charAt(i) == '@'){
				i++;
				if(i == s.length()) var = var + " ";
				else var = var + s.substring(i,i+1);
			} else alfa = alfa + s.substring(i,i+1);
			i++;	
		}
		if (what.equals("Var")) return var; else return alfa;
	}
	
	static String substitution(String bs, String wh, int b, int l){
		//Підстановка в рядок bs рядка wh замісто підрядка, що починається з позиції b довжиною l символів
		if (l==0) return wh.concat(bs);   // only on begin of bs ==> b ==0  ...wh+bs
		else if(wh.length() == 0)  
			return (bs.substring(0, b-1)).concat(bs.substring(b+l-1));
		else if (b+l <= bs.length()) 
			return ((bs.substring(0,b-1)).concat(wh)).concat(bs.substring(b+l-1));
		else return (bs.substring(0,b-1)).concat(wh);
	}
	
	static boolean isEqual(String str, int m, String pt){
		// В рядку str з позиції m>0  находиться підрядок, що співпадає з рядком pt  
		return (str.substring(m-1, m-1+pt.length())).equals(pt);
	}
	
	static int findFirst(String str, String pt ){
		int m = 0;
		int i = 1;
		while ((m==0) && (i+pt.length() <= str.length()+1)) {
			if (isEqual(str,i,pt)) m = i; else i++;
		}
		return m;
	}
	
	public static String toInternal(int i) {
		String r = "";
		while (i-- > 0) r = r + "|";   
		return r;
	}
	
	public static String transNumeric(String init) {
		String res = "";
		int intC = 0;
		char s;
		for(int i = 0; i < init.length(); i++) {
			s = init.charAt(i);
			if (s != '|') {
			  if (intC > 0) res = res + intC;
			  intC = 0; res = res + s;
			} else intC++;
		}
		if (intC > 0) res = res + intC;
		if (res.isEmpty()) res = "0";
		return res;
	}
			
	public static String[] transferToArray(ArrayList <String> al){
		String[] res = null;
		if ((al !=null) && (al.size() > 0)){
			res = new String[al.size()];
			for(int i=0; i < al.size(); i++) res[i] = al.get(i);
		}	
		return res;
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
}

