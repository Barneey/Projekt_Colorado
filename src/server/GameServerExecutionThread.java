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
	
	public GameServerExecutionThread(Socket gameSocket, ServerDataBaseManager sDBM){
		this.gameSocket = gameSocket;
		this.sDBM = sDBM;
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
				objectOutputStream.writeObject(sDBM.getPlaymodes());
				objectOutputStream.flush();
				break;
			case "LEAVE_QUEUES":
				User leavingUser = (User)objectInputStream.readObject();
				break;
			case "JOIN_QUEUES":
				User joiningUser = (User)objectInputStream.readObject();
				Playmode[] joiningPlaymodes = (Playmode[])objectInputStream.readObject();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}