package server_client;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable{
	
	private ArrayList<User> alstUsers;
	private int teamsize;
	private int pmtID;
	
	public Team(int pmtID, int teamsize){
		alstUsers = new ArrayList<>();
		this.pmtID = pmtID;
		this.teamsize = teamsize;
	}
	
	public void addUser(User user){
		alstUsers.add(user);
	}
	
	public int getPmtID(){
		return this.pmtID;
	}
	
	public int getTeamSize(){
		return this.teamsize;
	}
	
	public int getCurrentPlayerCount(){
		return alstUsers.size();
	}
}