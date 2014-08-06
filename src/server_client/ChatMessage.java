package server_client;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String toString(boolean showTimestamp){
		String chatString = username + ": " + message;
		if(showTimestamp){
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			String formattedDate = formatter.format(messageDate);
			return "[" + formattedDate + "] " + chatString;
		}else{
			return chatString;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChatMessage))
			return false;
		ChatMessage other = (ChatMessage) obj;
		if (channelCID != other.channelCID)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageDate == null) {
			if (other.messageDate != null)
				return false;
		} else if (!messageDate.equals(other.messageDate))
			return false;
		if (userID != other.userID)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}