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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server_client.Playmode;
import server_client.User;

public class PlayPanel extends JPanel{
	
	private DatabaseConnection dbCon;
	private GameConnection gameCon;
	private ArrayList<PlaymodePanel> alstPlaymodePanels;
	private JPanel playmodePanel;
	private JButton jbttnJoinGameQueues;
	private JButton jbttnLeaveGameQueues;
	private User user;
	private JLabel jlblMessage;
	
	public PlayPanel(Dimension d, User user){
		this.user = user;
		Dimension buttonSize = new Dimension(100,75);
		dbCon = DatabaseConnection.getInstance();
		gameCon = GameConnection.getInstance();
		alstPlaymodePanels = new ArrayList<>();
		jlblMessage = new JLabel();
		jlblMessage.setPreferredSize(new Dimension(200, 25));
		add(jlblMessage);
		
		playmodePanel = new JPanel();
		playmodePanel.setLayout(new BoxLayout(playmodePanel, BoxLayout.Y_AXIS));
//		playmodePanel.setPreferredSize(new Dimension(1, 1));
		add(playmodePanel);
		
		jbttnJoinGameQueues = new JButton("Join Queue");
		jbttnJoinGameQueues.setPreferredSize(buttonSize);
		jbttnJoinGameQueues.addActionListener(new ALJoinQueues());
		add(jbttnJoinGameQueues);
		
		jbttnLeaveGameQueues = new JButton("Leave Queue");
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
	
	public class ALJoinQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			ArrayList<Playmode> alstPlaymodes = new ArrayList<>();
			if(alstPlaymodePanels.size() > 0){
				for (int i = 0; i < alstPlaymodePanels.size(); i++) {
					alstPlaymodes.add(alstPlaymodePanels.get(i).getPlaymode());
				}
				try {
					gameCon.joinQueues(user, alstPlaymodes.toArray(new Playmode[0]));
					jbttnJoinGameQueues.setEnabled(false);
					jbttnLeaveGameQueues.setEnabled(true);
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
	
	public class ALLeaveQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			try {
				gameCon.leaveQueues(user);
				jbttnJoinGameQueues.setEnabled(true);
				jbttnLeaveGameQueues.setEnabled(false);
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