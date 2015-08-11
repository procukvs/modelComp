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
		//boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	//isConst = rs.getBoolean(3);
	           	Function f = new Function(rs.getInt(4), rs.getString(1), rs.getString(2), rs.getString(3));
	           //	System.out.println(f.toString());
				program.add(f);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllFunction :" + sql + e.getMessage());
		}
		return program;
	}	
	
	// модифікує відредагований набір
	public void editRecursive(Recursive model) {
		int rows;
		try{
			sql = "update fRecursive set name = '" + model.name + "', descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editRecursive: Не змінило відредагований " + model.name + "!");
		}
		catch (Exception e) {
			System.out.println("ERROR: editRecursive :" + e.getMessage());
		}
	}		
		
	
	// створює новий порожній набір функцій
	public int newRecursive() {
		String name = db.findName("Recursive","Set");
		int cnt = db.maxNumber("REcursive")+1;
		int rows;
		String section = Parameters.getSection();
		try{
			sql = "insert into fRecursive values(" + cnt +  ",'" + section +  "','" + name + "','new set')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
			System.out.println("ERROR: newREcursive :" + sql + " "  + e.getMessage());
		}
		return cnt;
	}	
	
	// створює нову систему на основі системи з model (включаючи всі аксіоми та правила виводу)
	public int newRecursiveAs(Recursive model){
		String name = db.findName("Recursive", model.name);
		int cnt = db.maxNumber("Recursive")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into fRecursive select " + cnt + ", section, '" + name + "', descr from fRecursive where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				if (rows == 0) cnt =0;
				sql = "insert into fFunction select " + cnt + ", id, name, txBody, txComm " +
							" from fFunction where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: newRecursivetAs :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}

	public boolean deleteRecursive(Recursive model){
		int rows;
		boolean res = false;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from fFunction where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from fRecursive where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
				res = true;
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: deleteRecursive :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return res;
	}	
	
	// додає введений з файлу набір функцій model (включаючи всі функції)
	public int addRecursive(Recursive model){
		String name = model.name;
		int cnt = db.maxNumber("Recursive")+1;
		int rows;
		Function f;
		String section = Parameters.getSection();
		if (db.isModel("Recursive",name)) name = db.findName("Recursive", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into fRecursive values(" + cnt + ",'" + section +  "','"  + name + "','" + model.descr + "')";  
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					f = (Function)model.program.get(i);
					sql = "insert into fFunction values(" + cnt + "," + f.getId() + ",'" + f.getName() + "','" +  
									f.gettxBody() + "','" + f.gettxComm() + "')";
					rows=rows + db.s.executeUpdate(sql);
				}
			if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addRecursive :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}			
	
	
	public void editFunction(int id, Function fun) {
		 try{	
			sql = "update fFunction set txBody = '" + fun.gettxBody() + "'," + "txComm = '" + fun.gettxComm() + "'" +
		 			"	where idModel = " + id + " and id = " + fun.getId() ;
		 	//System.out.println("Db.."+sql);
		 	db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: editFunction: " + e.getMessage() );
		 }  			
	}
	
	public void newFunction(int idModel, Function fun) {
		 try{	
			sql = "insert into fFunction values(" + idModel + "," + fun.getId() + ",'" + fun.getName() +
					"','" + fun.gettxBody() + "','" + fun.gettxComm() + "')";
			db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: newFunction: " + e.getMessage() );
		 }  			
	}
	
	public void deleteFunction(int idModel, int id){
		try {
			db.conn.setAutoCommit(false);
			try{	
				//int cnt = cntRule(algo);
				sql = "delete from fFunction where idModel = " + idModel + " and id = " + id;
				db.s.execute(sql);	
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteFunction: " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}	
}
