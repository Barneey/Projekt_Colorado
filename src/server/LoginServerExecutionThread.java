package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import server_client.User;

public class LoginServerExecutionThread extends Thread{

	private Socket loginSocket;
	private ServerDataBaseManager sDBM;
	
	public LoginServerExecutionThread(Socket loginSocket, ServerDataBaseManager sDBM){
		this.loginSocket = loginSocket;
		this.sDBM = sDBM;
	}
	
	public void run(){
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}