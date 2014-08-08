package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class MainFrame extends JFrame{
	
	
	private final Dimension DEFAULT_MAIN_FRAMESIZE; 
	private final Dimension PREFERRED_PANEL_SIZE;
	private User user;

	public MainFrame(User user) {
		super(JGSystem.NAME);
		this.user = user;
		this.DEFAULT_MAIN_FRAMESIZE = new Dimension(1024, 768);
		this.PREFERRED_PANEL_SIZE = new Dimension(1024-5-25, 768-25-25);
		this.setSize(this.DEFAULT_MAIN_FRAMESIZE);
		this.setMaximumSize(this.DEFAULT_MAIN_FRAMESIZE);
		this.setLayout(new BorderLayout());
		
				JPanel jpnlMainPanel = new JPanel();
				jpnlMainPanel.setPreferredSize(this.PREFERRED_PANEL_SIZE);
				jpnlMainPanel.setLayout(new BorderLayout());
					JPanelMainFrameHeader jpnlHeader = new JPanelMainFrameHeader();
					jpnlHeader.setPreferredSize(new Dimension(this.DEFAULT_MAIN_FRAMESIZE.width, 110));
				jpnlMainPanel.add(jpnlHeader, BorderLayout.NORTH);
				
					JPanel jpnlWest = new JPanel();
					jpnlWest.setLayout(new BoxLayout(jpnlWest, BoxLayout.Y_AXIS));
						UserPanel userPanel = new UserPanel(user);
						userPanel.setPreferredSize(new Dimension(200, 110));
					jpnlWest.add(userPanel);
					jpnlWest.add(Box.createVerticalStrut(30));
						ChatPanel chatPanel = new ChatPanel(user);
						chatPanel.setPreferredSize(new Dimension(200, 500));
					jpnlWest.add(chatPanel);
				jpnlMainPanel.add(jpnlWest, BorderLayout.WEST);
					JPanel jpnlCenter = new JPanel();
					jpnlCenter.setLayout(new BorderLayout());
						ContentPanel contentPanel = new ContentPanel();
						contentPanel.setPreferredSize(new Dimension(770, 485));
					jpnlCenter.add(contentPanel, BorderLayout.CENTER);
						MenuPanel menuPanel = new MenuPanel(contentPanel, user);
						menuPanel.setPreferredSize(new Dimension(this.DEFAULT_MAIN_FRAMESIZE.width - 200, 110));
					jpnlCenter.add(menuPanel, BorderLayout.NORTH);
				jpnlMainPanel.add(jpnlCenter, BorderLayout.CENTER);
				
			JScrollPane jscrllMainScrollPane = new JScrollPane(jpnlMainPanel);
				
		this.add(jscrllMainScrollPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WLMainFrame());
//		this.addMouseListener(new MLMainFrame());
//		this.addComponentListener(new CLMainFrame());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		if(!checkNickname(user)){
			requestNickname(user);
			userPanel.updatePanel(user);
			revalidate();
		}
	}
	
	private void requestNickname(User user){
		new RequestNicknameFrame(user);
	}
	
	private boolean checkNickname(User user){
		return user.getNick() != null;
	}
	
	@Override
    public void paint(Graphics g) {
        Dimension currentSize = getSize();
        Dimension maximumSize = getMaximumSize();

        if (currentSize.width > maximumSize.width || currentSize.height > maximumSize.height) {
            currentSize.width = Math.min(maximumSize.width, currentSize.width);
            currentSize.height = Math.min(maximumSize.height, currentSize.height);
            Point p = getLocation();
            p.setLocation(Math.max(p.x, 0),  Math.max(p.y, 0));
            setSize(currentSize);
            setLocation(p);
        }
        super.paint(g);
    }
	
	private class MLMainFrame implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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

		}
		
	}
	
	private class CLMainFrame implements ComponentListener{

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private class WLMainFrame implements WindowListener{

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
			Object[] object = {"Yes", "9 9 9"};
			int answer = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the JGame Collection?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, object, object[1]);
			if(answer == 0)
			{
				JGSystem.getInstance().exit(user);
			}	
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