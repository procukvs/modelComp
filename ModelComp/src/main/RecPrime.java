package main;

public class RecPrime extends RecBody {
	RecBody g,h;
	public RecPrime(RecBody g, RecBody h){
		super(h.rank-1, false);
		this.g = g; this.h = h;
	}

	public String toString(){
		return "@R(" + g.toString() + "," + h.toString() + ")";
	} 
}
