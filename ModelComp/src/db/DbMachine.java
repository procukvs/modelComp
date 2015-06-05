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

	// �������� ������������ ������ �������
	public void editMachine(Machine model) {
		int rows;
		try{
			int isNumeric = (model.isNumeric?1:0); 
			sql = "update tMachine set name = '" + model.name + "', sMain = '" + model.main + "', " +
						"sAdd = '" + model.add + "', isNumeric = " + isNumeric + ", Rank = " + model.rank + ", " +
						"sInitial = '" + model.init + "', sFinal = '" + model.fin + "', " +
						"descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editMachine: �� ������ ������������� " + model.name + "!");
			//System.out.println("rows = " + rows + " sql=" + sql);
		}
		catch (Exception e) {
			//System.out.println(e.getMessage());
			System.out.println("ERROR: DbMachine: editMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
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
            	// �� �������, �� �� ������� � ��"������� ������ 
            	model.no = findNo("_" + model.main + model.add, id);
            	// �� ����� �������� � ��� �����
            	model.program = getProgram(id);
            }
       	}
		catch (SQLException e) {
			System.out.println("ERROR: DbMachine: getMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		//System.out.println("DbMachine: getMachine :" + id + " " + model.name);
		return model;
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
	        	// ��� ������ �������� ���� ����, ��� �Ѳ �������� ������ 
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
		      	// ��� ������ �������� ���� �������
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
	        	// ��� ������ �������� ���� ����, ��� �Ѳ �������� ������ 
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
	        	// ��� ������ �������� ���� �������
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
