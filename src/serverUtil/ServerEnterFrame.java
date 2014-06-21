package serverUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.ServerDataBaseManager;

public class ServerEnterFrame extends JFrame{
	
	private JTextArea jtaEnter;
	
	public ServerEnterFrame(int type){
		super();
		String sType;
		switch (type) {
		case ServerDataBaseManager.QUERY:
			sType = "Query";
			break;
		case ServerDataBaseManager.UPDATE:
			sType = "Update";
			break;
		default:
			sType = "Default";
			break;
		}
		setTitle("Enter " + sType);
		setLayout(new BorderLayout());
		
		
		jtaEnter = new JTextArea(20, 40);
		JScrollPane scrollPane = new JScrollPane(jtaEnter); 
//		jtaEnter.setEditable(false);
		add(scrollPane, BorderLayout.CENTER);
		
		JButton jbttnEnterQuery = new JButton("Enter " + sType);
		jbttnEnterQuery.addActionListener(new ALEnter(type));
		add(jbttnEnterQuery, BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public class ALEnter implements ActionListener{
		private int type;
		
		public ALEnter(int type){
			this.type = type;
		}
		
		public void actionPerformed(ActionEvent ae){
			ServerDataBaseManager sDBM = new ServerDataBaseManager();
			sDBM.enter(jtaEnter.getText(),type);
		}
	}

}
