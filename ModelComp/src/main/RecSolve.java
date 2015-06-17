package main;

public class RecSolve extends RecBody {
	RecBody g;
	int max;
	public RecSolve(RecBody g, int max){
		super(g.rank-1,false);
		this.g =g; this.max = max;
	}
	
	public String toString(){
		return "@M(" + g.toString() + "," + max + ")";
	} 
}
