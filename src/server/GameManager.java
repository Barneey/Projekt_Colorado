package server;

import server_client.Playmode;
import server_client.User;

public class GameManager {
	
	
	private static GameManager instance;
	
	private GameManager(){
		
	}

	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	
	public void createNewGame(Playmode playmode, User[] user){
		System.out.println("new game created");
	}
}
