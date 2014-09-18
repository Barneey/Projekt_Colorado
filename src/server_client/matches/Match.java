package server_client.matches;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;

import server_client.Playmode;
import server_client.Team;
import server_client.User;

public abstract class Match extends JPanel implements Runnable, KeyListener {
	
	public static final int FFA = 0;
	public static final int TEAM = 1;
	public static final int UNDERDOG = 2;
	public static final int TEST = 3;
	private int matchType;
	protected transient Image offscreen;
	protected transient Graphics offscreenGraphics;
	protected HashMap<String, GameObject> gameObjects;
	protected HashMap<Integer, Boolean> userIDtoMatchLoaded;
	protected HashMap<Integer, ArrayList<Integer>> userIDtoGameEvents;
	protected Playmode playmode;
	protected Integer[] leftUser;
	protected int userID;
	protected int gameID;
	
	public Match(int matchType, Playmode playmode){
		this.gameID = -1;
		this.matchType = matchType;
		this.playmode = playmode;
		this.userIDtoMatchLoaded = new HashMap<>();
		this.userIDtoGameEvents = new HashMap<>();
		this.addKeyListener(this);
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				userIDtoMatchLoaded.put(user.getID(), false);
				userIDtoGameEvents.put(user.getID(), new ArrayList<Integer>());
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
	protected abstract void updateGameObjects();
	protected abstract void updateGame();
	protected abstract void executeGameEvents(Integer[] events);
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
	protected void drawGameObject(GameObject gameObject){
		if(gameObject != null){
			offscreenGraphics.drawImage(gameObject.getCurrentImage(), gameObject.getX(), gameObject.getY(), this);
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
	
	protected void animateGameObject(GameObject gameObject, boolean relocate){
		gameObject.animate(relocate);
	}
	
	protected void animateGameObject(GameObject gameObject){
		gameObject.animate(true);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void addGameEvent(int event){
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				ArrayList<Integer> events = userIDtoGameEvents.get(user.getID());
				events.add(event);
				userIDtoGameEvents.put(user.getID(), events);
			}
		}
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

	public void setPlaymode(Playmode playmode) {
		this.playmode = playmode;
	}

	public void setLeftUser(Integer[] leftUser) {
		this.leftUser = leftUser;
		for (Integer i : this.leftUser) {
			userIDtoMatchLoaded.remove(i);
		}
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public void updateGameInformation(HashMap<String, GameObjectInformation> gameObjectInformation) {
		Iterator<Entry<String, GameObjectInformation>> it = gameObjectInformation.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, GameObjectInformation> entry = it.next();
			gameObjects.get(entry.getKey()).setGameInformation(entry.getValue());
		}
		updateGame();
	}

	public HashMap<String, GameObjectInformation> getGameInformation() {
		HashMap<String, GameObjectInformation> matchInformation = new HashMap<>();
		Iterator<Entry<String, GameObject>> it = gameObjects.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, GameObject> entry = it.next();
			matchInformation.put(entry.getKey(), entry.getValue().getInformation());
		}
		return matchInformation;
	}

	public Integer[] getEventsFor(int userID) {
		ArrayList<Integer> alstEvents = userIDtoGameEvents.get(userID);
		Integer[] aEvents = alstEvents.toArray(new Integer[0]);
		userIDtoGameEvents.put(userID, new ArrayList<Integer>());
		return aEvents;
	}
}