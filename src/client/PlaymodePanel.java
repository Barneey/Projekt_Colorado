package client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		addMouseListener(new MLPlaypanel());
	}
	
	public boolean isSelected(){
		return jchckSelected.isSelected();
	}
	
	public Playmode getPlaymode(){
		return this.playmode;
	}
	
	public void setEnabled(boolean b){
		jchckSelected.setEnabled(b);
	}
	
	public class MLPlaypanel implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(jchckSelected.isEnabled()){
				jchckSelected.setSelected(!isSelected());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}