package server;

import java.net.ServerSocket;

public class LoginServer extends Server{

	private boolean running;
	
	public void stopMe(){
		running = false;
	}
	
	public void continueMe(){
		running = true;
	}
	
	public void run(){
		running = true;
		final int LOGIN_PORT = 4711;
		try {
			ServerSocket loginServerSocket = new ServerSocket(LOGIN_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while(true){
				if(running){
					new LoginServerExecutionThread(loginServerSocket.accept(), sDBM).start();
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}