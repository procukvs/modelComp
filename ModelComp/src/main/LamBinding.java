package main;

public class LamBinding {
	String name;
	Lambda body;
	public LamBinding(String name, Lambda body){
		this.name=name; this.body=body;
	}
	public String getName(){return name;}
	public Lambda getBody(){return body;}

}
