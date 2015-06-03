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
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		return model;
	}
	
	
	
	// створює новий порожній алгоритм
	public int newAlgorithm() {
		//Algorithm model= null;
		String name = db.findName("Algorithm","Algorithm");
		int cnt = db.maxNumber("Algorithm")+1;
		int rows;
		try{
			sql = "insert into mAlgorithm values(" + cnt + ",'" + name + "','|#','',1,2,'new')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
				//System.out.println(e.getMessage());
			System.out.println("ERROR: newAlgorithm :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return cnt;
	}
	
	// створює новий алгоритм на основі алгоритму з model (включаючи всі підстановки)
	public int newAlgorithmAs(Algorithm model){
		//Algorithm newModel= null;
		String name = db.findName("Algorithm", model.name);
		int cnt = db.maxNumber("Algorithm")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into mRule select " + cnt + ", id, sLeft, sRigth, isEnd, txComm " +
						" from mRule where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "insert into mAlgorithm select " + cnt + ",'" + name + "', sMain, sAdd, " + 
						"isNumeric, Rank, descr from mAlgorithm where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				if (rows == 0) cnt =0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: newAlgorithmAs :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}
	
	public void deleteAlgorithm(Algorithm model){
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from mRule where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from mAlgorithm where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: deleteAlgorithm :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	// модифікує відредагований алгоритм
	public void editAlgorithm(Algorithm model) {
		int rows;
		try{
			int isNumeric = (model.isNumeric?1:0); 
			sql = "update mAlgorithm set name = '" + model.name + "', sMain = '" + model.main + "', " +
						"sAdd = '" + model.add + "', isNumeric = " + isNumeric + ", Rank = " + model.rank + "," +
						"descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editAlgorithm: Не змінило відредагований " + model.name + "!");
			//System.out.println("rows = " + rows + " sql=" + sql);
		}
		catch (Exception e) {
			//System.out.println(e.getMessage());
			System.out.println("ERROR: DbAlgorithm: editAlgorithm :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
	}	
	
	// додає введений з файлу алгоритм model (включаючи всі підстановки)
	public int addAlgorithm(Algorithm model){
		String name = model.name;
		int cnt = db.maxNumber("Algorithm")+1;
		int rows;
		Rule r;
		if (db.isModel("Algorithm",name)) name = db.findName("Algorithm", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				int isNumeric = (model.isNumeric?1:0); 
				sql = "insert into mAlgorithm values(" + cnt + ",'" + name + "','" + model.main + "','" +
						model.add + "'," + isNumeric + "," + model.rank + ",'" + model.descr + "')";  
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					r = (Rule)model.program.get(i);
					sql = "insert into mRule values(" + cnt + "," + (i+1) + ",'" + r.getsLeft() +
							"','" + r.getsRigth() + "'," + (r.getisEnd()?1:0) + ",'" + r.gettxComm() + "')";
					rows=rows + db.s.executeUpdate(sql);
				}
				if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addAlgorithm :" + sql);
				System.out.println(">>> " + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}		
	
	public void editRule(int algo, int row, Rule rule) {
		 try{	int isEnd = (rule.getisEnd()?1:0); 
			 	sql = "update mRule set sLeft = '" + rule.getsLeft() + "'," + "sRigth = '" + rule.getsRigth() + 
			 			"', isEnd = " +	isEnd + ", txComm = '" + rule.gettxComm() + "'" +
			 			"	where idModel = " + algo + " and id = " + row ;
			 	//System.out.println("1:" + sql);
			 	db.s.execute(sql);
		      
	        } catch (SQLException e) {
				System.out.println("ERROR: editRule: Could not editRule.");
				System.out.println(">>> " + e.getMessage());
		    }  	
	}
	
	public void newRule(int algo, int row, Rule rule) {
		try {
			db.conn.setAutoCommit(false);
			try{	
				int cnt = cntRule(algo);
				int isEnd = (rule.getisEnd()?1:0); 
				if (row < cnt) {
					// звільнити місце для нового правила 
					for(int r = cnt; r > row; r--) {
						sql = "update mRule set id = id+1" + "	where idModel = " + algo + " and id = " + r;
						//System.out.println("1:" + sql);
						db.s.execute(sql);	 
					}
				}
				sql = "insert into mRule values(" + algo + "," + (row+1) +	",'" + rule.getsLeft() + "','"
					 	+ rule.getsRigth() + "'," + isEnd + ",'" + rule.gettxComm() + "')";
				// System.out.println("1:" + sql);
				db.s.execute(sql);
				//cnt = 1/0;
				db.conn.commit();
			} catch (Exception e) {     //SQLException e
				System.out.println("ERROR: newRule: Could not newRule.");
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	public void deleteRule(int algo, int row){
		try {
			db.conn.setAutoCommit(false);
			try{	
				int cnt = cntRule(algo);
				sql = "delete from mRule " + " where idModel = " + algo + " and id = " + row;
				//System.out.println("1:" + sql);
				db.s.execute(sql);
				if (row < cnt) {
					// підтягнути правила що залишилися 
					for(int r = row+1; r <= cnt; r++) {
						sql = "update mRule set id = id-1" + "	where idModel = " + algo + " and id = " + r;
						//System.out.println("1:" + sql);
						db.s.execute(sql);	 
					}
				}
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteRule: Could not deleteRule.");
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	public void moveUp(int algo, int row) {
		try {
			db.conn.setAutoCommit(false);
			try{
				if (row > 1){
					int prev = row-1; 
					sql = "update mRule set id = 0  " +  "	where idModel = " + algo + " and id = " + row ;
					//System.out.println("1:" + sql);
					db.s.execute(sql);
					sql = "update mRule set id =  " + row +  "	where idModel = " + algo + " and id = " + prev;
					//System.out.println("2:" + sql);
					db.s.execute(sql);	
					sql = "update mRule set id =  " + prev + "	where idModel = " + algo + " and id = 0";
					//System.out.println("3:" + sql);
					db.s.execute(sql);
				}
				db.conn.commit();
			} catch (Exception e) {
				System.out.println("ERROR: moveUp: Could not moveUp.");
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}	
	
	public void moveDown(int algo, int row) {
		try {
			db.conn.setAutoCommit(false);
			try{    // take count rules 
				int cnt = cntRule(algo);
				if (row < cnt){
					int next = row+1; 
					sql = "update mRule set id = 0 where idModel = " + algo + " and id = " + row ;
					//System.out.println("1:" + sql);
					db.s.execute(sql);
					sql = "update mRule set id =  " + row +  "	where idModel = " + algo + " and id = " + next;
					//System.out.println("2:" + sql);
					db.s.execute(sql);	
					sql = "update mRule set id =  " + next + " where idModel = " + algo + " and id = 0";
					//System.out.println("3:" + sql);
					db.s.execute(sql);
				}
				db.conn.commit();
		    } catch (Exception e) {
				System.out.println("ERROR: moveDown: Could not moveDown.");
				System.out.println(">>> " + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
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

	private int cntRule(int algo) {
		int cnt = 0;
    	try{ 	
    		sql = "select count(*) from mRule where idModel = " + algo;
    		db.s.execute(sql);
    		rs = db.s.getResultSet();
    		if((rs!=null) && (rs.next()))cnt = rs.getInt(1);
    	}catch (Exception e){
    		System.out.println(e.getMessage());
       	}
    	return cnt;
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
