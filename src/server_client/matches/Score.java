package server_client.matches;

import java.io.Serializable;

import server_client.User;

public class Score implements Serializable, Comparable<Score>{

	private User user;
	private int score;
	private int newScore;

	public Score(User user){
		this.user = user;
		this.score = 0;
		this.newScore = 0;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void addNewScore(int score){
		this.newScore += score;
	}
	
	public int getID(){
		return this.user.getID();
	}

	@Override
	public int compareTo(Score obj) {
		return this.score - obj.getScore();
	}
	
	public String toString(){
		return user.getNick() + " \t " + score + " +" + newScore;
	}

	public void recalculate(int intervalCurrent, int intervalMax) {
		double dScore = newScore / (intervalMax - intervalCurrent);
		score += dScore;
		newScore -= dScore;
	}
}