package main;

public class Move {
	private char in; 		//  ������, �� �������� � ������
	private char out;		//  ���� ������ ��������
	private String state;	//  � ���� ���� �������
	private char go; 		//  �� �������� �� ������
	
	public Move(char in, char out, String state, char go) {
    	this.in = in; this.out = out;
    	this.state = state; this.go = go;
    }
	public char getIn() {return in;}
	public char getOut() {return out;}
	public String getState() {return state;}
	public char getGo() {return go;}
	
	public String show() {
		return "'" + in + "' -> " + state + out + go;
	}
	
	public boolean iswfMove(Machine main){
		
		return true;
	}

}
