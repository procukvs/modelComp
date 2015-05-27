package main;
import java.util.*;

public class Model {
	public int id;
	public String name;
	public String descr;
	public ArrayList program;
	Model(int id, String name) {
		this.id = id; this.name = name;
		descr = ""; program = null;
	}
}
