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
            	model.extend();
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getRecursive:" + e.getMessage());
		}
		return model;
	}
	
	private ArrayList getAllFunction(int rec) {
		ArrayList program = new ArrayList();
		sql = "select name, txBody,  txComm, id, idModel " +
		      "  from fFunction where idModel = " + rec + " order by name";
		boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	isConst = rs.getBoolean(3);
	           	Function f = new Function(rs.getInt(4), rs.getString(1), rs.getString(2), rs.getString(3));
	           //	System.out.println(f.toString());
				program.add(f);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllFunction :" + sql + e.getMessage());
		}
		return program;
	}	

}
