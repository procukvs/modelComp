package file;

import java.util.ArrayList;

import db.DbAccess;
import main.*;

public class WorkFile {
	private static WorkFile workFile = null;
	private static InputText in = new InputText();
	private static OutputText out = new OutputText();
	//=========
	private String line = "";
	private int nLine;
	private int nextChar;
	private char next = ' ';
	private int lex;
	private String valueLex, valuePrev;
	private String errorText;
	private String type = "Algorithm";
	//======================
	private ArrayList listModel;
	
	public void setType(String type) {this.type = type;}
	public String getErrorText() {return errorText;}
	public OutputText getOut() {return out;}
	public ArrayList getListModel() {return listModel;}
	
	public static WorkFile getWorkFile() {
		if(workFile == null) workFile = new WorkFile();
		return workFile;
	}
	
	public String testingInput(String name) {
		String s = "";
		String rs;
		if(in.open(name)) {
			System.out.println("WorkFile:TestingInput: File " + name + " is open.."); 
			while ((rs = in.input()) != null)
				System.out.println("WorkFile:TestingInput:.." + rs);
			in.close();
			System.out.println("WorkFile:TestingInput:File " + name + " is close.."); 
		} else s = "Not open input file " + name + "!"; 
		return s;
	}
	
	public String testingOutput(String name, String [] wr) {
		String s = "";
		//String rs;
		if(out.open(name)) {
			System.out.println("WorkFile:TestingOutput:File " + name + " is open..");
			for (int i = 0; i < wr.length; i++){
			    out.output(wr[i]);
				System.out.println("WorkFile:TestingOutput:.." + wr[i]);
			}
			out.close();
			System.out.println("WorkFile:TestingOutput:File " + name + " is close.."); 
		} else s = "Not open output file " + name + "!"; 
		return s;
	}
	
	//public String outputModel( Model model){
	//	return model.output(out); 
	//}
	public String outputAlgorithm(String name, Algorithm model) {
		String res = "";
		String wr;
		Rule r;
		if(out.open(name)) {
			System.out.println("WorkFile:outputAlgorithm:File " + name + " is open..");
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
			System.out.println("WorkFile:outputAlgorithm:File " + name + " is close.."); 
		} else res = "Not open output file " + name + "!"; 
		return res;
	}
	
	
	public boolean inputListModel(DbAccess db, String name){
		boolean bres = true;
		boolean go = true;
		listModel = new ArrayList ();
		ArrayList row = null;
		Model model = null;
		String txComm = "";
		errorText = "";
		if(in.open(name)) {
			type = "Model";
		    line = ""; nLine = 0; nextChar = 1;
		    getChar(); get();
		    while((errorText.isEmpty()) && (lex != 10)){
		   	  if (lex == 4) {txComm = valueLex; get();} 
		   	 //  System.out.println("0: type = "	+ type + " errorText = " + errorText + "!" + " lex= " + lex);
			  if (lex == 1) {
			   	type = valueLex; lex = 20;
			   	switch (type){
			   	case "Computer": model = computer(txComm); break;
			   	case "Algorithm": model = algorithm(txComm); break;
			   	case "Machine" : model = machine(txComm); break;
			   	case "System": model = post(txComm); break;
			   	case "Recursive": model = recursive(txComm); break;
				case "Calculus": model = calculus(txComm); break;
			   	default: errorText = "Очікується тип моделі Algorithm/Machine/System/Recursive/Computer/Calculus !";	
			   	}
			  } else errorText = "Очікується тип моделі Algorithm/Machine/System/Recursive/Computer/Calculus !";
			  
			  //System.out.println("1: type = "	+ type + " errorText = " + errorText + "!");
			  if (errorText.isEmpty()) {
				 if(model != null){
					 String nameIn = model.name;
					 String type1 = ((type.equals("System"))?"Post":type);  
					 int idModel = db.addModel(type1, model);
					 
					 // System.out.println("File " + name + " model " + nameIn + " type " + type1 + " id " + idModel); 
					 if (idModel > 0) {
						row = new ArrayList();
						row.add(type1);
						row.add(db.getModelName(type1,idModel)); //??????????????????????????
						row.add(model.descr);
						row.add(model.getIsNumeric());
						row.add(model.getRank());
						row.add(idModel);
						listModel.add(row);
						//errorText = Model.title(type, 8) + " " + model.name + " з файлу " + name + "  введено!";
						type = "Model";
					}
					else errorText = Model.title(type1, 8)+ " " + nameIn + " з файлу " + name + "  введено, але не збережено в базі даних !";
				 } else errorText = "Дивна ситуація (тип " + type + "): Не введена модель і немає повідомлення про помилку !!";
			  }
			  
			  //System.out.println("2: type = "	+ type + " lex = " + lex);
		    }
			if (errorText.isEmpty()) 
				if (lex != 10) errorText = "Не знайдено кінця файлу ";
			if (errorText.isEmpty()) errorText = "Введено " + listModel.size() + " моделей з файла " + name + ".";
			else errorText = "." + nLine + ": " + errorText;
		}  else {
			bres = false; errorText = "Not open input file " + name + "!"; 
		}
		return bres;
	}
	
	public Model inputModel(String name) {
		Model model = null;
		String txComm = "";
		errorText = "";
		if(in.open(name)) {
			//System.out.println("File " + name + " is open.."); 
			type = "Model";
		    line = ""; nLine = 0; nextChar = 1;
		    getChar(); get();
		    // можливо обробляємо коментар
		    if (lex == 4) {txComm = valueLex; get();} 
		    if (lex == 1) {
		    	type = valueLex; lex = 20;
		    	switch (type){
		    	case "Computer": model = computer(txComm); break;
		    	case "Algorithm": model = algorithm(txComm); break;
		    	case "Machine" : model = machine(txComm); break;
		    	case "System": model = post(txComm); break;
		    	case "Recursive": model = recursive(txComm); break;
		    	case "Calculus": model = calculus(txComm); break;
		    	default: errorText = "Очікується тип моделі Algorithm/Machine/System/Recursive/Computer/Calculus !";	
		    	}
		    } else errorText = "Очікується тип моделі Algorithm/Machine/System/Recursive/Computer/Calculus !";
		    
			if (errorText.isEmpty()) 
				if (lex != 10) errorText = "Не знайдено кінця файлу ";
			if (!errorText.isEmpty()) model = null; 
			
		  	in.close();
			//System.out.println("File " + name + " is close.."); 
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
		int id = 0;
		ArrayList rules = new ArrayList();
		//if (lex == 4) {txComm = valueLex; get();} 
		//System.out.println("init algorithm: lex =" + lex + " valueLex ="+ valueLex);
		exam(20,"службове слово Algorithm");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя алгоритму");
		if (errorText.isEmpty()) {
			model = new Algorithm(0,valuePrev);
			model.descr = txComm;
			model.program = rules;
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
			id++;
			r = rule(id);
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
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;		
	}
	
	private Rule rule(int id) {
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
			return new Rule(id, sLeft,sRigth,isEnd, txComm, id);
		} else return null;
	}
	
	private Machine machine (String txComm) {
		Machine model = null;
		State st;
		int idSt =0;
		ArrayList states = new ArrayList();
		exam(20,"службове слово Machine");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя машини");
		if (errorText.isEmpty()) {
			model = new Machine(0,valuePrev);
			model.no = "";
			model.descr = txComm;
			model.program = states;
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
		if (errorText.isEmpty()) {
			exam(24,"службове слово Initial");
			if (errorText.isEmpty()) exam(2, "рядок - початковий стан");
			if (errorText.isEmpty()){
				if (StringWork.isState(valuePrev)) {
					model.init = valuePrev; 
					exam(13,"символ ;"); 
				} else errorText = "Формат початкового стану - @STT. Стан \"" + valuePrev + "\" не коректний !";
			}
		}
		if (errorText.isEmpty()) {
			exam(25,"службове слово Final");
			if (errorText.isEmpty()) exam(2, "рядок - заключний стан");
			if (errorText.isEmpty()){
				if (StringWork.isState(valuePrev)) {
					model.fin = valuePrev; 
					exam(13,"символ ;"); 
				} else errorText = "Формат заключного стану - @STT. Стан \"" + valuePrev + "\" не коректний !";
			}
		}
		
		while ((errorText.isEmpty()) && (lex != 23)){
			idSt++;
			st = state(model,idSt);
			if (st != null){
				//System.out.println(",,,,,<<<<");
				//System.out.println(st.show("_" + model.main + model.add + model.no));
				model.program.add(st);
			}
			//get();
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя машини");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем машини " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
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
		exam(2, "рядок - ім'я стану");
		if (errorText.isEmpty()) {
			nameSt = valuePrev ;
			if (!StringWork.isState(nameSt)) errorText = "Формат стану - @TT. Стан \"" + nameSt + "\" не коректний !";
			while ((errorText.isEmpty()) && (lex == 15)){
				get();
				exam(2, "рядок - вхідний символ переходу");
				if (errorText.isEmpty()) {
					inCh = valuePrev;
					if(inCh.length() != 1) errorText = "Рядок - вхідний символ переходу " + inCh  + " повинен складатися з одного символа !";
					else exam(14,"символи ->");
				}
				if (errorText.isEmpty()) {
					exam(2, "рядок - перехід");
					if (errorText.isEmpty()) {
						go = valuePrev;
						if ((!go.isEmpty()) && (!StringWork.isMove(go))) errorText = "Формат переходу - @TTSM. Перехід \"" + go + "\" не коректний !";
						// add new going inCh -> go !!!!!!!!
						int pos = inSym.indexOf(inCh.charAt(0));
						if (pos < 0) {
							pos = inSym.length(); model.no = model.no + inCh; inSym = inSym + inCh;
						} 
						if(goingA[pos].isEmpty()) goingA[pos] = go;
						else errorText = "Для стану " + nameSt + " і вхідного символ переходу " + inCh  + " вказано більше одного визначеного переходу !";
					}
				}
			}
			if (errorText.isEmpty()) exam (13,"символ ;");
		}
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			// forming State st !!!!!!
			for(int i = 0; i < inSym.length(); i++) going.add(goingA[i]) ;  
			st = new State(nameSt,id, going, txComm);
		} 
		return st;
	}
	
	private Post post (String txComm) {
		Post model = null;
		Derive r;
		int id = 0;
		ArrayList rules = new ArrayList();
		//if (lex == 4) {txComm = valueLex; get();} 
		//System.out.println("init algorithm: lex =" + lex + " valueLex ="+ valueLex);
		exam(20,"службове слово System");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя системи");
		if (errorText.isEmpty()) {
			model = new Post(0,valuePrev);
			model.descr = txComm;
			model.program = rules;
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
			id++;
			r = derive(id);
			if (r != null) model.program.add(r);
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя системи");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем системи " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;		
	}	
	
	private Derive derive(int id) {
		String sLeft = "", sRigth = "";
		String txComm = "";
		boolean isAxiom = true;
		sRigth = valueLex;
		exam(2, "рядок - аксіома/ліва частина правила виводу");
		if (errorText.isEmpty()) {
			if(lex == 14){
				get();
				sLeft = sRigth; isAxiom = false;
				sRigth = valueLex;
				exam(2, "рядок - права частина правила виводу");
			}
			if (errorText.isEmpty()) exam (13,"символ ;");
		}	
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			return new Derive(id, isAxiom, sLeft,sRigth, txComm, id);
		} else return null;
	}		
	
	private Recursive recursive(String txComm){
		Recursive model = null;
		Function f;
		int id = 0;
		ArrayList functions = new ArrayList();
		exam(20,"службове слово Recursive");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя набору функцій");
		if (errorText.isEmpty()) {
			model = new Recursive(0,valuePrev);
			model.descr = txComm;
			model.program = functions;
		}
		while ((errorText.isEmpty()) && (lex != 23)){
			//   if (lex == 4) {txComm = valueLex; get();} 
			id++;
			f = function(id);
			if (f != null) model.program.add(f);
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя набору функцій");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем набору функцій " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;
	}
	
	private Function function(int id) {
		String name = "", txBody = "";
		String txComm = "";
		int rank = 1;
		if (lex == 4) {txComm = valueLex; get();}
		name = valueLex;
		exam(1, "ідентифікатор - імя функції");
		exam(15, "символ :");
		if (errorText.isEmpty()) {
			if(lex == 3) rank = new Integer(valueLex);
			exam(3, "натуральне число - арність функції");
			if (errorText.isEmpty()) exam(16, "символ =");
		}
		if (errorText.isEmpty()){
		   while ((lex!=13) && (lex!=4) && (lex!=23) && (lex!=10)){
			 txBody = txBody + valueLex; get(); 
		   }  
		   exam (13,"символ ;");
		}
		if (errorText.isEmpty())
			return new Function(id, name, txBody, txComm);
		else return null;
	}		

	private Calculus calculus(String txComm){
		Calculus model = null;
		LambdaDecl f;
		int id = 0;
		ArrayList functions = new ArrayList();
		exam(20,"службове слово Calculus");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя набору виразів");
		if (errorText.isEmpty()) {
			model = new Calculus(0,valuePrev);
			model.descr = txComm;
			model.program = functions;
		}
		while ((errorText.isEmpty()) && (lex != 23)){
			//   if (lex == 4) {txComm = valueLex; get();} 
			id++;
			f = lambdaDecl(id);
			if (f != null) model.program.add(f);
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя набору виразів");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем набору виразів " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;
	}

	private LambdaDecl lambdaDecl(int id) {
		String name = "", txBody = "";
		String txComm = "";
		int rank = 1;
		if (lex == 4) {txComm = valueLex; get();}
		name = valueLex;
		//System.out.println("lambdaDecl: lex =" + lex + " valueLex ="+ valueLex);
		exam(1, "ідентифікатор - імя виразу");
		//System.out.println("lambdaDecl: lex =" + lex + " valueLex ="+ valueLex);
		exam(16, "символ =");
		//System.out.println("lambdaDecl: lex =" + lex + " valueLex ="+ valueLex);
		/*
		if (errorText.isEmpty()) {
			if(lex == 3) rank = new Integer(valueLex);
			exam(3, "натуральне число - арність функції");
			if (errorText.isEmpty()) exam(16, "символ =");
		}
		*/
		if (errorText.isEmpty()){
			// вводимо тіло виразу !!!! всі символи до '\n' або ';'
			//  ... в іншому випадку пропускаємо ВСІ проміжки 
			while ((next != '\n') && (next != ';')){
				valueLex = valueLex + next; getChar();
			}
			txBody = valueLex; 
			get(); 
			/*
		     while ((lex!=13) && (lex!=4) && (lex!=23) && (lex!=10)){ // ; ' end eof
			   txBody = txBody + valueLex; get(); 
		     } 
		    */  
		   exam (13,"символ ;");
		}
		if (errorText.isEmpty())
			return new LambdaDecl(id, id, name, txBody, txComm);
		else return null;
	}			
	
	private Computer computer (String txComm) {
		Computer model = null;
		Instruction r;
		int id = 0;
		ArrayList rules = new ArrayList();
		//if (lex == 4) {txComm = valueLex; get();} 
		//System.out.println("init algorithm: lex =" + lex + " valueLex ="+ valueLex);
		exam(20,"службове слово Computer");
		if (errorText.isEmpty())  exam(1, "ідентифікатор - імя машини");
		if (errorText.isEmpty()) {
			model = new Computer(0,valuePrev);
			model.descr = txComm;
			model.program = rules;
			exam(15,"символ :");
			if (errorText.isEmpty()) {
				exam(3, "число - арність машини");
				if (errorText.isEmpty()) {
					model.rank = new Integer(valuePrev);
					exam(13,"символ ;"); 
				}
			}
		}
		while ((errorText.isEmpty()) && (lex != 23)){
			id++;
			r = instruction(id);
			if (r != null) model.program.add(r);
		}
		if (errorText.isEmpty()) {
			if (lex == 23){
				get();
				exam(1, "ідентифікатор - імя машини");
				if (errorText.isEmpty()) {
					if (!(model.name.equals(valuePrev)))
						errorText = "Заключне імя " + valuePrev  + " не співпадає з іменем машини " + model.name + "!";
				}
			} else errorText = "Очікується службове слово end !";
		}	
		//if (errorText.isEmpty()) 
		//	if (lex != 10) errorText = "Не знайдено кінця файлу ";
		if (!errorText.isEmpty()) model = null; 
		return model;		
	}	
	
	private Instruction instruction(int id) {
		String cod = "Z";
		String txComm = "";
		int num = 0, reg1 = 0, reg2 = 0, next = 0;
		exam(3, "ціле - номер команди");
		if (errorText.isEmpty()) {
			num = new Integer(valuePrev);
			exam (15,"символ :");
		}
		if (errorText.isEmpty()) {
			cod = valueLex;
			switch(lex){
			case 31: case 32:
				get();
				exam (17,"символ (");
				if (errorText.isEmpty()) {
					exam(3, "ціле - регістр 1");
					if (errorText.isEmpty()) {
						reg1 = new Integer(valuePrev); exam (18,"символ )");
					}
				}
				break;
			case 33:
				get();
				exam (17,"символ (");
				if (errorText.isEmpty()) {
					exam(3, "ціле - регістр 1");
					if (errorText.isEmpty()) {
						reg1 = new Integer(valuePrev); exam (12,"символ ,");
					}
					exam(3, "ціле - регістр 2");
					if (errorText.isEmpty()) {
						reg2 = new Integer(valuePrev); exam (18,"символ )");
					}
				}
				break;
			case 34:
				get();
				exam (17,"символ (");
				if (errorText.isEmpty()) {
					exam(3, "ціле - регістр 1");
					if (errorText.isEmpty()) {
						reg1 = new Integer(valuePrev); exam (12,"символ ,");
					}
					exam(3, "ціле - регістр 2");
					if (errorText.isEmpty()) {
						reg2 = new Integer(valuePrev); exam (12,"символ ,");
					}
					exam(3, "ціле - номер наступної команди");
					if (errorText.isEmpty()) {
						next = new Integer(valuePrev); exam (18,"символ )");
					}
				}
				break;
			default: errorText = "Очікується код команди Z/S/T/J !";
			}
		}	
		if (errorText.isEmpty()) {
			if (lex == 4) {txComm = valueLex; get();}
			//System.out.println(".." + num + ".." + cod + ".." + reg1 + ".." + reg2 + ".." +next + ".." +txComm + ".." +id);
			return new Instruction(num, cod, reg1,reg2, next, txComm, id);
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
					//System.out.println("getChar:*" + nLine + "*" + line + "*");
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
					case "Algorithm": if(type.equals("Algorithm")) lex = 20; break;
					case "Alphabet": lex = 21; break;
					case "Numerical": lex = 22; break;
					case "end": lex = 23; break;
					case "Computer": if(type.equals("Computer"))lex = 20; break;
					case "Machine": if(type.equals("Machine"))lex = 20; break;
					case "Calculus": if(type.equals("Calculus"))lex = 20; break;
					case "Initial": if(type.equals("Machine"))lex = 24; break;
					case "Final": if(type.equals("Machine"))lex = 25; break;
					case "System": if(type.equals("Post"))lex = 20; break;
					case "Z": if(type.equals("Computer"))lex = 31; break;
					case "S": if(type.equals("Computer"))lex = 32; break;
					case "T": if(type.equals("Computer"))lex = 33; break;
					case "J": if(type.equals("Computer"))lex = 34; break;
				}
			} else if (StringWork.isDigit(next)) {
				lex = 3;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isDigit(next));
			} else  {
				lex = 40;   valueLex = ""+next;// undefined lexem !!!
				switch (next){
					case '.': lex = 11; getChar(); break;
					case ',': lex = 12; getChar(); break;
					case ';': lex = 13; getChar(); break;
					case '-': getChar(); 
						if (next == '>') {lex = 14; getChar();}	break;
					case ':': if(type.equals("Machine") || type.equals("Recursive") || type.equals("Computer")) lex = 15; getChar(); break;
					case '=': if(type.equals("Recursive") ||type.equals("Calculus")) lex = 16; getChar(); break;
					case '(': if(type.equals("Computer")) lex = 17; getChar(); break;
					case ')': if(type.equals("Computer")) lex = 18; getChar(); break;
					//case '\n':	
					default: getChar();	
				}
			}
			
		} else lex = 10;  // eof !!!
	}
	
	
}
