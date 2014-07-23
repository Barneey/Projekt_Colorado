package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import server_client.ChatChannel;
import server_client.User;

public class ChatPanel extends JPanel{
	
	private JTextField jtxtChannelName;
	private JList<ChatChannel> jlstChannels;
	private DatabaseConnection dbCon;
	private JButton jbttnJoin;
	
	public ChatPanel(User user){
		setLayout(null);
		Dimension channelListSize = new Dimension(190, 400);
		dbCon = DatabaseConnection.getInstance();
		
		jtxtChannelName = new JTextField("Deu-1", 20);
		jtxtChannelName.setSize(100, 25);
		jtxtChannelName.setLocation(10, 10);
		jtxtChannelName.addFocusListener(new FLChatChannel());
		jtxtChannelName.setDocument(new LengthRestrictedDocument(20));
		this.add(jtxtChannelName);
		
		jbttnJoin = new JButton("Join");
		jbttnJoin.setSize(70, 25);
		jbttnJoin.setLocation(120, 10);
		jbttnJoin.addActionListener(new ALJoinChannel(user));
		this.add(jbttnJoin);
		
		jlstChannels = new JList<>();
		jlstChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstChannels.setPreferredSize(new Dimension(channelListSize.width, channelListSize.height - 5));
		jlstChannels.addMouseListener(new MAChannelList(user));
		loadChannel();
		JScrollPane jscrllChannels = new JScrollPane(jlstChannels,
										            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrllChannels.setSize(channelListSize);
		jscrllChannels.setLocation(10, 45);
		this.add(jscrllChannels);
		this.setSize(200, 450);
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
	
	private class MAChannelList implements MouseListener{

		private User user;
		
		public MAChannelList(User user){
			this.user = user;
		}
		
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
	
	private void loadChannel(){
		ChatChannel[] chatChannels = dbCon.getAllPublicChannels();
		if(chatChannels != null){
			jlstChannels.setListData(chatChannels);
		}else{
			jbttnJoin.setEnabled(false);
			jlstChannels.setEnabled(false);
			jtxtChannelName.setEnabled(false);
		}
	}
	
	private class ALJoinChannel implements ActionListener{
		
		private User user;
		
		public ALJoinChannel(User user){
			this.user = user;
		}
		
		public void actionPerformed(ActionEvent ae){
			ArrayList<ChatChannel> channels = new ArrayList<>();
			for (int i = 0; i < jlstChannels.getModel().getSize(); i++) {
				channels.add(jlstChannels.getModel().getElementAt(i));
			}
			boolean channelExists = false;
			ChatChannel channel = new ChatChannel(-1, "", jtxtChannelName.getText());
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