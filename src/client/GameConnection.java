package client;

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
	
	

}
