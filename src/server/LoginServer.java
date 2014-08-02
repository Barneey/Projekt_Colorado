package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import server_client.User;

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
							user.setLastLogin(new Date());
							sDBM.updateUser(user);
							break;
						case "CHECK_UNIQUE_NICKNAME":
							String nickname = objectInputStream.readObject().toString();
							objectOutputStream.writeBoolean(sDBM.checkUniqueNickname(nickname));
							objectOutputStream.flush();
							break;
						case "UPDATE_USER":
							sDBM.updateUser((User)objectInputStream.readObject());
							break;
						case "GET_DATE":
							objectOutputStream.writeObject(new Date());
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