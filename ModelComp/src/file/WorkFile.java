package file;

import main.*;

public class WorkFile {
	private InputText in = new InputText();
	private OutputText out = new OutputText();
	//=========
	private String line = "";
	private int nLine;
	private int nextChar;
	private char next = ' ';
	private int lex;
	private String valueLex, valuePrev;
	private String errorText;
	
	public String getErrorText() {return errorText;}
	
	public String testingInput(String name) {
		String s = "";
		String rs;
		if(in.open(name)) {
			System.out.println("File " + name + " is open.."); 
			while ((rs = in.input()) != null)
				System.out.println(".." + rs);
			in.close();
			System.out.println("File " + name + " is close.."); 
		} else s = "Not open input file " + name + "!"; 
		return s;
	}
	
	public String testingOutput(String name, String [] wr) {
		String s = "";
		//String rs;
		if(out.open(name)) {
			System.out.println("File " + name + " is open..");
			for (int i = 0; i < wr.length; i++){
			    out.output(wr[i]);
				System.out.println(".." + wr[i]);
			}
			out.close();
			System.out.println("File " + name + " is close.."); 
		} else s = "Not open output file " + name + "!"; 
		return s;
	}
	
	public String outputAlgorithm(String name, Algorithm model) {
		String res = "";
		String wr;
		Rule r;
		if(out.open(name)) {
			System.out.println("File " + name + " is open..");
			if (!model.descr.isEmpty()) out.output("'" + model.descr);
			out.output("Algorithm " + model.name);
			wr = " Alphabet \"" + model.main + "\", \"" + model.add + "\";";
			if (model.isNumeric) wr = wr + " Numerical " + model.rank + ";";
			out.output(wr); 
			for (int i = 0; i < model.program.size(); i++){
				r = (Rule)model.program.get(i);
				wr = "  \"" + r.getsLeft() + "\" ->";
				if (r.getisEnd()) wr = wr + ".";
				wr = wr + " \"" + r.getsRigth() + "\";";
				if (!(r.gettxComm().isEmpty())) wr = wr + " '" + r.gettxComm();
			    out.output(wr);
			}
			out.output("end " + model.name);
			out.close();
			System.out.println("File " + name + " is close.."); 
		} else res = "Not open output file " + name + "!"; 
		return res;
	}
	
	public Algorithm inputAlgorithm(String name) {
		Algorithm model = null;
		errorText = "";
		if(in.open(name)) {
			System.out.println("File " + name + " is open.."); 
		    line = ""; nLine = 0; nextChar = 1;
		    getChar(); get();
		    model = algorithm();
		  	in.close();
			System.out.println("File " + name + " is close.."); 
			if (!errorText.isEmpty()) errorText = "." + nLine + ": " + errorText;
		} else errorText = "Not open input file " + name + "!"; 
		return model;		
	}
	
	private Algorithm algorithmT(){
		while (lex != 10){
			System.out.println("."+ nLine+ ". lex =" + lex + ". valueLex =" + valueLex + ". valuePrev = " + valuePrev);
			get();
		}
		return null;
	}
	
	private Algorithm algorithm () {
		Algorithm model = null;
		String txComm = "";
		//boolean isNumeric = false;
		//int rank = 2;
		Rule r;
		if (lex == 4) {txComm = valueLex; get();} 
		exam(20,"службове слово Algorithm");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя алгоритму");
		if (errorText.isEmpty()) {
			model = new Algorithm(0,valuePrev);
			model.descr = txComm;
			exam(21,"службове слово Alphabet");
			if (errorText.isEmpty()) exam(2, "рядок - основний алфавіт");
		}
		if (errorText.isEmpty()) {
			model.main = StringWork.isAlfa("", valuePrev);
			exam(12,"символ ,");
			if (errorText.isEmpty()) exam(2, "рядок - додатковий алфавіт");
		}
		if (errorText.isEmpty()) {
			model.add = StringWork.isAlfa(model.main, valuePrev);
			exam(13,"символ ;"); 
			if (lex == 22){
				model.isNumeric = true; get(); 
				exam(3,"ціле число - арність функції");
				if (errorText.isEmpty()) {
					model.rank = new Integer(valuePrev);
					exam(13,"символ ;"); 
				}
			} else model.isNumeric = false;
		}
		while ((errorText.isEmpty()) && (lex != 23)){
			r = rule();
			if (r != null) model.program.add(r);
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя алгоритму");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем алгоритму " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		if (errorText.isEmpty()) 
			if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;		
	}
	
	private Rule rule() {
		String sLeft, sRigth = "";
		String txComm = "";
		boolean isEnd = false;
		sLeft = valueLex;
		exam(2, "рядок - права частина підстановки");
		if (errorText.isEmpty()) {
			exam(14,"символи ->");
		}
		if (errorText.isEmpty()) {
			if (lex == 11) {isEnd = true; get();}
			sRigth = valueLex;
			exam(2, "рядок - ліва частина підстановки");
			if (errorText.isEmpty()) exam (13,"символ ;");
		}
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			return new Rule(sLeft,sRigth,isEnd, txComm);
		} else return null;
	}
	
	private void exam(int lx, String what) {
		if (lex == lx) {
			valuePrev = valueLex;
			get();
		}
		else errorText = "Очікується " + what + " !";
	}
	//=========================== Lexical =====================
	private void getChar() {
		// вводить слідуючий символ, при необхідності -- новий рядок !!! 
		if (line != null) {
			if (line.length() <= nextChar) {
				line = in.input(); nextChar = 1; next = ' ';
				if (line != null) {
					nLine++;
					//System.out.println("*" + nLine + "*" + line + "*");
					line = line + '\n'; next = line.charAt(0); 
				}
				//if ((line != null) && (!line.isEmpty())) next = line.charAt(0); 
			} else next = line.charAt(nextChar++);
		} else next = ' ';
	}
	
	private void get() {
		String value = "";
		// пропускаємо проміжки і порожні рядки .....
		while (((next == ' ') || (next == '\n')) && (line != null)) getChar();
		if (line != null) {
			valueLex = "";
			if (next == '\'') {
				lex = 4; getChar();
				while (next != '\n') {
					valueLex = valueLex + next; getChar();
				}
				//getChar();
			} else if (next == '"') {
				lex = 2; getChar();
				while (next != '"') {
					valueLex = valueLex + next; getChar();
				}
				getChar();
			} else if (StringWork.isAlfa(next)) {
				lex = 1;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isIden(next));
				switch (valueLex){
					case "Algorithm": lex = 20; break;
					case "Alphabet": lex = 21; break;
					case "Numerical": lex = 22; break;
					case "end": lex = 23; break;
				}
			} else if (StringWork.isDigit(next)) {
				lex = 3;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isDigit(next));
			} else  {
				lex = 40;  // undefined lexem !!!
				switch (next){
					case '.': lex = 11; getChar(); break;
					case ',': lex = 12; getChar(); break;
					case ';': lex = 13; getChar(); break;
					case '-': getChar(); 
						if (next == '>') {lex = 14; getChar();}	break;
					//case '\n':	
					default: getChar();	
				}
			}
			
		} else lex = 10;  // eof !!!
	}
	
	
}
