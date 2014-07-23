package client;	

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
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
	private boolean showTimestamp;
	private ChatChannel channel;
	private User user;
	
	public ChatFrame(ChatChannel channel, User user){
		construct(channel, user, true);
	}
	
	public ChatFrame(ChatChannel channel, User user, boolean channelExists){
		construct(channel, user, channelExists);
	}
	
	private void construct(ChatChannel channel, User user, boolean channelExists){
		setTitle(channel.getChannelName());
		
		this.user = user;
		this.channel = channel;
		this.dbCon = DatabaseConnection.getInstance();
		this.joinDate = dbCon.getServerDate(DatabaseConnection.CHAT_PORT);
		joinChannel(channel, user);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(10, 0)));
		
			JPanel jpnlLeftPanelOverall = new JPanel();
			jpnlLeftPanelOverall.setLayout(new BoxLayout(jpnlLeftPanelOverall, BoxLayout.Y_AXIS));
			jpnlLeftPanelOverall.add(Box.createRigidArea(new Dimension(0, 10)));
			
				JPanel jpnlLeftPanel = new JPanel();
				jpnlLeftPanel.setLayout(new BorderLayout());
				jpnlLeftPanel.setPreferredSize(new Dimension(300, 460));
						
					jtxtrMessages = new JTextArea();
					jtxtrMessages.setPreferredSize(new Dimension(300, 425));
					jtxtrMessages.setLineWrap(true);
					jtxtrMessages.setEditable(false);
					loadMessages(channel, joinDate);

					JScrollPane jscrllMessages = new JScrollPane(jtxtrMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
																JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

					jpnlLeftPanel.add(jscrllMessages, BorderLayout.CENTER);
					
					jtxtfldEnterMessage = new JTextField();
					jtxtfldEnterMessage.setPreferredSize(new Dimension(300, 25));
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
					loadUsers(channel);
				JScrollPane jscrllChannels = new JScrollPane(jlstUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
															JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			jpnlRightPanel.add(jscrllChannels);
			jpnlRightPanel.add(Box.createRigidArea(new Dimension(0,10)));
		add(jpnlRightPanel);
		add(Box.createRigidArea(new Dimension(10, 0)));

		pack();
		setLocationRelativeTo(null);
		setVisible(true);		
	}
	
	private void joinChannel(ChatChannel channel, User user){
		dbCon.joinChannel(channel, user);
	}
	
	private void loadUsers(ChatChannel channel){
		User[] alstUsers = dbCon.loadUsers(channel);
		jlstUsers.setListData(alstUsers);
	}
	
	private void loadMessages(ChatChannel channel, Date joinDate){
		ChatMessage[] alstMessages = dbCon.loadMessages(channel, joinDate);
		boolean showTimestamp = UserSettingsClient.getInstance().isShowTimestamp();
		for (ChatMessage chatMessage : alstMessages) {
			jtxtrMessages.append(chatMessage.toString(showTimestamp));
			jtxtrMessages.append("\n");
		}
	}
	
	private void addMessage(ChatChannel channel, User user){
		ChatMessage chatMessage = new ChatMessage(user.getNick(), channel.getChannelID(), jtxtfldEnterMessage.getText());
		dbCon.addMessage(chatMessage);
	}
	
	private class ALAddMessage implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			addMessage(channel, user);
		}
	}
}