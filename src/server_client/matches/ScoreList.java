package server_client.matches;

import java.io.Serializable;
import java.util.Arrays;

import server_client.User;

public class ScoreList implements Serializable{
	
	private Score[] scores;
	
	public ScoreList(User[] user){
		scores = new Score[user.length];
		for (int i = 0; i < user.length; i++) {
			scores[i] = new Score(user[i]);
		}
	}

	public Score[] getOrderedScore() {
		Arrays.sort(scores);
		return scores;
	}

	public void calculateNewScores(int intervalCurrent, int intervalMax) {
		for (Score score : scores) {
			score.recalculate(intervalCurrent, intervalMax);
		}
	}

	public void addScoreFor(User user, int newScore) {
		for (Score score : this.scores) {
			if(score.getID() == user.getID()){
				score.addNewScore(newScore);
			}
		}
	}	
}