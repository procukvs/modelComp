package main;

public class Rule extends Command  {
	private int num = 1;
	private String sLeft = "";
	private String sRigth = "";
	private boolean isEnd = false;
	//private String txComm = "";
    public String show(String dummy){
      String s = sLeft + " ->" ;
      if (isEnd) s = s+".";
      s = s + " " + sRigth;
      if (txComm.length()>0) s = s +  " '" + txComm;
      return s;
    }
    public Rule(int num, String sLeft, String sRigth, boolean isEnd, String txComm, int id) {
    	super(id,txComm);
    	this.num = num; this.isEnd = isEnd; 
    	this.sLeft = sLeft; this.sRigth = sRigth;
    }
    public int getNum() {return num;}
    public String getsLeft() {return sLeft;}
    public String getsRigth() {return sRigth;}
    public boolean getisEnd() {return isEnd;}
   // public String gettxComm() {return txComm;}
    
    public String output() {
    	String wr = "  \"" + sLeft + "\" ->";
		if (isEnd) wr = wr + ".";
		wr = wr + " \"" + sRigth + "\";";
    	return wr;
   };
}
