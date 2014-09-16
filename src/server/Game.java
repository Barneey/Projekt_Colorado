package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import server_client.Playmode;
import server_client.matches.Match;

public class Game {
	
	private int gID;
	private Playmode playmode;
	private Queue<Match> upcomingMatches;
	private Queue<Match> finishedMatches;
	private ArrayList<Integer> alstLeftUser;
	
	public Game(Playmode playmode){
		this.playmode = playmode; 
		this.upcomingMatches = new PriorityQueue<>();
		this.finishedMatches = new PriorityQueue<>();
		this.alstLeftUser = new ArrayList<>();
	}
	
	public void setGID(int gID){
		this.gID = gID;
		Iterator<Match> it = upcomingMatches.iterator();
		while(it.hasNext()){
			it.next().setGameID(gID);
		}
	}

	public int getGID() {
 		return this.gID;
	}

	public Playmode getPlaymode() {
		return this.playmode;
	}
	
	public void addMatch(Match match){
		match.setPlaymode(playmode);
		match.setGameID(this.gID);
		upcomingMatches.add(match);
	}
	
	public Match getCurrentMatch(){
		return upcomingMatches.peek();
	}

	public void setMatchLoaded(int userID, boolean matchLoaded) {
		getCurrentMatch().setMatchLoaded(userID, matchLoaded);
	}
	
	public boolean nextMatch(){
		Match upcomingMatch = null;
		if((upcomingMatch = upcomingMatches.poll()) != null){
			upcomingMatch.setLeftUser(alstLeftUser.toArray(new Integer[0]));
			finishedMatches.add(upcomingMatches.poll());	
			return true;
		}
		return false;
	}

	public void leaveUser(int userID) {
		alstLeftUser.add(userID);
		getCurrentMatch().setLeftUser(alstLeftUser.toArray(new Integer[0]));
	}
}