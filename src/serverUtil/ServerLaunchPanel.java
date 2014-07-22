package serverUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server.ChatServer;
import server.LoginServer;

public class ServerLaunchPanel extends JPanel{
	
	private String name;
	private boolean running;
	private Thread thread;
	private JButton jbttnStart;
	private JButton jbttnStop;
	private ServerStatusPanel statusLamp;
	
	public ServerLaunchPanel(String name, Thread thread){
		super();
		this.name = name;
		this.thread = thread;
		this.running = false;
		
		JLabel jlbName = new JLabel(this.name);
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
			if(thread.isAlive()){
				if(thread.getClass() == LoginServer.class){
					((LoginServer)thread).continueMe();
				}else if(thread.getClass() == ChatServer.class){
					((ChatServer)thread).continueMe();
				}else if(true){
					// TODO Extend for more possible servers
				}
			}else{
				thread.start();
			}
			running = true;
			statusLamp.setStatus(running);
			jbttnStart.setEnabled(false);
			jbttnStop.setEnabled(true);
		}
	}
	
	private class ALStop implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if(thread.getClass() == LoginServer.class){
				((LoginServer)thread).stopMe();
			}else if(thread.getClass() == ChatServer.class){
				((ChatServer)thread).stopMe();
			}else if(true){
				// TODO Extend for more possible servers
			
			}
			running = false;
			statusLamp.setStatus(running);
			jbttnStart.setEnabled(true);
			jbttnStop.setEnabled(false);
		}
	}
	
	public boolean isRunning(){
		return this.running;
	}
}