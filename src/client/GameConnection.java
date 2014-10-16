package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import server_client.Playmode;
import server_client.User;
import server_client.matches.GameObject;
import server_client.matches.GameObjectInformation;
import server_client.matches.Match;
import server_client.matches.Score;
import server_client.matches.ScoreList;

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

	public synchronized void leaveQueues(User user) throws UnknownHostException, IOException, SocketTimeoutException{
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject("LEAVE_QUEUES");
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
		
		socket.close();
	
	}

	public synchronized void joinQueues(User user, Playmode[] playmodes) throws UnknownHostException, IOException, SocketTimeoutException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject("JOIN_QUEUES");
		objectOutputStream.writeObject(user);
		objectOutputStream.writeObject(playmodes);
		objectOutputStream.flush();
		
		socket.close();
	}

	public synchronized int getGameinformation(User user) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("GET_GAMEINFORMATION");
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
		
		int gameID = (Integer)objectInputStream.readObject();
		
		socket.close();
		return gameID;
	}

	public synchronized Match getCurrentMatch(int gameID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("GET_CURRENT_MATCH");
		objectOutputStream.writeObject(gameID);
		objectOutputStream.flush();
		
		Match currentMatch = (Match)objectInputStream.readObject();
		
		socket.close();
		
		return currentMatch;
	}

	public synchronized void setMatchLoaded(int gameID, int id, Boolean b) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		objectOutputStream.writeObject("SET_MATCH_LOADED");
		objectOutputStream.writeInt(gameID);
		objectOutputStream.writeInt(id);
		objectOutputStream.writeBoolean(b);
		objectOutputStream.flush();
		
		socket.close();
	}

	public synchronized boolean isEveryoneFinishedLoading(int gameID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("IS_MATCH_FULLY_LOADED");
		objectOutputStream.writeObject(gameID);
		objectOutputStream.flush();
		
		boolean isMatchFullyLoaded = (Boolean)objectInputStream.readObject();
		
		socket.close();
		
		return isMatchFullyLoaded;
	}

	public synchronized boolean isGameFinished(int gameID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("IS_GAME_FINISHED");
		objectOutputStream.writeObject(gameID);
		objectOutputStream.flush();
		
		boolean gameFinished = (Boolean)objectInputStream.readObject();
		
		socket.close();
		
		return gameFinished;
	}

	public synchronized void leaveGame(int gameID, int userID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		objectOutputStream.writeObject("LEAVE_GAME");
		objectOutputStream.writeObject(gameID);
		objectOutputStream.writeObject(userID);
		objectOutputStream.flush();
		
		socket.close();
	}
	
	public synchronized void leaveGames(int userID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		objectOutputStream.writeObject("LEAVE_GAMES");
		objectOutputStream.writeObject(userID);
		objectOutputStream.flush();
		
		socket.close();
	}

	public synchronized HashMap<String, GameObjectInformation> updateGameObjects(int userID, Integer[] actions, HashMap<String, GameObjectInformation> clientPlayerObjects, int gameID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

		objectOutputStream.writeObject("UPDATE_GAME_INFORMATION");
		objectOutputStream.writeInt(userID);
		objectOutputStream.writeObject(actions);
		objectOutputStream.writeObject(clientPlayerObjects);
		objectOutputStream.writeInt(gameID);
		objectOutputStream.flush();
		
		HashMap<String, GameObjectInformation> gameObjectInformation = new HashMap<>();
		
		gameObjectInformation = (HashMap<String, GameObjectInformation>)objectInputStream.readObject();
		
		socket.close();
		
		return gameObjectInformation;
	}

	public Integer[] getGameEvents(int gameID, int userID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Integer[] gameEvents = new Integer[0];
		
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

		objectOutputStream.writeObject("GET_GAME_EVENTS");
		objectOutputStream.writeInt(gameID);
		objectOutputStream.writeInt(userID);
		objectOutputStream.flush();
		
		gameEvents = (Integer[])objectInputStream.readObject();
		
		socket.close();
		
		return gameEvents;
	}

	public ScoreList getScoreList(int gameID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		ScoreList scoreList = null;
		
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("GET_SCORELIST");
		objectOutputStream.writeInt(gameID);
		objectOutputStream.flush();
		

		scoreList = (ScoreList)objectInputStream.readObject();
		
		socket.close();
		
		return scoreList;
	}

	public synchronized Match getNextMatch(int gameID, int userID) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("GET_NEXT_MATCH");
		objectOutputStream.writeInt(gameID);
		objectOutputStream.writeInt(userID);
		objectOutputStream.flush();
		
		Match nextMatch = (Match)objectInputStream.readObject();
		
		socket.close();
		
		return nextMatch;
	}

//	public boolean nextMatch(int gameID, int userID) throws UnknownHostException, IOException, SocketException, ClassNotFoundException{
//		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
//		
//		OutputStream outputStream = socket.getOutputStream();
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//		
//		InputStream inputStream = socket.getInputStream();
//		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//		
//		objectOutputStream.writeObject("NEXT_MATCH");
//		objectOutputStream.writeInt(gameID);
//		objectOutputStream.writeInt(userID);
//		objectOutputStream.flush();
//		
//		boolean nextMatch = objectInputStream.readBoolean();
//		
//		socket.close();
//		return nextMatch;
//	}
}