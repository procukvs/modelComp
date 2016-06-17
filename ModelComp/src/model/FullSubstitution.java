package model;

import java.util.*;

public class FullSubstitution {
	// ����� str �������� � ��������� ������������ ������� ������ sub.rule �� ����� sub.pos ��� ����������� �� ����� sub.str.......
	//  isTh - ������� , isFst - �� ����� sub.pos �������� ������,  any - ���� ��������� ����� str �� ��������� ������ ����������
	public String str;
	public Substitution sub;
	public boolean isTh;
	public boolean isFst;
	public ArrayList <Substitution> any;
	FullSubstitution(String str, Substitution sub, boolean isTh, boolean isFst) {
		this.str = str; this.sub = sub;
		this.isTh = isTh; this.isFst = isFst;
		any = null;
	}
}
