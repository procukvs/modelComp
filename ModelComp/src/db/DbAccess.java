package db;

import java.sql.*;

//import java.text.*;
//import java.util.*;
import org.sqlite.*;

public class DbAccess {
	private Connection conn = null;
	private Statement s; 
	private String sql;
	private  ResultSet rs;
	// nameDB = "" + "Is driver" + "No driver" + nameDB
  	private String nameDB = "";
	public DbAccess(){ 
		try
	    {
		 // Class.forName("com.mysql.jdbc.Driver");
		  Class.forName("org.sqlite.JDBC");
		  nameDB = "Is driver";
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
}
