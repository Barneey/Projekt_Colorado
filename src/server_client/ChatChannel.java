package server_client;

import java.io.Serializable;

public class ChatChannel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2158745707425588062L;
	private int channelID;
	private String channelType;
	private String channelName;
	
	public ChatChannel(int channelID, String channelType, String channelName){
		this.channelID = channelID;
		this.channelType = channelType;
		this.channelName = channelName;
	}

	public void setChannelID(int channelID){
		this.channelID = channelID;
	}
	
	public int getChannelID() {
		return channelID;
	}

	public String getChannelType() {
		return channelType;
	}

	public String getChannelName() {
		return channelName;
	}
	
	@Override
	public String toString(){
		return channelName;
	}
}