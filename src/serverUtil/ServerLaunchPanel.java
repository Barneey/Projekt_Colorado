package serverUtil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server.Server;

public class ServerLaunchPanel extends JPanel{
	

	private String name;
	private Server server;
	private JButton jbttnStart;
	private JButton jbttnStop;
	private ServerStatusPanel statusLamp;
	
	public ServerLaunchPanel(String name, Server server){
		super();
		this.name = name;
		this.server = server;
		
		JLabel jlbName = new JLabel(this.name);
		jlbName.setPreferredSize(new Dimension(80, 25));
		add(jlbName);
		
		jbttnStart = new JButton("Start");
		jbttnStart.addActionListener(new ALStart());
		add(jbttnStart);
		
		jbttnStop = new JButton("Stop");
		jbttnStop.addActionListener(new ALStop());
		jbttnStop.setEnabled(false);
		add(jbttnStop);
		
		statusLamp = new ServerStatusPanel();
		add(statusLamp);
	}
	
	private class ALStart implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if(server.isAlive()){
				server.continueMe();
			}else{
				server.start();
			}
			statusLamp.setStatus(true);
			jbttnStart.setEnabled(false);
			jbttnStop.setEnabled(true);
		}
	}
	
	private class ALStop implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			server.stopMe();
			statusLamp.setStatus(false);
			jbttnStart.setEnabled(true);
			jbttnStop.setEnabled(false);
		}
	}
}