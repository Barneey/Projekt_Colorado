package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
	
	public PlayPanel(Dimension d, User user){
		this.user = user;
		Dimension buttonSize = new Dimension(100,75);
		dbCon = DatabaseConnection.getInstance();
		gameCon = GameConnection.getInstance();
		alstPlaymodePanels = new ArrayList<>();
		playmodePanel = new JPanel();
		playmodePanel.setLayout(new BoxLayout(playmodePanel, BoxLayout.Y_AXIS));
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
	}
	
	public class ALJoinQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			ArrayList<Playmode> alstPlaymodes = new ArrayList<>();
			if(alstPlaymodePanels.size() > 0){
				for (int i = 0; i < alstPlaymodePanels.size(); i++) {
					alstPlaymodes.add(alstPlaymodePanels.get(i).getPlaymode());
				}
				gameCon.joinQueues(user, alstPlaymodes.toArray(new Playmode[0]));
				jbttnJoinGameQueues.setEnabled(false);
				jbttnLeaveGameQueues.setEnabled(true);
			}
		}
	}
	
	public class ALLeaveQueues implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			gameCon.leaveQueues(user);
			jbttnJoinGameQueues.setEnabled(true);
			jbttnLeaveGameQueues.setEnabled(false);
		}
	}
}