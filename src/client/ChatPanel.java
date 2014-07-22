package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	
	public ChatPanel(User user){
		setLayout(null);
		Dimension channelListSize = new Dimension(190, 400);
		dbCon = DatabaseConnection.getInstance();
		
		jtxtChannelName = new JTextField("Deu-1", 20);
		jtxtChannelName.setSize(100, 25);
		jtxtChannelName.setLocation(10, 10);
		this.add(jtxtChannelName);
		
		JButton jbttnJoin = new JButton("Join");
		jbttnJoin.setSize(70, 25);
		jbttnJoin.setLocation(120, 10);
		jbttnJoin.addActionListener(new ALJoinChannel());
		this.add(jbttnJoin);
		
		jlstChannels = new JList<>();
		jlstChannels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstChannels.setPreferredSize(new Dimension(channelListSize.width, channelListSize.height - 5));
		jlstChannels.addMouseListener(new MAChannelList());
		loadChannel();
		JScrollPane jscrllChannels = new JScrollPane(jlstChannels,
										            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrllChannels.setSize(channelListSize);
		jscrllChannels.setLocation(10, 45);
		this.add(jscrllChannels);
		this.setSize(200, 450);
	}
	
	private class MAChannelList implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() >= 2){
				int index = jlstChannels.locationToIndex(e.getPoint());
				new ChatFrame(jlstChannels.getModel().getElementAt(index));
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
		}
	}
	
	private class ALJoinChannel implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			
		}
	}
}