package server_client;

import java.io.Serializable;
import java.util.ArrayList;

public class Playmode implements Serializable{

	private int pmID;
	private String titel;
	private String descText;
	private ArrayList<Team> alstTeams;
	
	public Playmode(int pmID, String titel, String descText){
		this.alstTeams = new ArrayList<>();
		this.pmID = pmID;
		this.titel = titel;
		this.descText = descText;
	}
	
	public int getPmID() {
		return pmID;
	}

	public String getTitel() {
		return titel;
	}

	public String getDescText() {
		return descText;
	}
	
	public Integer[] getTeamSizes(){
		return alstTeams.toArray(new Integer[0]);
	}
	
	public int getNeededPlayerCount(){
		int playerCount = 0;
		for (Team t : alstTeams) {
			playerCount+=t.getTeamSize();
		}
		return playerCount;
	}
	
	public Team[] getTeams(){
		return alstTeams.toArray(new Team[0]);
	}

	public void addTeam(Team team) {
		alstTeams.add(team);
	}
}