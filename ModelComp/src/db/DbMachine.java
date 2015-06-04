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
		model= null;
		sql = "select name, sMain, sAdd, sInitial, sFinal,  isNumeric, Rank, descr " +
			      "  from tMachine where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Machine(id, rs.getString(1));
            	model.main = rs.getString(2);
            	model.add = rs.getString(3);
            	model.no = "";
            	model.init = rs.getString(4);
            	model.fin = rs.getString(5);
            	model.isNumeric = rs.getBoolean(6);
            	model.rank = rs.getInt(7);
            	model.descr = rs.getString(8);
            	model.program = getAllStates(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getMachine :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		return model;
	}
	
	private ArrayList <State> getAllStates(int mach) {
		ArrayList <State> states = new ArrayList();
		sql = "select id, sState, txComm " +
		      "  from tState where idModel = " + mach + " order by sState";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати одну підстановку
				State st = new State(rs.getString(2),rs.getInt(1), null, rs.getString(3));
				states.add(st);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllStates :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return states;
	}
	
	private String getNo() {
		
		String no = "";
		sql = "select id, sState, txComm " +
		      "  from tState where idModel = " + 8 + " order by sState";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати одну підстановку
				State st = new State(rs.getString(2),rs.getInt(1), null, rs.getString(3));
		//		states.add(st);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllStates :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return no;
	}
	
}
