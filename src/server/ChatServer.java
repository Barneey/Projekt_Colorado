package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread{

	private boolean running;
	
	public void stopMe(){
		running = false;
	}
	
	public void continueMe(){
		running = true;
	}
	
	public void run(){
		running = true;	
		final int CHAT_PORT = 4712;
		try {
			ServerSocket chatServerSocket = new ServerSocket(CHAT_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while(true){
				if(running){
					Socket chatSocket = chatServerSocket.accept();
					
					InputStream inputStream = chatSocket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					
					OutputStream outputStream = chatSocket.getOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

					String order = (String)objectInputStream.readObject();
					switch (order) {
					case "GET_ALL_PUBLIC_CHANNELS":
						objectOutputStream.writeObject(sDBM.getAllChatChannels());
						objectOutputStream.flush();
						break;

					default:
						break;
					}
					chatSocket.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
