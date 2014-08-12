package server_client;

import java.io.Serializable;
import java.util.Map;

public class Playmode implements Serializable{

	private int pid;
	private String titel;
	private String descText;
	private Integer[] teamSizes;
	
	public Playmode(int pid, String titel, String descText, Integer[] teamSizes){
		this.pid = pid;
		this.titel = titel;
		this.descText = descText;
		this.teamSizes = teamSizes;
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
		return teamSizes;
	}
}