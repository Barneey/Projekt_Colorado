package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	
	private ContentPanel contentPanel;
	private PlayPanel playPanel;
	private ProfilePanel profilePanel;
	private RankingPanel rankingPanel;
	private ShopPanel shopPanel;
	private SettingsPanel settingsPanel;
	
	public MenuPanel(ContentPanel contentPanel){
		this.contentPanel = contentPanel;
		Dimension buttonSize = new Dimension(150,70);

		JButton jbttnPlay = new JButton("Play");
		jbttnPlay.setEnabled(false);
		jbttnPlay.setPreferredSize(buttonSize);
		jbttnPlay.addActionListener(new ALPlay());
		add(jbttnPlay);
		
		JButton jbttnProfile = new JButton("Profile");
		jbttnProfile.setEnabled(false);
		jbttnProfile.setPreferredSize(buttonSize);
		jbttnProfile.addActionListener(new ALProfile());
		add(jbttnProfile);
		
		JButton jbttnRanking = new JButton("Ranking");
		jbttnRanking.setPreferredSize(buttonSize);
		jbttnRanking.addActionListener(new ALRanking());
		add(jbttnRanking);
		
		JButton jbttnShop = new JButton("Shop");
		jbttnShop.setEnabled(false);
		jbttnShop.setPreferredSize(buttonSize);
		jbttnShop.addActionListener(new ALShop());
		add(jbttnShop);
		
		JButton jbttnSettings = new JButton("Settings");
		jbttnSettings.setEnabled(false);
		jbttnSettings.setPreferredSize(buttonSize);
		jbttnSettings.addActionListener(new ALSettings());
		add(jbttnSettings);
	}
	
	private class ALPlay implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			if(playPanel == null){
				playPanel = new PlayPanel(contentPanel.getPreferredSize());
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