package server_client.matches;

import java.awt.Graphics;

import javax.swing.JPanel;


public abstract class Match extends JPanel{
	
	public static final int FFA = 0;
	public static final int TEAM = 1;
	public static final int UNDERDOG = 2;
	public static final int TEST = 3;
	private int matchType;
	
	public Match(int matchType){
		this.matchType = matchType;
	}
	
	public abstract void paint(Graphics g);

	public int getMatchType(){
		return this.matchType;
	}
}