package model;

public class Substitution {
	// ����� str -��������� ������������ ���������� rule � ������� pos �� ������� �����  ====> Algorithm
	// ����� str - �������� �������� ����� ����� ������������� ������� ������ rule �� ����� pos   ===> Post
	public int pos;
	public int rule;
	public String str;
	Substitution(int rule, int pos, String str ){
		this.rule = rule; this.pos = pos; this.str = str;
	}

}
