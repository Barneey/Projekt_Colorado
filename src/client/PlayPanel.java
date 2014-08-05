package client;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import server_client.Playmode;

public class PlayPanel extends JPanel{
	
	private DatabaseConnection dbCon;
	private ArrayList<PlaymodePanel> alstPlaymodePanels;
	private JPanel playmodePanel;
	
	public PlayPanel(Dimension d){
		dbCon = DatabaseConnection.getInstance();
		alstPlaymodePanels = new ArrayList<>();
		playmodePanel = new JPanel();
		playmodePanel.setLayout(new BoxLayout(playmodePanel, BoxLayout.Y_AXIS));
		add(playmodePanel);
		
		
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
	
}