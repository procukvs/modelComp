package model;

public class Configuration {
	// ����� ������������ ������ ������� 
	public String tape;			//������
	public int pos;				//������� �������� �������   0 <= pos < tape.length()
	public String st;			//���� ������ 
		// st = "@TT"- ����, st = "%s@TT" - ������������ ����  st = "%g@TTS - ������������ ������� st="%c@TT" -��������� ������� �������� 
	Configuration(String tape, int pos, String st ){
		this.tape = tape; this.pos = pos; this.st = st;
	}
}
