package file;

import java.util.ArrayList;

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
	private String type = "Algorithm";
	
	public void setType(String type) {this.type = type;}
	public String getErrorText() {return errorText;}
	public OutputText getOut() {return out;}
	
	
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
	
	public String outputModel(String name, Model model){
		return model.output(name, out); 
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
	
	public Model inputModel(String name) {
		Model model = null;
		String txComm = "";
		errorText = "";
		if(in.open(name)) {
			System.out.println("File " + name + " is open.."); 
			type = "Model";
		    line = ""; nLine = 0; nextChar = 1;
		    getChar(); get();
		    // ������� ���������� ��������
		    if (lex == 4) {txComm = valueLex; get();} 
		    if (lex == 1) {
		    	type = valueLex; lex = 20;
		    	switch (type){
		    	case "Algorithm": model = algorithm(txComm); break;
		    	case "Machine" : model = machine(txComm); break;
		    	default: errorText = "��������� ��� ����� Algorithm/Machine !";	
		    	}
		    	
		    } else errorText = "��������� ��� ����� Algorithm/Machine !";
		   // model = algorithm();
		  	in.close();
			System.out.println("File " + name + " is close.."); 
			if (!errorText.isEmpty()) errorText = "." + nLine + ": " + errorText;
		} else errorText = "Not open input file " + name + "!"; 
		return model;		
	}
	
	
	public Algorithm inputAlgorithm(String name) {
		Algorithm model = null;
		errorText = "";
		if(in.open(name)) {
			System.out.println("File " + name + " is open.."); 
		    line = ""; nLine = 0; nextChar = 1;
		    getChar(); get();
		    model = algorithm1();
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
	
	private Algorithm algorithm1 () {
		String txComm = "";
		if (lex == 4) {txComm = valueLex; get();} 
		return algorithm (txComm);
	}
	
	private Algorithm algorithm (String txComm) {
		Algorithm model = null;
		Rule r;
		ArrayList rules = new ArrayList();
		//if (lex == 4) {txComm = valueLex; get();} 
		//System.out.println("init algorithm: lex =" + lex + " valueLex ="+ valueLex);
		exam(20,"�������� ����� Algorithm");
		if (errorText.isEmpty())  exam(1, "������������� - ��� ���������");
		if (errorText.isEmpty()) {
			model = new Algorithm(0,valuePrev);
			model.descr = txComm;
			model.program = rules;
			exam(21,"�������� ����� Alphabet");
			if (errorText.isEmpty()) exam(2, "����� - �������� ������");
		}
		if (errorText.isEmpty()) {
			model.main = StringWork.isAlfa("", valuePrev);
			exam(12,"������ ,");
			if (errorText.isEmpty()) exam(2, "����� - ���������� ������");
		}
		if (errorText.isEmpty()) {
			model.add = StringWork.isAlfa(model.main, valuePrev);
			exam(13,"������ ;"); 
			if (lex == 22){
				model.isNumeric = true; get(); 
				exam(3,"���� ����� - ������ �������");
				if (errorText.isEmpty()) {
					model.rank = new Integer(valuePrev);
					exam(13,"������ ;"); 
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
				exam(1, "������������� - ��� ���������");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "�������� ��� " + valuePrev  + " �� ������� � ������ ��������� " + model.name + "!";
				}
			} else errorText = "��������� �������� ����� end !";
		}	
		if (errorText.isEmpty()) 
			if (lex != 10) errorText = "�� �������� ���� ����� ";
		if (!errorText.isEmpty()) model = null; 
		return model;		
	}
	
	private Rule rule() {
		String sLeft, sRigth = "";
		String txComm = "";
		boolean isEnd = false;
		sLeft = valueLex;
		exam(2, "����� - ����� ������� ����������");
		if (errorText.isEmpty()) {
			exam(14,"������� ->");
		}
		if (errorText.isEmpty()) {
			if (lex == 11) {isEnd = true; get();}
			sRigth = valueLex;
			exam(2, "����� - ��� ������� ����������");
			if (errorText.isEmpty()) exam (13,"������ ;");
		}
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			return new Rule(sLeft,sRigth,isEnd, txComm);
		} else return null;
	}
	
	private Machine machine (String txComm) {
		Machine model = null;
		State st;
		int idSt =0;
		ArrayList states = new ArrayList();
		exam(20,"�������� ����� Machine");
		if (errorText.isEmpty())  exam(1, "������������� - ��� ������");
		if (errorText.isEmpty()) {
			model = new Machine(0,valuePrev);
			model.no = "";
			model.descr = txComm;
			model.program = states;
			exam(21,"�������� ����� Alphabet");
			if (errorText.isEmpty()) exam(2, "����� - �������� ������");
		}
		if (errorText.isEmpty()) {
			model.main = StringWork.isAlfa("", valuePrev);
			exam(12,"������ ,");
			if (errorText.isEmpty()) exam(2, "����� - ���������� ������");
		}
		if (errorText.isEmpty()) {
			model.add = StringWork.isAlfa(model.main, valuePrev);
			exam(13,"������ ;"); 
			if (lex == 22){
				model.isNumeric = true; get(); 
				exam(3,"���� ����� - ������ �������");
				if (errorText.isEmpty()) {
					model.rank = new Integer(valuePrev);
					exam(13,"������ ;"); 
				}
			} else model.isNumeric = false;
		}
		if (errorText.isEmpty()) {
			exam(24,"�������� ����� Initial");
			if (errorText.isEmpty()) exam(2, "����� - ���������� ����");
			if (errorText.isEmpty()){
				if (StringWork.isState(valuePrev)) {
					model.init = valuePrev; 
					exam(13,"������ ;"); 
				} else errorText = "������ ����������� ����� - @STT. ���� \"" + valuePrev + "\" �� ��������� !";
			}
		}
		if (errorText.isEmpty()) {
			exam(25,"�������� ����� Final");
			if (errorText.isEmpty()) exam(2, "����� - ��������� ����");
			if (errorText.isEmpty()){
				if (StringWork.isState(valuePrev)) {
					model.fin = valuePrev; 
					exam(13,"������ ;"); 
				} else errorText = "������ ���������� ����� - @STT. ���� \"" + valuePrev + "\" �� ��������� !";
			}
		}
		
		while ((errorText.isEmpty()) && (lex != 23)){
			idSt++;
			st = state(model,idSt);
			if (st != null){
				//System.out.println(st.show("_" + model.main + model.add + model.no));
				model.program.add(st);
			}
			//get();
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "������������� - ��� ������");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "�������� ��� " + valuePrev  + " �� ������� � ������ ������ " + model.name + "!";
				}
			} else errorText = "��������� �������� ����� end !";
		}	
		if (errorText.isEmpty()) 
			if (lex != 10) errorText = "�� �������� ���� ����� ";
		if (!errorText.isEmpty()) model = null; 
		//if (model != null) model.show();
		return model;		
	}	

	private State state(Machine model, int id) {
		String inSym = "_" + model.main + model.add + model.no;
		State st = null;
		String nameSt = "";
		ArrayList <String> going = new ArrayList();
		String txComm = "";
		String inCh = "";
		String go = "";
		String[] goingA = new String[100];
		for(int i = 0; i < 100; i++)  goingA[i] = "";  
		exam(2, "����� - ��'� �����");
		if (errorText.isEmpty()) {
			nameSt = valuePrev ;
			if (!StringWork.isState(nameSt)) errorText = "������ ����� - @TT. ���� \"" + nameSt + "\" �� ��������� !";
			while ((errorText.isEmpty()) && (lex == 15)){
				get();
				exam(2, "����� - ������� ������ ��������");
				if (errorText.isEmpty()) {
					inCh = valuePrev;
					if(inCh.length() != 1) errorText = "����� - ������� ������ �������� " + inCh  + " ������� ���������� � ������ ������� !";
					else exam(14,"������� ->");
				}
				if (errorText.isEmpty()) {
					exam(2, "����� - �������");
					if (errorText.isEmpty()) {
						go = valuePrev;
						if ((!go.isEmpty()) && (!StringWork.isMove(go))) errorText = "������ �������� - @TTSM. ������� \"" + go + "\" �� ��������� !";
						// add new going inCh -> go !!!!!!!!
						int pos = inSym.indexOf(inCh.charAt(0));
						if (pos < 0) {
							pos = inSym.length(); model.no = model.no + inCh; inSym = inSym + inCh;
						} 
						if(goingA[pos].isEmpty()) goingA[pos] = go;
						else errorText = "��� ����� " + nameSt + " � �������� ������ �������� " + inCh  + " ������� ����� ������ ����������� �������� !";
					}
				}
			}
			if (errorText.isEmpty()) exam (13,"������ ;");
		}
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			// forming State st !!!!!!
			for(int i = 0; i < inSym.length(); i++) going.add(goingA[i]) ;  
			st = new State(nameSt,id, going, txComm);
		} 
		return st;
	}
	
	
	
	
	
	
	
	private void exam(int lx, String what) {
		if (lex == lx) {
			valuePrev = valueLex;
			get();
		}
		else errorText = "��������� " + what + " !";
	}
	//=========================== Lexical =====================
	private void getChar() {
		// ������� �������� ������, ��� ����������� -- ����� ����� !!! 
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
		// ���������� ������� � ������ ����� .....
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
					case "Algorithm": if(type.equals("Algorithm")) lex = 20; break;
					case "Alphabet": lex = 21; break;
					case "Numerical": lex = 22; break;
					case "end": lex = 23; break;
					case "Machine": if(type.equals("Machine"))lex = 20; break;
					case "Initial": if(type.equals("Machine"))lex = 24; break;
					case "Final": if(type.equals("Machine"))lex = 25; break;
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
					case ':': if(type.equals("Machine")) lex = 15; getChar(); break;
					//case '\n':	
					default: getChar();	
				}
			}
			
		} else lex = 10;  // eof !!!
	}
	
	
}
