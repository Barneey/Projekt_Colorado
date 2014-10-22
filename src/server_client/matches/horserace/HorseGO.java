package server_client.matches.horserace;

import java.awt.Dimension;

import server_client.matches.GameObject;

public class HorseGO extends GameObject{
	
	
	
	public HorseGO(int x, int y, Dimension size){
		super(x, y, size);
		setGameInformation(new HorseGOI(x, y, 0));
	}
	
	public boolean isRunning(){
		return ((HorseGOI)this.getInformation()).isRunning();
	}
	
	public void setRunning(boolean running){
		((HorseGOI)this.getInformation()).setRunning(running);
	}
	
	@Override
	public void relocate(){
		if(isRunning()){
			super.relocate();
		}
	}
}
