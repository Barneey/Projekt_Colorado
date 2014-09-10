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
	private Match currentMatch;
	
	public PlayFrame(int gameID,  User user){
		gameCon = GameConnection.getInstance();
		this.gameID = gameID;
		this.user = user;
		
		try {
			currentMatch = gameCon.getCurrentMatch(gameID);
			setLayout(new BorderLayout());
			JLabel jlblMessage = new JLabel("Loading data...", SwingConstants.CENTER);
			add(jlblMessage, BorderLayout.NORTH);
			setSize(currentMatch.getSize().width + 16, currentMatch.getSize().height + 39);
			setLocationRelativeTo(null);
			setVisible(true);
			

			boolean everyoneFinishedLoading = false;
			boolean matchRunning = false;
			boolean matchLoaded = false;
			boolean gameFinished = false;
			while(!gameFinished){
				if(!matchLoaded){
					currentMatch.loadImages();
					gameCon.setMatchLoaded(gameID, user.getId(), true);
					matchLoaded = true;
					jlblMessage.setText("Waiting for other players...");
				}
				while(!everyoneFinishedLoading){
					everyoneFinishedLoading = gameCon.isEveryoneFinishedLoading(gameID);
					try {
						Thread.sleep(66);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				if(!matchRunning && everyoneFinishedLoading){
					remove(jlblMessage);
					add(currentMatch);
					(new Thread(currentMatch)).start();	
					matchRunning = true;
				}
				if(matchRunning){
					if(gameCon.isGameFinished(gameID)){
						gameFinished = true;
						dispose();
						// TODO handle game finished;
					}else{
						if(!gameCon.isEveryoneFinishedLoading(gameID)){
							// Neues Match muss geladen werden
						}
					}
					try {
						Thread.sleep(66);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
			}
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
	}
}