package main;
import java.util.*;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList program;
	
	//-----------------------------------------------
	public String getMain() {return "";}
	public String getAdd() {return "";}
	public String getAllChar() {return ""; }
	public String getNo() {return "";}
	public boolean getIsNumeric() {return true;}
	public int getRank() {return 2;}
	public String getInit() {return "";}
	public String getFin() {return "";}
	public void setMain(String main) { }
	public void setAdd(String add) { }
	public void setIsNumeric(boolean isNumeric) { }
	public void setRank(int rank){}
	public void setInit(String init) { }
	public void setFin(String fin) { }
	//----------------------------------------------	
	
	
	
	private static String[][] titles = { 
			{"Algorithm", "�������� ��������� �������","��������","���������","ϳ��������� ���������",	"����������",
								"��������","�����", "��������", "����","ϳ���������",
								"����������","���������� ����������"}, 
			{"Machine", "������ �������","������","������","�������� ������ (������� ��������)", "�������", 
								"������","����", "������", "�����","����",
								"����", "������� �������"} };
				
	Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
	
	public String takeResult(ArrayList sl, int nodef) { return "";}
	
	public ArrayList getStepSource(ArrayList sl, boolean internal) {return null;}
	
	public ArrayList getDataSource(int idModel) {
		return null;
	}
	
	public String[] iswfModel(){
		return null;
	}
	
	public ArrayList eval(String str, int nodef){
		return null;
	}
	
	public String show(){
		 return "Model-show";
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
	
	// ��������� ������������ ����� ������� � �������i
	public int findMaxNumber() {
		return program.size();
	}
	
	public int findCommand(int num) {return num-1;}
}
