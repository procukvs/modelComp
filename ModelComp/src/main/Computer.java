package main;

import java.util.*;

import db.*;
import file.*;

public class Computer extends Model {
	public int rank = 1;
	
	public Computer(int id, String name) {
		super(id,name);
	}
	
	//-------------------------
	public int getRank() {return rank;}
	public String getType() {return "Computer";}
	//----------------------------------------------
	
	//-----work DB ------- 
	public String dbInsertModel(int where, String nmInsert) {
		return DbAccess.getDbComputer().insertComputer(this.id, where, nmInsert, this.findMaxNumber());
	}
	public boolean  dbDelete() {
		return DbAccess.getDbComputer().deleteComputer(this);
	}
	public int dbNewAs() { 
		return DbAccess.getDbComputer().newComputertAs(this);
	}
	
	public ArrayList getDataSource(int idModel) {
		ArrayList data = new ArrayList();
		ArrayList row;
		State inst;
		Instruction com;
		if (program != null) {
			for (int i = 0; i < program.size(); i++){
				row = new ArrayList();
				com = (Instruction)program.get(i);
				row.add(com.getNum());
				row.add("  " + com.toCommand());
				row.add(com.gettxComm());
				row.add(com.getId());
				row.add(idModel);
				data.add(row);
			} 
		}
        return data;
	}
	
	public int evalMaxReg(){
		int mr = 0;
		int r = 0;
		Instruction com;
		if (program != null) {
			for (int i = 0; i < program.size(); i++){
				com = (Instruction)program.get(i);
				r = com.evalMaxReg();
				if(r > mr) mr=r;
			} 
		}
		return mr;
	}
	
	public ArrayList <String> iswfNum(String num) {
		ArrayList <String> mes = new ArrayList <String>();
		int numI = -1;
		if (StringWork.isPosNumber(num)) numI = new Integer(num);
		if((numI < 0) || (numI > program.size()+1)) 
			mes.add("E:Порядковий номер команди повинен бути не меньше 1 і не більше " + (program.size() + 1) + ".");
		return mes;
	}	
	
	public String testPart(String cod, int i, String part) {
		String text = "";
		String[] what = {"Регістр 1","Регістр 2","Номер наступної команди"};
		boolean test = true;
		switch(cod){
		case "S": case "Z": test = (i==1); break;
		case "T": test = (i < 3); break;
		}
		if (test){
			if(!StringWork.isPosNumber(part))
				text = what[i-1] + " " + part + " - недодатнє число.";
		}
		return text;		
	}
	
	public ArrayList eval(int[] arg, int nodef){
		ArrayList sl = new ArrayList();
		int mr = evalMaxReg();
		int[] allReg = new int[mr];
		int next =1; 
		int step = 0;
		int sPr = (program==null?0:program.size());
		int r1=0;
		Instruction com = null;
		int[] asl;
		boolean go = (step < nodef) && (sPr > 0);
		for (int i = 0; i < mr; i++)
			if(i < arg.length) allReg[i] = arg[i]; else allReg[i] = 0;
		//----------------------------
		sl.add(arg);
		//---------------------------
		while (go){
			com = (Instruction)program.get(next-1);
			r1 = com.getReg1()-1;
			//-------------------------------
			asl = new int[3];
			asl[0]=next; asl[1]=-1; asl[2]=-1;
			switch(com.getCod()){
			case "Z": asl[1] = r1; asl[2] = 0;break;
			case "S": asl[1] = r1; asl[2] = allReg[r1]+1; break;
			case "T": asl[1] =com.getReg2()-1; asl[2]= allReg[r1]; break;
			case "J": 
			}
			sl.add(asl);
			//----------------------------------
			switch(com.getCod()){
			case "Z": allReg[r1] = 0; break;
			case "S": allReg[r1] = allReg[r1]+1; break;
			case "T": allReg[com.getReg2()-1] = allReg[r1]; break;
			case "J": if(allReg[com.getReg2()-1] == allReg[r1]) next = com.getNext()-1;
			}
			
			next++;
			step++;
			go = (next > 0) && (next <= sPr) && (step < nodef);
		}
		//--------------------------------
		asl = new int[1]; asl[0]=allReg[0];
		sl.add(asl);
		//-------------------------------
		return sl;
	}	
	
	public String takeResult(ArrayList sl, int nodef){
		String text = "";
		int r = ((int[])sl.get(sl.size() - 1))[0];
		if (sl.size() == nodef + 2) text = "Невизначено"; else text = "" + r;
		return text;
	}

	public int takeCountStep(ArrayList sl, int nodef) { return sl.size()-2;}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal) {
		ArrayList data = new ArrayList();
		ArrayList row;
		Instruction com;
		int[] asl;
		int mr = evalMaxReg();
		int[] allReg = new int[mr];
		int v;
		if (sl != null) {
			//--configuration Step, Ninst, Inst, r1,r2,....,rMr.
			//------------------ initial configuration-----------
			asl = (int[]) sl.get(0);
			row = new ArrayList();
			row.add(0); row.add(0); row.add("");
			for (int i = 0; i < mr; i++){
				if(i<asl.length) v = asl[i]; else v=0;
				allReg[i] = v;
				row.add(v);
			}
			data.add(row);
			for(int i = 1; i < sl.size()-1; i++ ){
				asl = (int[])sl.get(i);
				row = new ArrayList();
				row.add(i);
				row.add(asl[0]);
				com = (Instruction)program.get(asl[0]-1); 
				//com = (Instruction)program.get(0);
				row.add(com.toCommand());
				if (asl[1] >= 0) allReg[asl[1]] = asl[2];
				for (int j = 0; j < mr; j++) row.add(allReg[j]);
				data.add(row);
			}
		}
	    return data;
	}
	
	public String output(OutputText out) {
		String res = "";
		String wr;
		String s0 = "00000";
		String empty = "          ";
		String num;
		Instruction inst;
		//if(out.open(name)) {
		//	System.out.println("File " + name + " is open..");
			if (!descr.isEmpty()) out.output("'" + descr);
			out.output("Computer " + this.name + " : " + this.rank + ";");
			for (int i = 0; i < program.size(); i++){
				inst = (Instruction)program.get(i);
				num = ""+(i+1);
				if(num.length() < 3) num = s0.substring(1,3-num.length()) + num; 
				wr = inst.toCommand();
				if(wr.length() < 11) wr = num + " : " + wr + empty.substring(1,12-wr.length()); 
				if (!(inst.txComm.isEmpty())) wr = wr + " '" + inst.txComm;
			    out.output(wr);
			}
			out.output("end " + this.name);
		//	out.close();
		//	System.out.println("File " + name + " is close.."); 
		//} else res = "Not open output file " + name + "!"; 
		return res;
	}	
}
