package server;

import java.net.ServerSocket;

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
					new MainServerExecutionThread(mainServerSocket.accept(), sDBM).run();;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}