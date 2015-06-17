package main;

import java.util.*;

public class Recursive extends Model {
	HashMap <String, RecBody> map;
	public Recursive(int id, String name) {
		super(id,name);
		map = new HashMap <String, RecBody>();
	}

}
