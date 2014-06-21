package serverUtil;

import java.awt.Dimension;

import javax.swing.JFrame;

import server.ChatServer;
import server.LoginServer;

public class ServerLauncher extends JFrame{
	
	public static void main(String args[]){
		new ServerLauncher();
	}
	
	public ServerLauncher(){
		super("Server Launcher");
		setLayout(null);

		
		final Dimension PANEL_SIZE = new Dimension(270, 50);
		ServerLaunchPanel slpLoginServer = new ServerLaunchPanel("Login Server", new LoginServer());
		slpLoginServer.setSize(PANEL_SIZE);
		slpLoginServer.setLocation(5, 5);
		add(slpLoginServer);
		
		ServerLaunchPanel slpChatServer = new ServerLaunchPanel("Chat server", new ChatServer());
		slpChatServer.setSize(PANEL_SIZE);
		slpChatServer.setLocation(5, 50);
		add(slpChatServer);

		final Dimension FRAME_SIZE = new Dimension((int)PANEL_SIZE.getWidth() + 10, (int)(PANEL_SIZE.getHeight() * 2.3) + 10);
		setSize(FRAME_SIZE);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
