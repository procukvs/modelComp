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
	    m.updateCommand("@h0", '_', "@s0_>");
	    m.updateCommand("@h0", 'd', "@s0d>");
	    m.updateCommand("@h0", '#', "@s0#>");
	    m.updateCommand("@h0", '|', "@s0|>");
	    m.updateCommand("@h0", 'a', "@s0a>");
	    m.updateCommand("@s0", '_', "@z0_.");
	    m.updateCommand("@s0", 'd', "@s0d>");
	    m.updateCommand("@s0", '#', "@s0#>");
	    m.updateCommand("@s0", '|', "@s0|>");
	    m.updateCommand("@s0", 'a', "@s0a>");
	    System.out.println(m.show()); 
	    return m;
   	}
	
}
