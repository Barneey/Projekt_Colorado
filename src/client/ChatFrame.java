package client;

import javax.swing.JFrame;

import server_client.ChatChannel;

public class ChatFrame extends JFrame{
	
	public ChatFrame(ChatChannel channel){
		construct(channel, true);
	}
	
	public ChatFrame(ChatChannel channel, boolean channelExists){
		construct(channel, channelExists);
	}
	
	private void construct(ChatChannel channel, boolean channelExists){
		setTitle(channel.getChannelName());
		
		
		
		
		setLocationRelativeTo(null);
		setVisible(true);		
	}
}