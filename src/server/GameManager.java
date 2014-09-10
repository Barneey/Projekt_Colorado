package server;

import java.util.ArrayList;
import java.util.HashMap;

import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.Match;
import server_client.matches.soccer.SoccerMatch;

public class GameManager {
	
	
	private static GameManager instance;
	private HashMap<Integer, Game> gameIDtoGame;
	private HashMap<Integer, Game> userIDtoNewGame;
	private HashMap<Integer, ArrayList<Integer>> userIDtoRunningGameID;
	
	private GameManager(){
		gameIDtoGame = new HashMap<>();
		userIDtoNewGame = new HashMap<>();
		userIDtoRunningGameID = new HashMap<>();
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
		// TODO find a way to add new games according to the playmode
		game.addMatch(new SoccerMatch(0, playmode));
		ServerDataBaseManager sDBM = new ServerDataBaseManager();
		sDBM.createNewGame(game);
		gameIDtoGame.put(game.getGID(), game);
		for (int j = 0; j < user.length; j++) {
			userIDtoNewGame.put(user[j].getID(), game);
		}
	}

	public Game getNewGame(User user) {
		return userIDtoNewGame.remove(user.getID());
	}

	public Match getCurrentMatch(int gameID) {
		return gameIDtoGame.get(gameID).getCurrentMatch();
	}

	public void setMatchLoaded(int gameID, int userID, boolean matchLoaded) {
		gameIDtoGame.get(gameID).setMatchLoaded(userID, matchLoaded);
		ArrayList<Integer> runningGames = userIDtoRunningGameID.get(userID);
		if(runningGames == null){
			runningGames = new ArrayList<>();
		}
		runningGames.add(gameID);
		userIDtoRunningGameID.put(userID, runningGames);
	}
	
	public boolean isMatchLoaded(){
		return false;
	}

	public void leaveGame(int gameID, int userID) {
		gameIDtoGame.get(gameID).leaveUser(userID);
	}
}
