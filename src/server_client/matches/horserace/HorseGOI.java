package server_client.matches.horserace;

import server_client.matches.GameObjectInformation;

public class HorseGOI extends GameObjectInformation{

	private boolean running = false;
	
	public HorseGOI(int x, int y, int viewDegree){
		super(x, y, viewDegree);
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
}