package main;

import java.text.*;
import java.util.*;

import db.*;
import file.*;

public class Calculus extends Model {
	
	public Calculus(int id, String name) {
		super(id,name);
	}

	public String getType() {return "Calculus";}  
	
	public int dbNewAs() { 
		return DbAccess.getDbCalculus().newCalculusAs(this);
	}
	

	//-----work DB ------- 
	public String dbInsertModel(int where, String nmModel, String nmFunction) {
			return DbAccess.getDbCalculus().insertDeclLambda(this, where, nmModel, nmFunction);
		}
	public boolean dbDelete() {
		 return DbAccess.getDbCalculus().deleteCalculus(this);
	}

	public String output( OutputText out) {
		String res = "";
		String wr;
		LambdaDecl f;
		//if(out.open(name)) {
		//	System.out.println("File " + name + " is open..");
			if (!descr.isEmpty()) out.output("'" + descr); else out.output("'");
			out.output("Calculus " + this.name);
			for (int i = 0; i < program.size(); i++){
				f = (LambdaDecl)program.get(i);
				if (!(f.gettxComm().isEmpty())) out.output("   '" + f.gettxComm());
				wr = f.getName() + " = " +f.gettxBody(); 
				out.output("   " + wr + ";");
			}
			out.output("end " + this.name);
		//	out.close();
		//	System.out.println("File " + name + " is close.."); 
		//} else res = "Not open output file " + name + "!"; 
		return res;
	}			
	
	//знаходить імя виразу по замовчуванню : перше вільне з "base", "base00", "base01",...
	/*
	public String findName(String base){
		int us,i = 0;
		NumberFormat suf = new DecimalFormat("00"); 
		boolean isUse ;
		String name = base;
		do {
			isUse = false; us = 0;
			for(int j = 0; j < program.size(); j++){
				if(((LambdaDecl) program.get(j)).getName().equals(name)) us++;
			}
			if (us>0) name = base + suf.format(i);
			i++;
		} while (us>0);
		return name;
	}
    */
	public LambdaDecl newLambdaDecl(LambdaDecl ld){	
		//System.out.println("Calculus:newLambdaDecl " + ((ld==null)?"Null":"No null"));
	   int id = findMaxNumber();
	   int num = program.size()+1;
       if (ld != null){
		  return new LambdaDecl(id, num, this.findNameCommand(ld.getName()), ld.gettxBody(), ld.gettxComm()); 
	   } 
	   else return new LambdaDecl(id, num, this.findNameCommand("new"),"","");
    }
	
	public String[] iswfModel(){
		ArrayList <String> mes = new ArrayList<String>();
		LambdaDecl ld;
		for(int i = 0; i < program.size(); i++) {
			ld = (LambdaDecl)program.get(i);
			if (!ld.getiswf()){
				mes.add(ld.getName() + ": " + ld.geterrorText()); 
			}
		}
		return StringWork.transferToArray(mes);
	}	
	
	//перевіряє ім"я нового виразу на коректність...
	/*
	public String testName(String name){
		String st = "";
		LambdaDecl ld;
		if (StringWork.isIdentifer(name)){
			for(int i = 0; i < program.size(); i++) {
				ld = (LambdaDecl)program.get(i);
				if (ld.getName().equals(name))st = "Вираз з іменем " + name + " вже є в наборі.";
			}	
		} else st = "Імя виразу " + name + " - не ідентифікатор.";
		return st;
	}
	*/
	public ArrayList eval(String str, int max, boolean isStep) {
		// виконує обчислення виразу txt не більше ніж (nodef+1) редукцій :isStep=true => формуючи кроки !!
		ArrayList sl = new ArrayList();
		int step = 0;
		boolean go = true;
		String res = str;
		res = testingCalculus();
		if (res.isEmpty()){ 
			LamContext cnt = formContex();
			Lambda rb = analysLambda(str, cnt);
			if(rb != null) {
				return runEval(max,cnt,rb,isStep);
			}
			else {sl.add(new LamStep("Помилка у виразі: " +  errorText + " !"));
			}
		} else{
			sl.add(new LamStep(res));   
		}
			   
		return sl;
	}
	
//	=============================================================
	private String testingCalculus(){
		// testing correct calculus !
		String res = "";
		LambdaDecl l;
		int cntEr=0;
		for(int i = 0; i < program.size(); i++){
			l = (LambdaDecl) program.get(i);
			if (!l.getiswf()){
				res = res + ".." + l.getName() + ": "+ l.geterrorText();
				cntEr++;
			}
		}
		if (cntEr!=0) res = "В наборі " + cntEr + " виразів з помилками. В тому числі: " + res +""; 
		return res;
	}
	
	private LamContext formContex(){
		LamContext cnt = new LamContext();
		for(int i = 0; i < program.size(); i++){
			LambdaDecl f = (LambdaDecl) program.get(i);
			cnt.add(new LamBinding(f.getName(),f.getBody()));
		}	
		return cnt;
	}

//    --------------------------- for eval lambda -----------------------
	private Lambda getBinding(LamContext ctx, int i) {
		Lambda res = ctx.findBinding(i);
		String txt = "Null";
		if (res != null){
			txt = res.toString();;
			res = termShift(i+1,res);
		}  
		//System.out.println("getBinding " + ctx.toStringShort() + " " +  i + ": " + txt);		
		return res;
	}
	
	private Lambda termShift(int d, Lambda t){
		return walkShift(d,0,t);
	}
	private Lambda walkShift(int d, int c, Lambda t){
		Lambda tr=null;
		switch (t.getClass().getSimpleName()){
	    case "LamNmb": tr = new LamNmb(t.getName(),t.getInd()); break;
	    case "LamVar": if (t.getInd()>=c)tr = new LamVar(t.getName(),t.getInd()+d,t.getLng()+d); 
	    							else tr = new LamVar(t.getName(),t.getInd(),t.getLng()+d); break;
	    case "LamAbs": tr = new LamAbs(t.getName(), walkShift(d,c+1,t.getBody())); break;
	    case "LamApp": tr = new LamApp(walkShift(d,c,t.getBody()),walkShift(d,c,t.getArg())); break;
	    case "LamLet": tr = new LamLet(t.getName(),walkShift(d,c,t.getArg()),walkShift(d,c+1,t.getBody())); break;
	    default:
		}
		return tr;
	}
	private Lambda termSubst(int j, Lambda s, Lambda t){
		return walkSubst(j,s,0,t);
	}
	private Lambda walkSubst(int j, Lambda s, int c, Lambda t){
		Lambda tr=null;
		switch (t.getClass().getSimpleName()){
	    case "LamNmb": tr = new LamNmb(t.getName(),t.getInd()); break;
	    case "LamVar": if (t.getInd()==j+c)tr = termShift(c,s);	
	                      else tr = new LamVar(t.getName(),t.getInd(),t.getLng()); break;
	    case "LamAbs": tr = new LamAbs(t.getName(), walkSubst(j,s,c+1,t.getBody())); break;
	    case "LamApp": tr = new LamApp(walkSubst(j,s,c,t.getBody()),walkSubst(j,s,c,t.getArg())); break;
	    case "LamLet": tr = new LamLet(t.getName(),walkSubst(j,s,c,t.getArg()),walkSubst(j,s,c+1,t.getBody())); break;
	    default:
		}
		return tr;
	}
	private Lambda termSubstTop(Lambda s, Lambda t){
		return termShift((-1),termSubst(0,termShift(1,s),t));
	}
	
	public Lambda integerTerm(int n) {
		Lambda res = new LamVar("z",0,2);
		for(int i=n;i>0;i--){
			res = new LamApp(new LamVar("s",1,2),res);
		}
		res = new LamAbs("s", new LamAbs("z",res)); 
		return res;
	}
	
	private ArrayList runEval(int max, LamContext ctx, Lambda t, boolean isStep){
		ArrayList sl = new ArrayList();
		int i=0; 
		Lambda nx=t, pr; 
		LamStep tst;
		do{ pr = nx;
		   // System.out.println("...Step " + i+ " " + pr.toStringFull());
		   // System.out.println("...Step " + i+ " " + pr.toStringShort(0));
		    if (isStep){
		    	tst = evalStep(ctx,pr);
		    	if (tst!=null ){
		    		//System.out.println(tst.toString());
		    		sl.add(tst);
		    		nx = tst.getTerm()[0];
		    	} else nx = null;
		    } else 	nx = eval(ctx, pr);
			i++;
		} 
		while((i<=max) && (nx!=null) );
		if(nx!=null) sl.add(new LamStep("Виконано кроків більше ніж " + max + "."));
		else sl.add(new LamStep(""+(i-1), pr));
		return sl;
	}
	private Lambda eval(LamContext ctx, Lambda t){
		Lambda tr=null;
		Lambda temp;
		String txt;
		if(t==null) {System.out.println("Null ????? ");	return null;}
		else { 
			//System.out.println(t.getClass().getSimpleName() + ": " + t.toString());
			//System.out.println( t.toStringFull());
		}
		switch (t.getClass().getSimpleName()){
	    case "LamNmb": 	tr = integerTerm(t.getInd()); break;
	    case "LamVar":  tr = getBinding(ctx,t.getInd());  
	                        // System.out.println("    Binding: " + (tr==null?"Null":tr.toString())); 
	    				break;
	    case "LamApp":  Lambda body = t.getBody();
	    			   if (body== null) {
	    				  // System.out.println("Body-null: " + t.toString());
	    				   return null;
	    			   }
	                   if (body.getClass().getSimpleName().equals("LamAbs")){
	                	 // body = LamAbs(x t12)
	                	 tr = termSubstTop(t.getArg(), body.getBody());
	                   } else {
	                	 temp = eval(ctx,body);
	                	 if (temp == null) {
	                		 temp = eval(ctx,t.getArg());
	                		 if (temp == null) tr = null;
	                		 else tr = new LamApp(body,temp);
	                	 } else tr = new LamApp(temp,t.getArg());
	                   } break;
	    case "LamLet":  tr = termSubstTop(t.getArg(),t.getBody()); break;
	    case "LamAbs":  ctx.add(new LamBinding(t.getName(),null));
	    	           temp = eval(ctx,t.getBody());
	             	   if (temp == null) tr = null;
                	   else tr = new LamAbs(t.getName(),temp);
	             	   ctx.delete(); break;
	    default:
		}
		return tr;	
	}
	
	private LamStep evalStep(LamContext ctx, Lambda t){
		LamStep tr=null, tempStep;
		Lambda temp;
		String txt;
		if(t==null) {System.out.println("Null ????? ");	return null;}
		else { 
			        //System.out.println(t.getClass().getSimpleName() + ": " + t.toString());
			//System.out.println( t.toStringFull());
		}
		switch (t.getClass().getSimpleName()){
	    case "LamNmb": 	temp = integerTerm(t.getInd());
	    				tr = new LamStep("Nmb",t.getName(),temp,temp);  break;
	    case "LamVar":  temp = getBinding(ctx,t.getInd());
	    				if (temp==null) tr = null; else tr = new LamStep("Var",t.getName(),temp,temp);
	                     //System.out.println("    Binding: " + (tr==null?"Null":tr.toString())); 
	                     break;
	    case "LamApp": Lambda body = t.getBody();
	    			   if (body== null) {
	    				   System.out.println("Body-null: " + t.toString());
	    				   return null;
	    			   }
	                   if (body.getClass().getSimpleName().equals("LamAbs")){
	                	 // body = LamAbs(x t12)  substitution 
	                	 temp = termSubstTop(t.getArg(), body.getBody());
	                	 tr = new LamStep("App", body.getName(),temp,t.getArg(), body.getBody());
	                   } else {
	                	 tr = evalStep(ctx,body);
	                	 if (tr == null) {
	                		 tr = evalStep(ctx,t.getArg());
	                		 if (tr == null) tr = null;
	                		 else tr.modify(new LamApp(body,tr.getTerm()[0]));
	                	 } else tr.modify(new LamApp(tr.getTerm()[0],t.getArg()));
	                   }; break;
	    case "LamLet":  temp = termSubstTop(t.getArg(),t.getBody());
	    				tr = new LamStep("Let", t.getName(),temp,t.getArg(),t.getBody());
	    				break;
	    case "LamAbs":  ctx.add(new LamBinding(t.getName(),null));
	    	           tr = evalStep(ctx,t.getBody());
	             	   if (tr == null) tr = null;
                	   else tr.modify(new LamAbs(t.getName(),tr.getTerm()[0]));
	             	   ctx.delete(); break;
	    default:
		}
		return tr;	
	}
	
//	  ===================================================================
	
	public void extend(){
		LambdaDecl f;
		String st;
		Lambda rb;
		LamContext cnt = new LamContext();
		//System.out.println("extend: " + program.size());
		for(int i = 0; i < program.size(); i++){
			f = (LambdaDecl) program.get(i);
			// виконує синтаксичний аналіз і будує Lambda-вираз
			//System.out.println("extend: " + f.txBody);
			rb = analysLambda(f.txBody, cnt);
			f.setBody(rb);
			f.iswf = (rb != null);
			if(rb == null) f.errorText = "S:" + errorText; 
			
			if( rb == null) st = getErrorText(); else st = rb.toString(); 
			//System.out.println(f.name + ":" + f.txBody + " ==> " + st);
			cnt.add(new LamBinding(f.getName(),rb));
		}
	}
	
	public String fullAnalys(int num, String body){
		LamContext cnt = new LamContext();
		int mxDecl = (num==0?program.size():num-1);
		String st = "";
		Lambda rb;
		if (program.size() < mxDecl) mxDecl = program.size();
		for(int i = 0; i < mxDecl; i++){
			LambdaDecl f = (LambdaDecl) program.get(i);
			cnt.add(new LamBinding(f.getName(),f.getBody()));	
		}	
		rb = analysLambda(body, cnt);	
		if(rb == null) st = errorText; 
		return st;
	}	
		/*
		for(int i = 0; i < program.size(); i++){
			f = (Function) program.get(i);
			if(f.iswf) {
				rb = map.get(f.name);
				st = rb.setRank(f.name, map);
				f.rank = rb.rank;
				if(!st.isEmpty()){
					f.errorText = "R:" + st;
					f.iswf = false;
				}
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
					//System.out.println(f.name + ":" + f.errorText);
				}
			}
		} 
		*/
		//System.out.println("rank ...." );
		//System.out.println(toString());
	
	/*
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		LambdaDecl fun;
		RecBody rb;
		char[] typeInfo = {'I','S','B', 'S','S','I','I'}; 
		for (int i = 0; i < program.size(); i++){
			row = new ArrayList();
			fun = (LambdaDecl)program.get(i);
			row.add(fun.getNum());
			row.add(fun.getName());
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
	public ArrayList getStepSource(ArrayList sl, boolean internal) {
		ArrayList data = new ArrayList();
		ArrayList row;
		LamStep ls;
		if (sl != null) {
			for(int i = 0; i < sl.size()-1; i++ ){
				ls = (LamStep)sl.get(i);
				row = new ArrayList();
				row.add(i+1);
				row.add(ls.getWhat());
				//row.add(ls.getTerm()[1]);
				row.add(ls.takeArgs());
				row.add(ls.getTerm()[0].toStringShort(0));
				//row.add(ls.getTerm()[0].toStringFull());
				data.add(row);
			}
		} else System.out.println("Algorithm: getStepSource sl=null!!!!" );
	    return data;
	}
	
	public Lambda compressFull(Lambda t){
		return compress(formContex(),t);
	}
	private Lambda compress(LamContext ctx, Lambda t){
		Lambda res = t;
		Lambda temp;
		if ((t!=null)){
			switch (t.getClass().getSimpleName()){
			case "LamApp": 
				res = new LamApp(compress(ctx,t.getBody()),compress(ctx,t.getArg())); 
				temp = findEqTerm(res, ctx);
				if (temp!=null) res=temp; break;
			case "LamAbs": 
				temp = findEqTerm(t, ctx);
				if(temp== null){
					res = new LamAbs(t.getName(), compress(ctx, t.getBody()));
					temp = findEqTerm(res, ctx);
					if(temp!=null) {res= temp;}
				} else res = temp; break;
			case "LamLet": 
				res = new LamLet(t.getName(),compress(ctx,t.getArg()),compress(ctx,t.getBody())); 
				temp = findEqTerm(res, ctx);
				if (temp!=null) res=temp; break;
			}
		} 
	    return res;
	}
	
	public boolean isEquals(Lambda t1, Lambda t2){
		boolean res=false;
		if ((t1!=null) && (t2!=null)){
			switch (t1.getClass().getSimpleName()){
			case "LamVar": if (t2.getClass().getSimpleName().equals("LamVar")){ 
								res = (t1.getInd()==t2.getInd()); 
							} break;
			case "LamApp": if (t2.getClass().getSimpleName().equals("LamApp")){ 
								res = (isEquals(t1.getBody(),t2.getBody())) && (isEquals(t1.getArg(),t2.getArg())); 
							} break;
			case "LamAbs": if (t2.getClass().getSimpleName().equals("LamAbs")){ 
				                res = (isEquals(t1.getBody(),t2.getBody())); 
							} break;
			case "LamLet": if (t2.getClass().getSimpleName().equals("LamLet")){ 
								res = (isEquals(t1.getBody(),t2.getBody())) && (isEquals(t1.getArg(),t2.getArg())); 
							} break;				
			}
		}
		return res;
	}
	
	public Lambda findEqTerm(Lambda t, LamContext ctx ){
		Lambda res = null;
		if (!isNumber(t)){
			int i = 0; 
			int lngCtx = ctx.lngContext();
			while((i < lngCtx) && (res==null)){
				Lambda v = ctx.getLamBinding(i).getBody();
				if((v!=null) && isEquals(t,v)) 	res = new LamVar(ctx.getLamBinding(i).getName(),i,lngCtx);
				i++;
			}
		} else res = inNumber(t);
		return res;
	}
	
	public Lambda inNumber(Lambda t){
		Lambda res = null;
		if((t!=null) && t.getClass().getSimpleName().equals("LamAbs")){
			Lambda t1 = t.getBody();
			if((t1!=null) && t1.getClass().getSimpleName().equals("LamAbs")){
				int i = toInteger(t1.getBody());
				if (i>=0)  res = new LamVar(""+i,(-1),(-1));
			}	
		}
		return res;
	}
	
	private int toInteger(Lambda t){
	// повертає numb>=0 , якщо t-задає натуральне число 
	//    .. numb<0  , як що НЕ задає натуральне число	
		int res=-10;
		Lambda t1;
		if(t!=null){
			switch (t.getClass().getSimpleName()){
			case "LamVar": if(t.getInd()==0) res = 0; 
							break;
			case "LamApp": t1 = t.getBody();
				if ((t1!=null) && t1.getClass().getSimpleName().equals("LamVar"))	{
					if(t1.getInd()==1) {
						res = toInteger(t.getArg());
						if (res>= 0) res ++; else res = -10;
					}
				}
			}
		}
		return res;
	}
//=============================================================
//   ------ compress ---------
	private boolean isNumberList(Lambda t){
		Lambda t1;
		boolean res=false;
		if (t!=null){
			switch (t.getClass().getSimpleName()){
			case "LamVar": res=(t.getInd()==0); break;
			case "LamApp": t1 = t.getBody();
				if ((t1!=null) && t1.getClass().getSimpleName().equals("LamVar"))	{
					if(t1.getInd()==1) res = isNumberList(t.getArg());
				}
			}
		}
		return res;
	}
	
	private boolean isNumber(Lambda t){
		boolean res = false;
		if((t!=null) && t.getClass().getSimpleName().equals("LamAbs")){
			Lambda t1 = t.getBody();
			if((t1!=null) && t1.getClass().getSimpleName().equals("LamAbs"))
				res = isNumberList(t1.getBody());
		}
		return res;
	}
	
	
	public  Lambda analysLambda(String text, LamContext cnt){
		Lambda rb;
		errorText = "";
		textAnalys = text;	posAnalys = 0;
		getChar(); get();
		rb = term(cnt);
		if (errorText.isEmpty()){
			if (!eos) {
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
    //  lex = 1 => ідентифікатор, 2 => число,  ----> valueLex
	//  let - 3, in - 4, \ - 5, . - 6, ( - 7 ) - 8, = - 9, eos - 20. 
	private int lex;
	private String valueLex;
	private String valuePrev;
	private String errorText = "";
	
	//=============================
	private Lambda term(LamContext cnt) {
		//System.out.println("term: " + lex + " " + valueLex);
		Lambda r = null;
		if (lex == 3) r=letterm(cnt); else r=applic(cnt);  
		return r;
	}
	private Lambda letterm(LamContext cnt) {
		//System.out.println("letterm: " + lex + " " + valueLex);
		Lambda r = null;
		Lambda arg, body;
		String name;
		get(); name = valueLex;
		exam(1, "ідентифікатор");
		exam(9, "символ '='");
		if (errorText.isEmpty()) {
			arg = term(cnt);
			exam(4, "in");
			if (errorText.isEmpty()) {
				LamContext cnt1 = cnt.copy(); 
				cnt1.add(new LamBinding(name,arg));
				body = term(cnt1);
				if (errorText.isEmpty()) r = new LamLet(name,arg,body);
			}
		}
		return r;
	}
	private boolean isBeginFact (){
		return (lex==1)||(lex==2)||(lex==5)||(lex==7);
	}
	private Lambda applic(LamContext cnt) {
		//System.out.println("applic: " + lex + " " + valueLex);
		Lambda r = fact(cnt);
		Lambda f;
		while(errorText.isEmpty() && (isBeginFact())) { 
		   f = fact(cnt);
		   if (errorText.isEmpty()) r = new LamApp(r,f);
		}
		if (!errorText.isEmpty()) r=null; 
		return r;
	}
	private Lambda fact(LamContext cnt) {
		//System.out.println("fact: " + lex + " " + valueLex);
		Lambda r = null;
		switch(lex){
		case 1: int i = cnt.findIndexP(valueLex);
		        if (i>= 0){
		        	r = new LamVar(valueLex,i,cnt.lng()); get();
		        } else 	errorText = "Невідомий ідентифікатор " + valueLex;
			    break;
		case 2: r = new LamNmb(valueLex, StringWork.transToInt(valueLex)); get(); break;
		case 5: r = lambda(cnt); break;
		case 7: get(); r = term(cnt);
			    if (errorText.isEmpty()) exam(8, "символ ')'");
			    if (!errorText.isEmpty()) r=null; 
		        break;
		default: errorText = "Помилка в виразі";
		}
		return r;
	}
	private Lambda lambda(LamContext cnt) {
		//System.out.println("lambda: " + lex + " " + valueLex);
		Lambda r = null;
		ArrayList <String> varAr = new ArrayList();
		String var; 
		get(); var = valueLex;
		//System.out.println("lambda1: " + lex + " " + valueLex);
		exam(1, "ідентифікатор");
		if (errorText.isEmpty()){
			//System.out.println("lambda2: " + lex + " " + valueLex);
			varAr.add(var);
			while(lex==1) { 
				varAr.add(valueLex); get();
			}
			//System.out.println("lambda3: " + lex + " " + valueLex);
			exam(6, "символ '.'");
			if (errorText.isEmpty()) {
				LamContext cnt1 = cnt.copy(); 
				for(int i = 0; i <varAr.size(); i++){
					cnt1.add(new LamBinding(varAr.get(i),null));
				}
				r = term(cnt1);									
			}
			if (errorText.isEmpty()) {
				// звернути в оберненому порядку LamAbs !!!!!
				for(int i = varAr.size()-1; i >= 0; i--){
					r = new LamAbs(varAr.get(i),r);
				}
			} else r = null;
		}
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
				if(valueLex.equals("let")) lex = 3;	
				else if(valueLex.equals("in")) lex = 4;	
			} else if (StringWork.isDigit(next)) {
				lex = 2;
				do { 
					valueLex = valueLex + next; getChar();
				} while (StringWork.isDigit(next));
			} else {
				lex = 19;  // undefined lexem !!!
				switch (next){
				    case '\\': lex = 5; break;
				    case '.': lex = 6; break;
					case '(': lex = 7; break;
					case ')': lex = 8; break;
					case '=': lex = 9; break;
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
