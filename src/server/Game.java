package server;

import java.util.Queue;

import server_client.Playmode;
import server_client.matches.Match;

public class Game {
	
	private int gID;
	private Playmode playmode;
	private Queue<Match> upcomingMatches;
	private Queue<Match> finishedMatches;
	
	public Game(Playmode playmode){
		this.playmode = playmode; 
	}
	
	public void setGID(int gID){
		this.gID = gID;
	}

	public int getGID() {
 		return this.gID;
	}

	public Playmode getPlaymode() {
		return this.playmode;
	}
	
	public void addMatch(Match match){
		upcomingMatches.add(match);
	}
	
	public Match getCurrentMatch(){
		return upcomingMatches.peek();
	}
	
	public boolean nextMatch(){
		if(upcomingMatches.peek() != null){
			finishedMatches.add(upcomingMatches.poll());	
			return true;
		}
		return false;
	}
}