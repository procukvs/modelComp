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
	private static DbComputer dbComp;
	private static DbCalculus dbCalc;
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
		  dbComp = new DbComputer(this);
		  dbCalc = new DbCalculus(this);
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
	public static DbComputer getDbComputer() { return dbComp;}
	public static DbCalculus getDbCalculus() { return dbCalc;}
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
		     // System.out.println("Is connection to DB " + nameDB);
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
		        //System.out.println("Is disconnecting from DB " + nameDB);
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
			sql = "select count(*) from " + tableModel(type) + 
			      " where section = '" + Parameters.getSection() + "'";
			s.execute(sql);
			rs = s.getResultSet();
            if((rs!=null) && (rs.next()))cnt = rs.getInt(1);
       	}
		catch (Exception e) {System.out.println("ERROR:getModelCount:  " + e.getMessage());}
		return cnt;
	}
	
	public String getModelName(String type, int idModel) {
		String name = "";
		try{
			sql = "select name from " + tableModel(type) + " where id = " + idModel;
			s.execute(sql);
			rs = s.getResultSet();
            if((rs!=null) && (rs.next()))name = rs.getString(1);
       	}
		catch (Exception e) {System.out.println(e.getMessage());}
		return name;
	}
	
	// знаходить id моделі типа type, яка по порядку order
	public int getNumber(String type, int order){
		int number = 0;
		int i=1; 
		int order1 = order;
		if (order1<= 0) order1++;  
		sql = "select id from " + tableModel(type) + 
			  " where section = '" + Parameters.getSection() + "' order by name";
		//System.out.println("Initial==: getNumber :" + sql + " order = " + order);
		try{ 
			s.execute(sql);	
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next()) && (i <= order1))  {                       
	           	number = rs.getInt(1);	
	        	i++;
	        	//System.out.println("Initial==C: getNumber :" + sql + " i = " + i  + " number = " + number);
	       }
	   	}catch (Exception e){
			System.out.println("ERROR: getNumber :" + sql + " "  + e.getMessage());
		}
		//System.out.println("final =: getNumber : number = " + number);
		return number;
	}
	
	// знаходить порядковий номер моделі типа type з іменем name
	public int getOrder(String type, String name){
		int number = 0;
		boolean go = true;
		sql = "select name from " + tableModel(type) + 
			  " where section = '" + Parameters.getSection() + "' order by name";
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
	
	// знаходить інформацію про всі моделей даного типу і даного розділу 
	public ArrayList <Pair> getInfModels(String type, String section){
		ArrayList <Pair> all = new ArrayList <Pair> ();
		//boolean go = true;
		sql = "select id, name from " + tableModel(type) + 
			  " where section = '" + section + "' order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	all.add(new Pair(rs.getInt(1),rs.getString(2)));
		    }
		}catch (Exception e){
			System.out.println("ERROR: getInfModels :" + sql + e.getMessage());
		}
		return all;
	}
	
	
	// знаходить список всіх моделей даного типу 
	public ArrayList <String> getAllModel(String type){
		ArrayList <String> all = new ArrayList <String> ();
		//boolean go = true;
		sql = "select name from " + tableModel(type) + 
			  " where section = '" + Parameters.getSection() + "' order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	all.add(new String (rs.getString(1)));
		    }
		}catch (Exception e){
			System.out.println("ERROR: getAllModel :" + sql + e.getMessage());
		}
		return all;
	}
	
	// знаходить список всіх моделей для виводу 
	public ArrayList getAllModel(){
		ArrayList all = new ArrayList ();
		ArrayList <String> sl = new ArrayList <String> ();  /// for all section !!
		ArrayList row;
		String selectSec = "select value from pParameters where name = 'Section' order by value";
		String[] select = {
				"select 'Algorithm', name, descr, isNumeric, rank, id from mAlgorithm ", //order by name",
				"select 'Computer', name, descr, 1, rank,  id from rComputer ", //  order by name",
				"select 'Machine', name, descr, isNumeric, rank, id from tMachine ", // order by name",
				"select 'Post', name, descr, isNumeric, rank,  id from pPost ", // order by name",
				"select 'Recursive', name, descr, 0, 0,  id from fRecursive ", // order by name"
				"select 'Calculus', name, descr, 0, 0,  id from eCalculus " // order by name"
			};
		String section = "base";
		try{ 
			ResultSet rs;
			if (Parameters.getRegime().equals("teacher")) {
				s.execute(selectSec);
				rs = s.getResultSet();
				while((rs!=null) && (rs.next())) {
					sl.add(rs.getString(1));
				}
			} else sl.add("base");	
			for (int j = 0; j < sl.size(); j++){
				section = sl.get(j);
				for (int i = 0; i < select.length; i++ ){
					sql = select[i] + "where section = '" + section + "' order by name";
					//System.out.println("getAllModel " + sql );
					s.execute(sql);
					rs = s.getResultSet();
					while((rs!=null) && (rs.next())) {
						row = new ArrayList();
						if (Parameters.getRegime().equals("teacher")) row.add(section);
						row.add(rs.getString(1));
						row.add(rs.getString(2));
						row.add(rs.getString(3));
						row.add(rs.getBoolean(4));
						row.add(rs.getInt(5));
						row.add(false);
						row.add(rs.getInt(6));
						all.add(row);
					}
				}	
			}
		}catch (Exception e){
			System.out.println("ERROR: getAllModel :" + e.getMessage());
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
		     sql = "select count(*) from " + tableModel(type) + 
		    	   " where section = '" + Parameters.getSection() + "' and name = '" + name +"'";
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
	//  .... в розділі section =  Parameters.getSection()
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
		case "Computer" : idModel = dbComp.addComputer((Computer)model); break; 
		case "Algorithm" : idModel = dbAlgo.addAlgorithm((Algorithm)model); break; 
		case "Machine" : idModel = dbMach.addMachine((Machine)model); break; 
		case "Post" : idModel = dbPost.addPost((Post)model); break; 
		case "Recursive" : idModel = dbRec.addRecursive((Recursive)model); break; 
		case "Calculus" : idModel = dbCalc.addCalculus((Calculus)model); break; 
		}	
		//System.out.println("db.addModel: type " + type + " id " + idModel); 
		return idModel;
	}
	
	
	public void editModel(String type, Model model) {
		switch(type){
		case "Computer" : dbComp.editComputer((Computer)model); break;
		case "Algorithm" : dbAlgo.editAlgorithm((Algorithm)model); break;
		case "Machine" : dbMach.editMachine((Machine)model); break;
		case "Post" : dbPost.editPost((Post)model); break;
		case "Recursive": dbRec.editRecursive((Recursive)model); break;
		case "Calculus": dbCalc.editCalculus((Calculus)model); break;
		}	
	}
	public int newModel(String type) {
		int idModel = 0;
		switch(type){
		case "Computer" : idModel = dbComp.newComputer(); break;
		case "Algorithm" : idModel = dbAlgo.newAlgorithm(); break;
		case "Machine" : idModel = dbMach.newMachine(); break;
		case "Post" : idModel = dbPost.newPost(); break;
		case "Recursive" : idModel = dbRec.newRecursive(); break;
		case "Calculus" : idModel = dbCalc.newCalculus(); break;
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
		case "Recursive": return dbRec.getRecursive(id); 
		case "Computer": return dbComp.getComputer(id);
		case "Calculus": return dbCalc.getCalculus(id); 
		default: return null;
		}
	}
	
	public ArrayList getAllNameFunction(String type, String nmSet){
		ArrayList nmFunct = new ArrayList();
		switch(type){
		case "Recursive": 
		case "Calculus": 
			sql = "select name from " + tableCommand(type) +
				   " where idModel = (select id from " + tableModel(type) + " where name = '" + nmSet + "'" +
				                      " and section = '" + Parameters.getSection()+ "')" ; //+
				  // " order by num";
			//System.out.println("test: getAllNameFunction :" + sql);
			try{ 
				db.s.execute(sql);
		        ResultSet rs = db.s.getResultSet();
		        while((rs!=null) && (rs.next())) {
					nmFunct.add(rs.getString(1));
		        } 
			}catch (Exception e){
				System.out.println("ERROR: getAllNameFunction :" + sql + e.getMessage());
			}
			return nmFunct;
			//return dbCalc.getAllNameLambda(nmSet); 
		default: return null;
		}
	}	
	
	public void editCommand(String type, Model model, int id, Command cmd){
		switch(type){
		case "Computer" : dbComp.editInstruction(model.id, (Instruction)cmd); break;
		case "Algorithm" : dbAlgo.editRule(model.id, id, (Rule)cmd); break;
		case "Machine": dbMach.editState((Machine)model, id, (State)cmd); break;
		case "Post" : dbPost.editDerive(model.id, (Derive)cmd); break;
		case "Recursive" : dbRec.editFunction(model.id, (main.Function)cmd); break;
		case "Calculus" : dbCalc.editDeclLambda(model.id, (main.LambdaDecl)cmd); break;
		default: System.out.println(">>> Not realise editCommand for: " + type + "!");
		}
	}
	
	public void newCommand(String type, Model model, Command cmd){
		switch(type){
		case "Computer" : dbComp.newInstruction((Computer)model, (Instruction)cmd); break;
		case "Algorithm" : dbAlgo.newRule((Algorithm)model, (Rule)cmd); break;
		case "Machine": dbMach.newState((Machine)model, (State)cmd); break;
		case "Post" : dbPost.newDerive((Post)model, (Derive)cmd); break;
		case "Recursive" : dbRec.newFunction(model.id, (main.Function)cmd); break;
		case "Calculus" : dbCalc.newDeclLambda(model.id, (main.LambdaDecl)cmd); break;
		default: System.out.println(">>> Not realise newCommand for: " + type + "!");
		}
	}
	
	public void deleteCommand(String type, int idModel, int id, Command cmd){
		switch(type){
		case "Computer" : dbComp.deleteInstruction(idModel, (Instruction)cmd); break;
		case "Algorithm" : dbAlgo.deleteRule(idModel, (Rule)cmd); break;
		case "Machine": dbMach.deleteState(idModel, id); break;
		case "Post" : dbPost.deleteDerive(idModel, (Derive)cmd); break;
		case "Recursive": dbRec.deleteFunction(idModel, id); break;	
		case "Calculus": dbCalc.deleteDeclLambda(idModel, (LambdaDecl)cmd); break;	
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
	
	public ArrayList getDataSource_NoUse(String type, int id){
		switch(type){
		case "Algorithm" : return dbAlgo.getDataSource_NoUse(id);
		default: return null;
		}
	}
	
	private String tableModel(String type){
		switch(type){
		case "Machine": return "tMachine";
		case "Post" : return "pPost";
		case "Recursive" : return "fRecursive";
		case "Computer" : return "rComputer";
		case "Calculus" : return "eCalculus";
		default: return "mAlgorithm";
		}
	}
	
	private String tableCommand(String type){
		switch(type){
		case "Computer" : return "rInstruction";
		case "Machine": return "tProgram";
		case "Post" : return "pDerive";
		case "Recursive" : return "fFunction";
		case "Calculus" : return "eLambda";
		default: return "mRule";
		}
	}
	
	//==================================================
	//... робота з параметрами  --- таблиці pState + pParameters
	//... лише в режимі teacher !!!!
	//==================================================
	// встановлення глобальних параметрів -- при запуску програми + при змінні pState !! 
	public void setParameters(){
		sql = "select name, value  from pState order by name";
		try{ 
			s.execute(sql);
	        ResultSet rs = s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	//System.out.println("setParameters:"+ rs.getString(1) + "- " + rs.getString(2));
	        	switch(rs.getString(1)){
	    		case "Section": Parameters.setSection(rs.getString(2)); break;
	    		case "PostVar" : Parameters.setPostVar(rs.getString(2)); break;
	    		case "RecurSubst" : Parameters.setRecurSubst(rs.getString(2)); break;
	    		case "Version" : Parameters.setVersion(rs.getString(2)); break;
	    		default: System.out.println("Db:setParameters: невідомий параметр - "
	    		                     + rs.getString(1) + ", значення - " + rs.getString(2));
	    		}
	        }
		}catch (Exception e){
			System.out.println("ERROR: setParameters :" + e.getMessage());
		}
	}

	// знаходить стан всіх параметрів 
	public ArrayList getStateParameter(){
		ArrayList state = new ArrayList ();
		ArrayList row;
		String select = "select name, value, descr from pState where name <> 'Regime' order by name";
		try{ 
			//System.out.println("getAllModel " + sql );
			s.execute(select);
			ResultSet rs = s.getResultSet();
			while((rs!=null) && (rs.next())) {
				row = new ArrayList();
				row.add(rs.getString(1));
				row.add(rs.getString(2));
				row.add(rs.getString(3));
				state.add(row);
			}
		}catch (Exception e){
			System.out.println("ERROR: getStateParameter :" + e.getMessage());
		}
		return state;
	}
	
	// знаходить всі значення всіх параметрів 
	public ArrayList getAllParameter(){
		ArrayList all = new ArrayList ();
		ArrayList row;
		String select = "select name, value, descr from pParameters where name <> 'Regime' order by name, value";
		try{ 
			s.execute(select);
			ResultSet rs = s.getResultSet();
			while((rs!=null) && (rs.next())) {
				row = new ArrayList();
				row.add(rs.getString(1));
				row.add(rs.getString(2));
				row.add(rs.getString(3));
				all.add(row);
			}
		}catch (Exception e){
			System.out.println("ERROR: getAllParameter :" + e.getMessage());
		}
		return all;
	}		

		
	// знаходить всі значення параметра name 
	public ArrayList getParameterValue(String name){
		ArrayList all = new ArrayList ();
		String select = "select value from pParameters where name = '" + name + "' order by value";
		try{ 
			//System.out.println("getAllModel " + sql );
			s.execute(select);
			ResultSet rs = s.getResultSet();
			while((rs!=null) && (rs.next())) {
				all.add(rs.getString(1));
			}
		}catch (Exception e){
			System.out.println("ERROR: getParameterValue :" + e.getMessage());
		}
		return all;
	}		
	
	// перевіряє чи є в таблиці вказане значення параметру
	public boolean isParameterValue (String name, String value) {
		boolean is = false;
		try{
			sql = "select descr from pParameters where name = '" + name + "' and value = '" + value + "'";
			s.execute(sql);
			rs = s.getResultSet();
	        if((rs!=null) && (rs.next()))is = true;
	   	}
		catch (Exception e) {System.out.println("ERROR: isParameterValue: " + e.getMessage());}
		return is;		
	}
	
	//  додає нове значення параметру
	public void setParameterValue (String name, String value, String desc) {
		try{	
			sql = "insert into pParameters values('" + name + "', '" + value + "', '" + desc + "')";
			s.execute(sql);
		} catch (SQLException e) {
			System.out.println("ERROR: setParameterValue: " + e.getMessage());
	    }  	
	}
	
	//  встановлює нове значення параметру в стані
	public void updateStateParameter (String name, String value) {
		String desc = "";
		try{
			sql = "select descr from pParameters where name = '" + name + "' and value = '" + value + "'";
			s.execute(sql);
			rs = s.getResultSet();
	        if((rs!=null) && (rs.next()))desc = rs.getString(1);
			sql = "update pState  set value = '" + value + "', descr = '" + desc + "' where name = '" + name + "' ";
			s.execute(sql);
		} catch (SQLException e) {
			System.out.println("ERROR: updateStateParameter: " + e.getMessage());
	    }  	
	}

	//  модифікує опис (коментар) вказаного значення параметру
	public void updateParameterDesc (String name, String value, String desc) {
		try{	
			sql = "update pParameters set descr = '" + desc + "'" + 
		          " where name = '" + name + "' and value = '" + value + "'";
			s.execute(sql);
			sql = "update pState set descr = '" + desc + "'" + 
			          " where name = '" + name + "' and value = '" + value + "'";
				s.execute(sql);	
		} catch (SQLException e) {
			System.out.println("ERROR: updateParameterDesc: " + e.getMessage());
	    }  	
	}	
	
	// підраховує кількість моделей в розділі section
	public int getSectionCount(String section) {
		int cnt = 0;
		String [] table = {"mAlgorithm", "rComputer", "tMachine", "pPost", "fRecursive", "eCalculus"};
		try{
			for (int i = 0; i < table.length; i++){
				sql = "select count(*) from " + table[i] + " where section = '" + section + "'";
				s.execute(sql);
				rs = s.getResultSet();
				if((rs!=null) && (rs.next()))cnt = cnt + rs.getInt(1);
			}
       	}
		catch (Exception e) {System.out.println("ERROR: getSectionCount: " + e.getMessage());}
		return cnt;
	}	
	//  вилучає вказане значення параметру
	public void deleteParameterValue (String name, String value) {
		try{	
			sql = "delete from pParameters where name = '" + name + "' and value = '" + value + "'";
			s.execute(sql);
			sql = "update pState set value = 'Nothing', descr = 'Параметр вилучено !!!'" + 
			          " where name = '" + name + "' and value = '" + value + "'";
				s.execute(sql);	
		} catch (SQLException e) {
			System.out.println("ERROR: deleteParameterValue: " + e.getMessage());
	    }  	
	}		
}
