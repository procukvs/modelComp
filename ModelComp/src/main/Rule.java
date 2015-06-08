package main;

public class Rule extends Command  {
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
    public Rule(String sLeft, String sRigth, boolean isEnd, String txComm) {
    	super(txComm);
    	this.sLeft = sLeft; this.sRigth = sRigth;
    	this.isEnd = isEnd; 
    	//this.txComm = txComm;
    }
    public String getsLeft() {return sLeft;}
    public String getsRigth() {return sRigth;}
    public boolean getisEnd() {return isEnd;}
    public String gettxComm() {return txComm;}
}
