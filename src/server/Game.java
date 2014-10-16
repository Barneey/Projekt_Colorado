package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.Match;
import server_client.matches.ScoreList;

public class Game {
	
	private int gID;
	private Playmode playmode;
	private Queue<Match> upcomingMatches;
	private Queue<Match> finishedMatches;
	private Match runningMatch;
	private ArrayList<Integer> alstLeftUser;
	private ArrayList<Integer> alstNewMatchRequests;
	private ScoreList scoreList;
	
	public Game(Playmode playmode){
		this.playmode = playmode; 
		this.upcomingMatches = new PriorityQueue<>();
		this.finishedMatches = new PriorityQueue<>();
		this.runningMatch = null;
		this.alstLeftUser = new ArrayList<>();
		ArrayList<User> alstUsers = new ArrayList<>();
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				alstUsers.add(user);
			}
		}
		this.alstNewMatchRequests = new ArrayList<>();
		this.scoreList = new ScoreList(alstUsers.toArray(new User[0]));
	}
	
	public void setGID(int gID){
		this.gID = gID;
		Iterator<Match> it = upcomingMatches.iterator();
		while(it.hasNext()){
			it.next().setGameID(gID);
		}
		if(runningMatch != null){
			runningMatch.setGameID(gID);
		}
	}

	public int getGID() {
 		return this.gID;
	}

	public Playmode getPlaymode() {
		return this.playmode;
	}
	
	public void addMatch(Match match){
		match.setPlaymode(this.playmode);
		match.setGameID(this.gID);
		match.setScoreList(this.scoreList);
		if(this.runningMatch == null){
			this.runningMatch = match;
		}else{
			this.upcomingMatches.add(match);
		}
	}
	
	public Match getCurrentMatch(int userID){
		if(alstNewMatchRequests.contains(userID)){
			return this.upcomingMatches.peek();
		}
		return this.runningMatch;
	}

	public void setMatchLoaded(int userID, boolean matchLoaded) {
		getCurrentMatch(userID).setMatchLoaded(userID, matchLoaded);
	}
	
	public boolean nextMatch(int userID){
		this.scoreList = this.runningMatch.getScoreList();
		this.finishedMatches.add(this.runningMatch);	
		if(upcomingMatches.peek() != null){
			this.runningMatch = upcomingMatches.poll();
			this.runningMatch.setLeftUser(alstLeftUser.toArray(new Integer[0]));
			this.runningMatch.setScoreList(this.scoreList);
			return true;
		}
		return false;
	}

	public void leaveUser(int userID) {
		alstLeftUser.add(userID);
		Match currentMatch = getCurrentMatch(userID);
		if(currentMatch == null){
			//
		}else{
			getCurrentMatch(userID).setLeftUser(alstLeftUser.toArray(new Integer[0]));
		}
	}
	
	public ScoreList getScoreList(){
		return this.scoreList;
	}

	public Match requestNextMatch(int userID) {
		alstNewMatchRequests.add(userID);
		int userCount = 0;
		for (Team team : playmode.getTeams()) {
			userCount += team.getUser().length;
		}
		Match nextMatch = upcomingMatches.peek();
		if((alstNewMatchRequests.size() - alstLeftUser.size()) == userCount){
			nextMatch(userID);
			alstNewMatchRequests.clear();
		}
		return nextMatch;
	}
}