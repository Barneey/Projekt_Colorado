package server_client.matches;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;

import server_client.Playmode;
import server_client.Team;
import server_client.User;

public abstract class Match extends JPanel implements Runnable{
	
	public static final int FFA = 0;
	public static final int TEAM = 1;
	public static final int UNDERDOG = 2;
	public static final int TEST = 3;
	private int matchType;
	protected transient Image offscreen;
	protected transient Graphics offscreenGraphics;
	protected HashMap<String, GameObject> gameObjects;
	protected HashMap<Integer, Boolean> userIDtoMatchLoaded;
	protected Playmode playmode;
	
	public Match(int matchType, Playmode playmode){
		this.matchType = matchType;
		this.playmode = playmode;
		this.userIDtoMatchLoaded = new HashMap<>();
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				userIDtoMatchLoaded.put(user.getId(), false);
			}
		}
		super.setSize(720, 405);
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		gameObjects = new HashMap<>();
	}
	
	public abstract void paint(Graphics g);
	public abstract void loadImages();
	protected abstract void showGameInfo();
	
	protected void drawGameObject(GameObject gameObject, Image image){
		if(gameObject != null && image != null){
			offscreenGraphics.drawImage(image, gameObject.getX(), gameObject.getY(), this);
		}
	}
	
	protected void drawString(String string, Color c, Font font, Point location){
		Color cCopy = offscreenGraphics.getColor();
		Font fontCopy = offscreenGraphics.getFont();
		if(c != null){
			offscreenGraphics.setColor(c);
		}
		if(font != null){
			offscreenGraphics.setFont(font);
		}
		offscreenGraphics.drawString(string, location.x, location.y);
		if(c != null){
			offscreenGraphics.setColor(cCopy);
		}
		if(font != null){
			offscreenGraphics.setFont(fontCopy);
		}
	}
	
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
	
	public Team[] getTeams(){
		return playmode.getTeams();
	}
	
	public void setMatchLoaded(int userID, boolean matchLoaded){
		userIDtoMatchLoaded.put(userID, matchLoaded);
	}
	
	public boolean isLoaded(){
		Iterator<Entry<Integer, Boolean>> it = userIDtoMatchLoaded.entrySet().iterator();
		while(it.hasNext()){
			if(!it.next().getValue()){
				return false;
			}
			it.remove();
		}
		return true;
	}
}