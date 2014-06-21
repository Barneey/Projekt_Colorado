package client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.xml.ws.Endpoint;

import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */

public class DatabaseConnection {
	
	private String serverAddressLogin;
	private int portLogin;

	public DatabaseConnection() {
		serverAddressLogin = "localhost";
		portLogin = 4711;
	}
	
	public User loginUser(User user){
		try {

			Socket loginSocket = new Socket(serverAddressLogin, portLogin);
			
			OutputStream outputStream = loginSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			objectOutputStream.writeObject("VERIFY_LOGIN");
			objectOutputStream.writeObject(user);
			objectOutputStream.flush();
			
			InputStream inputStream = loginSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			user = (User)objectInputStream.readObject();
			loginSocket.close();			
		}catch (SocketException e){
			new ErrorFrame("Login Server unreachable!");
		} catch (Exception e) {
			new ErrorFrame("User verification failed!");
		}
		return user;
	}
}
