package server;

import java.util.HashMap;

import server_client.Playmode;
import server_client.Team;
import server_client.User;

public class GameManager {
	
	
	private static GameManager instance;
	private HashMap<Integer, Game> gameIDtoGame;
	private HashMap<Integer, Game> userIDtoNewGame;
	
	private GameManager(){
		gameIDtoGame = new HashMap<>();
		userIDtoNewGame = new HashMap<>();
	}

	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	
	public void createNewGame(Playmode playmode, User[] user) throws IndexOutOfBoundsException{
		int i = 0;
		for (Team team : playmode.getTeams()) {
			while(team.getCurrentPlayerCount() < team.getTeamSize()){
				team.addUser(user[i]);
				i++;
			}
		}
		Game game = new Game(playmode);
		ServerDataBaseManager sDBM = new ServerDataBaseManager();
		sDBM.createNewGame(game);
		gameIDtoGame.put(game.getGID(), game);
		for (int j = 0; j < user.length; j++) {
			userIDtoNewGame.put(user[j].getId(), game);
		}
	}

	public Game getNewGame(User user) {
		return userIDtoNewGame.remove(user.getId());
	}
}
