package server;

public abstract class Server extends Thread{
	
	public abstract void stopMe();
	
	public abstract void continueMe();
}
