package server;

import java.net.ServerSocket;

public class CashServer extends Server {

	private boolean running;

	public void stopMe() {
		running = false;
	}

	public void continueMe() {
		running = true;
	}

	public void run() {
		running = true;
		final int CASH_PORT = 4713;
		try {
			ServerSocket cashServerSocket = new ServerSocket(CASH_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while (true) {
				if (running) {
					new CashServerExecutionThread(cashServerSocket.accept(), sDBM).run();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}