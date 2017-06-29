package model.calc;

public class LamNBind {
	String name;   // ім'я
	int cnt;       //кількість його вкладених використань
	public LamNBind(String name, int cnt){
		this.name=name; this.cnt=cnt;
	}
	public String getName(){return name;}
	public int getCnt(){return cnt;}
	// переіменування імені
	public String newName() {
		String res = "";
		//for(int i=0; i<cnt; i++){res += '\'';		}
		if (cnt>0)res = res + cnt;
		return (name + res);
	}
	
	

}
