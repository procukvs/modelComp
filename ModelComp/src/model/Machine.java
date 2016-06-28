package model;

import java.util.*;

import db.DbAccess;
import file.OutputText;
import main.StringWork;
public class Machine extends Model {
	public String main = "|#";
	public String add = "";
	public String no = "";   // Символи не з main+add для яких ЩЕ є визначені переходи 
	public boolean isNumeric = true;
	public int rank = 1;
	public String init = "@a0";
	public String fin = "@zz";
			
	public Machine(int id, String name) {
		super(id,name);
	}
	
	//-----------------------------------------------
	public String getMain() {return main ;}
	public String getAdd() {return add;}
	public String getNo() {return no;}
	public boolean getIsNumeric() {return isNumeric;}
	public int getRank() {return rank;}
	public String getInit() {return init;}
	public String getFin() {return fin;}
	public String getAllChar() {return "_" + main + add + no;}
	public String getType() {return "Machine";}
	public void setMain(String main) {this.main = main; }
	public void setAdd(String add) {this.add = add; }
	public void setIsNumeric(boolean isNumeric) {this.isNumeric = isNumeric; }
	public void setRank(int rank){this.rank = rank;}
	public void setInit(String init) {this.init = init; }
	public void setFin(String fin) {this.fin = fin; }
	//----------------------------------------------
	//-----work DB ------- 
	/*
	public boolean dbDelete() {
		 return DbAccess.getDbMachine().deleteMachine(this);
	}
	public int dbNewAs() { 
		return DbAccess.getDbMachine().newMachineAs(this);
	}
	*/
	public void dbRenameState(String in,String out) {
		 DbAccess.getDbMachine().renameState(this, in, out);
	}
	public String dbInsertModel(String nmInsert) {
		return DbAccess.getDbMachine().insertMachine(this, nmInsert);
	}
		
	public String[] iswfModel(){
		String allNoAlfa = "";  // символи в переходах не із обєднаного алфавіту
		//String states = "";		
		// стани, що використовують символи не із обєднаного алфавіту
		SortedSet <String> states = new TreeSet <String>();
		ArrayList <String> badMoves = new ArrayList();  // невірні переходи. 
		String badMove = "";
		String [] res = null;
		State state;
		String move;
		String noChar = "";
		String allCh = "_" + main + add + no;
		String goodCh = "_" + main + add;
		int i;
		//System.out.println(name + ".." + id + ".."+ program.size());
		for(i = 0; i < program.size(); i++) {
			state = (State)program.get(i);
			badMove = "";
			for(int j = 0; j < state.getGoing().size(); j++){
				String c = allCh.substring(j,j+1) ; 
				//System.out.println("iswfModel j= " +j + " c =" + c + ".." + state.getGoing().size()); 
				move = state.getGoing().get(j);  // c -> move
				if(!move.isEmpty()){
					if (StringWork.isMove(move)) {   
						noChar = StringWork.isAlfa(goodCh,c + move.substring(3,4));
						//System.out.println("iswfModel goodCh = " +goodCh + " move = " + move + " c =" + move.substring(3,4) + ".." + noChar); 
						if (!noChar.isEmpty()) {
							//states = states + "," + state.getState();
							states.add(state.getState());
							if (allNoAlfa.isEmpty()) allNoAlfa = noChar;
							else allNoAlfa = StringWork.unionAlfa(allNoAlfa,noChar);
						}	
					} else badMove = badMove + ", '" + c + "' -> \"" + move + "\"";
				}	
			}
			if (!badMove.isEmpty())	badMoves.add("\"" + state.getState() + "\" : [" + badMove.substring(1) + "]");
			
		}
		int lb = badMoves.size();
		int r = lb;
		if ((r>0)  || (!states.isEmpty())) {
			if (r>0) { /*if(r > 3) r = 3;*/ r++;  }
			if (!states.isEmpty()) r = r+3;
			res = new String[r]; i = 0;
			if (lb > 0){
				res[0] = "Формат переходу - @TTSM.       " + lb + " станів мають невірні переходи";
				while((i < lb)/*&& (i < 3)*/){
					res[i+1] = badMoves.get(i);
					i++;
				}
				//if (lb > 3) res[i] = "..";
			}
			//System.out.println("iswfModel lb=" +lb + " states.isEmpty()=" + states.isEmpty() + " r=" + r + " res.length =" + res.length  + " i =" + i);
			if(!states.isEmpty()) {
				boolean first = true;
				if(states.size()>1) res[i] = "В станах "; else res[i] = "В станi ";
				for(String s:states){
				    if (first) {res[i] = res[i] + s; first = false;}
				    else res[i] = res[i] + ", " + s; 
				}
				//res[i] = "В станах " + states.substring(1);i++;
				res[++i] = " використовуються символи " + allNoAlfa; //i++;
				res[++i] = " що не входять в об\"єднаний алфавіт _" + main+add + " !";
			}
		}
		return res;
	}
	
	// перевіряє, що в командах моделі використовуються лише символи обєднаного алфавіту 
	public String iswfModelAlfa(String alfa){
		String allNoAlfa = "";  // символи в переходах не із обєднаного алфавіту
		State state;
		String move;
		String res = "", noChar = "";
		String allCh = "_" + alfa + no;
		String goodCh = "_" + alfa;
		for(int i = 0; i < program.size(); i++) {
			state = (State)program.get(i);
			for(int j = 0; j < state.getGoing().size(); j++){
				String c = allCh.substring(j,j+1) ; 
				//System.out.println("iswfModel j= " +j + " c =" + c + ".." + state.getGoing().size()); 
				move = state.getGoing().get(j);  // c -> move
				if(!move.isEmpty()){
					if (StringWork.isMove(move)) {    //iswfMove(move)
						noChar = StringWork.isAlfa(goodCh,c + move.substring(3,4));
						//System.out.println("iswfModel goodCh = " +goodCh + " move = " + move + " c =" + move.substring(3,4) + ".." + noChar); 
						if (!noChar.isEmpty()) {
							if (allNoAlfa.isEmpty()) allNoAlfa = noChar;
							else allNoAlfa = StringWork.unionAlfa(allNoAlfa,noChar);
						}	
					} 
				}	
			}
		}
		if (!allNoAlfa.isEmpty()) res = "Cимволи " + allNoAlfa + " не входять в об\"єднаний алфавіт _" + alfa ;
		return res;	
	}
/*
	public ArrayList getDataSource() {
		ArrayList data = new ArrayList();
		ArrayList row;
		State st;
		String allCh = "_" + main + add + no;
		//char[] typeInfo = {'S','S',...,'S','I','I'}; 
		//System.out.println("getDataSource : " + name + " " + idModel + " " +  program.size() );
		if (program != null) {
			for (int i = 0; i < program.size(); i++){
				row = new ArrayList();
				st = (State)program.get(i);
				row.add(st.getState());
				for(int j=0; j < allCh.length(); j++){
					row.add(st.getGoing().get(j));
				}
				row.add(st.gettxComm());
				row.add(st.getId());
				row.add(id);
				data.add(row);
			} 
		}
        return data;
	}
*/	
	// перевіряє чи є в програмі стан з іменем state
	public boolean isState(String state){
		boolean is = false;
		if ((program != null) && (program.size() > 0)) {
			int i = 0;
			while ((i < program.size()) && !is) {
				if(state.equals(((State)program.get(i)).getState())) is = true;
				i++;
			}
		}
		return is;
	}
	
	public String testMove(String move) {
		String text = "";
		if ((!move.isEmpty()) && (!StringWork.isMove(move))) text = "Перехід "  + move + 
				" - некоректний! Формат переходу @TTSM, @TT-стан,  S-символ об'єднаного алфавиту або '_', M - '<' або '.' або '>'";		
		return text;		
	}
	
	public String testState(String st){
		String text = "";
		if (!StringWork.isState(st)) 
			text = "Некоректний формат стану " + st  + " ! Назва стану має вид - @AS, A - буква, S - буква або цифра." ;		
		if(isState(st)) text = "Стан " + st + " вже є в програмі"; 
		return text;		
	}
		
	// формує стан програми --- всі переходи - НЕВИЗНАЧЕНІ!
	public State emptyState(String st) {
		ArrayList <String> going = new ArrayList();
		String allCh = "_" + main + add + no;
		for( int i = 0; i < allCh.length(); i++){
			char c = allCh.charAt(i);
			going.add("");
		}
		return new State(st,findMaxNumber() + 1,going,"---");
	}
	
	// додає стан в програму --- всі переходи - НЕВИЗНАЧЕНІ!
	public void addStateGoing(String st) {
		insertCommand(emptyState(st));
	}
	
	// знаходить максимальний номер стану в програмi
	public int findMaxNumber(){
		int cnt = 0;
		int num;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ ){
				num = ((State)program.get(i)).getId();
				if(cnt < num) cnt = num;
			}	
		}
		return cnt;
	}
	
	// знаходить порядковий номер команди в програмі за іменем стану st 
	public int findCommand(String st) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(st.equals(((State)program.get(i)).getState())) cnt = i;
		}
		return cnt;
	}
	
	public ArrayList eval(String str, int nodef) {
		// застосовує машину Тьюрінга до слова str не більше ніж (nodef+1) раз
		String allCh = getAllChar();
		ArrayList cl = new ArrayList();
		int step = 0;
		boolean go;
		String tape = "";
		String st = init;
		char inCh;
		String move;
		String nextSt;
		String out;
		String moveTape;
		int pos = 0;
		int i, j;
		ArrayList going; 
		if(str.isEmpty()) tape = new String("_"); else tape = str;
		go = !st.equals(fin);
		cl.add(new Configuration(tape,pos,st));
		while (go) {
			inCh = tape.charAt(pos);
			i = findCommand(st);
			j = allCh.indexOf(inCh);
			if((i < 0) || (j < 0)) { 
				if(i < 0) st = "%s" + st; 		// undefined state !
				else st = "%g" + st + inCh;		// undefined move !
				go = false; 
			} else {
				going = ((State)program.get(findCommand(st))).getGoing(); 
				move = (String)going.get(allCh.indexOf(inCh));
				if (move.isEmpty()){
					st = "%g" + st + inCh;		// undefined move !
					go = false; 
				} else {
					st = move.substring(0,3);
					out = move.substring(3,4);
					moveTape = move.substring(4);
					if (pos == 0) tape = out + tape.substring(pos+1);
					else if (pos == tape.length()-1) tape = tape.substring(0,pos) + out;
					else tape = tape.substring(0,pos) + out + tape.substring(pos+1);
					if (moveTape.equals(">")) {
						if (pos == tape.length()-1) tape = tape + "_";
						pos++;
					}
					if (moveTape.equals("<")) {
						if (pos == 0) tape = "_" + tape;
						else pos--;
					}
					step++;
					go = !st.equals(fin);
					if (go && (step > nodef)) {
						st = "%c" + st; go = false; // undefined count !
					}
				}
				
			}
			// compress tape
			while((pos > 0) && (tape.charAt(0)=='_')) {
				tape = tape.substring(1); pos--;
			}
			while((pos < tape.length()-1) && (tape.charAt(tape.length()-1)=='_')) {
				tape = tape.substring(0,tape.length()-1); 
			}
			cl.add(new Configuration(tape,pos,st));
		}
		return cl;
	}
	
	public String takeResult(ArrayList sl, int nodef){
		Configuration con = (Configuration)(sl.get(sl.size()- 1));
		String text = con.tape;
		String finState = con.st;
		if (finState.equals(fin)){
			//String noMain = StringWork.isAlfa("_" + main, text);
			//if(noMain.length()==0){
				if (text.equals("_")) text = "";
				if (isNumeric) text = StringWork.transNumeric(text);
			//}
			//else text = "Невизначено: Не основний -" + noMain + "::" + text;   
		} else 
			switch(finState.charAt(1)){
			case 's': text = "Невизначено: Стан - " + finState.substring(2); break;
			case 'g': text = "Невизначено: Перехід - " + finState.substring(2,5) + "+'" + finState.substring(5) + "'"; break;
			case 'c': text = "Невизначено: Вичерпана кількість переходів" ; break;
			default:  text = "Невизначено" ;
			};	
		return text ;
	}
	
	public int takeCountStep(ArrayList sl, int nodef) { return sl.size();}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Configuration con;
		String conf = "";
		if (sl != null) {
			for (int i = 0; i < sl.size(); i++){
				row = new ArrayList();
				con =  (Configuration)(sl.get(i));
				row.add(i+1);
				if (con.pos==0){
					conf = "[" + con.st + "]" + con.tape;
				} else {
					conf = con.tape.substring(0,con.pos) + "[" + con.st + "]" + con.tape.substring(con.pos);
				}
				row.add(conf);
				row.add(StringWork.transNumeric(conf));
				data.add(row);
			} 
		}
        return data;
	}
	
	public String output(OutputText out) {
		String allCh = "_" + main + add + no;
		String res = "";
		String wr;
		State st;
		//if(out.open(name)) {
		//	System.out.println("File " + name + " is open..");
			if (!descr.isEmpty()) out.output("'" + descr); else out.output("'");
			out.output("Machine " + this.name);
			wr = " Alphabet \"" + main + "\", \"" + add + "\";";
			if (isNumeric) wr = wr + " Numerical " + rank + ";";
			out.output(wr);
			wr = " Initial  \"" + init + "\";  Final \"" + fin + "\";";
			out.output(wr);
			for (int i = 0; i < program.size(); i++){
				st = (State)program.get(i);
				wr = st.output(allCh);
				if (!(st.txComm.isEmpty())) wr = wr + " '" + st.txComm;
			    out.output(wr);
			}
			out.output("end " + this.name);
		//	out.close();
		//	System.out.println("File " + name + " is close.."); 
		//} else res = "Not open output file " + name + "!"; 
		return res;
	}	
		
	//змінює перехід повязаний з символом in в стані st за іменем стану st 
	public void updateCommand(String st, char in, String move){
		String allCh = "_" + main + add + no;
		//char in = mv.getIn();
		int i = findCommand(st);
		if (i == -1) addStateGoing(st);
		i = findCommand(st);
		if (i >= 0){
			int pos = allCh.indexOf(in);
			if (pos >= 0) {
				((State)(program.get(i))).getGoing().remove(pos);
				((State)(program.get(i))).getGoing().add(pos, move);
			}
		}
		
	}
	
	//змінює перехід повязаний з символом in в стані  за номером стану num в базі даних
	public void updateCommand(int num, char in, String move){
		String allCh = "_" + main + add + no;
		int i = findCommand(num);
		if (i >= 0){
			int pos = allCh.indexOf(in);
			if (pos >= 0) {
				((State)(program.get(i))).getGoing().remove(pos);
			((State)(program.get(i))).getGoing().add(pos, move);
			}
		}
	}

	// вставляє команду/змінює going в програмі машини	
	public void insertCommand(State going) {
		boolean go = true;
		int i = 0;
		int comp = -1;
		String stNew = going.getState();
		State work;
		if (program != null) {
			while ((i < program.size()) && go){
				work =(State)program.get(i);
				comp = work.getState().compareTo(stNew);
				//System.out.println( comp + " ; " +  work.getState() + " ? " + stNew);
				if (comp < 0) i++; else go = false;
			}
		}	
		System.out.println("Add going " + going.show(""));
		if ((program == null) || (program.size() == 0)) {
			//System.out.println("Add in empty list ");
			if (program == null) program = new ArrayList();
			program.add(going);
		}
		else if (comp == 0){
			// заміна існуючої команди для даного стану
			//System.out.println("Is going " + i + " " +  ((Going)program.get(i)).show());
			program.remove(i);
			program.add(i, going);
		}
		else if (comp < 0) {
			// додаємо стан, котрий більше всіх, що входять в програму  
			//System.out.println("All lest: max =  " + i + " " + 
			//			((Going)program.get(program.size()-1)).show());
			program.add(going);
		}
		else {
			//System.out.println("Next bigger: max =  " + i + " " + ((Going)program.get(i)).show());
			program.add(i, going);
		}
	}
	
	public String show() {
		String maSt = "";
		String alfa = "_" + main + add + no;
		maSt = "Machine " + name + " (" + id + ") \n";
		maSt = maSt + "  Alphabets: <" + main + "><" + add + "><" + no + ">\n";
		maSt = maSt + "  States: init: " + init + " fin : " + fin +  " \n";
		maSt = maSt + "  Program = \n";
		if (program != null) {
			for(int i = 0; i < program.size(); i++) {
				maSt = maSt + i + ": " + ((State)program.get(i)).show(alfa) + "\n"; 
			}
		} 	
		return maSt;
	}	
	
	// будує імя ще НЕ використаного стану a0..a9,aa..az,b0..b9,ba..bz,...,z0..z9,za..zz
	public String newState(){
		String name = "";
		String first = "abcdefghijklmnopqrstuvwxyz";
		String second = "0123456789abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < 26; i++)
			for (int j = 0; j < 36; j++){
				name = "@" + first.substring(i,i+1) + second.substring(j,j+1) ;
				if(!isState(name)) return name;
			}
		return "";
	}
	// будує k нових імен станів ще НЕ використаних a0..a9,aa..az,b0..b9,ba..bz,...,z0..z9,za..zz
	public String[] newArState(int k){
		String first = "0abcdefghijklmnopqrstuvwxyz";
		String second = "0123456789abcdefghijklmnopqrstuvwxyz";
		String[] res = new String[k];
		String name;
		String maxSt = "@0z";
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0;	(i < program.size()); i++ ) {
				name = ((State)program.get(i)).getState();
				if(name.compareTo(maxSt) > 0) maxSt = name;
			}
		}
		//System.out.println("newArState: k =" + k + " maxSt =" + maxSt);
		int i = first.indexOf(maxSt.substring(1,2));
		int j = second.indexOf(maxSt.substring(2)) + 1;
		if (j == 36) {i++; j = 0;} 
		for (int l = 0; l < k; l++ ){
			if (i < 27) res[l] = "@" + first.substring(i,i+1) + second.substring(j,j+1) ; else res[l] = "@00";
			j++;
			if (j == 36) {i++; j = 0;} 
		}
		//for(i = 0; i < k; i++) System.out.println("newArState: k =" + k + " res[] =" + res[i]);
		return res;
	}
		
	// ====================== insert Machine =========================
	// розширює машину вствляючи в неї ІНШУ
	public String insertMachine(String section, Machine ins){
		String text = "";
		String allThis = '_' + main + add + no;
		String allIns = '_' + ins.main + ins.add + ins.no; 
		String shIns = ins.name.substring(0,1);
		// find new Symbols
		String newChar = StringWork.isAlfa(allThis, allIns); 
		//System.out.println("Machine:insertMachine  start= " + init + " fin = " + fin);
		// find all states and new states !!
		SortedSet <String> allStateThis = allState(this);
		if (this.findCommand(fin)==(-1))allStateThis.remove(fin);
		SortedSet <String> allStateIns = allState(ins);
		Map<String,String> newState = new HashMap<String,String>();
		String ns = lastState (allStateThis);
		for(String s:allStateIns){
			ns = nextState(ns,allStateThis);
			if (ns.equals("@!!")) text = "insertMachine: вичерпано ВСІ можливі стани ";
			else newState.put(s, ns);
		}
		if (text.isEmpty()){
			// build new program newProg
			String allNew = '_' + main + add + no+ newChar;
			ArrayList <Command> prog = this.program;
			ArrayList <Command> newProg = new ArrayList <Command>();
			String tempInit = newState.get(ins.init);
			int maxId = 0;
			//System.out.println("startMachine  = " + this.show());
			//System.out.println("insMachine  = " + ins.show());
			for(int i=0; i<prog.size(); i++){
				State st = (State)prog.get(i);
				Map<Character,String> goingMap = fromState (st, allThis, tempInit);
				ArrayList<String> newGoing =  toGoing(allNew, goingMap);
				if(maxId < st.getId()) maxId = st.getId(); 
				newProg.add(new State(st.getState(), st.getId(),newGoing, st.gettxComm()));
			}
			for(int i1=0; i1<ins.program.size(); i1++){
				State st1 = (State)ins.program.get(i1);
				Map<Character,String> goingMap1 = fromState (st1, allIns, newState);
				ArrayList<String> newGoing1 =  toGoing(allNew, goingMap1);
				String stN = newState.get(st1.getState());
				String newComm = (i1==0?ins.name:shIns) + " " + st1.gettxComm();
				newProg.add(new State(stN, ++maxId,newGoing1, newComm)); //st1.gettxComm()));
			}
			// modify Machine
			no = no + newChar;
			fin = newState.get(ins.fin);
			this.program = newProg;
			//System.out.println("allStateThis = " + allStateThis.toString()); 
			//System.out.println("allStateIns= " + allStateIns.toString());
			//System.out.println("newState = " + newState.toString());
			//System.out.println("newMachine  = " + this.show());
			//text = "New staTES !!";
		}
		//text = "Insert into " + this.name + " macnine " + ins.name + " newChar = " + newChar;
		if (text.isEmpty()) DbAccess.getDbMachine().saveMachine(this, section);
		return text;
	}
	
	private SortedSet <String> allState (Machine mach){
		SortedSet <String> allSt = new TreeSet <String>();
		String allCh = "_" + mach.main + mach.add + mach.no;
		for(int i = 0;	(i < mach.program.size()); i++ ) {
			State state = (State)mach.program.get(i);
			String name = ((State)mach.program.get(i)).getState();
			if (!allSt.contains(name)) allSt.add(name); 
		}
		if((mach.init!=null) && (!allSt.contains(mach.init))) allSt.add(mach.init); 
		if((mach.fin!=null) && (!allSt.contains(mach.fin))) allSt.add(mach.fin); 
		//isFin = allSt.contains(fin);
		for(int i = 0;	(i < mach.program.size()); i++ ) {
			State state = (State)mach.program.get(i);
			for(int j = 0; j < state.getGoing().size(); j++){
				String move = state.getGoing().get(j);  
				if(!move.isEmpty()){
					String stgo = move.substring(0,3);
					//if(!stgo.equals(fin))
					//  if (!allSt.contains(stgo)) 
					allSt.add(stgo); 
				}
			}	
			
		}
		return allSt;
	}

	// шукає "останній" використаний стан, ВСІ нові будуть за ним підряд!! 
	private String lastState(SortedSet <String> allState){
		String res = "@0a";
		for(String s:allState){
			if(!s.substring(0,2).equals("@z")){
				if ((res.charAt(1)  < s.charAt(1)) || (res.charAt(1) == s.charAt(1)) && (res.charAt(2) < s.charAt(2))) res = s;
			}
		}
		return res;
	}
	// будує слідуючий за st новий стан (ЩО не ВХОДИТЬ В allState), "@zz"- НЕМАЄ нових станів 
	private String nextState(String st, SortedSet <String> allState){
		String res = "";	
		do{
			st = nextState(st);
			if (!st.isEmpty())
		       if (!allState.contains(st)) res = st;
		} while((res.isEmpty() && !st.isEmpty()));
		return res;
	}
	
	// будує слідуючий за st новий стан, "@!!"- НЕМАЄ нових станів 
	private String nextState(String st){
		String res = "@zz";
		String first = "0abcdefghijklmnopqrstuvwxyz";
		String second = "0123456789abcdefghijklmnopqrstuvwxyz";	
		int i = first.indexOf(st.substring(1,2));
		int j = second.indexOf(st.substring(2)) + 1;
		if (j == 36) {i++; j = 0;} 
		if (i < 27) res = "@" + first.substring(i,i+1) + second.substring(j,j+1); else res ="@!!";
		return res;
	}
	
	private Map<Character,String> fromState (State st, String all, String initNew){
		Map<Character,String> go = new HashMap<Character,String>();
		ArrayList<String> going = st.getGoing();
		for(int i=0; i<going.size(); i++){
			String goFull = going.get(i);
			if (!goFull.isEmpty()){
				String sGo = goFull.substring(0,3);
				if(sGo.equals(fin)) goFull = initNew + goFull.substring(3,5);
				Character ch = new Character(all.charAt(i));
				go.put(ch, goFull);
			}
		}
		return go;
	}
	private Map<Character,String> fromState (State st, String all, Map<String,String> newState){
		Map<Character,String> go = new HashMap<Character,String>();
		ArrayList<String> going = st.getGoing();
		for(int i=0; i<going.size(); i++){
			String goFull = going.get(i);
			if (!goFull.isEmpty()){
				String sGo = goFull.substring(0,3);
				goFull = newState.get(sGo) + goFull.substring(3,5);
				Character ch = new Character(all.charAt(i));
				go.put(ch, goFull);
			}
		}
		return go;
	}
	private ArrayList<String> toGoing(String allCh, Map<Character,String> map){
		ArrayList<String> going = new ArrayList <String>();
		for( int i = 0; i < allCh.length(); i++){
			Character c = new Character(allCh.charAt(i));
			if (map.containsKey(c))	going.add(map.get(c)); else going.add("");
		}
		return going;
	}
}
