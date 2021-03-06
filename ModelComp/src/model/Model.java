package model;
import java.text.*;
import java.util.*;

import db.*;
import file.*;
import gui.*;
import main.*;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList <Command> program;
	
	public Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
	
	//-----------------------------------------------
	// ���� ����� ����� (������� �� ����)
	public String getMain() {return "";}
	public String getAdd() {return "";}
	public String getAllChar() {return ""; }
	public String getNo() {return "";}
	public boolean getIsNumeric() {return true;}
	public int getRank() {return 0;}
	public String getInit() {return "";}
	public String getFin() {return "";}
	public String getType() {return "Algorithm";}     
	public void setMain(String main) { }
	public void setAdd(String add) { }
	public void setIsNumeric(boolean isNumeric) { }
	public void setRank(int rank){}
	public void setInit(String init) { }
	public void setFin(String fin) { }
	//----------------------------------------------	
	
	private static String[][] titles = { 
			{"Algorithm", "��������� ��������� �������","��������","���������","ϳ��������� ���������",	"����������",
								"��������","�����", "��������", "����","ϳ���������",
								"����������","���������� ����������","������������ ���������� ���������"}, 
			{"Machine", "������ ������","������","������","�������� ������ (������� ��������)", "�������", 
								"������","����", "������", "�����","����",
								"����", "������� ������","������������ ������������ ������"},
			{"Post", "������� �����","�������","�������","������ �� ������� ������ �������", "��������", 
								"�������","����", "�������", "����","������/������� ������",
								"������/������� ������", "�������� �����","�� �������"},
			{"Recursive", "��������-���������� �������","���� �������","������","������� ������", "�������", 
									"����","�����", "����", "����","�������",
									"�������", "������� �������","������� ������"},
			{"Calculus", "������-������","���� ������","������","������ ������", "�������", 
										"����","�����", "����", "�����","�����",
										"�����", "������� ������","������ ������"},			
			{"Computer", "������ � ������������ ���������","������","������","��������", "�������", 
									"������","����", "������", "����","�������",
									"�������", "�������","������������ ������������ ������"},
			{"Input", "�������� ������� � �����","Input2","Input3","Input4", "Input5",
									"�����","Input7", "Input8", "Input9","Input10",
									"Input11", "Input12","Input13"},
			{"Output", "��������� ������� � ����","Output2","Output3","Output4", "Output5", 
									"�����","Output7", "Output8", "Outpu9t","Output10",
									"Output11", "Output12","Output13"},
     		{"State", "���� ���������","State2","State3","State4", "������", 
	    							"State6","State7", "State8", "State9","State10",
									"State11", "State12","State13"},
		    {"Parameters", "������������ ���������","Parameters2","Parameters3","Parameters4", "���������� ��������", 
									"Parameters6","���� �������� ���������", "Parameters8", "Parameters9","Parameters10",
									"Parameters11", "Parameters12","Parameters13"}									
	};
	
	//-----work DB ------- 
	public void dbRenameState(String in,String out) {}
	public String dbInsertModel(String nmInsert) { return "";}
	public String dbInsertModel(int were, String nmInsert) { return "";}
	public String dbInsertModel(int were, String nmModel, String nmFunction) { return "";}
	
	public String takeResult(ArrayList sl, int nodef) { return "";}
	
	public int takeCountStep(ArrayList sl, int nodef) { return 0;}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal, int var) {return null;}
	public ArrayList getStepSource(ArrayList sl, boolean internal) {return null;}
	
	public ArrayList getDataSource() {
		ArrayList data = new ArrayList();
		ArrayList row;
		//System.out.println("Model:getDataSourse: " + name + " " + id);
		for (int i = 0; i < program.size(); i++){
			Command cmd = (Command)program.get(i);
			data.add(cmd.getSource(this));
        } 
        return data;
	}
	
	public String[] iswfModel(){
		return null;
	}
	
	// ��������, �� � �������� ����� ���������������� ���� ������� ��������� ������� 
	public String iswfModelAlfa(String alfa){return "";	}
	
	public ArrayList eval(String str, int nodef){
		return null;
	}
	
	public ArrayList eval(int[] arg, int nodef){
		return null;
	}
	
	public ArrayList eval(String str, int nodef, boolean isStep){
		return null;
	}
	
	public String show(){
		 return "Model-show";
	}
	
	// Recursive, Calculus
	//��������� ��� ������� �� ������������ : ����� ����� � "base", "base00", "base01",...
	public String findNameCommand(String base){
		int us,i = 0;
		NumberFormat suf = new DecimalFormat("00"); 
		boolean isUse ;
		String name = base;
		do {
			isUse = false; us = 0;
			for(int j = 0; j < program.size(); j++){
				if(((Command) program.get(j)).getName().equals(name)) us++;
			}
			if (us>0) name = base + suf.format(i);
			i++;
		} while (us>0);
		return name;
	}
	//�������� ��"� ���� ������� �� �����������...
	public String testNameCommand(String name){
		String st = "";
		Command cmd;
		//String txt = "�������";
		//if(this.getType().equals("Calculuse")) txt = "������";
		if (StringWork.isIdentifer(name)){
			for(int i = 0; i < program.size(); i++) {
				cmd = (Command)program.get(i);
				if (cmd.getName().equals(name))st = "� ����� ��� ��������������� ���� ��� " + name + ".";
			}	
		} else st = "��� " + name + " - �� �������������.";
		return st;
	}
	
	public String output(OutputText out) {return "outputModel";}
	
	public String output(String name, OutputText out) {
		String res = "";
		if(out.open(name)) {
			res = this.output(out);
			out.close();
		} else res = "Not open output file " + name + "!"; 
		return res;
	}
	
	static public String title(String type, int num) {
		String rs = "";
		int j = -1;
		for(int i = 0; i < titles.length; i++ ) {
			if (type.equals(titles[i][0])) j = i;
		}
		if (j >= 0) rs = titles[j][num];
		return rs;
	}
	
	// ��������� ������������ ����� id !!! ������� � �������i
	public int findMaxNumber(){
		int cnt = 0;
		int num;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ ){
				num = ((Command)program.get(i)).getId();
				if(cnt < num) cnt = num;
			}	
		}
		return cnt;
	}
		
	// ���������  ���������� ����� ������� �� �� �������-������ � �� id
	public int findCommand(int id) {
		int cnt = -1;
		if ((program != null) && (program.size() > 0)) {
			for(int i = 0; i < program.size(); i++ )
				if(id == (((Command)program.get(i)).getId())) cnt = i;
		}
		return cnt;
	}
	
	public int findCommand(String key) {return 0;}
	
	private String getTypeModel(){
		return this.getClass().getName().substring(5);
	}
	
	/*
	public int getDbOrder(){
		//System.out.println(getTypeModel());
		return DbAccess.getDbAccess().getOrder(getTypeModel(), name); 
	}
	
	public int getDbNumber(int id){
		//System.out.println(getTypeModel());
		return DbAccess.getDbAccess().getNumber(getTypeModel(), id); 
	}
	*/
	//public boolean dbDelete(){ return false;} 
	/*
	public int dbNew(String type){
		//������� id ���� ����� � ��
		return DbAccess.getDbAccess().newModel(type);
	}
	*/
	//public int dbNewAs() { return 0;}
}
