package serverUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import server.ServerDataBaseManager;

public class ServerDataBaseManagement extends JFrame{
	
	private Dimension dmnsButton;
	private JList<String> jlstLog;
	private ServerDataBaseManager sDBM;
	
	public static void main (String args[]){
		new ServerDataBaseManagement();
	}
	
	public ServerDataBaseManagement() {
		super("Server Data Base Management");
		setLayout(new BorderLayout());
		dmnsButton = new Dimension(100, 25);
		sDBM = new ServerDataBaseManager();
		
		// Menu
		JMenuBar jmb = new JMenuBar();
		jmb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			JMenu jmEnter = new JMenu("Enter");
			jmb.add(jmEnter);
				JMenuItem jmiEnterQuery = new JMenuItem("Query");
				jmiEnterQuery.addActionListener(new ALEnter(ServerDataBaseManager.QUERY));
				jmEnter.add(jmiEnterQuery);
				JMenuItem jmiEnterUpdate = new JMenuItem("Update");
				jmiEnterUpdate.addActionListener(new ALEnter(ServerDataBaseManager.UPDATE));
				jmEnter.add(jmiEnterUpdate);				
		add(jmb, BorderLayout.NORTH);
		// Center
		JPanel jplAll = new JPanel(new BorderLayout());
		jplAll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
			JPanel jplButtons = new JPanel();
				
				JButton jbttnInitializeDB = new JButton("Initialize");
				jbttnInitializeDB.addActionListener(new ALInitializeDB());
				jbttnInitializeDB.setPreferredSize(dmnsButton);
				jplButtons.add(jbttnInitializeDB);
				
				JButton jbttnDeleteDB = new JButton("Delete");
				jbttnDeleteDB.addActionListener(new ALDeleteDB());
				jbttnDeleteDB.setPreferredSize(dmnsButton);
				jplButtons.add(jbttnDeleteDB);
				
				jplAll.add(jplButtons, BorderLayout.NORTH);
				
			
			jlstLog = new JList<>();
			jlstLog.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			jlstLog.setPreferredSize(new Dimension(300, 250));
			jplAll.add(jlstLog, BorderLayout.CENTER);
		
		add(jplAll, BorderLayout.CENTER);	
			
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public class ALEnter implements ActionListener{
		private int type;
		
		public ALEnter(int type){
			this.type = type;
		}
		
		public void actionPerformed(ActionEvent ae){
			new ServerEnterFrame(type);
		}
	}
	
	public class ALInitializeDB implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			sDBM.initializeDataBase(jlstLog);
		}
	}
	
	public class ALDeleteDB implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			sDBM.deleteDataBase(jlstLog);
		}
	}
}