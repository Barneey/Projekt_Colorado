package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import server.Game;
import server_client.Playmode;
import server_client.User;

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

	public void leaveQueues(User user) throws UnknownHostException, IOException, SocketTimeoutException{
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject("LEAVE_QUEUES");
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
		
		socket.close();
	
	}

	public void joinQueues(User user, Playmode[] playmodes) throws UnknownHostException, IOException, SocketTimeoutException {
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

	public Game getGameinformation(User user) throws UnknownHostException, IOException, SocketTimeoutException, ClassNotFoundException {
		Socket socket = new Socket(SERVER_ADDRESS_GAME, GAME_PORT);
		
		socket.setSoTimeout(TIMEOUT);
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		objectOutputStream.writeObject("");
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
		
		Game game = (Game)objectInputStream.readObject();
		
		socket.close();
		return game;
	}

}
