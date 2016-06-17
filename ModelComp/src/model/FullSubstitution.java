package model;

import java.util.*;

public class FullSubstitution {
	// Слово str отримано в результаті застосування правила виводу sub.rule на кроці sub.pos при застосуванні до слова sub.str.......
	//  isTh - теорема , isFst - на кроці sub.pos получено вперше,  any - інші отримання слова str на подальших кроках формування
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
