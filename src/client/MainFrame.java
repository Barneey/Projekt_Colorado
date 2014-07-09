package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class MainFrame extends JFrame{
	
	
	private final Dimension DEFAULT_MAIN_FRAMESIZE; 
	private JPanelMainFrameHeader jpnlHeader;

	public MainFrame(User user) {
		super(JGSystem.NAME);
		this.DEFAULT_MAIN_FRAMESIZE = new Dimension(1024, 768);
		this.setSize(this.DEFAULT_MAIN_FRAMESIZE);
		this.setLayout(new BorderLayout());
		
		this.jpnlHeader = new JPanelMainFrameHeader();
		this.jpnlHeader.setSize(this.getWidth(), 100);
		this.add(jpnlHeader, BorderLayout.NORTH);
		
		JPanel jpnlMainContent = new JPanel();
		
		
		this.add(jpnlMainContent, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WLMainFrame());
		this.addComponentListener(new CLMainFrame());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
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
			jpnlHeader.setSize(getWidth(), 100);
			
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
				JGSystem.exit();
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
