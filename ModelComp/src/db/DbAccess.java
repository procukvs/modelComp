package db;

import java.sql.*;
import java.text.*;
import java.util.*;

//import java.text.*;
//import java.util.*;
import org.sqlite.*;

import java.util.*;

import main.*;

public class DbAccess {
	protected Connection conn = null;
	protected Statement s; 
	private String sql;
	private  ResultSet rs;
	// nameDB = "" + "Is driver" + "No driver" + nameDB
  	private String nameDB = "";
  	//=========================================================
  	/*private DbAlgorithm dbAlgo;
  	private DbMachine dbMach;
  	
	public DbAccess(){ 
		try
	    {
		 // Class.forName("com.mysql.jdbc.Driver");
		  Class.forName("org.sqlite.JDBC");
		  nameDB = "Is driver";
		  dbAlgo = new DbAlgorithm(this);
		  dbMach = new DbMachine(this);
	     }
		catch(Exception ex)
        {
 			nameDB = "No driver";
            System.out.println("ERROR: Could not form DataBase .");
			System.out.println(">>> " + ex.getMessage());
        } 	
	} */
	//=========================================================
  	// realizy singleton !!!
	private static DbAccess db;
	private static DbAlgorithm dbAlgo;
  	private static DbMachine dbMach;
	private static DbPost dbPost;
	private static DbRecursive dbRec;
	private DbAccess(){ 
		try
	    {
		 // Class.forName("com.mysql.jdbc.Driver");
		  Class.forName("org.sqlite.JDBC");
		  nameDB = "Is driver";
		  dbAlgo = new DbAlgorithm(this);
		  dbMach = new DbMachine(this);
		  dbPost = new DbPost(this);
		  dbRec = new DbRecursive(this);
	     }
		catch(Exception ex)
        {
 			nameDB = "No driver";
            System.out.println("ERROR: Could not form DataBase " + ex.getMessage());
	     } 	
	}
	public static DbAccess getDbAccess() {
		if(db == null) db = new DbAccess();
		return db;
	}
	public static DbAlgorithm getDbAlgorithm() { return dbAlgo;}
	public static DbMachine getDbMachine() { return dbMach;}
	public static DbPost getDbPost() { return dbPost;}
	public static DbRecursive getDbRecursive() { return dbRec;}
	//======================================================
	
	
	public Connection getConnection(){
		return conn;
	}
	public boolean connectionDb(String nmDb){
	       try{ 
		     // conn = DriverManager.getConnection("jdbc:mysql://localhost/" + nmDb + "?"
             //                                     + "user=vprots&password=vprots");
		      conn = DriverManager.getConnection("jdbc:sqlite:" + nmDb);
		      s = conn.createStatement();
		      nameDB = nmDb;
		      System.out.println("Is connection to DB " + nameDB);
		      return true;
	        } catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database " + nmDb + ".");
				System.out.println(">>> " + e.getMessage());
				return false;
		    } 
	}
	public void disConnect(){
		 try{
		        conn.close();	
		        conn = null;
		        System.out.println("Is disconnecting from DB " + nameDB);
		        nameDB = "Is driver"; 
	        } catch (SQLException e) {
				System.out.println("ERROR: Could not disconnect.");
				System.out.println(">>> " + e.getMessage());
				//e.printStackTrace();
		    }  
	}
	public String getNameDB(){
		return nameDB;
	}
	
	public int getModelCount(String type) {
		int cnt = 0;
		try{
			sql = "select count(*) from " + tableModel(type);
			s.execute(sql);
			rs = s.getResultSet();
            if((rs!=null) && (rs.next()))cnt = rs.getInt(1);
       	}
		catch (Exception e) {System.out.println(e.getMessage());}
		return cnt;
	}
	
	// знаходить id моделі типа type, яка по порядку order
	public int getNumber(String type, int order){
		int number = 0;
		int i=1; 
		sql = "select id from " + tableModel(type) + " order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && (i < order)) {
	        	number = rs.getInt(1);	
	        	i++; 
	        }
	        if (rs != null) number = rs.getInt(1);	       
		}catch (Exception e){
			System.out.println("ERROR: getNumber :" + sql + " "  + e.getMessage());
		}
		return number;
	}
	
	// знаходить порядковий номер моделі типа type з іменем name
	public int getOrder(String type, String name){
		int number = 0;
		boolean go = true;
		sql = "select name from " + tableModel(type) + " order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && go) {
	        	number++;	
	        	go = !(rs.getString(1).equals(name)); 
	        }
		}catch (Exception e){
			System.out.println("ERROR: getOrder :" + sql + "" + e.getMessage());
		}
		return number;
	}
	
	// знаходить список всіх моделей даного типу 
	public ArrayList <String> getAllModel(String type){
		ArrayList <String> all = new ArrayList <String> ();
		//boolean go = true;
		sql = "select name from " + tableModel(type) + " order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	all.add(new String (rs.getString(1)));
		    }
		}catch (Exception e){
			System.out.println("ERROR: getAllModel :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return all;
	}
	
	//знаходить найбільший номер у існуючих моделей типа type 
	public int maxNumber(String type){
		int i = 0;
		try{
			sql = "select max(id) from " + tableModel(type);
			s.execute(sql);
			rs = s.getResultSet();
	        if((rs!=null) && (rs.next()))i = rs.getInt(1);
	   	}
		catch (Exception e) {System.out.println(e.getMessage());}
		return i;
	}	
	
	public boolean isModel(String type, String name){
    	try{ int cnt = 0;
		     sql = "select count(*) from " + tableModel(type) + " where name = '" + name +"'";
             s.execute(sql);
             rs = s.getResultSet();
             if((rs!=null) && (rs.next()))cnt = rs.getInt(1);
             return (cnt > 0);
    	}catch (Exception e){
    		System.out.println(e.getMessage());
    		return false;
    	}
	}
	
	//знаходить імя моделі типа type по замовчуванню : перше вільне з "base00", "base01",...
	public String findName(String type, String base){
		int i = 0;
		NumberFormat suf = new DecimalFormat("00"); 
		boolean isUse;
		String name;
		do {
			i++;
			name = base + suf.format(i);
			isUse = isModel(type, name);
		} while (isUse);
		return name;
	}
	
	// додає введену з файлу модель model типа type (включаючи її програму)
	public int addModel(String type, Model model) {
		int idModel = 0;
		switch(type){
		case "Algorithm" : idModel = dbAlgo.addAlgorithm((Algorithm)model); break; 
		case "Machine" : idModel = dbMach.addMachine((Machine)model); break; 
		case "Post" : idModel = dbPost.addPost((Post)model); break; 
		}	
		return idModel;
	}
	
	
	public void editModel(String type, Model model) {
		switch(type){
		case "Algorithm" : dbAlgo.editAlgorithm((Algorithm)model); break;
		case "Machine" : dbMach.editMachine((Machine)model); break;
		case "Post" : dbPost.editPost((Post)model); break;
		}	
	}
	public int newModel(String type) {
		int idModel = 0;
		switch(type){
		case "Algorithm" : idModel = dbAlgo.newAlgorithm(); break;
		case "Machine" : idModel = dbMach.newMachine(); break;
		case "Post" : idModel = dbPost.newPost(); break;
		}	
		return idModel;
	}
/*	
	public void deleteModel(String type, Model model) {
		switch(type){
		case "Algorithm" : dbAlgo.deleteAlgorithm((Algorithm)model); break;
		case "Machine" : dbMach.deleteMachine((Machine)model); break;
		}	
	}
*/	
	/*
	public int newModelAs(String type, Model model) {
		int idModel = 0;
		switch(type){
		case "Algorithm" : idModel = dbAlgo.newAlgorithmAs((Algorithm)model); break;
		}	
		return idModel;
	} */
	
	public Model getModel(String type, int id){
		//System.out.println(" getModel :" + type + " " + id);
		switch(type){
		case "Algorithm" : return dbAlgo.getAlgorithm(id);
		case "Machine": return dbMach.getMachine(id); 
		case "Post": return dbPost.getPost(id); 
		default: return null;
		}
	}
	
	public void editCommand(String type, Model model, int id, Command cmd){
		switch(type){
		case "Algorithm" : dbAlgo.editRule(model.id, id, (Rule)cmd); break;
		case "Machine": dbMach.editState((Machine)model, id, (State)cmd); break;
		case "Post" : dbPost.editDerive(model.id, (Derive)cmd); break;
		default: System.out.println(">>> Not realise editCommand for: " + type + "!");
		}
	}
	
	public void newCommand(String type, Model model, Command cmd){
		switch(type){
		case "Algorithm" : dbAlgo.newRule(model.id, (Rule)cmd); break;
		case "Machine": dbMach.newState((Machine)model, (State)cmd); break;
		case "Post" : dbPost.newDerive(model.id, (Derive)cmd); break;
		default: System.out.println(">>> Not realise newCommand for: " + type + "!");
		}
	}
	
	public void deleteCommand(String type, int idModel, int id, Command cmd){
		switch(type){
		case "Algorithm" : dbAlgo.deleteRule(idModel, id); break;
		case "Machine": dbMach.deleteState(idModel, id); break;
		case "Post" : dbPost.deleteDerive(idModel, (Derive)cmd); break;
		default: System.out.println(">>> Not realise deleteCommand for: " + type + "!");
		}
	}
	/*
	public void moveDown(String type, int idModel, int id){
		switch(type){
		case "Algorithm": dbAlgo.moveDown(idModel, id); break;
		default: break;
		}
	}
	*/
	public void moveUp(String type, Model model, int row){
		String table = tableCommand(type);
		int idModel = model.id;
		try {
			db.conn.setAutoCommit(false);
			try{
				if (row > 1){
					int prev = row-1; 
					sql = "update " + table + " set num = 0  " +  "	where idModel = " + idModel + " and num = " + row ;
					//System.out.println("moveUp row = " + row + " prev = " + prev);
					db.s.execute(sql);
					sql = "update " + table + " set num =  " + row +  "	where idModel = " + idModel + " and num = " + prev;
					//System.out.println("2:" + sql);
					db.s.execute(sql);	
					sql = "update " + table + " set num =  " + prev + "	where idModel = " + idModel + " and num = 0";
					//System.out.println("3:" + sql);
					db.s.execute(sql);
				}
				db.conn.commit();
			} catch (Exception e) {
				System.out.println("ERROR: moveUp: "+ e.getMessage());
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}

	public void moveDown(String type, Model model, int row) {
		String table = tableCommand(type);;
		int idModel = model.id;
		try {
			db.conn.setAutoCommit(false);
			try{    // take count rules 
				int cnt = model.program.size();
				if (row < cnt){
					int next = row+1; 
					sql = "update " + table + " set num = 0 where idModel = " + idModel + " and num = " + row ;
					//System.out.println("moveDown row = " + row + " next = " + next);
					db.s.execute(sql);
					sql = "update " + table + " set num =  " + row +  "	where idModel = " + idModel + " and num = " + next;
					//System.out.println("2:" + sql);
					db.s.execute(sql);	
					sql = "update " + table + " set num =  " + next + " where idModel = " + idModel + " and num = 0";
					//System.out.println("3:" + sql);
					db.s.execute(sql);
				}
				db.conn.commit();
		    } catch (Exception e) {
				System.out.println("ERROR: moveDown: " + e.getMessage());
				db.conn.rollback();
			} 
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	public ArrayList getDataSource(String type, int id){
		switch(type){
		case "Algorithm" : return dbAlgo.getDataSource(id);
		default: return null;
		}
	}
	
	private String tableModel(String type){
		switch(type){
		case "Computer" : return "mComputer";
		case "Machine": return "tMachine";
		case "Post" : return "pPost";
		default: return "mAlgorithm";
		}
	}
	
	private String tableCommand(String type){
		switch(type){
		case "Computer" : return "mProgram";
		case "Machine": return "tProgram";
		case "Post" : return "pDerive";
		default: return "mRule";
		}
	}
}
