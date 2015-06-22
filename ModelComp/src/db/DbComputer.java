package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbComputer {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Computer model; 
	
	DbComputer(DbAccess db){
		this.db = db; 
	}
		
	public Computer getComputer(int id){
		model= null;
		sql = "select name, rank, descr from rComputer where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Computer(id, rs.getString(1));
            	model.rank = rs.getInt(2);
            	model.descr = rs.getString(3);
            	model.program = getAllInstruction(id);
            	//model.extend();
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getComputer:" + e.getMessage());
		}
		return model;
	}
	
	private ArrayList getAllInstruction(int rec) {
		ArrayList program = new ArrayList();
		sql = "select num, cod, reg1, reg2, next, txComm, id, idModel " +
		      "  from rInstruction where idModel = " + rec + " order by num";
		boolean isConst;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	            Instruction f = new Instruction(rs.getInt(1), rs.getString(2), rs.getInt(3), 
	            								rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getInt(7)); 
	            //	System.out.println(f.toString());
				program.add(f);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllInstruction :" + sql + e.getMessage());
		}
		return program;
	}
	
	public void newInstruction(int comp, Instruction inst) {
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "update rInstruction set num = num+1" + "	where idModel = " + comp + " and num >= " + inst.getNum();
				db.s.execute(sql);	 
				sql = "insert into rInstruction values(" + comp + "," + inst.getId() + "," + inst.getNum() +	",'" + inst.getCod() + "',"
				 		+ inst.getReg1() + "," + inst.getReg2() + "," + inst.getNext() + ",'" + inst.gettxComm() + "')";
				db.s.execute(sql);
				sql = "update rInstruction set next = next+1 " + "	where idModel = " + comp + " and cod = 'J' and next >= " + inst.getNum();
				db.s.execute(sql);	
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR:newInstruction: " + e.getMessage() );
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}

	public void editInstruction(int comp, Instruction inst) {
		 try{	
			sql = "update rInstruction set cod = '" + inst.getCod() + "'," + "reg1 = " + inst.getReg1() + 
		 			", reg2 = " +	inst.getReg2() + ", next = " + inst.getNext() + ", txComm = '" + inst.gettxComm() + "'" +
		 			"	where idModel = " + comp + " and id = " + inst.getId() ;
		 	//System.out.println("Db.."+sql);
		 	db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: editDErive: " + e.getMessage() );
		 }  			
	}
	
	public void deleteInstruction(int comp, Instruction inst){
		try {
			db.conn.setAutoCommit(false);
			try{	
				sql = "delete from rInstruction "  + " where idModel = " + comp + " and id = " + inst.getId();
				//System.out.println("deteDerive id = " + rule.getId() + " num = " + rule.getNum());
				db.s.execute(sql);
				sql = "update rInstruction set num = num-1 " + "	where idModel = " + comp + " and num >= " + inst.getNum();
				db.s.execute(sql);	
				sql = "update rInstruction set next = next-1 " + "	where idModel = " + comp + " and cod = 'J' and next > " + inst.getNum();
				db.s.execute(sql);	
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteInstruction: " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	//--------------------------------- all for insert !!!!!!!!!!
	// âñòàâèòè â ïğîãğàìó êîìïşòåğà idModel ïğîãğàìó êîìïşòåğà ins ï³ñëÿ êîìàíäè where
	//  ....maxId - íàéá³ëüøèé id â ïğîãğàì³ idModel
	public String insertComputer(int idModel, int where, String ins, int maxId) {
		String res = "";
		int idIns = getIdComputer(ins);
		int k = getSizeProgram(idIns);
		if ((idIns > 0) && (k>0)){
			try {
				db.conn.setAutoCommit(false);
				try{	
					
					sql = "update rInstruction set num = num + " + k + " where idModel = " + idModel + " and num > " + where;
					db.s.execute(sql);	
					sql = "update rInstruction set next = next+ " + k + "	where idModel = " + idModel + " and cod = 'J' and next > " + where;
					db.s.execute(sql);	
					//sql = "delete from rInstruction "  + " where idModel = " + comp + " and id = " + inst.getId();
					//System.out.println("deteDerive id = " + rule.getId() + " num = " + rule.getNum());
					//db.s.execute(sql);
					sql = "insert into rInstruction select " + idModel + ",id+" + maxId + ",num+" + where + ", cod, reg1, reg2, next, txComm "+ 
							" from  rInstruction where idModel = " + idIns;
					db.s.execute(sql);	
					sql = "update rInstruction set next = next+ " + where + 
							" where idModel = " + idModel + " and cod = 'J' and num > " + where + " and num < " + (where + k+1);
					db.s.execute(sql);	
					db.conn.commit();
				}	catch (Exception e) {
					System.out.println("ERROR: insertComputer: " + e.getMessage());
					db.conn.rollback();
				}  
				db.conn.setAutoCommit(true);
			}	
			catch (Exception e) { System.out.println(e.getMessage());}		
		}
		else {
			if (idIns == 0)	res = "insertComputer: Íå çíàéäåíî ïğîãğàìó ç ³ìåíåì " + ins + "!" ;
			else res = "insertComputer: Ïğîãğàìà êîìï'şòåğó " + ins + " íå ì³ñòèòü æîäíî¿ êîìàíäè!" ;
		}
		return res;
	}
	
	private int getIdComputer(String name) {
		int res = 0;
		try{ 
			sql = "select id from rComputer where name = '" + name + "'";
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	res= rs.getInt(1); 
            }
		}catch (Exception e){
			System.out.println("ERROR: DbComputer : getIdComputer :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return res;	
	}	
	
	private int getSizeProgram(int id) {
		int res = 0;
		try{ 
			sql = "select count(*) from rInstruction where idModel = " + id ;
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	res= rs.getInt(1); 
            }
		}catch (Exception e){
			System.out.println("ERROR: DbComputer : getSizeProgram :" + sql);
			System.out.println(">>> " + e.getMessage());
		}
		return res;	
	}	
	
}
