package main;

import java.util.*;

public class Recursive extends Model {
	HashMap <String, RecBody> map;
	public Recursive(int id, String name) {
		super(id,name);
		map = new HashMap <String, RecBody>();
	}
	
	public void extend(){
		Function f;
		String st;
		RecBody rb;
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			rb = analysRecBody(f.txBody);
			f.iswf = (rb != null);
			if(rb == null) f.errorText = "S:" + errorText; //getErrorText(); 
			//String st;
			//if( rb == null) st = getErrorText(); else st = rb.toString(); 
			//System.out.println(f.name + ":" + f.txBody + " ==> " + f.getRank());
			map.put(f.name,rb);
		}
		
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			if(f.iswf) {
				rb = map.get(f.name);
				st = rb.setRank(f.name, map);
				f.rank = rb.rank;
				if(!st.isEmpty())f.errorText = "R:" + st;
				else f.isConst = rb.isConst(map);
				//System.out.println(f.name + ":" + f.errorText);
			}
		}
		
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			if(f.iswf) {
				rb = map.get(f.name);
				st = rb.iswf(map);
				if(!st.isEmpty()) {
					f.iswf = false; f.errorText = "C:" + st;
					System.out.println(f.name + ":" + f.errorText);
				}
			}
		} 
		//System.out.println("rank ...." );
		//System.out.println(toString());
		
		
	}

	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Function fun;
		RecBody rb;
		char[] typeInfo = {'S','I','B', 'B', 'S','S','I','I'}; 
		for (int i = 0; i < program.size(); i++){
			row = new ArrayList();
			fun = (Function)program.get(i);
			//rb = map.get(fun.getName());
			row.add(fun.getName());
			row.add(fun.getRank());
			row.add(fun.getisConst());
			row.add(fun.getiswf());
			row.add(fun.gettxBody());
			row.add(fun.gettxComm());
			//row.add((rb==null)?"":rb.toTest());
			row.add(fun.getId());
			row.add(idModel);
			data.add(row);
        } 
        return data;
	}		
	
	
	
	public String toString() {
		String rs = "[";
		for(String nf:map.keySet()){
			RecBody rb = map.get(nf);
			rs = rs + "\n.." + nf + ":" ;
			if (rb != null) rs = rs + rb.rank + ":" + rb.toString();
		}
		return rs + "\n]";
	}
	
	
	public String isBody(String text){
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		recBody();
		if (errorText.isEmpty()){
			if (!eos) errorText = "�� �������� ���� �����!";
		}
		if (errorText.isEmpty()) return "����� �������!";
		else return errorText;
	}
	
	public  RecBody analysRecBody(String text){
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		return recBody();
	}
	
	public String getErrorText() {return errorText;}
	
	//===========================================================
	private String textAnalys;
	private int posAnalys;
	//-------------
	private boolean eos;
	private char next;
	//-------------
	//  lex = 1 => �������������, 2 => �����, 3 => inn , 4 => a1, 5 => z1  ----> valueLex
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
			// errorText = "� �������� " + valueLex + " ������� " + valueLex.charAt(1) + " ������ ������ " + valueLex.charAt(2)+ ".";
		//case 4: r = new RecBase(0,1); get(); break;
		//case 5: r = new RecBase(0,0); get(); break;
		case 6: get(); r = recSuper(); break;
		case 7: get(); r = recPrime(); break;
		case 8: get(); r = recSolve(); break;
		default: errorText = "������� � �����";
		}
		return r;
	}
	private RecBody recSuper() {
		RecBody r = null;
		RecBody f = null;
		ArrayList <RecBody> af = null;
		exam(9, "������ '('");
		if (errorText.isEmpty()) {
			f = recBody();
			exam(13, "������ ','");
			exam(11, "������ '['");
			if (errorText.isEmpty()) {
				af = new ArrayList<RecBody> ();
				af.add(recBody());
				while(errorText.isEmpty() && (lex == 13)) {
					get(); af.add(recBody());
				}
				exam(12, "������ ']'");
				exam(10, "������ '('");
			}
		}
		if (errorText.isEmpty()) r = new RecSuper(f,af);
		return r;
	}	
	private RecBody recPrime() {
		RecBody r = null;
		RecBody g = null;
		RecBody h = null;
		exam(9, "������ '('");
		if (errorText.isEmpty()) {
			g=recBody();
			exam(13, "������ ','");
			if (errorText.isEmpty()) {
				h=recBody();
				exam(10, "������ '('");
			}
		}
		if (errorText.isEmpty()) r = new RecPrime(g,h);
		return r;
	}
	private RecBody recSolve() {
		RecBody r = null;
		RecBody g = null;
		int max = 100;
		exam(9, "������ '('");
		if (errorText.isEmpty()) {
			g = recBody();
			exam(13, "������ ','");
			exam(2, "�����");
			if (errorText.isEmpty()) max = new Integer(valuePrev);
			exam(10, "������ '('");
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
			else errorText = "��������� " + what + " .";
		}	
	}	
}