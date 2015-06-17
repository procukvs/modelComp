package main;

import java.util.*;

public class RecBody {
	int rank;
	boolean isConst;
	public RecBody(int rank, boolean isConst) {
		this.rank = rank; this.isConst = isConst;
	}
	
	public String toString() {return "";}
	
	public RecBody fromString(String text){
		return null;
	}
	
	public String isBody(String text){
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		recBody();
		if (errorText.isEmpty()){
			if (!eos) errorText = "Не знайдено кінця рядка!";
		}
		if (errorText.isEmpty()) return "Аналіз успішний!";
		else return errorText;
	}
	
	public RecBody analysRecBody(String text){
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
		case 1: r = new RecName(valueLex, 0, false); get(); break;
		case 3:
			if(valueLex.charAt(1) >= valueLex.charAt(2)) {
				int b = valueLex.charAt(1) - '0';
				int wh = valueLex.charAt(2) - '0';
				r = new RecBase(b,wh); get();
			}
			else errorText = "В селекторі " + valueLex + " арність " + valueLex.charAt(1) + " меньше вибора " + valueLex.charAt(2)+ ".";
			break;
		case 4: r = new RecBase(0,1); get(); break;
		case 5: r = new RecBase(0,0); get(); break;
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
				exam(10, "символ '('");
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
				exam(10, "символ '('");
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
			exam(10, "символ '('");
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
			else errorText = "Очікується " + what + " .";
		}	
	}
	
}

