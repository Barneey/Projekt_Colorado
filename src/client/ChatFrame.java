package client;	

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server_client.ChatChannel;
import server_client.ChatMessage;
import server_client.User;

public class ChatFrame extends JFrame{
	
	private JTextArea jtxtrMessages;
	private JTextField jtxtfldEnterMessage;
	private JList<User> jlstUsers;
	private DatabaseConnection dbCon;
	private Date joinDate;
	private ChatChannel channel;
	private User user;
	private Thread updater;
	private ArrayList<ChatMessage> chatMessages;
	private JScrollPane jscrllMessages;
	
	public ChatFrame(ChatChannel channel, User user){
		construct(channel, user, true);
	}
	
	public ChatFrame(ChatChannel channel, User user, boolean channelExists){
		construct(channel, user, channelExists);
	}
	
	private void construct(ChatChannel channel, User user, boolean channelExists){
		setTitle(channel.getChannelName());
		this.chatMessages = new ArrayList<>();
		this.updater = new Updater();
		this.user = user;
		this.channel = channel;
		this.dbCon = DatabaseConnection.getInstance();
		this.joinDate = dbCon.getServerDate(DatabaseConnection.CHAT_PORT);
		joinChannel(channelExists);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(10, 0)));
		
			JPanel jpnlLeftPanelOverall = new JPanel();
			jpnlLeftPanelOverall.setLayout(new BoxLayout(jpnlLeftPanelOverall, BoxLayout.Y_AXIS));
			jpnlLeftPanelOverall.add(Box.createRigidArea(new Dimension(0, 10)));
			
				JPanel jpnlLeftPanel = new JPanel();
				jpnlLeftPanel.setLayout(new BorderLayout());
				jpnlLeftPanel.setPreferredSize(new Dimension(300, 460));
						
					jtxtrMessages = new JTextArea();
					jtxtrMessages.setFont(JGSystem.FONT_CHAT);
					jtxtrMessages.setLineWrap(true);
					jtxtrMessages.setEditable(false);
					loadMessages();

					jscrllMessages = new JScrollPane(jtxtrMessages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
																JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					jpnlLeftPanel.add(jscrllMessages, BorderLayout.CENTER);
					
					jtxtfldEnterMessage = new JTextField();
					jtxtfldEnterMessage.setPreferredSize(new Dimension(300, 25));
					jtxtfldEnterMessage.setDocument(new LengthRestrictedDocument(100));
					jtxtfldEnterMessage.addActionListener(new ALAddMessage());
					jpnlLeftPanel.add(jtxtfldEnterMessage, BorderLayout.SOUTH);
					
			jpnlLeftPanelOverall.add(jpnlLeftPanel);
			jpnlLeftPanelOverall.add(Box.createRigidArea(new Dimension(0,10)));
		add(jpnlLeftPanelOverall);
			
			JPanel jpnlRightPanel = new JPanel();
			jpnlRightPanel.setLayout(new BoxLayout(jpnlRightPanel, BoxLayout.Y_AXIS));
			jpnlRightPanel.setPreferredSize(new Dimension(150, 450));
			jpnlRightPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
					jlstUsers = new JList<>();
					jlstUsers.setPreferredSize(new Dimension(150, 450));
					loadUsers();
				JScrollPane jscrllChannels = new JScrollPane(jlstUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
															JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			jpnlRightPanel.add(jscrllChannels);
			jpnlRightPanel.add(Box.createRigidArea(new Dimension(0,10)));
		add(jpnlRightPanel);
		add(Box.createRigidArea(new Dimension(10, 0)));

		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WLChatFrame());
		setLocationRelativeTo(null);
		setVisible(true);

		startUpdatingChannel();
	}
	
	private void joinChannel(boolean channelExists){
		dbCon.joinChannel(channel, user, channelExists);
	}
	
	private void loadUsers(){
		User[] alstUsers = dbCon.loadUsers(channel);
		if(alstUsers != null){
			Arrays.sort(alstUsers);
			jlstUsers.setListData(alstUsers);
		}
	}
	
	private void loadMessages(){
		ArrayList<ChatMessage> newMessages = new ArrayList<>(Arrays.asList(dbCon.loadMessages(channel, joinDate)));
		if(chatMessages.size() > 0){
			newMessages.removeAll(chatMessages);
		}
		boolean showTimestamp = UserSettingsClient.getInstance().isShowTimestamp();
		if(newMessages.size() > 0){
			for (ChatMessage chatMessage : newMessages) {
				chatMessages.add(chatMessage);
				jtxtrMessages.append(chatMessage.toString(showTimestamp));
				jtxtrMessages.append("\n");
			}
		}
		JScrollBar vertical = jscrllMessages.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private class Updater extends Thread{
		@Override
		public void run() {
			while(true){
				try {
					sleep(100);
					loadMessages();
					loadUsers();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void startUpdatingChannel(){
		updater.start();
	}
	
//	private void stopUpdatingChannel(){
//		updater.interrupt();
//	}
	
	private void leaveChannel(){
		dbCon.leaveChannel(channel, user);
	}
	
	private class ALAddMessage implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			ChatMessage chatMessage = new ChatMessage(user.getNick(), user.getId(), channel.getChannelID(), jtxtfldEnterMessage.getText());
			dbCon.addMessage(chatMessage);
			jtxtfldEnterMessage.setText("");
			loadMessages();
			loadUsers();
		}
	}

	private class WLChatFrame implements WindowListener{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			leaveChannel();
			dispose();
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}