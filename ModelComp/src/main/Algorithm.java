package main;

import java.util.*;

import file.*;
import db.*;

public class Algorithm extends Model {
	public String main = "|#";
	public String add = "";
	public boolean isNumeric = true;
	public int rank = 1;
	
	public Algorithm(int id, String name) {
		super(id,name);
	}

	//-----------------------------------------------
	public String getMain() {return main;}
	public String getAdd() {return add;}
	public boolean getIsNumeric() {return isNumeric;}
	public int getRank() {return rank;}
	public String getAllChar() {return  main + add;}
	public String getType() {return "Algorithm";}
	public void setMain(String main) {this.main = main; }
	public void setAdd(String add) {this.add = add; }
	public void setIsNumeric(boolean isNumeric) {this.isNumeric = isNumeric; }
	public void setRank(int rank){this.rank = rank;}
	//----------------------------------------------
	
	//-----work DB ------- 
	public void dbDelete() {
		 DbAccess.getDbAlgorithm().deleteAlgorithm(this);
	}
	public int dbNewAs() { 
		return DbAccess.getDbAlgorithm().newAlgorithmAs(this);
	}
	
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Rule rule;
		char[] typeInfo = {'I','S','S','B','S','I'}; 
		//sql = "select id, sLeft, sRigth, isEnd, txComm, idModel " +
    	//			" from mRule where idModel = " + idModel;
    	//System.out.println("model = " + model + "   " + sql);
        //db.s.execute(sql);
        //ResultSet rs = db.s.getResultSet();
		// while((rs!=null) && (rs.next())) {
        	// тут будемо зберігати комірки одного рядка
		//System.out.println("getDataSource : " + name + " " + idModel + " " +  program.size() );
		for (int i = 0; i < program.size(); i++){
			row = new ArrayList();
			rule = (Rule)program.get(i);
			row.add(i+1);
			row.add(rule.getsLeft());
			row.add(rule.getsRigth());
			row.add(rule.getisEnd());
			row.add(rule.gettxComm());
			row.add(idModel);
			data.add(row);
        } 
        return data;
	}
	
	public String[] iswfModel(){
		String noLeft;
		String noRigth;
		String allNoAlfa = "";
		String rules = "";
		Rule rule;
		for(int i = 0; i < program.size(); i++) {
			rule = (Rule)program.get(i);
			noLeft = StringWork.isAlfa(main+add,rule.getsLeft());
        	noRigth = StringWork.isAlfa(main+add,rule.getsRigth());
        	if(!noLeft.isEmpty()) noRigth = StringWork.unionAlfa(noLeft, noRigth);
        	if (!noRigth.isEmpty()) {
        		rules = rules + "," + (i+1);
        		if (allNoAlfa.isEmpty()) allNoAlfa = noRigth;
        		else allNoAlfa = StringWork.unionAlfa(allNoAlfa,noRigth);
        	}
		}
		if (!rules.isEmpty()) 
			return new String[] {"В підстановках " + rules.substring(1),
								" використовуються символи " + allNoAlfa,
								" що не входять в об\"єднаний алфавіт " + main+add + " !"};
		else return null;
	}
	
	public int findFirst(String str) {
		// знаходить першу підстановку, ліва частина якої водить в рядок str
		int ns = 0;
		int i = 1;
		Rule r;
		while ((ns == 0) && (i <= program.size())){
			r = (Rule)program.get(i-1);
			if (StringWork.findFirst(str, r.getsLeft())> 0) ns = i; else i++;
		}
		return ns;
	}
	
	
	public ArrayList eval(String str, int nodef) {
		// застосовує алгоритм до слова str не більше ніж (nodef+1) раз
		ArrayList sl = new ArrayList();
		int step = 0;
		boolean go = true;
		String next = str;
		int np;
		Rule rule;
		int pos;
		while (go && (step <= nodef)) {
			np = findFirst(next);
			if (np > 0) {
				rule = (Rule)program.get(np-1);
				if (rule.getsLeft().isEmpty()) pos = 0; 
				else pos = StringWork.findFirst(next, rule.getsLeft());
				next = StringWork.substitution(next, rule.getsRigth(), pos, rule.getsLeft().length());
				sl.add(new Substitution(np,pos,next));
				if (rule.getisEnd()) go = false; else step++;
			} else go = false;
		}
		return sl;
	}
	
	public String takeResult(ArrayList sl, int nodef){
		String text = "";
		text = ((Substitution)(sl.get(sl.size()- 1))).str;
		if (isNumeric) text = StringWork.transNumeric(text);
		if (sl.size() == nodef + 1) text = "Невизначено";
		return text;
	}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Substitution sb;
		if (sl != null) {
			for(int i = 0; i < sl.size(); i++ ){
				sb = (Substitution)sl.get(i);
				row = new ArrayList();
				row.add(i+1);
				row.add(sb.rule);
				if (internal) row.add(extractPrev( sb));
				else row.add(StringWork.transNumeric(extractPrev( sb)));
				if (internal) row.add(extract(sb));
				else row.add(StringWork.transNumeric(extract( sb)));
				data.add(row);
			}
		}
	    return data;
	}
	
	
	public String extract(Substitution sub) {
		// виділяє в результаті підстановки sub, що підставлено *..*! 
		String res = sub.str;
		int pos = sub.pos;
		Rule r = (Rule)program.get(sub.rule-1);
		int l = r.getsRigth().length();
		if (pos > 0) res = res.substring(0,pos-1) + "*" + res.substring(pos-1,pos-1+l) + "*" + res.substring(pos-1+l) ;
		else res = "*" + res.substring(0,l) + "*" + res.substring(l);
		return res;
	}
	
	public String extractPrev(Substitution sub) {
		// виділяє в початковому рядку підстановки sub, що замінено *..*! 
		String res = sub.str;
		int pos = sub.pos;
		Rule r = (Rule)program.get(sub.rule-1);
		int l = r.getsRigth().length();
		String in = r.getsLeft();
		if (pos > 0) res = res.substring(0,pos-1) + "*" + r.getsLeft() + "*" + res.substring(pos-1+l) ;
		else res = "*" + r.getsLeft() + "*" + res.substring(l);
		return res;
	}
	
	public String output(String name, OutputText out) {
		String res = "";
		String wr;
		Rule r;
		if(out.open(name)) {
			System.out.println("File " + name + " is open..");
			if (!descr.isEmpty()) out.output("'" + descr);
			out.output("Algorithm " + this.name);
			wr = " Alphabet \"" + main + "\", \"" + add + "\";";
			if (isNumeric) wr = wr + " Numerical " + rank + ";";
			out.output(wr); 
			for (int i = 0; i < program.size(); i++){
				r = (Rule)program.get(i);
				wr = r.output();
				if (!(r.txComm.isEmpty())) wr = wr + " '" + r.txComm;
			    out.output(wr);
			}
			out.output("end " + this.name);
			out.close();
			System.out.println("File " + name + " is close.."); 
		} else res = "Not open output file " + name + "!"; 
		return res;
	}	
}
