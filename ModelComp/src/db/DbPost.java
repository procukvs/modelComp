package db;

import java.sql.*;
import java.util.*;

import main.*;

public class DbPost {
	private DbAccess db;
	private String sql;
	private  ResultSet rs;
	private Post model; 

	DbPost(DbAccess db){
		this.db = db; 
	}
		
	public Post getPost(int id){
		model= null;
		sql = "select name, sMain, sAdd, isNumeric, Rank, descr from pPost where id = " + id;
		try{
			db.s.execute(sql);
			rs = db.s.getResultSet();
            if((rs!=null) && (rs.next())){
            	model = new Post(id, rs.getString(1));
            	model.main = rs.getString(2);
            	model.add = rs.getString(3);
            	model.isNumeric = rs.getBoolean(4);
            	model.rank = rs.getInt(5);
            	model.descr = rs.getString(6);
            	model.program = getAllDerives(id);
            }
       	}
		catch (Exception e) {
			System.out.println("ERROR: getPost :" + sql + e.getMessage());
		}
		//System.out.println(">>>get>> " + id + " row = " + model.program.size());
		return model;
	}
	
	private ArrayList getAllDerives(int post) {
		ArrayList program = new ArrayList();
		sql = "select num, isAxiom, sLeft, sRigth,  txComm, id, idModel " +
		      "  from pDerive where idModel = " + post + " order by num";
		String sLeft = "";
		boolean isAxiom;		
		try{ 
			db.s.execute(sql);
	        ResultSet rs = db.s.getResultSet();
	        while((rs!=null) && (rs.next())) {
	        	isAxiom = rs.getBoolean(2);
	        	if (isAxiom) sLeft = ""; else sLeft = rs.getString(3);
	        	Derive rule = new Derive(rs.getInt(1), rs.getBoolean(2), sLeft,rs.getString(4), rs.getString(5), rs.getInt(6));
				program.add(rule);
	        } 
		}catch (Exception e){
			System.out.println("ERROR: getAllDerives :" + sql + e.getMessage());
		}
		return program;
	}	
	
	// модифікує відредаговану систему
	public void editPost(Post model) {
		int rows;
		try{
			int isNumeric = (model.isNumeric?1:0); 
			sql = "update pPost set name = '" + model.name + "', sMain = '" + model.main + "', " +
						"sAdd = '" + model.add + "', isNumeric = " + isNumeric + ", Rank = " + model.rank + "," +
						"descr  = '" + model.descr + "' where id = " + model.id;
			rows=db.s.executeUpdate(sql);
			if (rows == 0)
				System.out.println("editPost: Не змінило відредагований " + model.name + "!");
		}
		catch (Exception e) {
			System.out.println("ERROR: editPost :" + e.getMessage());
		}
	}		
	
	
	// створює нову порожню систему
	public int newPost() {
		String name = db.findName("Post","Post");
		int cnt = db.maxNumber("Post")+1;
		int rows;
		String section = Parameters.getSection();
		try{
			sql = "insert into pPost values(" + cnt + ",'" + section + "','" + name + "','|#','',1,2,'new')";
			rows=db.s.executeUpdate(sql);
			if (rows == 0) cnt = 0;
		}
		catch (Exception e) {
			System.out.println("ERROR: newPost :" + sql + " "  + e.getMessage());
		}
		return cnt;
	}	
	
	// створює нову систему на основі системи з model (включаючи всі аксіоми та правила виводу)
	public int newPostAs(Post model){
		String name = db.findName("Post", model.name);
		int cnt = db.maxNumber("Post")+1;
		int rows;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "insert into pPost select " + cnt + ", section, '" + name + "', sMain, sAdd, " + 
							"isNumeric, Rank, descr from pPost where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				if (rows == 0) cnt =0;
				sql = "insert into pDerive select " + cnt + ", id, num, sLeft, sRigth, isAxiom, txComm " +
							" from pDerive where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: newPostAs :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return cnt;
	}
	
	public boolean deletePost(Post model){
		int rows;
		boolean res = false;
		try {
			db.conn.setAutoCommit(false);
			try{
				sql = "delete from pDerive where idModel = " + model.id;
				rows=db.s.executeUpdate(sql);
				sql = "delete from pPost where id = " + model.id;
				rows=db.s.executeUpdate(sql);
				db.conn.commit();
				res = true;
			}
			catch (Exception e) {
				db.conn.rollback();
				System.out.println("ERROR: deletePost :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		return res;
	}	
	
	public void newDerive(Post post, Derive rule) {
		int id = post.id;
		int maxNum = post.findMaxNumber();
		try {
			db.conn.setAutoCommit(false);
			try{
				int isAxiom = (rule.getisAxiom()?1:0); 
				//sql = "update pDerive set num = num+1" + "	where idModel = " + post + " and num >= " + rule.getNum();
				for (int i = maxNum; i >= rule.getNum(); i--){
					sql = "update pDerive set num = num+1" + "	where idModel = " + id + " and num = " + i;
					db.s.execute(sql);
				}	
				sql = "insert into pDerive values(" + id + "," + rule.getId() + "," + rule.getNum() +	",'" + rule.getsLeft() + "','"
				 		+ rule.getsRigth() + "'," + isAxiom + ",'" + rule.gettxComm() + "')";
				//System.out.println("newDerive2: " + sql );
				db.s.execute(sql);
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR:newDerive: " + e.getMessage() );
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	
	public void editDerive(int post, Derive rule) {
		 try{	
			int  isAxiom = (rule.getisAxiom()?1:0); 
			//System.out.println("Db.."+rule.output());
		 	sql = "update pDerive set sLeft = '" + rule.getsLeft() + "'," + "sRigth = '" + rule.getsRigth() + 
		 			"', isAxiom = " +	isAxiom + ", txComm = '" + rule.gettxComm() + "'" +
		 			"	where idModel = " + post + " and id = " + rule.getId() ;
		 	//System.out.println("Db.."+sql);
		 	db.s.execute(sql);
	  	 } catch (SQLException e) {
			System.out.println("ERROR: editDErive: " + e.getMessage() );
		 }  			
	}
	public void deleteDerive(int post, Derive rule){
		try {
			db.conn.setAutoCommit(false);
			try{	
				sql = "delete from pDerive "  + " where idModel = " + post + " and id = " + rule.getId();
				//System.out.println("deteDerive id = " + rule.getId() + " num = " + rule.getNum());
				db.s.execute(sql);
				sql = "update pDerive set num = num-1 " + "	where idModel = " + post + " and num >= " + rule.getNum();
				db.s.execute(sql);	 
				db.conn.commit();
			}	catch (Exception e) {
				System.out.println("ERROR: deleteDerive: " + e.getMessage());
				db.conn.rollback();
			}  
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
	}
	// додає введену з файлу систему ПОста model (включаючи всі аксіоми/правила виводу)
	public int addPost(Post model){
		String name = model.name;
		String namein = name;
		int cnt = db.maxNumber("Post")+1;
		int rows;
		Derive r;
		String section = Parameters.getSection();
		if (db.isModel("Post",name)) name = db.findName("Post", model.name);
		try {
			db.conn.setAutoCommit(false);
			try{
				int isNumeric = (model.isNumeric?1:0); 
				sql = "insert into pPost values(" + cnt +  ",'" + section +  "','" + name + "','" + model.main + "','" +
						model.add + "'," + isNumeric + "," + model.rank + ",'" + model.descr + "')";  
				rows=db.s.executeUpdate(sql);
				for (int i = 0; i < model.program.size(); i++) {
					r = (Derive)model.program.get(i);
					sql = "insert into pDerive values(" + cnt + "," + (i+1) + "," + r.getNum() + ",'" + r.getsLeft() +
							"','" + r.getsRigth() + "'," + (r.getisAxiom()?1:0) + ",'" + r.gettxComm() + "')";
					rows=rows + db.s.executeUpdate(sql);
				}
			//if (rows != model.program.size() + 1) cnt = 0;
				db.conn.commit();
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				db.conn.rollback();
				System.out.println("ERROR: addPost :" + e.getMessage());
			}
			db.conn.setAutoCommit(true);
		}	
		catch (Exception e) { System.out.println(e.getMessage());}	
		//System.out.println("dbPost.addPost: nameIn " + namein  + " name "  + name +  " id " + cnt); 
		return cnt;
	}		
}
