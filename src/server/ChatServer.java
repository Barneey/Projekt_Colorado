package server;

import java.net.ServerSocket;

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
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
