package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server_client.Playmode;
import server_client.User;

public class PlayPanel extends JPanel{
	
	private DatabaseConnection dbCon;
	private GameConnection gameCon;
	private ArrayList<PlaymodePanel> alstPlaymodePanels;
	private JPanel playmodePanel;
	private JGCButton jbttnJoinGameQueues;
	private JGCButton jbttnLeaveGameQueues;
	private User user;
	private JLabel jlblMessage;
	private NewGameRequester newGameRequester;
	private boolean searching;
	
	public PlayPanel(Dimension d, User user){
		this.user = user;
		Dimension buttonSize = new Dimension(100,75);
		dbCon = DatabaseConnection.getInstance();
		gameCon = GameConnection.getInstance();
		alstPlaymodePanels = new ArrayList<>();
		searching = false;
		jlblMessage = new JLabel();
		jlblMessage.setPreferredSize(new Dimension(200, 25));
		add(jlblMessage);
		
		playmodePanel = new JPanel();
		playmodePanel.setLayout(new BoxLayout(playmodePanel, BoxLayout.Y_AXIS));
		add(playmodePanel);
		
		jbttnJoinGameQueues = new JGCButton("Join Queue");
		jbttnJoinGameQueues.setPreferredSize(buttonSize);
		jbttnJoinGameQueues.addActionListener(new ALJoinQueues());
		add(jbttnJoinGameQueues);
		
		jbttnLeaveGameQueues = new JGCButton("Leave Queue");
		jbttnLeaveGameQueues.setEnabled(false);
		jbttnLeaveGameQueues.setPreferredSize(buttonSize);
		jbttnLeaveGameQueues.addActionListener(new ALLeaveQueues());
		add(jbttnLeaveGameQueues);
		setPreferredSize(d);
	}
	
	public void loadPlaymodes(){
		try {
			Playmode[] playmodes = dbCon.getPlaymodes();
			alstPlaymodePanels.clear();
			playmodePanel.removeAll();
			if(playmodes != null){
				for (Playmode playmode : playmodes) {
					PlaymodePanel playmodepanel = new PlaymodePanel(playmode);
					alstPlaymodePanels.add(playmodepanel);
					playmodePanel.add(playmodepanel);
				}
				setPlaymodeSelection(!searching);
			}
		} catch (SocketTimeoutException | SocketException e ) {
			jlblMessage.setText("Connection timed out");
		} catch (UnknownHostException e) {
			jlblMessage.setText("Server not found");
		} catch (ClassNotFoundException e) {
			jlblMessage.setText("Internal Error: Missing Class");
		} catch (IOException e) {
			e.printStackTrace();
			jlblMessage.setText("Internal Error: IO");
		}
	}
	
	private void setPlaymodeSelection(boolean b){
		for (int i = 0; i < alstPlaymodePanels.size(); i++) {
				alstPlaymodePanels.get(i).setEnabled(b);;
		}
	}
	
	public class ALJoinQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			jlblMessage.setText("");
			ArrayList<Playmode> alstPlaymodes = new ArrayList<>();
			if(alstPlaymodePanels.size() > 0){
				for (int i = 0; i < alstPlaymodePanels.size(); i++) {
					if(alstPlaymodePanels.get(i).isSelected()){
						alstPlaymodes.add(alstPlaymodePanels.get(i).getPlaymode());
					}
				}
				if(alstPlaymodes.size() <= 0){
					jlblMessage.setText("Please select at least one gamemode!");
				}else{
					try {
						gameCon.joinQueues(user, alstPlaymodes.toArray(new Playmode[0]));
						jbttnJoinGameQueues.setEnabled(false);
						jbttnLeaveGameQueues.setEnabled(true);
						setPlaymodeSelection(false);
						searching = true;
						newGameRequester = new NewGameRequester();
						newGameRequester.start();
					} catch (SocketTimeoutException e) {
						jlblMessage.setText("Connection timed out");
					} catch (UnknownHostException e) {
						jlblMessage.setText("Server not found");
					} catch (IOException e) {
						jlblMessage.setText("Internal Error: IO");
					}
				}
			}
		}
	}
	
	public class ALLeaveQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			jlblMessage.setText("");
			try {
				gameCon.leaveQueues(user);
				jbttnJoinGameQueues.setEnabled(true);
				jbttnLeaveGameQueues.setEnabled(false);
				setPlaymodeSelection(true);
				searching = false;
				newGameRequester.interrupt();
			} catch (SocketTimeoutException e) {
				jlblMessage.setText("Connection timed out");
			} catch (UnknownHostException e) {
				jlblMessage.setText("Server not found");
			} catch (IOException e) {
				jlblMessage.setText("Internal Error: IO");
			}
		}
	}
	
	private class NewGameRequester extends Thread{
		
		public void run(){
			boolean gameFound = false;
			boolean searching = true;
			while(!gameFound && searching){
				try {
					jlblMessage.setText("Searching for a game.");
					int gameID = gameCon.getGameinformation(user);
					if(gameID != -1){
						gameFound = true;
						jlblMessage.setText("Game found");
						jbttnJoinGameQueues.setEnabled(false);
						jbttnLeaveGameQueues.setEnabled(false);
						new PlayFrame(gameID, user);
					}else{
						try {
							sleep(333);
							jlblMessage.setText("Searching for a game..");
							sleep(333);
							jlblMessage.setText("Searching for a game...");
							sleep(333);
						} catch (InterruptedException e) {
							searching = false;
							jlblMessage.setText("");
						} catch (Exception e) {
							searching = false;
							jlblMessage.setText("");
						}
					}
				} catch (ClassNotFoundException e){
					searching = false;
					jlblMessage.setText("Internal Error: Class not found");
				} catch (SocketTimeoutException e) {
					searching = false;
					jlblMessage.setText("Connection timed out");
				} catch (UnknownHostException e) {
					searching = false;
					jlblMessage.setText("Server not found");
				} catch (IOException e) {
					searching = false;
					jlblMessage.setText("Internal Error: IO");
				}
			}
		}
	}
}