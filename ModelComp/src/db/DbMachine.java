package db;

import java.sql.*;
import java.util.*;

import main.*;
public class DbMachine {
	private DbAccess db;
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
            	model.program = getAllStates(id);
            	// добудувати всі переходи
            	getAllGoing(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: DbMachine: getMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		System.out.println("DbMachine: getMachine :" + id + " " + model.name);
		return model;
	}
	
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
					st = (State)model.program.get(num);
					going = st.getGoing();
					going.remove(pos);
					going.add(pos,go);
					model.program.remove(num);
					model.program.add(num, new State(st.getState(),num, going, st.gettxComm()));
				} 
	        } 
		}catch (Exception e){
			System.out.println("ERROR: DbMachine: getAllGoing :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
	}
	
}
