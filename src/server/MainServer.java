package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer extends Server{

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
		final int MAIN_PORT = 4714;
		try {
			ServerSocket mainServerSocket = new ServerSocket(MAIN_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while(true){
				if(running){
					Socket mainSocket = mainServerSocket.accept();
					
					InputStream inputStream = mainSocket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					
					OutputStream outputStream = mainSocket.getOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
					
					String order = (String)objectInputStream.readObject();
					
					switch (order) {
					case "GET_RANKING":
						objectOutputStream.writeObject(sDBM.getRanking());
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