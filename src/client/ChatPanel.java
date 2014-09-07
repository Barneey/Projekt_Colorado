package client;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import server_client.ChatChannel;
import server_client.User;

public class ChatPanel extends JPanel{
	
	private JTextField jtxtChannelName;
	private JList<ChatChannel> jlstChannels;
	private DatabaseConnection dbCon;
	private JGCButton jbttnJoin;
	private JButton jbttnRefresh;
	private User user;
	private ChannelListUpdater updater;
	private BufferedImage bffredImgRefreshButtonInactive;
	private Image ImgRefreshButtonActive;
	private boolean refreshButtonLabeled;
	private boolean refreshing;
	private GUIImageLoader imgLdr;
	
	public ChatPanel(User user){
		Dimension channelListSize = new Dimension(190, 450);
		this.dbCon = DatabaseConnection.getInstance();
		this.user = user;
		this.updater = new ChannelListUpdater();
		this.imgLdr = new GUIImageLoader();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel jpnlUpperPanel = new JPanel();
			jpnlUpperPanel.setLayout(new BoxLayout(jpnlUpperPanel, BoxLayout.X_AXIS));
			
			jtxtChannelName = new JTextField(20);
			jtxtChannelName.setDocument(new LengthRestrictedDocument(20));
			jtxtChannelName.setPreferredSize(new Dimension(100, 25));
			jtxtChannelName.setMaximumSize(jtxtChannelName.getPreferredSize());
			jtxtChannelName.addFocusListener(new FLChatChannel());
			jtxtChannelName.setText("Deu-1");
			jpnlUpperPanel.add(jtxtChannelName);
		
			jbttnJoin = new JGCButton("Join");
			jbttnJoin.setPreferredSize(new Dimension(50, 25));
			jbttnJoin.addActionListener(new ALJoinChannel());
			jpnlUpperPanel.add(jbttnJoin);
			
			jbttnRefresh = new JButton();
			bffredImgRefreshButtonInactive = imgLdr.loadBufferedImage(GUIImageLoader.REFRESH_BUTTON_INAKTIVE);
			refreshButtonLabeled = (bffredImgRefreshButtonInactive == null); 
			if(refreshButtonLabeled){
				jbttnRefresh.setText("Refresh");
				jbttnRefresh.setPreferredSize(new Dimension(20, 25));
			}else{
				jbttnRefresh.setIcon(new ImageIcon(bffredImgRefreshButtonInactive));
				jbttnRefresh.setPreferredSize(new Dimension(20, 25));
				ImgRefreshButtonActive = imgLdr.loadImage(GUIImageLoader.REFRESH_BUTTON_AKTIVE);
			}
			jbttnRefresh.addMouseListener(new MLAutoRefresh());
			jpnlUpperPanel.add(jbttnRefresh);
			this.refreshing = false;
		this.add(jpnlUpperPanel);
		
		add(Box.createVerticalStrut(10));

		jlstChannels = new JList<>();
		jlstChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstChannels.addMouseListener(new MAChannelList());
		loadChannel();
		JScrollPane jscrllChannels = new JScrollPane(jlstChannels,
										            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrllChannels.setPreferredSize(channelListSize);
		this.add(jscrllChannels);
		updater.setRunning(refreshing);
		updater.start();
	}

	private void loadChannel(){
		ChatChannel[] chatChannels = dbCon.getAllPublicChannels();
		if(chatChannels != null){
			jbttnJoin.setEnabled(true);
			jlstChannels.setEnabled(true);
			jtxtChannelName.setEnabled(true);
			int selectedIndex = jlstChannels.getSelectedIndex();
			jlstChannels.setListData(chatChannels);
			jlstChannels.setSelectedIndex(selectedIndex);
		}else{
			jbttnJoin.setEnabled(false);
			jlstChannels.setEnabled(false);
			jtxtChannelName.setEnabled(false);
		}
	}
	
	private class ChannelListUpdater extends Thread{
		
		private boolean running;
		
		@Override
		public void run(){
			running = true;
			while(true){
				if(running){
					try {
						sleep(500);
						loadChannel();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public void setRunning(boolean running){
			this.running = running;
		}
	}
	
	
	private class FLChatChannel implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			jtxtChannelName.selectAll();
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	
	private class MLAutoRefresh implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent me) {
			if(SwingUtilities.isLeftMouseButton(me)){
				loadChannel();
			}else if(SwingUtilities.isRightMouseButton(me)){
				refreshing = !refreshing;
				updater.setRunning(refreshing);
				if(refreshing){
					jbttnRefresh.setIcon(new ImageIcon(ImgRefreshButtonActive));
				}else{
					jbttnRefresh.setIcon(new ImageIcon(bffredImgRefreshButtonInactive));
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class MAChannelList implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			jtxtChannelName.setText(jlstChannels.getModel().getElementAt(jlstChannels.locationToIndex(e.getPoint())).toString());
			if(e.getClickCount() >= 2){
				int index = jlstChannels.locationToIndex(e.getPoint());
				new ChatFrame(jlstChannels.getModel().getElementAt(index), user);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	private class ALJoinChannel implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			ArrayList<ChatChannel> channels = new ArrayList<>();
			for (int i = 0; i < jlstChannels.getModel().getSize(); i++) {
				channels.add(jlstChannels.getModel().getElementAt(i));
			}
			boolean channelExists = false;
			ChatChannel channel = new ChatChannel(-1, "public", jtxtChannelName.getText());
			for (ChatChannel chatChannel : channels) {
				if(!channelExists && chatChannel.getChannelName().equals(channel.getChannelName())){
					channelExists = true;
					channel = chatChannel;
				}
			}
			new ChatFrame(channel, user, channelExists);
		}
	}
}