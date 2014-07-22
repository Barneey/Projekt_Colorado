package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server_client.User;

public class LoginServer extends Thread{

	private boolean running;
	
	public void stopMe(){
		running = false;
	}
	
	public void continueMe(){
		running = true;
	}
	
	public void run(){
		running = true;
		final int LOGGIN_PORT = 4711;
		try {
			ServerSocket loginServerSocket = new ServerSocket(LOGGIN_PORT);
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			while(true){
				if(running){

					Socket loginSocket = loginServerSocket.accept(); 
					
					InputStream inputStream = loginSocket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					OutputStream outputStream = loginSocket.getOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

					String order = (String)objectInputStream.readObject();
					
					switch(order){
						case "VERIFY_LOGIN":
							User user = (User) objectInputStream.readObject();
							sDBM.verifyUser(user);
							objectOutputStream.writeObject(user);
							objectOutputStream.flush();
							break;
						default:

							break;
					}
					loginSocket.close();	
				}			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}