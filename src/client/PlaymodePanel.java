package client;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server_client.Playmode;

public class PlaymodePanel extends JPanel{
	
	private JCheckBox jchckSelected;
	private Playmode playmode;
	
	public PlaymodePanel(Playmode playmode){
		super();
		this.playmode = playmode;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		jchckSelected = new JCheckBox();
		add(jchckSelected);
		
		add(new JLabel(playmode.getTitel() + " • "));
		add(Box.createVerticalStrut(10));
		add(new JLabel(playmode.getDescText()));
		add(Box.createRigidArea(new Dimension(300,0)));
	}
	
	public boolean isSelected(){
		return jchckSelected.isSelected();
	}
	
	public Playmode getPlaymode(){
		return this.playmode;
	}

}
