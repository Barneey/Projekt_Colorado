package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Server{

	private boolean running;
	
	@Override
	public void stopMe() {
		running = false;
	}

	@Override
	public void continueMe() {
		running = true;
	}
	
	public void run(){
		running = true;
		final int GAME_PORT = 4715;
		try {
			ServerSocket gameServerSocket = new ServerSocket(GAME_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while(true){
				if(running){
					Socket gameSocket = gameServerSocket.accept();
					
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

					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}