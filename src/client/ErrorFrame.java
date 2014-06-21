package client;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorFrame extends JFrame{

	public ErrorFrame(String message){
		super("Error");
		
		JLabel jlbMessage = new JLabel("  " + message);
		add(jlbMessage);
		
		pack();
		setSize(this.getWidth() + 20, this.getHeight() + 50);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
