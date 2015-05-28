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

	public ArrayList getDataSource(int idModel){
		try{ 
			ArrayList data = new ArrayList();
			char[] typeInfo = {'I','S','S','B','S','I'}; 
			sql = "select id, sLeft, sRigth, isEnd, txComm, idModel " +
	    				" from mRule where idModel = " + idModel;
	    	//System.out.println("model = " + model + "   " + sql);
	        db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	// тут будемо зберігати комірки одного рядка
				ArrayList row = new ArrayList();
				for(int i = 0; i < typeInfo.length; i++) {
					switch (typeInfo[i]){
					case 'B': row.add(rs.getBoolean(i+1)); break;
					case 'I': row.add(rs.getInt(i+1)); break;
					case 'S': row.add(rs.getString(i+1)); break;
					default: row.add(rs.getObject(i+1)); 
					}
				}	        	
				data.add(row);
	        } 
	        return data;
   		}catch (Exception e){
			System.out.println("ERROR: DbAlgorithm-getDataSource :" + sql);
			System.out.println(">>> " + e.getMessage());
			return null;
		}
	}
}
