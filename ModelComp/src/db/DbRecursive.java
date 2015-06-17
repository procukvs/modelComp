package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbRecursive {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Recursive model; 
	
	DbRecursive(DbAccess db){
		this.db = db; 
	}
		
	public Recursive getRecursive(int id){
		model= null;
		sql = "select name, descr from fRecursive where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Recursive(id, rs.getString(1));
            	model.descr = rs.getString(2);
            	model.program = getAllFunction(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getRecursive:" + e.getMessage());
		}
		return model;
	}
	
	private ArrayList getAllFunction(int rec) {
		ArrayList program = new ArrayList();
		sql = "select name, Rank, isConst, txBody,  txComm, id, idModel " +
		      "  from fFunction where idModel = " + rec + " order by name";
		boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	isConst = rs.getBoolean(3);
	           	Function f = new Function(rs.getInt(6), rs.getString(1), rs.getInt(2), rs.getBoolean(3), rs.getString(4), rs.getString(5));
				program.add(f);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllFunction :" + sql + e.getMessage());
		}
		return program;
	}	

}
