package server;

import server_client.Playmode;

public class Game {
	
	private int gID;
	private Playmode playmode;
	
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
}