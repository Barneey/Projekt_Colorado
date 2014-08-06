package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import server_client.ChatChannel;
import server_client.ChatMessage;
import server_client.User;

public class CashServerExecutionThread extends Thread{
	
	private Socket cashSocket;
	private ServerDataBaseManager sDBM;

	
	public CashServerExecutionThread(Socket cashSocket, ServerDataBaseManager sDBM){
		this.cashSocket = cashSocket;
		this.sDBM = sDBM;
	}
	
	public void run(){
		try {
			InputStream inputStream = cashSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(
					inputStream);

			OutputStream outputStream = cashSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);

			String order = (String) objectInputStream.readObject();
			switch (order) {
			case "GET_DATE":
				objectOutputStream.writeObject(new Date());
				break;
			default:
				break;
			}
			cashSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}