package model;

public class Configuration {
	// ќписуЇ конф≥гурац≥ю машини “ьюр≥нга 
	public String tape;			//стр≥чка
	public int pos;				//позиц≥€ читаЇмого символу   0 <= pos < tape.length()
	public String st;			//стан машини 
		// st = "@TT"- стан, st = "%s@TT" - невизначений стан  st = "%g@TTS - невизначений перех≥д st="%c@TT" -вичерпана к≥льк≥сть переход≥в 
	Configuration(String tape, int pos, String st ){
		this.tape = tape; this.pos = pos; this.st = st;
	}
}
