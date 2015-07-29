package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbMachine {
	private DbAccess db;
	//private Connection con;
	private Statement st; 
	private String sql;
	private  ResultSet rs;
	private Machine model; 
	
	DbMachine(DbAccess db){
		this.db = db; 
	}
	
	public Machine getMachine(int id){
		//System.out.println("DbMachine: getMachine :" + id);
		model= null;
		sql = "select name, sMain, sAdd, sInitial, sFinal,  isNumeric, Rank, descr " +
			      "  from tMachine where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Machine(id, rs.getString(1));
            	//model.name = rs.getString(1);
            	model.main = rs.getString(2);
            	model.add = rs.getString(3);
               	model.init = rs.getString(4);
            	model.fin = rs.getString(5);
            	model.isNumeric = rs.getBoolean(6);
            	model.rank = rs.getInt(7);
            	model.descr = rs.getString(8);
            	// всі символи, що не входять в об"єднаний алфавіт 
            	model.no = findNo("_" + model.main + model.add, id);
            	// всі стани збережені в базі даних
            	model.program = getProgram(id);
            }
       	}
		catch (SQLException e) {
			System.out.println("ERROR: DbMachine: getMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return model;
	}
	
	// модифікує відредаговану машину Тьюрінга
	public void editMachine(Machine model) {
		int rows;
		try{
			int isNumeric = (model.isNumeric?1:0); 
			sql = "update tMachine set name = '" + model.name + "', sMain = '" + model.main + "', " +
						"sAdd = '" + model.add + "', isNumeric = " + isNumeric + ", Rank = " + model.rank + ", " +
						"sInitial = '" + model.init + "', sFinal = '" + model.fin + "', " +
						"descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			//System.out.println("rows = " + rows + " sql=" + sql);
			if (rows == 0)
				System.out.println("editMachine: Не змінило відредагований " + model.name + "!");
			//System.out.println("rows = " + rows + " sql=" + sql);
		}
		catch (Exception e) {
			//System.out.println(e.getMessage());
			System.out.println("ERROR: DbMachine: editMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
	}	
	
	
	
	// створює нову порожню машину Тюрінга
	public int newMachine() {
		String name = db.findName("Machine","Machine");
		int cnt = db.maxNumber("Machine")+1;
		int rows;
		String section = Parameters.getSection();
		try{
			sql = "insert into tMachine values(" + cnt + ",'" + section + "','" + name + "','|#','','@a0', '@zz', 1,2,'new')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
			//System.out.println(e.getMessage());
			System.out.println("ERROR: newMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return cnt;
	}
		
	// створює нову машину Тюрінга на основі машини з model (включаючи всі стани)
	public int newMachineAs(Machine model){
		//Algorithm newModel= null;
		String name = db.findName("Machine", model.name);
		int cnt = db.maxNumber("Machine")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into tMachine select  " + cnt + "  As id, section , '" + name + "' as name, sMain, SAdd, " +
							" sInitial, sFinal, isNumeric, Rank , descr from  tMachine where id = " + model.id;  
				//System.out.println(">>> " + sql);
				rows=db.s.executeUpdate(sql);
				sql = "insert into tProgram select " + cnt + ", id, sState, txComm "+ 
						" from  tProgram where idModel = " + model.id;
				//System.out.println(">>> " + sql);
				rows=db.s.executeUpdate(sql);
				sql = "insert into tMove select " + cnt + ", id, sIn, sOut, sNext, sGo " +
						" from tMove where idModel = " + model.id;
				//System.out.println(">>> " + sql);
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: newMachineAs :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}
	
	// додає введену з файлу машину Тюрінга model (включаючи всі стани)
	public int addMachine(Machine model){
		String allCh = "_" + model.main + model.add + model.no;
		String name = model.name;
		int cnt = db.maxNumber("Machine")+1;
		int rows;
		State st;
		String move = "";
		String inCh = "";
		String section = Parameters.getSection();
		if (db.isModel("Machine",name)) name = db.findName("Machine", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				int isNumeric = (model.isNumeric?1:0); 
				sql = "insert into tMachine values(" + cnt +  ",'" + section +  "','" + name + "','" + model.main + "','" +
						model.add + "','" + model.init + "','" + model.fin + "'," + isNumeric + "," + model.rank + ",'" + model.descr + "')";  
				//System.out.println(">>> " + sql);
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					st = (State)model.program.get(i);
					sql = "insert into tProgram values(" + cnt + "," + st.getId() + ",'" + st.getState() + "','" + st.gettxComm() + "')";
					//System.out.println(">>> " + sql);
					rows=rows + db.s.executeUpdate(sql);
					for(int j = 0; j < st.getGoing().size(); j++) {
						move = st.getGoing().get(j);
						if (!move.isEmpty()) {
							if(j < allCh.length()) {
								inCh = allCh.substring(j,j+1);                        ///else inCh = " ";
							    sql = "insert into tMove values(" + cnt + "," + st.getId() + ",'" + inCh + "','" + move.substring(3,4)
						    			+ "','" + move.substring(0,3) + "','" + move.substring(4,5) + "')";	
							    //System.out.println(">>> " + sql);
							    db.s.execute(sql);
							}    
						}
					}
				}
				if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addMachine :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}		
	
	public boolean deleteMachine(Machine model){
		int rows;
		boolean res = false;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from tMove where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from tProgram where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from tMachine where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
				res = true;
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: deleteMachine :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return res;
	}
	
	// переіменування в машині mach стану in на стан out
	public void renameState(Machine mach, String in, String out) {
		String fst = mach.init;
		String last = mach.fin; 
		boolean edit = false;
		if (fst.equals(in)) {edit = true; fst = out;}
		if (last.equals(in)) {edit = true; last = out;}
		try {
			db.conn.setAutoCommit(false);
			try{
				if (edit){
					sql = "update tMachine set sInitial = '" + fst + "', sFinal = '" + last + "' where id = " + mach.id;
					//System.out.println("renameState " + sql);
					db.s.execute(sql);
				}
				sql = "update tProgram set sState = '" + out + "' where idModel = " + mach.id + " and sState = '" + in + "'";
				//System.out.println("renameState " + sql);
				db.s.execute(sql);
				sql = "update tMove set sNext = '" + out + "' where idModel = " + mach.id + " and sNext = '" + in + "'";
				//System.out.println("renameState " + sql);
				db.s.execute(sql);
				db.conn.commit();
			} catch (Exception e) {     //SQLException e
				System.out.println(">>> renameState " + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}		
	}
	
	public void newState(Machine mach, State st){
		String allCh = "_" + mach.main + mach.add + mach.no;
		String move = "";
		String inCh = "";
		try {
			db.conn.setAutoCommit(false);
			try{	
				sql = "insert into tProgram values(" + mach.id + "," + st.getId() + ",'" + st.getState() + "','" + st.gettxComm() + "')";
				db.s.execute(sql);
				for(int i = 0; i < st.getGoing().size(); i++) {
					move = st.getGoing().get(i);
					if (!move.isEmpty()) {
						if(i < allCh.length()) inCh = allCh.substring(i,i+1); else inCh = " ";
					    sql = "insert into tMove values(" + mach.id + "," + st.getId() + ",'" + inCh + "','" + move.substring(3,4)
					    			+ "','" + move.substring(0,3) + "','" + move.substring(4,5) + "')";	
					    db.s.execute(sql);	 
					}
				}
				db.conn.commit();
			} catch (Exception e) {     //SQLException e
				System.out.println(">>> newState" + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}		
	}
	
	public void editState(Machine mach, int id, State st){
		String allCh = "_" + mach.main + mach.add + mach.no;
		String move = "";
		String inCh = "";
		try {
			db.conn.setAutoCommit(false);
			try{	
				sql = "update tProgram set txComm = '" + st.gettxComm() + "' where idModel = " + mach.id + " and id = " + id;
				//System.out.println("editState " + sql);
				db.s.execute(sql);
				sql = "delete from tMove where idModel = " + mach.id + " and id = " + id;
				//System.out.println("editState " + sql);
				db.s.execute(sql);	
				for(int i = 0; i < st.getGoing().size(); i++) {
					move = st.getGoing().get(i);
					if (!move.isEmpty()) {
						if(i < allCh.length()) inCh = allCh.substring(i,i+1); else inCh = " ";
					    sql = "insert into tMove values(" + mach.id + "," + id + ",'" + inCh + "','" + move.substring(3,4)
					    			+ "','" + move.substring(0,3) + "','" + move.substring(4,5) + "')";	
					   // System.out.println("editState " + sql);
					    db.s.execute(sql);	 
					}
				}
				db.conn.commit();
			} catch (Exception e) {     //SQLException e
				System.out.println("ERROR: editState: mach = " + mach.id);
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}		
	}
	
	
	public void deleteState(int mach, int row){
		try {
			db.conn.setAutoCommit(false);
			try{	
				//int cnt = cntRule(algo);
				sql = "delete from tMove where idModel = " + mach + " and id = " + row;
				db.s.execute(sql);
				sql = "delete from tProgram where idModel = " + mach + " and id = " + row;
				db.s.execute(sql);	
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteState: Could not delete State.");
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	private String findNo(String alfa, int mach ) {
		String no = "";
		String in;
		int p;
		sql = "select distinct sIn from tMove where idModel = " + mach;
		try{ 
			db.s.execute(sql);
		    ResultSet rs = db.s.getResultSet();
		    while((rs!=null) && (rs.next())) {
		       	in = rs.getString(1);
		       	p = (alfa + no).indexOf(in.charAt(0));
		       //	System.out.println(" findNo :" + in + " " + p);
		       	if( p < 0 ) no = no + in;
		    } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine : findNo :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return no;
	}
	
	private ArrayList <State> getProgram(int mach) {
		ArrayList <State> states = new ArrayList();
		String inSym = "_" + model.main + model.add + model.no;
		int idSt;
		String state;
		String comm;
		ArrayList <String> going;
		sql = "select id, sState, txComm " +
		      "  from tProgram where idModel = " + mach + " order by sState";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати один стан, але ВСІ переходи порожні 
	        	idSt = rs.getInt(1);
	        	state = rs.getString(2);
	        	comm = rs.getString(3);
	        	//System.out.println("getProgram :" + idSt + " " + state);
	        	going = getGoing(mach, idSt, inSym);
				State st = new State(state,idSt, going, comm);
				states.add(st);
	        } 
		}catch (SQLException  e){
			System.out.println("ERROR: DbMachine: getProgram :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return states;
	}

	private ArrayList <String> getGoing(int mach, int idSt, String alfa){
		ArrayList <String> going = new ArrayList();
		int l = alfa.length();
		String[] goingA = new String[l];
		for(int i = 0; i < l; i++)  goingA[i] = "";            
		sql = "select sIn, sOut, sNext, sGo " +
			      "  from tMove where idModel = " + mach + " and id = " + idSt; 
		try{ 
			//con = db.conn;
			st= db.conn.createStatement();
			st.execute(sql);
		    ResultSet rs = st.getResultSet();
		    String in;
		    while((rs!=null) && (rs.next())) {
		      	// тут будемо зберігати один перехід
		       //	num = rs.getInt(1);
		       	in = rs.getString(1);
		       	int pos = alfa.indexOf(in.charAt(0));
				if (pos >= 0) {
					String go = new String(rs.getString(3) + rs.getString(2) + rs.getString(4));
					goingA[pos] = go;
				} 
	       } 
		} catch (SQLException  e){
			System.out.println("ERROR: DbMachine: getGoing :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
			
		for(int i = 0; i < l; i++) going.add(goingA[i]) ;  
		return going;
	}
	
	
	//--------------------------------- all for insert !!!!!!!!!!
	// вставити в машину mach машину ins
	public String insertMachine(Machine mach, String ins) {
		int idIns = getIdMachine(ins);
		int maxCom = mach.findMaxNumber();
		String res = "";
		if (idIns > 0) {
			ArrayList <String> insSt = getAllState(idIns);
			ArrayList <Integer> insId = getAllId(idIns);
			String[] newSt = mach.newArState(insSt.size());
			ResultSet rs;
			try {
				st= db.conn.createStatement();
				db.conn.setAutoCommit(false);
				try{
					int idCom, idComN;
					String state, stateN;
					String comm = ""; 
					sql = "select id, sState, txComm from tProgram where idModel = " + idIns + " order by id" ;
					db.s.execute(sql);
				    rs = db.s.getResultSet();
				    while((rs!=null) && (rs.next())) {
				    	idCom = rs.getInt(1); state = rs.getString(2); comm = rs.getString(3);
				    	idComN = findInArrayI(insId,idCom) + maxCom + 1;
				    	stateN = newSt[findInArrayS(insSt,state)];
				    	comm = ins + " " + comm;
				    	sql = "insert into tProgram values(" + mach.id + "," + idComN + ",'" + stateN + "','" + comm + "')"; 
				    	st.execute(sql);
				    }	
				    sql = "select id, sIn, sOut, sNext, sGo from tMove where idModel = " + idIns ;
					db.s.execute(sql);
				    rs = db.s.getResultSet();
				    while((rs!=null) && (rs.next())) {
				    	idCom = rs.getInt(1); state = rs.getString(4);
				    	idComN = findInArrayI(insId,idCom) + maxCom + 1;
				    	stateN = newSt[findInArrayS(insSt,state)];
				    	sql = "insert into tMove values(" + mach.id + "," + idComN + ",'" + rs.getString(2) + "','"
				    					+ rs.getString(3) + "','" + stateN + "','" + rs.getString(5) + "')"; 
				    	st.execute(sql);			    	
				    } 
					db.conn.commit();
				} catch (Exception e) {     //SQLException e
					System.out.println("ERROR: insertMachine: mach = " + mach.id + " " + ins);
					System.out.println(">>> ERROR: editState: " + e.getMessage());
					res = "ERROR: insertMachine: mach = " + mach.id + " " + ins + " " + e.getMessage();
					db.conn.rollback();
				} 
				db.conn.setAutoCommit(true);
			}	
			catch (Exception e) { System.out.println(e.getMessage());}				
		}
		else res = "insertMachine: Не знайдено машину з іменем " + ins + "!" ;
		return res;
	}
	
	
	//
	private int getIdMachine(String name) {
		int res = 0;
		String section = Parameters.getSection();
		try{ 
			sql = "select id from tMachine where name = '" + name + "' and section = '" + section + "'";
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	res= rs.getInt(1); 
            }
		}catch (Exception e){
			System.out.println("ERROR: DbMachine : getIdMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return res;	
	}
	
	
	// вибирає всі номера станів машини id
	private ArrayList <Integer> getAllId(int id){
		ArrayList <Integer> idAr = new ArrayList();
		sql = "select id from tProgram where idModel = " + id + " order by id";
		try{ 
			db.s.execute(sql);
		    ResultSet rs = db.s.getResultSet();
		    while((rs!=null) && (rs.next())) {
					idAr.add(rs.getInt(1));
		    } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine: getAllId :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return idAr;
	}
	
	// вибирає всі стани, що використовуються в машині id
	private ArrayList <String> getAllState(int id){
		ArrayList <String> stAr = new ArrayList();
		String st;
		ResultSet rs;
		String inSt = "@a0";
		String finSt = "@zz";
		try{ 
			sql = "select sInitial, sFinal from tMachine where id = " + id;
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	inSt = rs.getString(1); finSt = rs.getString(2);
            }
			stAr.add(inSt);
			sql = "select sState from tProgram where idModel = " + id + " order by sState" ;
			db.s.execute(sql);
		    rs = db.s.getResultSet();
		    while((rs!=null) && (rs.next())) {
		    	st = rs.getString(1);
		    	if (!st.equals(inSt) && !st.equals(finSt)) 	stAr.add(st);
		    }
		    stAr.add(finSt);
			sql = "select distinct sNext from tMove where idModel = " + id + " order by sNext" ;
			db.s.execute(sql);
		    rs = db.s.getResultSet();
		    while((rs!=null) && (rs.next())) {
		    	st = rs.getString(1);
		       	if (findInArrayS(stAr,st) == -1) stAr.add(st);
		    } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine : getAllState :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return stAr;
	}
	
	private int findInArrayS(ArrayList<String> al, String st) {
		int i = 0;
		int j = -1;
		while ((j == -1) && (i < al.size())) {
			if (al.get(i).equals(st)) j = i; else i++;
		}
		return j;
	}
	
	private int findInArrayI(ArrayList<Integer> al, int cm) {
		int i = 0;
		int j = -1;
		while ((j == -1) && (i < al.size())) {
			if (al.get(i) == cm) j = i; else i++;
		}
		return j;
	}
	/*
	private ArrayList <State> getAllStates(int mach) {
		ArrayList <State> states = new ArrayList();
		String inSym = "_" + model.main + model.add + model.no;
		sql = "select id, sState, txComm " +
		      "  from tProgram where idModel = " + mach + " order by sState";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати один стан, але ВСІ переходи порожні 
				State st = new State(rs.getString(2),rs.getInt(1), undefinedGoing(inSym), rs.getString(3));
				states.add(st);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine: getAllStates :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return states;
	}
	
	private ArrayList <String> undefinedGoing(String alfa){
		ArrayList <String> going = new ArrayList();
		for(int i = 0; i < alfa.length(); i++) going.add("");
		return going;
	}
	
	private void getAllGoing(int mach) {
		String allCh = "_" + model.main + model.add + model.no;
		String in = "";
		State st;
		ArrayList going;
		int num;
		sql = "select id, sIn, sOut, sNext, sGo " +
		      "  from tMove where idModel = " + mach; // + " order by id, sIn";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати один перехід
	        	num = rs.getInt(1);
	        	in = rs.getString(2);
	        	int pos = allCh.indexOf(in.charAt(0));
				if (pos >= 0) {
					String go = new String(rs.getString(4) + rs.getString(3) + rs.getString(5));
					st = (State)model.program.get(num-1);
					going = st.getGoing();
					going.remove(pos);
					going.add(pos,go);
					model.program.remove(num-1);
					model.program.add(num-1, new State(st.getState(),num, going, st.gettxComm()));
				} 
	        } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine: getAllGoing :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
	}
	*/
}
