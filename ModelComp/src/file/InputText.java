package file;

import java.io.BufferedReader;
import java.io.FileReader;

public class InputText {
	private BufferedReader r;
	public boolean open(String name) {
		boolean b = false;
		try {
			r = new BufferedReader( new FileReader(name));
			b = true;
		} catch(Exception e) {
			System.out.println(">>>: InputText.open:  " + e.getMessage());	
		}
		return b;
	}
	
	public String input() {
		// null - якщо кінець потоку
		String s = null;
		try {
			s = r.readLine();
		} catch(Exception e) {
			System.out.println(">>>: InputText.input " + e.getMessage());	
		}	
		return s;
	}
	
	public void close() {
		try {
			r.close();
		} catch(Exception e) {
			System.out.println(">>>: InputText.close " + e.getMessage());	
		}	
	}
}
