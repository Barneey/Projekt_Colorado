package server_client.matches;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JPanel;

public abstract class Match extends JPanel implements Runnable{
	
	public static final int FFA = 0;
	public static final int TEAM = 1;
	public static final int UNDERDOG = 2;
	public static final int TEST = 3;
	private int matchType;
	protected transient Image offscreen;
	protected transient Graphics offscreenGraphics;
	protected HashMap<String, GameObject> gameObjects;
	
	public Match(int matchType){
		this.matchType = matchType;
		super.setSize(720, 405);
//		offscreen = createImage(getWidth(), getHeight());
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		gameObjects = new HashMap<>();
	}
	
	public abstract void paint(Graphics g);
	public abstract void loadImages();
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void setSize(int width, int height){
		throw new IllegalArgumentException("The Size can not be changed");
	}
	
	public void setSize(Dimension d){
		throw new IllegalArgumentException("The Size can not be changed");
	}
	
	public int getMatchType(){
		return this.matchType;
	}
}