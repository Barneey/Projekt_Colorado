package client;

import javax.swing.JFrame;

import server_client.ChatChannel;

public class ChatFrame extends JFrame{
	
	public ChatFrame(ChatChannel channel){
		super(channel.getChannelName());
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}