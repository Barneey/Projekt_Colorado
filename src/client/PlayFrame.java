package client;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import server_client.matches.Match;


public class PlayFrame extends JFrame{

	private GameConnection gameCon;
	
	public PlayFrame(int gameID){
		gameCon = GameConnection.getInstance();
		
		Match currentMatch;
		try {
			currentMatch = gameCon.getCurrentMatch(gameID);
			setLayout(null);
			add(currentMatch);
			setSize(currentMatch.getSize().width + 16, currentMatch.getSize().height + 39);
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