package db;

import java.sql.*;
import java.util.*;

import main.*;
import model.calc.Calculus;
import model.calc.LambdaDecl;

public class DbCalculus {
	private DbAccess db;
	private String sql;
	private ResultSet rs;
	private Calculus model; 
	
	DbCalculus(DbAccess db){
		this.db = db; 
	}
		
	public Calculus getCalculus(int id){
		model= null;
		sql = "select name, descr from eCalculus where id = " + id;
		try{
			 	//System.out.println("getCalculus: " + sql);
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Calculus(id, rs.getString(1));
            	model.descr = rs.getString(2);
            	//System.out.println("getCalculus: " + rs.getString(1));
            	model.program = getAllDeclLambda(id);
            	//System.out.println("getCalculus: befor extend");
            	model.extend();
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getCalculus:" + e.getMessage());
		}
		return model;
	}
	
	private ArrayList getAllDeclLambda(int rec) {
		ArrayList program = new ArrayList();
		sql = "select num, name, txBody,  txComm, id, idModel " +
		      "  from eLambda where idModel = " + rec + " order by num";
		//boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	//isConst = rs.getBoolean(3);
	           	LambdaDecl f = new LambdaDecl(rs.getInt(5), rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
	           	//System.out.println(rs.getInt(1));
				program.add(f);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllDeclLambda :" + sql + e.getMessage());
		}
		return program;
	}	
	
	public LambdaDecl getLambdaDecl(String nmModel, String nmFunct){
		sql = "select txBody, txComm from eLambda "  +
			  " where idModel = (select id from eCalculus where name = '" + nmModel + "'" +
				                      " and section = '" + Parameters.getSection()+ "') " + 
			  "   and name = '" + nmFunct + "'"; //+
			//System.out.println("DbCalculus: getLambdaDecl :" + sql);
		try{ 
			db.s.execute(sql);
	        rs = db.s.getResultSet();
	        if((rs!=null) && (rs.next())) {
				return new LambdaDecl(0,0,nmFunct,rs.getString(1), rs.getString(2) );
	        } 
	        else return null;
		}catch (Exception e){
			System.out.println("ERROR: getLambdaDecl :" + sql + e.getMessage());
			return null;
		}
	}	
	
/*
	public ArrayList getAllNameLambda(String nmSet) {
		ArrayList nmLambda = new ArrayList();
		sql = "select name from eLambda " +
			   "where idModel = (select id from eCalculus where name = '" + nmSet + "'" +
				                      " and section = '" + Parameters.getSection()+ "')" +
			   " order by num";
		//boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	//LambdaDecl f = new LambdaDecl(rs.getInt(5), rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
	           		//System.out.println(rs.getInt(1));
				nmLambda.add(rs.getString(1));
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllNameLambda :" + sql + e.getMessage());
		}
		return nmLambda;
	}	
*/	
	// модифікує відредагований набір
	public void editCalculus(Calculus model) {
		int rows;
		try{
			sql = "update eCalculus set name = '" + model.name + "', descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editCalculus: Не змінило відредагований " + model.name + "!");
		}
		catch (Exception e) {
			System.out.println("ERROR: editCalculus :" + e.getMessage());
		}
	}		
		
	
	// створює новий порожній набір функцій
	public int newCalculus() {
		String name = db.findName("Calculus","Set");
		int cnt = db.maxNumber("Calculus")+1;
		int rows;
		String section = Parameters.getSection();
		try{
			sql = "insert into eCalculus values(" + cnt +  ",'" + section +  "','" + name + "','new set')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
			System.out.println("ERROR: newCalculus :" + sql + " "  + e.getMessage());
		}
		return cnt;
	}	
	
	// створює нову систему на основі системи з model (включаючи всі аксіоми та правила виводу)
	public int newCalculusAs(Calculus model){
		String name = db.findName("Calculus", model.name);
		int cnt = db.maxNumber("Calculus")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into eCalculus select " + cnt + ", section, '" + name + "', descr from eCalculus where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				if (rows == 0) cnt =0;
				sql = "insert into eLambda select " + cnt + ", id, num, name, txBody, txComm " +
							" from eLambda where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: newCalculusAs :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}

	public boolean deleteCalculus(Calculus model){
		int rows;
		boolean res = false;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from eLambda where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from eCalculus where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
				res = true;
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: deleteCalculus :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return res;
	}	
	
	// додає введений з файлу набір функцій model (включаючи всі функції)
	public int addCalculus(Calculus model){
		String name = model.name;
		int cnt = db.maxNumber("Calculus")+1;
		int rows;
		LambdaDecl f;
		String section = Parameters.getSection();
		if (db.isModel("Calculus",name)) name = db.findName("Calculus", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into eCalculus values(" + cnt + ",'" + section +  "','"  + name + "','" + model.descr + "')";  
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					f = (LambdaDecl)model.program.get(i);
					sql = "insert into eLambda values(" + cnt + "," + f.getId() + "," + f.getNum() + ",'" + f.getName() + "','" +  
									f.gettxBody() + "','" + f.gettxComm() + "')";
					rows=rows + db.s.executeUpdate(sql);
				}
			if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addCalculus :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}			
	
	
	public void editDeclLambda(int id, LambdaDecl fun) {
		 try{	
			sql = "update eLambda set txBody = '" + fun.gettxBody() + "'," + "txComm = '" + fun.gettxComm() + "'" +
		 			"	where idModel = " + id + " and id = " + fun.getId() ;
		 	//System.out.println("Db.."+sql);
		 	db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: editDeclLambda: " + e.getMessage() );
		 }  			
	}
	
	public void newDeclLambda(int idModel, LambdaDecl fun) {
		/*
		 try{	
			sql = "insert into eLambda values(" + idModel + "," + fun.getId() + "," + fun.getNum() + ",'" + fun.getName() +
					"','" + fun.gettxBody() + "','" + fun.gettxComm() + "')";
			db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: newDeclLambda: " + e.getMessage() );
		 }
		 */
		int maxNum = getMaximumNum(idModel);
		try {
			db.conn.setAutoCommit(false);
			try{
				 // потрібно перемістити функції, щоб звільнити місце !!!                               
				for (int i = maxNum; i >= fun.getNum(); i--){
					sql = "update eLambda set num = num+1" + "	where idModel = " + idModel + " and num = " + i;
					db.s.execute(sql);
				}
				sql = "insert into eLambda values(" + idModel + "," + fun.getId() + "," + fun.getNum() + ",'" + fun.getName() +
						"','" + fun.gettxBody() + "','" + fun.gettxComm() + "')";			
				//System.out.println("newDeclLambda:" + sql + " : num = " + fun.getNum());
				db.s.execute(sql);
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR:newDeclLambda: " + e.getMessage() );
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}
		
	}
	
	//--------------------------------- all for insert !!!!!!!!!!
	// вставити в набір номер idModel вираз з іменем nmDecl з набору з іменем nmModel після виразу where
	//  ....maxId - найбільший id в програмі idModel
	public String insertDeclLambda(Calculus model, int where, String nmModel, String nmDecl) {
		String res = "";
		LambdaDecl ld = getLambdaDecl(nmModel, nmDecl);
		int maxNum = getMaximumNum(model.id);
		if (ld!=null){
			try {
				db.conn.setAutoCommit(false);
				try{
					 // потрібно перемістити вирази, щоб звільнити місце !!!                               
					for (int i = maxNum; i > where; i--){
						sql = "update eLambda set num = num+1" + "	where idModel = " + model.id + " and num = " + i;
						db.s.execute(sql);
					}
					sql = "insert into eLambda values(" + model.id + "," + (model.findMaxNumber()+1) + 
							           ","  + (where+1) + ",'" + model.findNameCommand(nmDecl) +
							           "','" + ld.gettxBody() + "','" + ld.gettxComm() + "')";			
					//System.out.println("DbCalculus:insertDeclLambda:" + sql);
					db.s.execute(sql);
					db.conn.commit();
				}	catch (Exception e) {
					System.out.println("ERROR:DbCalculus:insertDeclLambda:" + e.getMessage() );
					res = "ERROR:DbCalculus:insertDeclLambda:" + e.getMessage();
					db.conn.rollback();
				}  
				db.conn.setAutoCommit(true);
			}	
			catch (Exception e) { System.out.println(e.getMessage());}
		}
		else res = "insertDeclLambda: Не знайдено в наборі " + nmModel + " вираз з іменем " + nmDecl  + "!" ;
		return res;
	}
	
	public void deleteDeclLambda(int idModel, LambdaDecl ld){
		int maxNum = getMaximumNum(idModel);
		try {
			db.conn.setAutoCommit(false);
			try{	
				sql = "delete from eLambda where idModel = " + idModel + " and id = " + ld.getId();
				db.s.execute(sql);
				if (ld.getNum() < maxNum) {
					// підтягнути вирази що залишилися 
					for(int r = ld.getNum()+1; r <= maxNum; r++) {
						sql = "update eLambda set num = num-1" + "	where idModel = " + idModel + " and num = " + r;
						db.s.execute(sql);	 
					}
				}
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteDeclLambda: " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}
	}
	
	//знаходить найбільший номер num у функцій набору idModel 
	public int getMaximumNum(int idModel){
		int i = 0;
		try{
			sql = "select max(num) from eLambda where idModel = " + idModel;
			db.s.execute(sql);
			rs = db.s.getResultSet();
		    if((rs!=null) && (rs.next()))i = rs.getInt(1);
		}
		catch (Exception e) {System.out.println(e.getMessage());}
		return i;
	}			
}
