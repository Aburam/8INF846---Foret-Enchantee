package model.agent;

public class Score {

	private int score;
	
	public Score(){
		score = 0;
	}
	
	public int getScore(){
		return score;
	}
	
	public void sortieTrouvee(int nbCases){
		score = score + 10*nbCases;
	}
	
	public void mort(int nbCases){
		score = score - 10*nbCases;
	}
	
	public void mouvement(){
		score--;
	}
	
	public void utiliserRoche(){
		score = score -10;
	}
	
	public String toString(){
		String ret = "Mesure de performance : " + score;
		return ret;
	}
}
