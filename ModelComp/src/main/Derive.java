package main;

public class Derive extends Command {
	private int num = 1;
	private boolean isAxiom = false;
	private String sLeft = "";
	private String sRigth = "";
	private int id; // внутр≥шн≥й номер в баз≥ даних (кл€ч разом з id модел≥) 
	//================================
	private boolean isMulty = true;  // допустим≥ в л≥в≥й частин≥ повторенн€ зм≥нних
	private static String varChar = "RSTUVWXYZ";
	
	
	//private String txComm = "";
    public String show(String dummy){
      String s = ""; 
      if (!isAxiom) s = sLeft + " ->" ;
      s = s + " " + sRigth;
      if (txComm.length()>0) s = s +  " '" + txComm;
      return s;
    }
    public Derive(int num, boolean isAxiom, String sLeft, String sRigth,  String txComm, int id) {
    	super(txComm);
    	this.num = num; this.isAxiom = isAxiom; 
    	this.sLeft = sLeft; this.sRigth = sRigth;
    	this.id = id;
    }
    public int getNum() {return num;}
    public boolean getisAxiom() {return isAxiom;}
    public String getsLeft() {return sLeft;}
    public String getsRigth() {return sRigth;}
    public String gettxComm() {return txComm;}
    public int getId() { return id;}
    
    public String output() {
    	String wr = "";
		if (!isAxiom) wr = "  \"" + sLeft + "\" -> ";
		wr = wr + "\"" + sRigth + "\";";
    	return wr;
   }
   
   public String iswfDerive(Post post){
	   return "";
   }
   
  
   
}
