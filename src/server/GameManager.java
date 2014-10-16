package server;

import java.util.ArrayList;
import java.util.HashMap;

import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObjectInformation;
import server_client.matches.Match;
import server_client.matches.Score;
import server_client.matches.ScoreList;
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
		int matchType = Match.TEST;
		if(playmode.getTitel().equals("FFA")){
			matchType = Match.FFA;
		}else if(playmode.getTitel().equals("TEAM")){
			matchType = Match.TEAM;
		}else if(playmode.getTitel().equals("UNDERDOG")){
			matchType = Match.UNDERDOG;
		}else if(playmode.getTitel().equals("Test")){
			matchType = Match.TEST;
		}
		// TODO find a way to add new games according to the playmode
		game.addMatch(new SoccerMatch(matchType, playmode));
		
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

	public Match getCurrentMatch(int gameID, int userID) {
		return gameIDtoGame.get(gameID).getCurrentMatch(userID);
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

	public synchronized HashMap<String, GameObjectInformation> updateGameInformation(HashMap<String, GameObjectInformation> gameObjectInformation,int gameID, int userID) {
		Match currentMatch = getCurrentMatch(gameID, userID);
		currentMatch.updateGameInformation(gameObjectInformation);
		return currentMatch.getGameInformation();
	}

	public Integer[] getGameEvents(int gameID, int userID) {
		Match currentMatch = getCurrentMatch(gameID, userID);
		return currentMatch.getEventsFor(userID);
		
	}

	public void performActions(int gameID, int userID, Integer[] actions) {
		getCurrentMatch(gameID, userID).performClientActions(userID, actions);
	}

	public ScoreList getScoreList(int gameID, int userID) {
		return getCurrentMatch(gameID, userID).getScoreList();
	}

	public Match getNextMatch(int gameID, int userID) {
		return gameIDtoGame.get(gameID).requestNextMatch(userID);
	}
}