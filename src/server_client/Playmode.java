package server_client;

import java.io.Serializable;

public class Playmode implements Serializable{

	private int pid;
	private String titel;
	private String descText;
	
	public Playmode(int pid, String titel, String descText){
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
}