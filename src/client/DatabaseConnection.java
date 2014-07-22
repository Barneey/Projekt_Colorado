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

import server_client.ChatChannel;
import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */

public class DatabaseConnection {
	
	private String serverAddressLogin;
	private String serverAddressChat;
	public static final int LOGIN_PORT = 4711;
	public static final int CHAT_PORT = 4712;
	public static final int CASH_PORT = 4713;
	private static DatabaseConnection instance;

	private DatabaseConnection() {
		serverAddressLogin = "localhost";
		serverAddressChat = "localhost";
	}
	
	public static DatabaseConnection getInstance(){
		if(instance == null){
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
	public boolean validateUniqueNickname(String nickname){
		boolean isUniqueNickname = false;
		try {
			Socket loginSocket = new Socket(serverAddressLogin, LOGIN_PORT);
			
			OutputStream outputStream = loginSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject("CHECK_UNIQUE_NICKNAME");
			objectOutputStream.writeObject(nickname);
			objectOutputStream.flush();

			InputStream inputStream = loginSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			isUniqueNickname = objectInputStream.readBoolean();
			
			
			loginSocket.close();
		}catch (SocketException e){
			new ErrorFrame("Login Server unreachable!");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
		return isUniqueNickname;
	}
	
	public User loginUser(User user){
		try {
			Socket loginSocket = new Socket(serverAddressLogin, LOGIN_PORT);
			
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
	
	public void updateUser(User user, int port){
		if(port == LOGIN_PORT || port == CASH_PORT){
			try {
				String serverAddress = (port == LOGIN_PORT ? serverAddressLogin : serverAddressChat);
				Socket loginSocket = new Socket(serverAddress, port);
				
				OutputStream outputStream = loginSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						outputStream);
				objectOutputStream.writeObject("UPDATE_USER");
				objectOutputStream.writeObject(user);
				objectOutputStream.flush();

				loginSocket.close();			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ChatChannel[] getAllPublicChannels(){
		ChatChannel[] channels = null;
		try {
			Socket chatSocket = new Socket(serverAddressLogin, CHAT_PORT);
		
			OutputStream outputStream = chatSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject("GET_ALL_PUBLIC_CHANNELS");
			objectOutputStream.flush();

			InputStream inputStream = chatSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			channels = (ChatChannel[])objectInputStream.readObject();
			
			chatSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channels;
	}
}