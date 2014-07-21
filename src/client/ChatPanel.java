package client;

import javax.swing.JLabel;
import javax.swing.JPanel;

import server_client.User;

public class ChatPanel extends JPanel{
	
	public ChatPanel(User user){
		setLayout(null);
		
		JLabel jlblTest = new JLabel("Test");
		jlblTest.setSize(50, 25);
		jlblTest.setLocation(10, 10);
		this.add(jlblTest);
		
		this.setSize(200, 110);
	}
}