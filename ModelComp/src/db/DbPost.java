package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbPost {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Post model; 

	DbPost(DbAccess db){
		this.db = db; 
	}
		
	public Post getPost(int id){
		model= null;
		sql = "select name, sMain, sAdd, isNumeric, Rank, descr from pPost where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Post(id, rs.getString(1));
            	model.main = rs.getString(2);
            	model.add = rs.getString(3);
            	model.isNumeric = rs.getBoolean(4);
            	model.rank = rs.getInt(5);
            	model.descr = rs.getString(6);
            	model.program = getAllDerives(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getPost :" + sql + e.getMessage());
		}
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		return model;
	}
	
	private ArrayList getAllDerives(int post) {
		ArrayList program = new ArrayList();
		sql = "select num, isAxiom, sLeft, sRigth,  txComm, id, idModel " +
		      "  from pDerive where idModel = " + post + " order by num";
		String sLeft = "";
		boolean isAxiom;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	isAxiom = rs.getBoolean(2);
	        	if (isAxiom) sLeft = ""; else sLeft = rs.getString(3);
	        	Derive rule = new Derive(rs.getInt(1), rs.getBoolean(2), sLeft,rs.getString(4), rs.getString(5), rs.getInt(6));
				program.add(rule);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllDerives :" + sql + e.getMessage());
		}
		return program;
	}	
	
	
	
}
