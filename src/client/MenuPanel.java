package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import server_client.User;

public class MenuPanel extends JPanel{
	
	private ContentPanel contentPanel;
	private PlayPanel playPanel;
	private ProfilePanel profilePanel;
	private RankingPanel rankingPanel;
	private ShopPanel shopPanel;
	private SettingsPanel settingsPanel;
	private User user;
	
	public MenuPanel(ContentPanel contentPanel, User user){
		this.user = user;
		this.contentPanel = contentPanel;
		Dimension buttonSize = new Dimension(150,70);

		JGCButton jbttnPlay = new JGCButton("Play");
		jbttnPlay.setPreferredSize(buttonSize);
		jbttnPlay.addActionListener(new ALPlay());
		add(jbttnPlay);
		
		JGCButton jbttnProfile = new JGCButton("Profile");
		jbttnProfile.setEnabled(false);
		jbttnProfile.setPreferredSize(buttonSize);
		jbttnProfile.addActionListener(new ALProfile());
		add(jbttnProfile);
		
		JGCButton jbttnRanking = new JGCButton("Ranking");
		jbttnRanking.setPreferredSize(buttonSize);
		jbttnRanking.addActionListener(new ALRanking());
		add(jbttnRanking);
		
		JGCButton jbttnShop = new JGCButton("Shop");
		jbttnShop.setEnabled(false);
		jbttnShop.setPreferredSize(buttonSize);
		jbttnShop.addActionListener(new ALShop());
		add(jbttnShop);
		
		JGCButton jbttnSettings = new JGCButton("Settings");
		jbttnSettings.setEnabled(false);
		jbttnSettings.setPreferredSize(buttonSize);
		jbttnSettings.addActionListener(new ALSettings());
		add(jbttnSettings);
	}
	
	private class ALPlay implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			if(playPanel == null){
				playPanel = new PlayPanel(contentPanel.getPreferredSize(), user);
			}
			playPanel.loadPlaymodes();
			contentPanel.setContent(playPanel);
		}
	}

	private class ALProfile implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			// TODO write Method
		}
	}

	private class ALRanking implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			if(rankingPanel == null){
				rankingPanel = new RankingPanel(contentPanel.getPreferredSize());
			}
			rankingPanel.loadRanking();
			contentPanel.setContent(rankingPanel);
		}
	}

	private class ALShop implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			// TODO write Method
		}
	}

	private class ALSettings implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			// TODO write Method
		}
	}
}