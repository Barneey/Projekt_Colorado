package server_client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Playmode implements Serializable{

	private int pid;
	private String titel;
	private String descText;
	private ArrayList<Integer> teamSizes;
	
	public Playmode(int pid, String titel, String descText){
		this.teamSizes = new ArrayList<>();
		this.pid = pid;
		this.titel = titel;
		this.descText = descText;
	}
	
	public int getPid() {
		return pid;
	}

	public String getTitel() {
		return titel;
	}

	public String getDescText() {
		return descText;
	}
	
	public Integer[] getTeamSizes(){
		return teamSizes.toArray(new Integer[0]);
	}
	
	public int getNeededPlayerCount(){
		int playerCount = 0;
		for (Integer i : teamSizes) {
			playerCount+=i;
		}
		return playerCount;
	}

	public void addPlaymodeTeam(int size) {
		teamSizes.add(size);
	}
}