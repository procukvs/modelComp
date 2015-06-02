package file;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class OutputText {
	private BufferedWriter w;
	public boolean open(String name) {
		boolean b = false;
		try {
			w = new BufferedWriter( new FileWriter(name));
			b = true;
		} catch(Exception e) {
			System.out.println(">>>: OutputText.open:  " + e.getMessage());	
		}
		return b;
	}
	
	public void output(String str) {
		// виводить рядок як одну лінію!!
		try {
			w.write(str);
			w.newLine();
		} catch(Exception e) {
			System.out.println(">>>: OutputText.output " + e.getMessage());	
		}	
	}
	
	public void close() {
		try {
			w.close();
		} catch(Exception e) {
			System.out.println(">>>: OutputText.close " + e.getMessage());	
		}	
	}
}
