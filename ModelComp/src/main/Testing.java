package main;

public class Testing {
	
	public static Machine formMachine(){
		    
	    Machine m = new Machine(5, "Test1");
	    m.add = "a"; m.no = "d";
	    m.init = "@h0"; m.fin = "@z0";
	   // System.out.println(m.show());
	    ((Machine)m).addStateGoing("@s0");
	    ((Machine)m).addStateGoing("@h0"); 
	    ((Machine)m).addStateGoing("@z0");
	    System.out.println(m.show()); 
	    m.updateCommand("@h0", new Move('_','_',"@s0", '>'));
	    m.updateCommand("@h0", new Move('d','d',"@s0", '>'));
	    m.updateCommand("@h0", new Move('#','#',"@s0", '>'));
	    m.updateCommand("@h0", new Move('|','|',"@s0", '>'));
	    m.updateCommand("@h0", new Move('a','a',"@s0", '>'));
	    m.updateCommand("@s0", new Move('_','_',"@z0", '.'));
	    m.updateCommand("@s0", new Move('d','d',"@s0", '>'));
	    m.updateCommand("@s0", new Move('#','#',"@s0", '>'));
	    m.updateCommand("@s0", new Move('|','|',"@s0", '>'));
	    m.updateCommand("@s0", new Move('a','a',"@s0", '>'));
	    System.out.println(m.show()); 
	    return m;
   	}
	
}
