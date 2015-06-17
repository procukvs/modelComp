package main;

public class RecName extends RecBody {
	String name;
	public RecName(String name, int rank, boolean isConst) {
		super(rank,isConst);
		this.name = name;
	}

	public String toString(){return name;} 
}
