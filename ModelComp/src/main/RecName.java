package main;

import java.util.HashMap;

public class RecName extends RecBody {
	String name;
	/*
	public RecName(String name, int rank, boolean isConst) {
		super(rank,isConst);
		this.name = name;
	}
	*/
	public RecName(String name) {
		this.name = name;
	}
	
	public String toString(){return name ;} 
	
	public String toTest(){return "<Name:" + rank + ":" + name + ">";} 
	
	public String setRank(String init, HashMap <String, RecBody> map) {
		String st = "";
		if (rank == 0) {
			if(init.equals(name)) st = "������� "+ name + " ������� ���� ����."; 
			else st = "�� ����������� ������� ������� " + name + ".";
		} else if (rank == -1){ 
			rank = 0;
			if (map.containsKey(name)) {
				RecBody bd = map.get(name);
				if(bd != null) {
					if (bd.rank == -1) st = bd.setRank(init, map);
					rank = bd.rank;
				} else st = "������� " + name + " ������ ����������� �������.";
			} else st = "�� �������� ������� " + name + "."; 
			//System.out.println("RecName- end" + name + " :rank =  " + rank );
		} 
		return st;
	}
	public boolean isConst(HashMap <String, RecBody> map) {
		return map.get(name).isConst(map);
	}
	public String iswf(HashMap <String, RecBody> map) {
		return "";
	}
	public void setIswf(HashMap <String, RecBody> map) {
		String st="";
		if (map.containsKey(name)) {
			RecBody bd = map.get(name);
			if(bd != null) st = bd.iswf(map);
			else st = "������� " + name + " ������ ����������� �������.";
		} else st = "�� �������� ������� " + name + "."; 
	}
}