package db;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;


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
  	private DbAlgorithm dbAlgo;
  	
	public DbAccess(){ 
		try
	    {
		 // Class.forName("com.mysql.jdbc.Driver");
		  Class.forName("org.sqlite.JDBC");
		  nameDB = "Is driver";
		  dbAlgo = new DbAlgorithm(this);
	     }
		catch(Exception ex)
        {
 			nameDB = "No driver";
            System.out.println("ERROR: Could not form DataBase .");
			System.out.println(">>> " + ex.getMessage());
        } 	
	}
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
		sql = "select id from " + tableModel(type) + " order by id";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && (i < order)) {
	        	number = rs.getInt(1);	
	        	i++; 
	        }
	        if (rs != null) number = rs.getInt(1);	       
		}catch (Exception e){
			System.out.println("ERROR: getNumber :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return number;
	}
	
	// знаходить порядковий номер моделі типа type з номером id
	public int getOrder(String type, int id){
		int number = 0;
		boolean go = true;
		sql = "select id from " + tableModel(type) + " order by id";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && go) {
	        	number++;	
	        	go = !(rs.getInt(1) == id); 
	        }
		}catch (Exception e){
			System.out.println("ERROR: getNumber :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return number;
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
		}	
		return idModel;
	}
	
	
	public void editModel(String type, Model model) {
		switch(type){
		case "Algorithm" : dbAlgo.editAlgorithm((Algorithm)model); break;
		}	
	}
	
	public int newModel(String type) {
		int idModel = 0;
		switch(type){
		case "Algorithm" : idModel = dbAlgo.newAlgorithm(); break;
		}	
		return idModel;
	}
	
	public void deleteModel(String type, Model model) {
		switch(type){
		case "Algorithm" : dbAlgo.deleteAlgorithm((Algorithm)model); break;
		}	
	}
	
	public int newModelAs(String type, Model model) {
		int idModel = 0;
		switch(type){
		case "Algorithm" : idModel = dbAlgo.newAlgorithmAs((Algorithm)model); break;
		}	
		return idModel;
	}
	
	public Model getModel(String type, int id){
		switch(type){
		case "Algorithm" : return dbAlgo.getAlgorithm(id);
		default: return null;
		}
	}
	
	public void editCommand(String type, int idModel, int id, Command cmd){
		switch(type){
		case "Algorithm" : dbAlgo.editRule(idModel, id, (Rule)cmd); break;
		default: System.out.println(">>> Not realise editCommand for: " + type + "!");
		}
	}
	
	public void newCommand(String type, int idModel, int id, Command cmd){
		switch(type){
		case "Algorithm" : dbAlgo.newRule(idModel, id, (Rule)cmd); break;
		default: System.out.println(">>> Not realise newCommand for: " + type + "!");
		}
	}
	
	public void deleteCommand(String type, int idModel, int id){
		switch(type){
		case "Algorithm" : dbAlgo.deleteRule(idModel, id); break;
		default: System.out.println(">>> Not realise deleteCommand for: " + type + "!");
		}
	}
	
	public void moveDown(String type, int idModel, int id){
		switch(type){
		case "Algorithm": dbAlgo.moveDown(idModel, id); break;
		default: break;
		}
	}
	
	public void moveUp(String type, int idModel, int id){
		switch(type){
		case "Algorithm": dbAlgo.moveUp(idModel, id); break;
		default: break;
		}
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
		default: return "mAlgorithm";
		}
	}
	
}
