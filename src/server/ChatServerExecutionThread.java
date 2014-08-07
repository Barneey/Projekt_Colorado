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

public class ChatServerExecutionThread extends Thread{
	
	private Socket chatSocket;
	private ServerDataBaseManager sDBM;

	
	public ChatServerExecutionThread(Socket chatSocket, ServerDataBaseManager sDBM){
		this.chatSocket = chatSocket;
		this.sDBM = sDBM;
	}
	
	public void run(){
		try {
			InputStream inputStream = chatSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(
					inputStream);

			OutputStream outputStream = chatSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);

			String order = (String) objectInputStream.readObject();
			switch (order) {
			case "GET_ALL_PUBLIC_CHANNELS":
				objectOutputStream.writeObject(sDBM
						.getAllChatChannels());
				objectOutputStream.flush();
				break;
			case "GET_DATE":
				objectOutputStream.writeObject(new Date());
				break;
			case "CREATE_AND_JOIN_CHANNEL":
				ChatChannel creatingAndJoiningChannel = (ChatChannel)objectInputStream.readObject();
				User user = (User)objectInputStream.readObject();
				creatingAndJoiningChannel = sDBM.createAndJoinChannel(creatingAndJoiningChannel, user);
				objectOutputStream.writeObject(creatingAndJoiningChannel);
				objectOutputStream.flush();
				break;
			case "JOIN_CHANNEL":
				ChatChannel joiningChannel = (ChatChannel)objectInputStream.readObject();
				User joiningUser = (User)objectInputStream.readObject();
				sDBM.joinUserInChannel(joiningChannel, joiningUser);
				break;
			case "LEAVE_CHANNEL":
				ChatChannel leavingChannel = (ChatChannel)objectInputStream.readObject();
				User leavingUser = (User)objectInputStream.readObject();
				sDBM.leaveChannel(leavingChannel, leavingUser);
				objectOutputStream.writeObject(true);
				objectOutputStream.flush();
				break;
			case "LEAVE_ALL_CHANNELS":
				User leavingAllChannelsUser = (User)objectInputStream.readObject();
				sDBM.leaveAllChannels(leavingAllChannelsUser);
				break;
			case "LOAD_USERS":
				ChatChannel loadingUsersChannel = (ChatChannel)objectInputStream.readObject();
				User[] users = sDBM.loadUsers(loadingUsersChannel);
				objectOutputStream.writeObject(users);
				objectOutputStream.flush();
				break;
			case "LOAD_MESSAGES":
				ChatChannel loadingMessagesChannel = (ChatChannel)objectInputStream.readObject();
				Date joinDate = (Date)objectInputStream.readObject();
				ChatMessage[] messages = sDBM.loadMessages(loadingMessagesChannel, joinDate);
				objectOutputStream.writeObject(messages);
				objectOutputStream.flush();
				break;
			case "ADD_MESSAGE":
				ChatMessage addingMessage = (ChatMessage)objectInputStream.readObject();
				sDBM.addMessage(addingMessage);
				objectOutputStream.writeObject(true);
				objectOutputStream.flush();
				break;
			default:
				break;
			}
			chatSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}