package server;

import java.net.ServerSocket;

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
					new GameServerExecutionThread(gameServerSocket.accept(), sDBM).start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}