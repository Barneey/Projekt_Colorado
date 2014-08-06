package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainServerExecutionThread extends Thread{

	private Socket mainSocket;
	private ServerDataBaseManager sDBM;
	
	public MainServerExecutionThread(Socket mainSocket, ServerDataBaseManager sDBM){
		this.mainSocket = mainSocket;
		this.sDBM = sDBM;
	}
	
	public void run(){
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}