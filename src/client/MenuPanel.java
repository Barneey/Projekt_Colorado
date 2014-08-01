package client;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	
	private ContentPanel contentPanel;
	
	public MenuPanel(ContentPanel contentPanel){
		this.contentPanel = contentPanel;
		
		Dimension buttonSize = new Dimension(150,70);
		JButton jbttnPlay = new JButton("Play");
		jbttnPlay.setPreferredSize(buttonSize);
		jbttnPlay.setEnabled(false);
		add(jbttnPlay);
		
		JButton jbttnProfile = new JButton("Profile");
		jbttnProfile.setEnabled(false);
		jbttnProfile.setPreferredSize(buttonSize);
		add(jbttnProfile);
		
		JButton jbttnRanking = new JButton("Ranking");
		jbttnRanking.setEnabled(false);
		jbttnRanking.setPreferredSize(buttonSize);
		add(jbttnRanking);
		
		JButton jbttnShop = new JButton("Shop");
		jbttnShop.setEnabled(false);
		jbttnShop.setPreferredSize(buttonSize);
		add(jbttnShop);
		
		JButton jbttnSettings = new JButton("Settings");
		jbttnSettings.setEnabled(false);
		jbttnSettings.setPreferredSize(buttonSize);
		add(jbttnSettings);
	}

}
