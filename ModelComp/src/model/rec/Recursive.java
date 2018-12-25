package model.rec;

import java.text.*;
import java.util.*;
import javax.swing.tree.*;

import db.*;
import file.*;
import main.StringWork;
import model.Model;

public class Recursive extends Model {
	//private HashMap <String, RecBody> map;
	// похоже його можна ПРИБРАТИ !!!!!! до локального в певних методах !!!!!
	//  для доступу до функції через ім"я findCommand(nm) => >=0::+; -1::- 
	public Recursive(int id, String name) {
		super(id,name);
	}
	
	
	public String getType() {return "Recursive";}  
	public boolean getIsNumeric() {return false;}
	public int getRank() {return 0;}
	/*
	public int dbNewAs() { 
		return DbAccess.getDbRecursive().newRecursiveAs(this);
	}
	public boolean dbDelete() {
		 return DbAccess.getDbRecursive().deleteRecursive(this);
	}
	*/
	//-----work DB ------- 
	public String dbInsertModel(int where, String nmModel, String nmFunction) {
		return DbAccess.getDbRecursive().insertFunction(this, nmModel, nmFunction);
	}
	
	//============================= in load Recursive ===================
	//     build map!!!
	public void extend(){
		HashMap <String, RecBody> map = new HashMap <String, RecBody>();
		Function f;
		String st;
		RecBody rb;
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			rb = analysRecBody(f.txBody);
			f.setBody(rb);
			f.iswf = (rb != null);
			if(rb == null) f.errorText = "S:" + errorText; 
			map.put(f.name,rb);
		}
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			if(f.iswf) {
				rb = f.getBody();
				st = rb.setRank(f.name, map);
				f.rank = rb.rank;
				if(!st.isEmpty()){
					f.errorText = "R:" + st;
					f.iswf = false;
				}
				else f.isConst = rb.isConst(map);
			}
		}
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			if(f.iswf) {
				rb = f.getBody();
				st = rb.iswf(map);
				if(!st.isEmpty()) {
					f.iswf = false; f.errorText = "C:" + st;
				}
			}
		} 
	}

	
	//=================================
	private int limit, step;
	private String reasonUndef;
	private boolean noUndef;
	private int result;
	//=================================
	public String evalFunction(Function f, int[] arg, int limit){
		RecBody bf;
		this.limit = limit;	step = 0;
		reasonUndef = ""; noUndef = true;
		result = 0;
		//bf = map.get(f.getName());
		bf=f.getBody();
		result = bf.eval(arg, this);
		if (!noUndef){
			if (reasonUndef.equals("Limit")) return "Невизначено (вичерпана загальна кількість кроків)";
			else return "Невизначено (при виконанні операції " + reasonUndef + ")";
		} else	return "" + result;
		
	}
	
	public void stepEval() {
		step++;
		if (noUndef && (step>limit)){
			noUndef = false; reasonUndef = "Limit";
		}
	}
	
	public void setUndef(String name){
		noUndef = false; reasonUndef = name;
	}
	
	public boolean getNoUndef() {return noUndef;}
	public int getAllStep() {return step;}
	public int getResult() {return result;}
	
	
	public String[] iswfModel(){
		ArrayList <String> mes = new ArrayList<String>();
		String [] names = new String[] {"","",""};
		Function f;
		int j;
		for(int i = 0; i < program.size(); i++) {
			f = (Function)program.get(i);
			if (!f.getiswf()){
				switch(f.geterrorText().charAt(0)){
				case 'S': j=0; break;
				case 'R': j=1; break;
				case 'C': j=2; break;
				default: j = -1;
				}
				if (j >= 0){
					if(!names[j].isEmpty()) names[j] = names[j] + ",";
					names[j] = names[j] + f.getName();
				}
			}
		}
		if(!names[0].isEmpty()) mes.add("Функції " + names[0] + " містять синтаксичні помилки."); 
		if(!names[1].isEmpty()) mes.add("Для функцій " + names[1] + " не встановлено арність.");
		if(!names[2].isEmpty()) mes.add("Для функцій " + names[2] + " не виконуються контекстні умови."); 
		return StringWork.transferToArray(mes);
	}
	/*
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Function fun;
		RecBody rb;
		char[] typeInfo = {'S','I','B', 'B', 'S','S','I','I'}; 
		for (int i = 0; i < program.size(); i++){
			row = new ArrayList();
			fun = (Function)program.get(i);
			row.add(fun.getName());
			row.add(fun.getRank());
			row.add(fun.getisConst());
			row.add(fun.getiswf());
			row.add(fun.gettxBody());
			row.add(fun.gettxComm());
			row.add(fun.getId());
			row.add(idModel);
			data.add(row);
        } 
        return data;
	}		
	*/	
	public String toString() {
		String rs = "[";
		for(int i=0; i<program.size(); i++){
			Function f = (Function)program.get(i);
			RecBody rb = f.getBody();
			rs = rs + "\n.." + f.getName() + ":" ;
			if (rb != null) rs = rs + rb.rank + ":" + rb.toString();
		}
		return rs + "\n]";
	}

	public String output( OutputText out) {
		String res = "";
		String wr;
		Function f;
			if (!descr.isEmpty()) out.output("'" + descr); else out.output("'");
			out.output("Recursive " + this.name);
			for (int i = 0; i < program.size(); i++){
				f = (Function)program.get(i);
				if (!(f.gettxComm().isEmpty())) out.output("   '" + f.gettxComm());
				wr = f.getName() + ":" + ((f.getRank())<0?0:f.getRank()) + "=" +f.gettxBody(); 
				out.output("   " + wr + ";");
			}
			out.output("end " + this.name);
		return res;
	}		
	
	public String fullAnalys(String name, String body){
		String st = "";
		RecBody rb = analysRecBody(body);
		if (rb != null) {
			HashMap <String, RecBody> map = new HashMap <String, RecBody>();
			for(int i = 0; i < program.size(); i++){
				Function f = (Function) program.get(i);
				map.put(f.getName(),f.getBody());
			}
			st = rb.setRank(name, map);
			if(st.isEmpty()){
				st = rb.iswf(map);
				if(!st.isEmpty()) st = "R:" + st;
			}
			else st = "R:" + st;
		} else st = "S:" + errorText;
		return st;
	}
	
	//перевіряє ім"я нової функції на коректність...
	//public String testName(String name){ return this.testNameCommand(name);}
	/*
	public String testName(String name){
		String st = "";
		if (StringWork.isIdentifer(name)){
			if(map.containsKey(name)) st = "Функція з іменем " + name + " вже є в наборі.";
		} else st = "Імя функції " + name + " - не ідентифікатор.";
		return st;
	}
	*/
	// повертає список ВСІХ функцій, котрі використовують функцію name
	public String usingName(String name) {
		boolean first = true;
		String res = "";
		//for(String nf:map.keySet()){
		for(int i=0; i <program.size();i++){
			Function f = (Function)program.get(i);
			String nf = f.getName();
			if (!nf.equals(name)){
				RecBody rb = f.getBody();  //map.get(nf);
				if ((rb != null) && (rb.usingName(name))){
						if (!first) res = res + ", ";
						first = false;
						res = res + nf;
				}
			}	
		}
		return res;
	}
	//знаходить імя функції по замовчуванню : перше вільне з "base", "base00", "base01",...
	//public String findName(String base){  return this.findNameCommand(base);} 
	/*
	public String findName(String base){
		int i = 0;
		NumberFormat suf = new DecimalFormat("00"); 
		boolean isUse;
		String name=base;
		do {
			isUse = map.containsKey(name);
			if (isUse) name = base + suf.format(i);
			i++;
		} while (isUse);
		return name;
	}
	*/
	
   public Function newFunction(Function f){	
	   int num = findMaxNumber();
	   if (f != null){
		  return new Function(num, this.findNameCommand(f.name), f.txBody, f.txComm); 
	   } 
	   else return new Function(num, this.findNameCommand("new"),"","");
   }
	
   	// 	знаходить порядковий номер функції в програмі за іменем name 
	public int findCommand(String name) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(name.equals(((Function)program.get(i)).getName())) cnt = i;
		}
		return cnt;
	}
   
   
	public String isBody(String text){
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		recBody();
		if (errorText.isEmpty()){
			//if (!eos) errorText = "Не знайдено кінця рядка!";
			if (lex != 20) errorText = "Не знайдено кінця рядка!";
		}
		if (errorText.isEmpty()) return "Аналіз успішний!";
		else return errorText;
	}
	
	public  RecBody analysRecBody(String text){
		RecBody rb;
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		rb = recBody();
		if (errorText.isEmpty()){
			if (lex!=20){    //(!eos) {
				errorText = "Не знайдено кінця рядка!";rb = null;
			}
		}
		return rb;
	}
	
	public String getErrorText() {return errorText;}
	
	//===========================================================
	private String textAnalys;
	private int posAnalys;
	//-------------
	private boolean eos;
	private char next;
	//-------------
	//  lex = 1 => ідентифікатор, 2 => число, 3 => inn , 4 => a1, 5 => z1  ----> valueLex
	//  @s - 6, @R - 7, @M - 8, ( - 9, ) - 10, [ - 11, ] - 12, , - 13, eos - 20. 
	private int lex;
	private String valueLex;
	private String valuePrev;
	private String errorText = "";
	//=============================
	private RecBody recBody() {
		//System.out.println("recBody: " + lex + " " + valueLex);
		RecBody r = null;
		switch(lex){
		case 1: r = new RecName(valueLex); get(); break;
		case 3: case 4: case 5:	r = new RecBase(valueLex); get(); break;
			// errorText = "В селекторі " + valueLex + " арність " + valueLex.charAt(1) + " меньше вибора " + valueLex.charAt(2)+ ".";
		//case 4: r = new RecBase(0,1); get(); break;
		//case 5: r = new RecBase(0,0); get(); break;
		case 6: get(); r = recSuper(); break;
		case 7: get(); r = recPrime(); break;
		case 8: get(); r = recSolve(); break;
		default: errorText = "Помилка в виразі";
		}
		return r;
	}
	private RecBody recSuper() {
		RecBody r = null;
		RecBody f = null;
		ArrayList <RecBody> af = null;
		exam(9, "символ '('");
		if (errorText.isEmpty()) {
			f = recBody();
			exam(13, "символ ','");
			exam(11, "символ '['");
			if (errorText.isEmpty()) {
				af = new ArrayList<RecBody> ();
				af.add(recBody());
				while(errorText.isEmpty() && (lex == 13)) {
					get(); af.add(recBody());
				}
				exam(12, "символ ']'");
				exam(10, "символ ')'");
			}
		}
		if (errorText.isEmpty()) r = new RecSuper(f,af);
		return r;
	}	
	private RecBody recPrime() {
		RecBody r = null;
		RecBody g = null;
		RecBody h = null;
		exam(9, "символ '('");
		if (errorText.isEmpty()) {
			g=recBody();
			exam(13, "символ ','");
			if (errorText.isEmpty()) {
				h=recBody();
				exam(10, "символ ')'");
			}
		}
		if (errorText.isEmpty()) r = new RecPrime(g,h);
		return r;
	}
	private RecBody recSolve() {
		RecBody r = null;
		RecBody g = null;
		int max = 100;
		exam(9, "символ '('");
		if (errorText.isEmpty()) {
			g = recBody();
			exam(13, "символ ','");
			exam(2, "число");
			if (errorText.isEmpty()) max = new Integer(valuePrev);
			exam(10, "символ ')'");
		}
		if (errorText.isEmpty()) r = new RecSolve(g,max);
		return r;
	}
	//===========================
	private void getChar() {
		eos = (posAnalys >= textAnalys.length());
		next = ' ';
		if (!eos) {
			next = textAnalys.charAt(posAnalys);
			posAnalys++;
		}
	}
	private void get() {
		lex = 20;
		                          //if (test) System.out.println("eos="+eos+" next="+next +"?");
		while ((next == ' ') && !eos) getChar();
		if (!eos){
			valueLex = "";
			if (StringWork.isAlfa(next)) {
				lex = 1;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isIden(next));
				if(StringWork.isSelect(valueLex)) lex = 3;
				else if(valueLex.equals("a1")) lex = 4;	
				else if(valueLex.equals("z1")) lex = 5;	
			} else if (StringWork.isDigit(next)) {
				lex = 2;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isDigit(next));
			} else if (next == '@'){
				getChar();
				switch (next) {
				case 'S': lex = 6; break;
				case 'R': lex = 7; break;
				case 'M': lex = 8; break;
				default: lex = 19; // undefined lexem !!!
				}
				if (lex != 19) getChar();
			} else  {
				lex = 19;  // undefined lexem !!!
				switch (next){
					case '(': lex = 9; break;
					case ')': lex = 10; break;
					case '[': lex = 11; break;
					case ']': lex = 12; break;
					case ',': lex = 13; break;
				}
				if (lex != 19) getChar();
			}
		}
	}
	private void exam(int lx, String what) {
		if (errorText.isEmpty()) {
			if (lex == lx) {
				valuePrev = valueLex; get();
			}
			else errorText = "Очікується " + what + " .";
		}	
	}	
	
	//===========================================
	public String testFunction(Function f, int[] arg, int limit, DefaultMutableTreeNode  root){
		RecBody bf;
		String result = "..";
		this.limit = limit;	step = 0;
		reasonUndef = ""; noUndef = true;
		//bf = map.get(f.getName());
		bf = f.getBody();
		result = bf.test(arg, this, root);
		if (!noUndef){
			if (reasonUndef.equals("Limit")) return "Невизначено (вичерпана загальна кількість кроків)";
			else return "Невизначено (при виконанні операції " + reasonUndef + ")";
		} else	return "" + result;
		
	}
}
