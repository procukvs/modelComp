package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbComputer {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Statement st; 
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
	
	// створює нову машину з порожньою програмою
	public int newComputer() {
		String name = db.findName("Computer","Computer");
		int cnt = db.maxNumber("Computer")+1;
		int rows;
		String section = Parameters.getSection();
		try{
			sql = "insert into rComputer values(" + cnt +  ",'" + section + "','"  + name + "',1,'new')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
			System.out.println("ERROR: newComputer :" + sql + " "  + e.getMessage());
		}
		return cnt;
	}	

	// модифікує відредаговану машину
	public void editComputer(Computer model) {
		int rows;
		try{
			sql = "update rComputer set name = '" + model.name + "', " +
						" Rank = " + model.rank + ", descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editComputer: Не змінило відредаговану " + model.name + "!");
		}
		catch (Exception e) {
			System.out.println("ERROR: editComputer :" + e.getMessage());
		}
	}		
		
	
	// створює нову машину на основі машини з model (включаючи всю програму)
	public int newComputertAs(Computer model){
		String name = db.findName("Computer", model.name);
		int cnt = db.maxNumber("Computer")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into rComputer select " + cnt +  ", section, '"  + name + "'," + 
							" Rank, descr from rComputer where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				if (rows == 0) cnt =0;
				sql = "insert into rInstruction select " + cnt + ", id, num, cod,reg1, reg2, next, txComm " +
							" from rInstruction where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: newComputerAs :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}

	public boolean deleteComputer(Computer model){
		int rows;
		boolean res = false;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from rInstruction where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from rComputer where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
				res = true;
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: deleteComputer :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return res;
	}
	
	// додає введену з файлу машину model (включаючи всі команди програми)
	public int addComputer(Computer model){
		String name = model.name;
		int cnt = db.maxNumber("Computer")+1;
		int rows;
		Instruction r;
		String section = Parameters.getSection();
		if (db.isModel("Computer",name)) name = db.findName("Computer", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into rComputer values(" + cnt +  ",'" + section +  "','" + name + "'," + model.rank + ",'" + model.descr + "')";  
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					r = (Instruction)model.program.get(i);
					sql = "insert into rInstruction values(" + cnt + "," + (i+1) + "," + r.getNum() + ",'" + r.getCod() +
							"','" + r.getReg1() + "'," + r.getReg2() + "," + r.getNext() + ",'" + r.gettxComm() + "')";
					rows=rows + db.s.executeUpdate(sql);
				}
				if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addComputer :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}		
	
	public void newInstruction(Computer comp, Instruction inst) {
		int id = comp.id;
		int maxNum = comp.findMaxNum();
		try {
			db.conn.setAutoCommit(false);
			try{
				//sql = "update rInstruction set num = num+1" + "	where idModel = " + id + " and num >= " + inst.getNum();
				for (int i = maxNum; i >= inst.getNum(); i--){
					sql = "update rInstruction set num = num+1" + "	where idModel = " + id + " and num = " + i;
					db.s.execute(sql);
				}	
				sql = "insert into rInstruction values(" + id + "," + inst.getId() + "," + inst.getNum() +	",'" + inst.getCod() + "',"
				 		+ inst.getReg1() + "," + inst.getReg2() + "," + inst.getNext() + ",'" + inst.gettxComm() + "')";
				db.s.execute(sql);
				sql = "update rInstruction set next = next+1 " + "	where idModel = " + id + " and cod = 'J' and next >= " + inst.getNum();
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
				int cnt = getSizeProgram(comp);
				sql = "delete from rInstruction "  + " where idModel = " + comp + " and id = " + inst.getId();
				//System.out.println("deteDerive id = " + rule.getId() + " num = " + rule.getNum());
				db.s.execute(sql);
				//sql = "update rInstruction set num = num-1 " + "	where idModel = " + comp + " and num >= " + inst.getNum();
				//db.s.execute(sql);	
				
				// підтягнути правила що залишилися 
				for(int r = inst.getNum()+1; r <= cnt; r++) {
					sql = "update rInstruction set num = num-1" + "	where idModel = " + comp + " and num = " + r;
					//System.out.println("delete Instruction 2:" + sql);
					db.s.execute(sql);	 
				}
				
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
	// вставити в програму компютера comp програму компютера ins після команди where
	//  ....maxId - найбільший id в програмі comp
	//  ....maxNum - найбільший num в програмі comp
	// якщо (where < maxNum), то програма ins ВСТАВЛЯЄТЬСЯ в програму comp, іначе ДОПИСУЄТЬСЯ до comp
	public String insertComputer(Computer comp, int where, String ins) {
		String res = "";
		int maxNum = comp.findMaxNum();
		int maxId = comp.findMaxNumber();
		int idIns = getIdComputer(ins);
		int k = getSizeProgram(idIns);
		int idCom, num,reg1,reg2,next;
		String cod, comm; 
		boolean first = true;
		//System.out.println("DbComputer:isertComputer--idModel=" + comp.id+".maxNum="+maxNum +".maxId="+maxId + ".where="+where +".ins="+ins + ".k="+k);
		if ((idIns > 0) && (k>0)){
			try {
				st= db.conn.createStatement();
				db.conn.setAutoCommit(false);
				try{	
					//System.out.println("DbComputer:isertComputer=1");
					// Програму com необхідно зробити стандартною !!!!!
					sql = "update rInstruction set next = " + (maxNum+1) + "	where idModel = " + comp.id + " and cod = 'J' and next > " + (maxNum+1);
					db.s.execute(sql);	
					if (where < maxNum ){
						// якщо (where < maxNum), то програма ins ВСТАВЛЯЄТЬСЯ в програму comp
						for (int i = maxNum; i > where; i--){
							sql = "update rInstruction set num = num + "  + k + " where idModel = " + comp.id + " and num = " + i;
							db.s.execute(sql);
						}	
						//System.out.println("DbComputer:isertComputer=2");
						sql = "update rInstruction set next = next+ " + k + "	where idModel = " + comp.id + " and cod = 'J' and next > " + where;
						db.s.execute(sql);	
					}
					// якщо (where = maxNum), то програма ins ДОПИСУЄТЬСЯ до програми comp
					//System.out.println("DbComputer:isertComputer=3");
					sql = "select id, num, cod, reg1, reg2, next, txComm from rInstruction where idModel = " + idIns + " order by num" ;
					db.s.execute(sql);
					rs = db.s.getResultSet();
					while((rs!=null) && (rs.next())) {
						idCom = rs.getInt(1) + maxId; 
						num = rs.getInt(2) + where; 
						cod = rs.getString(3); reg1 = rs.getInt(4); reg2 = rs.getInt(5); next = rs.getInt(6);
						// коригування програми ins до стандартної!!!!!
						if (next > k+1) next = k+1; 
						comm = rs.getString(7);
						if(first){
							comm = ins + " " + comm; first = false;
						}
						else comm = ins.charAt(0) + " " + comm;
						//System.out.println("DbComputer:isertComputer=4 -- idCom=" + idCom + " num=" + num);
						sql = "insert into rInstruction values(" + comp.id + "," + idCom + "," + num + ",'" + cod + "'," +
						                                         reg1 + "," + reg2 + "," + next + ",'" + comm + "')"; 
						st.execute(sql);
					}	
					sql = "update rInstruction set next = next+ " + where + 
							" where idModel = " + comp.id + " and cod = 'J' and num > " + where + " and num < " + (where + k+1);
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
			if (idIns == 0)	res = "insertComputer: Не знайдено програму з іменем " + ins + "!" ;
			else res = "insertComputer: Програма комп'ютеру " + ins + " не містить жодної команди!" ;
		}
		return res;
	}
	
	private int getIdComputer(String name) {
		int res = 0;
		String section = Parameters.getSection();
		try{ 
			sql = "select id from rComputer where name = '" + name + "' and section = '" + section + "'";
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
