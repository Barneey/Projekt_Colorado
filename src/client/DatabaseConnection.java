package client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.ws.Endpoint;

import server_client.ChatChannel;
import server_client.ChatMessage;
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
	private String serverAddressCash;
	public static final int LOGIN_PORT = 4711;
	public static final int CHAT_PORT = 4712;
	public static final int CASH_PORT = 4713;
	private static DatabaseConnection instance;
	private final int TIMEOUT = 10000;

	private DatabaseConnection() {
		serverAddressLogin = "localhost";
		serverAddressChat = "localhost";
		serverAddressCash = "localhost";
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
			
			loginSocket.setSoTimeout(TIMEOUT);
			
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
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
		return isUniqueNickname;
	}
	
	public User loginUser(User user){
		try {
			Socket loginSocket = new Socket(serverAddressLogin, LOGIN_PORT);
			
			loginSocket.setSoTimeout(TIMEOUT);
			
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
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			e.printStackTrace();
			new ErrorFrame("User verification failed!");
		}
		return user;
	}
	
	public void updateUser(User user, int port){
		if(port == LOGIN_PORT || port == CASH_PORT){
			try {
				String serverAddress = (port == LOGIN_PORT ? serverAddressLogin : serverAddressCash);
				Socket socket = new Socket(serverAddress, port);
				
				socket.setSoTimeout(TIMEOUT);
				
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						outputStream);
				objectOutputStream.writeObject("UPDATE_USER");
				objectOutputStream.writeObject(user);
				objectOutputStream.flush();

				socket.close();			
			}catch (SocketException e){
				new ErrorFrame("Server unreachable!");
			} catch (SocketTimeoutException e){
				new ErrorFrame("Connection timed out");
			} catch (Exception e) {
				new ErrorFrame("An unknown Error occoured");
			}
		}
	}

	public ChatChannel[] getAllPublicChannels(){
		ChatChannel[] channels = null;
		try {
			Socket chatSocket = new Socket(serverAddressLogin, CHAT_PORT);
			
			chatSocket.setSoTimeout(TIMEOUT);
			
			OutputStream outputStream = chatSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject("GET_ALL_PUBLIC_CHANNELS");
			objectOutputStream.flush();

			InputStream inputStream = chatSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			channels = (ChatChannel[])objectInputStream.readObject();
			
			chatSocket.close();
			
		}catch (SocketException e){
			new ErrorFrame("Chat Server unreachable!");
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
			e.printStackTrace();
		}
		return channels;
	}

	public Date getServerDate(int port){
		Date date = null;
		if(port == LOGIN_PORT || port == CHAT_PORT){
			try {
				String serverAddress = (port == LOGIN_PORT ? serverAddressLogin : serverAddressChat);
				Socket socket = new Socket(serverAddress, port);
				
				socket.setSoTimeout(TIMEOUT);
				
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject("GET_DATE");
				objectOutputStream.flush();

				InputStream inputStream = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				date = (Date)objectInputStream.readObject();
				
				socket.close();			
			}catch (SocketException e){
				new ErrorFrame("Server unreachable!");
			} catch (SocketTimeoutException e){
				new ErrorFrame("Connection timed out");
			} catch (Exception e) {
				new ErrorFrame("An unknown Error occoured");
			}
		}
		return date;
	}
	
	public void joinChannel(ChatChannel channel, User user, boolean channelExists){
		try {
			Socket socket = new Socket(serverAddressChat, CHAT_PORT);
			
			socket.setSoTimeout(TIMEOUT);
			
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			if(!channelExists){
				objectOutputStream.writeObject("CREATE_CHANNEL");
				objectOutputStream.writeObject(channel);
				objectOutputStream.flush();
				
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				channel = (ChatChannel)objectInputStream.readObject();
			}
			objectOutputStream.writeObject("JOIN_CHANNEL");
			objectOutputStream.writeObject(channel);
			objectOutputStream.writeObject(user);
			objectOutputStream.flush();

			socket.close();
			
		}catch (SocketException e){
			new ErrorFrame("Server unreachable!");
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
	}
	
	public User[] loadUsers(ChatChannel channel){
		User[] users = null;
		try {
			Socket socket = new Socket(serverAddressChat, CHAT_PORT);
			
			socket.setSoTimeout(TIMEOUT);
			
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			objectOutputStream.writeObject("LOAD_USERS");
			objectOutputStream.writeObject(channel);
			objectOutputStream.flush();
			
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			users = (User[])objectInputStream.readObject();
			
			
			socket.close();
			
		}catch (SocketException e){
			new ErrorFrame("Server unreachable!");
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
		return users;
	}
	
	public ChatMessage[] loadMessages(ChatChannel channel, Date joinDate){
		ChatMessage[] chatMessages = null;
		try {
			Socket socket = new Socket(serverAddressChat, CHAT_PORT);
			
			socket.setSoTimeout(TIMEOUT);
			
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			objectOutputStream.writeObject("LOAD_MESSAGES");
			objectOutputStream.writeObject(channel);
			objectOutputStream.writeObject(joinDate);
			objectOutputStream.flush();
			
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			chatMessages = (ChatMessage[])objectInputStream.readObject();

			socket.close();
			
		}catch (SocketException e){
			new ErrorFrame("Server unreachable!");
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
		return chatMessages;
	}
	
	public void addMessage(ChatMessage chatMessage){
		try {
			Socket socket = new Socket(serverAddressChat, CHAT_PORT);
			
			socket.setSoTimeout(TIMEOUT);
			
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			objectOutputStream.writeObject("ADD_MESSAGE");
			objectOutputStream.writeObject(chatMessage);
			objectOutputStream.flush();
			
			socket.close();
			
		}catch (SocketException e){
			new ErrorFrame("Server unreachable!");
		} catch (SocketTimeoutException e){
			new ErrorFrame("Connection timed out");
		} catch (Exception e) {
			new ErrorFrame("An unknown Error occoured");
		}
	}
}