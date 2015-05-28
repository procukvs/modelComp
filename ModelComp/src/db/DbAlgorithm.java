package db;

import java.sql.*;
import java.util.*;

import main.*;

class DbAlgorithm {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Algorithm model; 
	
	DbAlgorithm(DbAccess db){
		this.db = db; 
	}
		
	public Algorithm getAlgorithm(int id){
		model= null;
		sql = "select name, sMain, sAdd, isNumeric, Rank, descr " +
			      "  from mAlgorithm where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Algorithm(id, rs.getString(1));
            	model.main = rs.getString(2);
            	model.add = rs.getString(3);
            	model.isNumeric = rs.getBoolean(4);
            	model.rank = rs.getInt(5);
            	model.descr = rs.getString(6);
            	model.program = getAllRules(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getAlgorithm :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return model;
	}
	
	private ArrayList getAllRules(int algo) {
		ArrayList rules = new ArrayList();
		sql = "select id, sLeft, sRigth, isEnd, txComm, idModel " +
		      "  from mRule where idModel = " + algo + " order by id";
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати одну підстановку
				Rule rule = new Rule(rs.getString(2),rs.getString(3), rs.getBoolean(4),rs.getString(5));
				rules.add(rule);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllRules :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return rules;
	}

}
