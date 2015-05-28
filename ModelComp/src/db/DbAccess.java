package db;

import java.sql.*;
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
	
	public int getNumber(String type, int order){
		int number = 0;
		int i=1; 
		sql = "select id from " + tableModel(type) + " order by id";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && (i < order)) { i++; }
            if (rs != null) number = rs.getInt(1);	       
		}catch (Exception e){
			System.out.println("ERROR: getNumber :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return number;
	}
	
	public Model getModel(String type, int id){
		switch(type){
		case "Algorithm" : return dbAlgo.getAlgorithm(id);
		default: return null;
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
		default: return "mAlgorithm";
		}
	}
	
}
