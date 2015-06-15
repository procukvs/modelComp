package main;

public class Substitution {
	// Описує str -результат застосування підстановки rule в позиції pos до деякого слова  ====> Algorithm
	// Описує str - попереднє виведене слово перед застосуванням правила виводу rule на кроці pos   ===> Post
	public int pos;
	public int rule;
	public String str;
	Substitution(int rule, int pos, String str ){
		this.rule = rule; this.pos = pos; this.str = str;
	}

}
