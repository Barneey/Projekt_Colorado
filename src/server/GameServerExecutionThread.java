package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import server_client.Playmode;
import server_client.User;
import server_client.matches.GameObjectInformation;

public class GameServerExecutionThread extends Thread{
	
	private Socket gameSocket;
	private ServerDataBaseManager sDBM;
	private GameQueues gameQueues;
	
	public GameServerExecutionThread(Socket gameSocket, ServerDataBaseManager sDBM){
		this.gameSocket = gameSocket;
		this.sDBM = sDBM;
		this.gameQueues = GameQueues.getInstance();
	}
	
	public synchronized void run(){
		try {
			InputStream inputStream = gameSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			OutputStream outputStream = gameSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			String order = (String)objectInputStream.readObject();
			
			switch (order) {
			case "UPDATE_GAME_INFORMATION":{
				int userID = objectInputStream.readInt();
				Integer[] actions = (Integer[])objectInputStream.readObject();
				HashMap<String, GameObjectInformation> gameObjectInformation = (HashMap<String, GameObjectInformation>)objectInputStream.readObject();
				int gameID = objectInputStream.readInt();
				objectOutputStream.writeObject(GameManager.getInstance().updateGameInformation(gameObjectInformation, gameID));
				if(actions.length > 0){
					GameManager.getInstance().performActions(gameID, userID, actions);
				}
				objectOutputStream.flush();
				break;	
				}
			case "GET_GAME_EVENTS":{
				int gameID = objectInputStream.readInt();
				int userID = objectInputStream.readInt();
				objectOutputStream.writeObject(GameManager.getInstance().getGameEvents(gameID, userID));
				break;
				}
			case "GET_PLAYMODES":
				Playmode[] playmodes = sDBM.getPlaymodes();
				objectOutputStream.writeObject(playmodes);
				objectOutputStream.flush();
				GameQueues.getInstance().setPlaymodes(playmodes);
				break;
			case "LEAVE_QUEUES":
				User leavingUser = (User)objectInputStream.readObject();
				gameQueues.leaveQueues(leavingUser);
				break;
			case "JOIN_QUEUES":
				User joiningUser = (User)objectInputStream.readObject();
				Playmode[] joiningPlaymodes = (Playmode[])objectInputStream.readObject();
				for (int i = 0; i < joiningPlaymodes.length; i++) {
					gameQueues.addUserIntoQueue(joiningUser, joiningPlaymodes[i]);
				}
				break;
			case "GET_GAMEINFORMATION":
				User requestingUser = (User)objectInputStream.readObject();
				Game game = GameManager.getInstance().getNewGame(requestingUser);
				if(game == null){
					objectOutputStream.writeObject(-1);
				}else{
					objectOutputStream.writeObject(game.getGID());	
				}
				objectOutputStream.flush();
				break;
			case "GET_CURRENT_MATCH":{
				int gameID = (Integer)objectInputStream.readObject();
				objectOutputStream.writeObject(GameManager.getInstance().getCurrentMatch(gameID));
				objectOutputStream.flush();
				break;
			}
			case "SET_MATCH_LOADED":{
				int gameID = objectInputStream.readInt();
				int userID = objectInputStream.readInt();
				boolean matchLoaded = objectInputStream.readBoolean();
				GameManager.getInstance().setMatchLoaded(gameID, userID, matchLoaded);
				break;
			}
			case "IS_MATCH_FULLY_LOADED":{
				int gameID = (Integer)objectInputStream.readObject();
				objectOutputStream.writeObject(GameManager.getInstance().getCurrentMatch(gameID).isLoaded());
				objectOutputStream.flush();
				break;
			}
			case "IS_GAME_FINISHED":{
				int gameID = (Integer)objectInputStream.readObject();
				objectOutputStream.writeObject(GameManager.getInstance().getCurrentMatch(gameID) == null);
				objectOutputStream.flush();
				break;
			}
			case "LEAVE_GAME":{
				int gameID = (Integer)objectInputStream.readObject();
				int userID = (Integer)objectInputStream.readObject();
				GameManager.getInstance().leaveGame(gameID, userID);
				break;
			}
			case "LEAVE_GAMES":{
//				GameManager.getInstance().leaveGames((Integer)objectInputStream.readObject());
				break;
			}
			case "GET_SCORELIST":{
				int gameID = objectInputStream.readInt();
				objectOutputStream.writeObject(GameManager.getInstance().getScoreList(gameID));
				objectOutputStream.flush();
			}
			case "GET_NEXT_MATCH":{
				int gameID = objectInputStream.readInt();
				int userID = objectInputStream.readInt();
				objectOutputStream.writeObject(GameManager.getInstance().getNextMatch(gameID, userID));
				objectOutputStream.flush();
			}
//			case "NEXT_MATCH":{
//				int gameID = objectInputStream.readInt();
//				int userID = objectInputStream.readInt();
//				objectOutputStream.writeBoolean(GameManager.getInstance().nextMatch(gameID, userID));
//				objectOutputStream.flush();
//			}
			default:
				break;
			}
			gameSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}