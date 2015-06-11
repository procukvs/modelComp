package main;

import java.util.*;
import java.util.regex.Pattern;

public class Post extends Model {
	public String main = "|#";
	public String add = "";
	public boolean isNumeric = true;
	public int rank = 1;
	//================================
	private boolean isMulty = false;  // допустимі в лівій частині повторення змінних
	private static String varChar = "RSTUVWXYZ";
	
	public Post(int id, String name) {
		super(id,name);
	}

	//-----------------------------------------------
	public String getMain() {return main;}
	public String getAdd() {return add;}
	public boolean getIsNumeric() {return isNumeric;}
	public int getRank() {return rank;}
	public String getAllChar() {return  main + add;}
	public String getType() {return "Post";}
	public void setMain(String main) {this.main = main; }
	public void setAdd(String add) {this.add = add; }
	public void setIsNumeric(boolean isNumeric) {this.isNumeric = isNumeric; }
	public void setRank(int rank){this.rank = rank;}
	//----------------------------------------------
	
	//-----work DB ------- 
	public void dbDelete() {
		// DbAccess.getDbAlgorithm().deleteAlgorithm(this);
	}
	public int dbNewAs() { 
		return 0; //DbAccess.getDbAlgorithm().newAlgorithmAs(this);
	}
	
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Derive rule;
		char[] typeInfo = {'I','B', 'S','S','S','I','I'}; 
		for (int i = 0; i < program.size(); i++){
			row = new ArrayList();
			rule = (Derive)program.get(i);
			row.add(rule.getNum());
			row.add(rule.getisAxiom());
			row.add(rule.getsLeft());
			row.add(rule.getsRigth());
			row.add(rule.gettxComm());
			row.add(rule.getId());
			row.add(idModel);
			data.add(row);
        } 
        return data;
	}	
	
	
	///Pattern.matches("[0-9]*[1-9]", s);
	public ArrayList <String> iswfModelVar(){
		ArrayList <String> mes = new ArrayList <String> ();
		String badAxiom = "";
		String badRule = "";
		String dublRule = "";
		Derive rule;
		if (!noSymbolVar(main+add)) mes.add(" @ не може входити в об'єднаний алфавіт.");
		for(int i = 0; i < program.size(); i++) {
			rule = (Derive)program.get(i);
			if (rule.getisAxiom()) {
				//System.out.println(rule.getsRigth() + " " + noSymbolVar(rule.getsRigth()));
				if (!noSymbolVar(rule.getsRigth())) badAxiom = badAxiom + "," + rule.getNum();
			} else {
				boolean good = goodVars(rule.getsLeft()) && goodVars(rule.getsRigth());
				//System.out.println(StringWork.extract(rule.getsLeft(),"Var") + " < "  + rule.getsLeft() + " : " + StringWork.extract(rule.getsRigth(),"Var") + " < " + rule.getsRigth());
				//System.out.println(StringWork.extract(rule.getsLeft(),"Alfa") + " < "  + rule.getsLeft() + " : " + StringWork.extract(rule.getsLeft(),"Var") + " < " + rule.getsLeft());
				if (good) good = onlyLeftVars(rule.getsLeft(), rule.getsRigth()) ;
				if(!good) badRule = badRule + "," + rule.getNum();
				if ((!isMulty) &&  (!noDublicateVar(rule.getsLeft()))) dublRule = dublRule + "," + rule.getNum();;
			}
		}
		if (badAxiom.length() > 0) 	mes.add("Аксіоми " + badAxiom.substring(1) + " містять змінні.");
		if (badRule.length() > 0) 	mes.add("В правилах виводу " + badRule.substring(1) + " змінні використовуються некоректно.");
		if (dublRule.length() > 0) 	mes.add("В лівих частинах правил виводу " + dublRule.substring(1) + " змінні повторюються.");
		//System.out.println(mes.size());
		return mes;
	}	
	
	public String[] iswfModel(){
		ArrayList <String> mes = iswfModelVar();
		String noLeft;
		String noRigth;
		String allNoAlfa = "";
		String rules = "";
		Derive rule;
		for(int i = 0; i < program.size(); i++) {
			rule = (Derive)program.get(i);
			noLeft = StringWork.isAlfa(main+add, StringWork.extract(rule.getsLeft(),"Alfa"));
        	noRigth = StringWork.isAlfa(main+add, StringWork.extract(rule.getsRigth(),"Alfa"));
        	if(!noLeft.isEmpty()) noRigth = StringWork.unionAlfa(noLeft, noRigth);
        	if (!noRigth.isEmpty()) {
        		rules = rules + "," + (i+1);
        		if (allNoAlfa.isEmpty()) allNoAlfa = noRigth;
        		else allNoAlfa = StringWork.unionAlfa(allNoAlfa,noRigth);
        	}
		}
		if (!rules.isEmpty()) {
			mes.add("В підстановках " + rules.substring(1));
			mes.add(" використовуються символи " + allNoAlfa);
			mes.add(" що не входять в об\"єднаний алфавіт " + main+add + " !");
			
		}	
		//System.out.println(mes.size());
		//System.out.println(StringWork.transferToArray(mes)[0]);
		return StringWork.transferToArray(mes);
	}
	
	//===========================================
	public boolean noSymbolVar(String s) {
		return Pattern.matches("[^@]*", s);
	}
	public boolean goodVars(String s) {
		return Pattern.matches("[" + varChar + "]*", StringWork.extract(s,"Var"));
	}
	public boolean onlyLeftVars(String l, String r) {
		
		return Pattern.matches("[" + StringWork.extract(l,"Var") + "]*", StringWork.extract(r,"Var"));
	}
	public boolean noDublicateVar(String s) {
		String lVar = StringWork.extract(s,"Var");
		String compresVar = StringWork.isAlfa("", lVar);
		return lVar.length() == compresVar.length();
	}
	
	
}
