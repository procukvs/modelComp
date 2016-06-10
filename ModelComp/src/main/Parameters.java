package main;

public class Parameters {
	static String regime = "user";
	static String section = "base";
	static String postVar = "duplicate";
	static String recurSubst = "operation";
	static String version = "1.0.0";
	public static String getRegime() {
		return regime;
	}
	public static String getSection() {
		return section;
	}
	public static String getPostVar() {
		return postVar;
	}
	public static String getRecurSubst() {
		return recurSubst;
	}	
	public static String getVersion() {
		return version;
	}
	public static void setRegime(String s) {
		regime = s;
	}
	public static void setSection(String s) {
		section = s;
	}
	public static void setPostVar(String s) {
		postVar = s;
	}
	public static void setRecurSubst(String s) {
		recurSubst = s;
	}
	public static void setVersion(String s) {
		version = s;
	}
}
