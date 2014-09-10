package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import server_client.Playmode;
import server_client.User;

public class GameServerExecutionThread extends Thread{
	
	private Socket gameSocket;
	private ServerDataBaseManager sDBM;
	private GameQueues gameQueues;
	
	public GameServerExecutionThread(Socket gameSocket, ServerDataBaseManager sDBM){
		this.gameSocket = gameSocket;
		this.sDBM = sDBM;
		this.gameQueues = GameQueues.getInstance();
	}
	
	public void run(){
		try {
			InputStream inputStream = gameSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			OutputStream outputStream = gameSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			String order = (String)objectInputStream.readObject();
			
			switch (order) {
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
				int gameID = (Integer)objectInputStream.readObject();
				int userID = (Integer)objectInputStream.readObject();
				boolean matchLoaded = (Boolean)objectInputStream.readObject();
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
//				int userID
				break;
			}
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}