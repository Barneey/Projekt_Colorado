package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorFrame extends JDialog{

	public ErrorFrame(String message){
		super();
		
		setTitle("Error");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		add(Box.createRigidArea(new Dimension(0,25)));
		
		JLabel jlbMessage = new JLabel("  " + message);
		jlbMessage.setAlignmentX(CENTER_ALIGNMENT);
		add(jlbMessage);
		
		add(Box.createRigidArea(new Dimension(0,25)));
		
		JButton jbttnOkay = new JButton("Okay");
		jbttnOkay.setPreferredSize(new Dimension(50, 25));
		jbttnOkay.addActionListener(new ALOkay());
		jbttnOkay.setAlignmentX(CENTER_ALIGNMENT);
		add(jbttnOkay);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}
	
	private class ALOkay implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			dispose();
		}
	}
}
