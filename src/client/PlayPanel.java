package client;

import java.awt.Dimension;

import javax.swing.JPanel;

public class PlayPanel extends JPanel{
	
	private DatabaseConnection dbCon;
	
	public PlayPanel(Dimension d){
		dbCon = DatabaseConnection.getInstance();
	}
	
	public void loadPlaymodes(){
		
	}
	
}