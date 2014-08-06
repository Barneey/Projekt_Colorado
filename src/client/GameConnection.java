package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameConnection extends ServerConnection{
	
	private static GameConnection instance;
	private final int TIMEOUT = 5000;
	
	private GameConnection(){
		
	}
	
	public static GameConnection getInstance(){
		if(instance == null){
			instance = new GameConnection();
		}
		return instance;
	}
	
	public void joinQueue() throws UnknownHostException, IOException{
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
	}

}
