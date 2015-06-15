package main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import gui.*;
import db.*;

public class Post extends Model {
	public String main = "|#";
	public String add = "";
	public boolean isNumeric = true;
	public int rank = 1;
	//================================
	private boolean isMulty = false;  // �������� � ��� ������� ���������� ������
	private static String varChar = "RSTUVWXYZ";
	//=====================================
	private ArrayList <FullSubstitution> res = null; 		//new ArrayList <FullSubstitution>();
	private ArrayList <FullSubstitution> oneStep = null; 	//new ArrayList <FullSubstitution>();
	private Set <String> prevString = null; 				//new HashSet<String>();
	
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
	public boolean  dbDelete() {
		return DbAccess.getDbPost().deletePost(this);
	}
	public int dbNewAs() { 
		return DbAccess.getDbPost().newPostAs(this);
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
	
	// ��������� ������������ ����� ����� � �������i
	public int findMaxNumber(){
		int cnt = 0;
		int num;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ ){
				num = ((Derive)program.get(i)).getId();
				if(cnt < num) cnt = num;
			}	
		}
		return cnt;
	}	
	
	// ��������� ���������� ����� ������� � ������� �� ������� ����� num 
	public int findCommand(int num) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(num == (((Derive)program.get(i)).getId())) cnt = i;
		}
		return cnt;
	}	
	
	///Pattern.matches("[0-9]*[1-9]", s);
	public ArrayList <String> iswfModelVar(){
		ArrayList <String> mes = new ArrayList <String> ();
		String badAxiom = "";
		String badRule = "";
		String dublRule = "";
		Derive rule;
		if (!noSymbolVar(main+add)) mes.add("E: @ �� ���� ������� � ��'������� ������.");
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
		if (badAxiom.length() > 0) 	mes.add("E:������ " + badAxiom.substring(1) + " ������ �����.");
		if (badRule.length() > 0) 	mes.add("E:� �������� ������ " + badRule.substring(1) + " ����� ���������������� ����������.");
		if (dublRule.length() > 0) 	mes.add("E:� ���� �������� ������ ������ " + dublRule.substring(1) + " ����� ������������.");
		//System.out.println(mes.size());
		return mes;
	}	
	
	// ����������� �Ѳ ������� ������ �� ����� step �� ����� str
	public ArrayList <FullSubstitution> extendRules(String str, int step){
		ArrayList <FullSubstitution> res = new ArrayList <FullSubstitution>();
		Derive rule;
		for(int i = 0; i < program.size(); i++ ){
			rule = (Derive)program.get(i);
			if (!rule.getisAxiom())	res.addAll(rule.extend(str, main,step));
		}
		return res;
	}
	
	public int initialForm(){
		Derive rule;
		res = new ArrayList <FullSubstitution>();
		oneStep = new ArrayList <FullSubstitution>();
		prevString = new HashSet<String>();
		for(int i = 0; i < program.size(); i++ ){
			rule = (Derive)program.get(i);
			if (rule.getisAxiom())	{
				oneStep.add(new FullSubstitution(rule.getsRigth(), new Substitution(rule.getNum(), 0, ""),true,true));
			}
		}
		if (oneStep != null)  addOneStep(res,oneStep);
		return res.size(); 
	}
	
	public int stepForm(int step) {
		SortedSet<String> oneString = new TreeSet<String>(); 
		String st;
		for(int i = 0; i < oneStep.size(); i++){
			st = oneStep.get(i).str; 
			if (!prevString.contains(st)) oneString.add(st);
		}
		oneStep = new ArrayList <FullSubstitution>();
		Iterator <String> it = oneString.iterator();
		while(it.hasNext()){
			st =it.next();
			oneStep.addAll(extendRules(st, step));
			prevString.add(st);
		}
		if (oneStep != null)  addOneStep(res,oneStep);
		return res.size(); 
	}
	
	public int finalForm(){
		oneStep = null;
		prevString = null;
		return res.size(); 
	}
	
	public  ArrayList <FullSubstitution> eval(ShowForm showForm, int stepAll){
		String template = "HH:mm:ss";  /// "dd.MM.yyyy HH:mm:ss"
		DateFormat formatter = new SimpleDateFormat(template);
		//String text1;
		Date cur = new Date();
		String text = "���������� " + formatter.format(cur) + " : ";
		ArrayList <FullSubstitution> res = new ArrayList <FullSubstitution>();
		ArrayList <FullSubstitution> oneStep = new ArrayList <FullSubstitution>();
		Set <String> prevString = new HashSet<String>();
		SortedSet<String> oneString; 
		//ArrayList <String> oneString;  
		Derive rule;
		int step = 0;
		String st;
		for(int i = 0; i < program.size(); i++ ){
			rule = (Derive)program.get(i);
			if (rule.getisAxiom())	{
				oneStep.add(new FullSubstitution(rule.getsRigth(), new Substitution(rule.getNum(), 0, ""),true,true));
				
			}
		}
		if (oneStep != null)  addOneStep(res,oneStep);      //res.addAll(oneStep);
		System.out.println(text + " stepAll = " + stepAll + " " + oneStep.size());
		while (step < stepAll){
			step++;
			cur = new Date();
			showForm.setMessage(text + formatter.format(cur) + " ���� " + step + " .. " + res.size() + ".");
			// try{ 
			//    wait(100000);	   
			// } catch (Exception e) {
			//	System.out.println(">>> " + e.getMessage());
			// } 
			//showForm.updateUI();
		//showForm.tMessage.setVisible(false);
			//showForm.tMessage.setVisible(true);
			//show.setEnabled(false);
			System.out.println(text + formatter.format(cur) + " ���� " + step + " .. " + res.size() + ".");
			//oneString = new ArrayList <String>();
			// ��� �� ��������������� ���� ������� �����, ������ ��������
			oneString = new TreeSet<String>(); 
			for(int i = 0; i < oneStep.size(); i++){
				st = oneStep.get(i).str; 
				if (!prevString.contains(st)) oneString.add(st);
			}
			oneStep = new ArrayList <FullSubstitution>();
		//	for(int i = 0; i < oneString.size(); i++){
		//		oneStep.addAll(extendRules(oneString.get(i), step));
		//	}
			Iterator <String> it = oneString.iterator();
			while(it.hasNext()){
				st =it.next();
				oneStep.addAll(extendRules(st, step));
				prevString.add(st);
			}
			if (oneStep != null)  addOneStep(res,oneStep);
			//res.addAll(oneStep);
		}
		cur = new Date();
		//showForm.tMessage.setText(text + formatter.format(cur) + " �������� .. " + res.size() + ".");
		
		showForm.setMessage(text + formatter.format(cur) + " �������� .. " + res.size() + ".");
		System.out.println(text + formatter.format(cur) + " �������� .. " + res.size() + ".");
		return res;
	}
	
	private void addOneStep(ArrayList <FullSubstitution> res, ArrayList <FullSubstitution> oneStep) {
		FullSubstitution addFSub;
		FullSubstitution fSub;
		boolean no; 
		int j;
		for (int i = 0; i < oneStep.size(); i++){
			addFSub = oneStep.get(i); no = true; j = 0;
			String st = addFSub.str;
			while (no && (j < res.size())){
				fSub = res.get(j);
				no = !st.equals(fSub.str);
				if (!no) {
					// ��� ����� ����� ������� ������ !!! -- ����� �������� Sustitution !!
					if (fSub.any == null) fSub.any = new ArrayList <Substitution>();
					fSub.any.add(addFSub.sub);
				} else j++;
			}
			addFSub.isFst = no;
			res.add(addFSub);
		}
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
			mes.add("I:� ����������� " + rules.substring(1));
			mes.add("  ���������������� ������� " + allNoAlfa);
			mes.add("  �� �� ������� � ��\"������� ������ " + main+add + " !");
		}	
		//System.out.println(mes.size());
		//System.out.println(StringWork.transferToArray(mes)[0]);

		return StringWork.transferToArray(mes);
	}
	
	public ArrayList <String> iswfNum(String num) {
		ArrayList <String> mes = new ArrayList <String>();
		int numI = -1;
		if (StringWork.isPosNumber(num)) numI = new Integer(num);
		if((numI < 0) || (numI > program.size()+1)) 
			mes.add("E:���������� ����� ������/������� ������ ������� ���� �� ������ 1 � �� ����� " + (program.size() + 1) + ".");
		return mes;
	}
	
	public ArrayList <String> iswfLeft(String l) {
		ArrayList <String> mes =  new ArrayList <String>();
		if (!goodVars(l)) mes.add("E:� ��� ������� ������� ��������������� ������ �����.");
		if ((!isMulty) &&  (!noDublicateVar(l))) mes.add("E:� ��� ������� ������� ����� ������������.");
		String noSym = StringWork.isAlfa(main+add, StringWork.extract(l,"Alfa"));
		if (!noSym.isEmpty()) mes.add("I:������� " + noSym + " �� ������� � ��'������� ������.");
		return mes;
	}
	
	public ArrayList <String> iswfAxiom(String ax) {
		ArrayList <String> mes =  new ArrayList <String>();
		if (!noSymbolVar(ax)) mes.add("E:������ �� ���� ������ �����.");
		String noSym = StringWork.isAlfa(main+add, StringWork.extract(ax,"Alfa"));
		if (!noSym.isEmpty()) mes.add("I:������� " + noSym + " �� ������� � ��'������� ������.");
		return mes;
	}
	
	public ArrayList <String> iswfRigth(String l, String r) {
		ArrayList <String> mes = new ArrayList <String>();
		if (!goodVars(r)) mes.add("E:� ����� ������� ������� ���������������� ������ �����.");
		if (!onlyLeftVars(l, r)) mes.add("E:� ����� ������� ������� ���������������� �����, ���� ���� � ���.");
		String noSym = StringWork.isAlfa(main+add, StringWork.extract(r,"Alfa"));
		if (!noSym.isEmpty()) mes.add("I:������� " + noSym + " �� ������� � ��'������� ������.");
		return mes;
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
