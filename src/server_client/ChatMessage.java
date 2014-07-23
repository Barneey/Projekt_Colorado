package server_client;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable{

	private int userID;
	private String username;
	private int channelCID;
	private String message;
	private Date messageDate;
	
	public ChatMessage(String username, int userID, int channelCID, String message){
		this.username = username;
		this.userID = userID;
		this.channelCID = channelCID;
		this.message = message;
	}
	
	public int getChannelCID(){
		return this.channelCID;
	}
	
	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public int getUserID() {
		return userID;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String toString(boolean showTimestamp){
		return (showTimestamp ? "[" + messageDate + "] " : "" ) + username + ": " + message;
	}
}