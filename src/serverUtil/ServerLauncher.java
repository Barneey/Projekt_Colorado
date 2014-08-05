package serverUtil;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import server.ChatServer;
import server.GameServer;
import server.LoginServer;
import server.MainServer;

public class ServerLauncher extends JFrame{
	
	public static void main(String args[]){
		new ServerLauncher();
	}
	
	public ServerLauncher(){
		super("Server Launcher");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		final Dimension PANEL_SIZE = new Dimension(270, 50);
		
		ServerLaunchPanel slpLoginServer = new ServerLaunchPanel("Login Server", new LoginServer());
		slpLoginServer.setPreferredSize(PANEL_SIZE);
		add(slpLoginServer);
		
		ServerLaunchPanel slpChatServer = new ServerLaunchPanel("Chat Server", new ChatServer());
		slpChatServer.setPreferredSize(PANEL_SIZE);
		add(slpChatServer);

		ServerLaunchPanel slpMainServer = new ServerLaunchPanel("Main Server", new MainServer());
		slpMainServer.setPreferredSize(PANEL_SIZE);
		add(slpMainServer);
		
		ServerLaunchPanel slpGameServer = new ServerLaunchPanel("Game Server", new GameServer());
		slpGameServer.setPreferredSize(PANEL_SIZE);
		add(slpGameServer);
		
		pack();
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}