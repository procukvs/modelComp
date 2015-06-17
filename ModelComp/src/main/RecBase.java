package main;

public class RecBase extends RecBody {
	String name;
	int b;
	int wh;
	public RecBase (int  b, int wh){
		super(((b==0)||(b<0))?1:((b>9)?9:b), (b==0) && (wh==0));
		this.b = b; this.wh = wh; 
		if (b==0){
			if(wh == 0) name = "z1"; else name = "a1";
		} else {
			if (this.b < 0) this.b = 1;
			else if(this.b > 9) this.b = 9;
			if(this.wh <1) this.wh=1;
			else if(this.wh > this.b) this.wh = this.b;
			name = "i" + this.b + this.wh; 
		}
	}

	public String toString() {return name;} 
}
