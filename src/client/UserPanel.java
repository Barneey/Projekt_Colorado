package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import server_client.User;

public class UserPanel extends JPanel{
	
	private BufferedImage vCoinsImage;
	private BufferedImage rCoinsImage;
	private JLabel jlbMainInformation;
	private JProgressBar jprgLevelProgress;
	private JLabel jlbRealMoney;
	private JLabel jlbInGameMoney;
	private JLabel jlbLastLogin;
	
	public UserPanel(User user){
		setLayout(null);
		
		GUIImageLoader imgLdr = new GUIImageLoader();
		rCoinsImage = imgLdr.loadBufferedImage(GUIImageLoader.RCOINS);
		vCoinsImage = imgLdr.loadBufferedImage(GUIImageLoader.VCOINS);
		
		jlbMainInformation = new JLabel("[" + user.getLevel() + "] " + (user.getNick() == null ? "???" : user.getNick()));
		jlbMainInformation.setFont(JGSystem.FONT_SMALL);
		jlbMainInformation.setSize(100, 30);
		jlbMainInformation.setLocation(10, 10);
		this.add(jlbMainInformation);
		
		jprgLevelProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, user.getLevelUpExp());
		jprgLevelProgress.setValue(user.getCurrentExp());
		jprgLevelProgress.setSize(100, 20);
		jprgLevelProgress.setLocation(10, 45);
		this.add(jprgLevelProgress);
		
		jlbRealMoney = new JLabel("" + user.getrCoins(), SwingConstants.RIGHT);
		jlbRealMoney.setFont(JGSystem.FONT_SMALL);
		jlbRealMoney.setSize(60, 30);
		jlbRealMoney.setLocation(140, 10);
		this.add(jlbRealMoney);
		
		jlbInGameMoney = new JLabel("" + user.getvCoins(), SwingConstants.RIGHT);
		jlbInGameMoney.setFont(JGSystem.FONT_SMALL);
		jlbInGameMoney.setSize(60, 30);
		jlbInGameMoney.setLocation(140, 40);
		this.add(jlbInGameMoney);
		
		JSeparator jsprLine = new JSeparator(SwingConstants.HORIZONTAL);
		jsprLine.setSize(180, 10);
		jsprLine.setLocation(15, 75);
		this.add(jsprLine);
		
		DateFormat dtfLastLogin = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String lastLoginDate = dtfLastLogin.format(user.getLastLogin());
		jlbLastLogin = new JLabel("Last Login: " + lastLoginDate, SwingConstants.CENTER);
		jlbLastLogin.setFont(JGSystem.FONT_SMALL);
		jlbLastLogin.setSize(200, 30);
		jlbLastLogin.setLocation(0, 80);
		this.add(jlbLastLogin);
		
	}
	
	public void updatePanel(User user){
		jlbMainInformation.setText("[" + user.getLevel() + "] " + (user.getNick() == null ? "???" : user.getNick()));
		jprgLevelProgress.setMaximum(user.getLevelUpExp());
		jprgLevelProgress.setValue(user.getCurrentExp());
		jlbRealMoney.setText("" + user.getrCoins());
		jlbInGameMoney.setText("" + user.getvCoins());
	}
	
   @Override
	protected void paintComponent(Graphics g) {
		g.drawImage(vCoinsImage, 120, 50, null);  
		g.drawImage(rCoinsImage, 120, 20, null);
	}
}