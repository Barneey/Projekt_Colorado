package client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import server_client.User;
import server_client.matches.Match;


public class PlayFrame extends JFrame{

	private GameConnection gameCon;
	private int gameID;
	private User user;
	
	public PlayFrame(int gameID,  User user){
		gameCon = GameConnection.getInstance();
		this.gameID = gameID;
		this.user = user;
		
		Match currentMatch;
		try {
			currentMatch = gameCon.getCurrentMatch(gameID);
			setLayout(new BorderLayout());
			JLabel jlblMessage = new JLabel("Loading data...", SwingConstants.CENTER);
			add(jlblMessage, BorderLayout.NORTH);
			setSize(currentMatch.getSize().width + 16, currentMatch.getSize().height + 39);
			setLocationRelativeTo(null);
			setVisible(true);
			
			currentMatch.loadImages();
			gameCon.setMatchLoaded(gameID, user.getId(), true);
			remove(jlblMessage);
			add(currentMatch);
			
			(new Thread(currentMatch)).start();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLocationRelativeTo(null);
		setVisible(true);
	}
}