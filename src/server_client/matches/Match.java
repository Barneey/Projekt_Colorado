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
	public static final int NO_ALIGN = -1;
	public static final int VERTICAL_ALIGN_LEFT = 0;
	public static final int VERTICAL_ALIGN_CENTER = 1;
	public static final int VERTICAL_ALIGN_RIGHT = 2;
	public static final int HORIZONTAL_ALIGN_TOP = 3;
	public static final int HORIZONTAL_ALIGN_CENTER = 4;
	public static final int HORIZONTAL_ALIGN_BOTTOM = 5;
	protected static final int EVENT_MATCH_OVER = -1337;
	protected int matchType;
	protected transient Image offscreen;
	protected transient Graphics offscreenGraphics;
	protected HashMap<String, GameObject> gameObjects;
	protected HashMap<Integer, Boolean> userIDtoMatchLoaded;
	protected HashMap<Integer, ArrayList<Integer>> userIDtoClientEvents;
	protected ScoreList scoreList;
	protected Playmode playmode;
	protected Integer[] leftUser;
	protected int userID;
	protected int gameID;
	protected boolean running;
	protected boolean playing;
	protected boolean showingCountDown;
	protected boolean showingGameInfo;
	protected boolean showingAddingScore;
	protected boolean showingOnlyScore;
	protected int showingGameInfoCounter;
	protected int showingGameInfoMax;
	protected int showingScoreCounter;
	protected int showingScoreMax;
	protected boolean over;
	
	public Match(int matchType, Playmode playmode){
		this.gameID = -1;
		this.userID = -1;
		this.matchType = matchType;
		this.playmode = playmode;
		this.userIDtoMatchLoaded = new HashMap<>();
		this.userIDtoClientEvents = new HashMap<>();
		this.running = false;
		this.playing = false;
		this.showingCountDown = false;
		this.showingGameInfo = false;
		this.showingAddingScore = false;
		this.showingOnlyScore = true;
		this.showingGameInfoCounter = 0;
		this.showingGameInfoMax = 100;
		this.showingScoreCounter = 0;
		this.showingScoreMax = 150;
		this.over = false;
		this.addKeyListener(this);
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				userIDtoMatchLoaded.put(user.getID(), false);
				userIDtoClientEvents.put(user.getID(), new ArrayList<Integer>());
			}
		}
		super.setSize(720, 405);
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		gameObjects = new HashMap<>();
	}
	
	public abstract void loadImages();
	public abstract void paint(Graphics g);
	protected abstract void startServerLoop();
	protected abstract void updateServerGame();
	protected abstract Integer[] getActions();
	protected abstract void updateGameObjects();
	protected abstract boolean executeGameEvents(Integer[] events);
	public abstract void performClientActions(int userID, Integer[] actions);
	protected abstract void endMatch();

	public void setUserID(int userID){
		this.userID = userID;
	}
	
	protected void drawGameObject(GameObject gameObject){
		if(gameObject != null){
			offscreenGraphics.drawImage(gameObject.getCurrentImage(), gameObject.getX(), gameObject.getY(), this);
		}
	}
	
	protected void drawString(String string, Color c, Font font, int verticalAlign, int horizontalAlign ,int xOffset, int yOffset){
		Color cCopy = offscreenGraphics.getColor();
		Font fontCopy = offscreenGraphics.getFont();
		if(c != null){
			offscreenGraphics.setColor(c);
		}
		if(font != null){
			offscreenGraphics.setFont(font);
		}
        int stringWidth = (int)offscreenGraphics.getFontMetrics().getStringBounds(string, offscreenGraphics).getWidth();
        int xStart = 0;
        switch (verticalAlign) {
		case VERTICAL_ALIGN_LEFT:
			xStart = 0;
			break;
		case VERTICAL_ALIGN_CENTER:
			xStart = offscreen.getWidth(this) / 2 - stringWidth / 2;
			break;
		case VERTICAL_ALIGN_RIGHT:
			xStart = offscreen.getWidth(this) - stringWidth;
			break;
		case NO_ALIGN:
			xStart = 0;
			break;
		default:
			xStart = 0;
			break;
		}
        int stringHeight = (int)offscreenGraphics.getFontMetrics().getStringBounds(string, offscreenGraphics).getHeight();
        int yStart = 0;
        switch (horizontalAlign) {
		case HORIZONTAL_ALIGN_TOP:
			yStart = 0;
			break;
		case HORIZONTAL_ALIGN_CENTER:
			yStart = offscreen.getHeight(this) / 2 - stringHeight / 2;
			break;
		case HORIZONTAL_ALIGN_BOTTOM:
			yStart = offscreen.getHeight(this) - stringHeight;
			break;
		case NO_ALIGN:
			yStart = 0;
			break;
		default:
			yStart = 0;
			break;
		}
		offscreenGraphics.drawString(string, xStart + xOffset, yStart + yOffset);
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
	
	public void addClientEvent(int event){
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				ArrayList<Integer> events = userIDtoClientEvents.get(user.getID());
				events.add(event);
				userIDtoClientEvents.put(user.getID(), events);
			}
		}
	}
	
	public void setSize(int width, int height){
		throw new IllegalArgumentException("The Size can not be changed");
	}
	
	public void setSize(Dimension d){
		throw new IllegalArgumentException("The Size can not be changed");
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
		if(!running){
			running = true;
			startServerLoop();
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
		ArrayList<Integer> alstEvents = userIDtoClientEvents.get(userID);
		Integer[] aEvents = alstEvents.toArray(new Integer[0]);
		userIDtoClientEvents.put(userID, new ArrayList<Integer>());
		return aEvents;
	}
	
	public void setScoreList(ScoreList scoreList){
		this.scoreList = scoreList;
	}

	public ScoreList getScoreList() {
		return this.scoreList;
	}
	
	public boolean isOver(){
		return this.over;
	}
}