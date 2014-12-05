package server_client.matches.horserace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import client.GameConnection;
import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObject;
import server_client.matches.GameObjectInformation;
import server_client.matches.Match;
import server_client.matches.Score;
import server_client.matches.soccer.SoccerImageLoader;

public class HorseRaceMatch extends Match{
	
	private HorseRaceImageLoader hImgLdr;
	private boolean imagesLoaded;
	private String animationStand;
	private String animationStandOpen;
	private String animationMove;
	private GameObject player;
	private transient BufferedImage[] backgroundStand;
	private transient BufferedImage[] backgroundStandOpen;
	private transient BufferedImage[] horseStand;
	private transient BufferedImage[] horseMove;
	private char keytoPress;
	private boolean playing;
	private int countdownTime;
	private int gameTime;
	private int addScoreTime;
	private int showScoreTime;
	private int currentGameTime;
	private int gameInfoTime;
	private ArrayList<Integer> finishedPlayerIDs;
	private final static int EVENT_PLAYER_FINISHED = 0;
	private final static int EVENT_SHOW_COUNTDOWN = 1;
	private final static int EVENT_START_GAME = 2;

	private final static int GAMEINTERVAL = 40;
	
	
	public HorseRaceMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		
		this.animationStand = "STAND";
		this.animationStandOpen = "STAND_OPEN";
		this.animationMove = "MOVE";
		this.keytoPress = 'A';
		this.gameInfoTime = (3 * 1000 / GAMEINTERVAL);
		this.countdownTime = (3 * 1000 / GAMEINTERVAL);
		this.gameTime = (1 * 60 * 1000 / GAMEINTERVAL);
		this.addScoreTime = (3 * 1000 / GAMEINTERVAL);
		this.showScoreTime = (3 * 1000 / GAMEINTERVAL);
		this.currentGameTime = 0;
		this.finishedPlayerIDs = new ArrayList<>();
		gameObjects.put("BACKGROUND", new GameObject(0, 0, this.getSize()));
		gameObjects.put("GOALLINE", new GameObject(655, 0, new Dimension(100, this.getHeight())));
		int playerCounter = 0;
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				HorseGO horse = new HorseGO(5, playerCounter * 40 + 25, new Dimension(40,15));
				horse.setAnimationCounterMax(30);
				gameObjects.put("PLAYER" + user.getID(), horse);
				playerCounter++;
			}
		}
		imagesLoaded = false;
	}

	@Override
	public void run() {
		try {
			showingGameInfo = showingCountDown = playing = showingAddingScore = showingOnlyScore = false;
			while(!isOver()){
				// GameInfo
				if(currentGameTime <= gameInfoTime){
					if(!showingGameInfo){
						showingGameInfo = true;
					}
					repaint();
				}
				// Countdown
				if(currentGameTime > gameInfoTime && currentGameTime <= (countdownTime + gameInfoTime)){
					if(!showingCountDown){
						showingCountDown = true;
						showingGameInfo = false;
					}
					repaint();
				}
				// Game
				if(currentGameTime > countdownTime + gameInfoTime && currentGameTime <= (countdownTime + gameTime + gameInfoTime)){
					if(!playing){
						showingCountDown = false;
						playing = true;
					}
					updateGameObjects();
					repaint();
					requestFocusInWindow();
				}
				// Add Scores up
				if(currentGameTime > (countdownTime + gameTime + gameInfoTime) && currentGameTime <= (countdownTime + gameTime + addScoreTime + gameInfoTime)){
					if(!showingAddingScore){
						this.scoreList = GameConnection.getInstance().getScoreList(gameID, userID); 
						showingAddingScore = true;
						running = false;
					}
					repaint();
				}
				// Show Scores only
				if(currentGameTime > (countdownTime + gameTime + addScoreTime + gameInfoTime) && currentGameTime <= (countdownTime + gameTime + addScoreTime + showScoreTime + gameInfoTime)){
					if(!showingOnlyScore){
						showingOnlyScore = true;
						showingAddingScore = false;
					}
					repaint();
				}
				// Match over
				if(currentGameTime > (countdownTime + gameTime + addScoreTime + showScoreTime + gameInfoTime)){
					over = true;
				}
				currentGameTime++;
				Thread.sleep(GAMEINTERVAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(playing){
			if(Character.toUpperCase(e.getKeyChar()) == keytoPress){
				GameObject player = gameObjects.get("PLAYER" + userID);
				player.changeSpeedBy(0.15);
				player.changeAnimationCounterMaxbyPercent(0.04);
				player.setCurrentAnimationType(animationMove);
				keytoPress = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int)(Math.random() * 26));
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void paint(Graphics g) {
		if(!imagesLoaded){
			loadImages();
		}
		// Clear screen
		offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
		// Draw background
		drawGameObject(gameObjects.get("BACKGROUND"));
		if(showingGameInfo){
			drawString("Press the displayed key to speed your unicorn up", Color.YELLOW, new Font(Font.SANS_SERIF, Font.BOLD, 12), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20);
		}
		if(showingCountDown){
			int countdown = (currentGameTime - gameInfoTime) * GAMEINTERVAL / 1000;
			int countdownLimit = countdownTime * GAMEINTERVAL / 1000;
			String sCountdown = (countdownLimit - countdown == 0 ? "GO!" : countdownLimit - countdown + "" );
			drawString(sCountdown, Color.RED, null, VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20);
		}
		if(playing){
			drawString(keytoPress + "", Color.YELLOW, new Font(Font.SANS_SERIF, Font.BOLD, 20), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20);
			int ms = currentGameTime * GAMEINTERVAL;
			int seconds = (ms / 1000) % 60;
			String sSeconds = seconds / 10 + "" + seconds % 10;
			int minutes = (ms / (1000*60)) % 60;
			drawString("Time: " + minutes + ":" + sSeconds , null, null, VERTICAL_ALIGN_RIGHT, NO_ALIGN, -20, 30);
		}
		// Draw score
		if(showingAddingScore){
			Score[] currentScore = scoreList.getOrderedScore();
			for (int i = 0; i < currentScore.length; i++) {
				drawString(i+1 + ". " + currentScore[i].toString(), Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 15), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20*i+100);
			}
			scoreList.calculateNewScores(currentGameTime - (countdownTime + gameTime + gameInfoTime), (countdownTime + gameTime + addScoreTime + gameInfoTime) - (countdownTime + gameTime + gameInfoTime) + 1);
		}
		if(showingOnlyScore){
			Score[] currentScore = scoreList.getOrderedScore();
			for (int i = 0; i < currentScore.length; i++) {
				drawString(i+1 + ". " + currentScore[i].toString(), Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 15), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20*i+100);
			}
			
		}
		boolean firstTeam = true;
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				drawGameObject(player);
				Color color = Color.RED;
				if(!firstTeam){
					color = Color.BLUE;
				}
				if(user.getID() == userID){
					color = Color.ORANGE;
				}
				drawString((user.getNick().length() >= 10 ? user.getNick().substring(0, 9) : user.getNick()), color, new Font(Font.SERIF, Font.PLAIN, 10), NO_ALIGN, NO_ALIGN, player.getX(), player.getY() - 5);
			}
			firstTeam = false;
		}
		g.drawImage(offscreen, 0, 0, this);
	}

	@Override
	public void loadImages() {
		if(hImgLdr == null){
			hImgLdr = new HorseRaceImageLoader();
		}
		GameObject background = gameObjects.get("BACKGROUND");
		if(backgroundStand == null){
			backgroundStand = new BufferedImage[1];
		}
		backgroundStand[0] = hImgLdr.scaleBufferedImage(hImgLdr.loadBufferedImage(HorseRaceImageLoader.BACKGROUND), background.getSize());
		background.addAnimation(animationStand, backgroundStand);
		if(backgroundStandOpen == null){
			backgroundStandOpen = new BufferedImage[1];
		}
		backgroundStandOpen[0] = hImgLdr.scaleBufferedImage(hImgLdr.loadBufferedImage(HorseRaceImageLoader.BACKGROUND_OPEN), background.getSize());
		background.addAnimation(animationStandOpen, backgroundStandOpen);
		
		boolean firstRun = true;
		if(horseStand == null){
			horseStand = new BufferedImage[1];
		}
		if(horseMove == null){
			horseMove = new BufferedImage[HorseRaceImageLoader.HORSE_MOVE.length];
		}
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject playerObject = gameObjects.get("PLAYER" + user.getID());
				if(firstRun){
					firstRun = false;
					horseStand[0] = (hImgLdr.scaleBufferedImage(hImgLdr.loadBufferedImage(HorseRaceImageLoader.HORSE_STAND), playerObject.getSize()));
					for(int i = 0; i < HorseRaceImageLoader.HORSE_MOVE.length; i++){
						horseMove[i] = (hImgLdr.scaleBufferedImage(hImgLdr.loadBufferedImage(HorseRaceImageLoader.HORSE_MOVE[i]), playerObject.getSize()));
					}
				}
				playerObject.addAnimation(animationStand, horseStand);
				playerObject.addAnimation(animationMove, horseMove);
			}
		}
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		imagesLoaded = true;
	}

	@Override
	protected void updateGameObjects() {
		boolean gameEventsGotten = false;
		try {
			gameEventsGotten = executeGameEvents(GameConnection.getInstance().getGameEvents(gameID, userID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, GameObjectInformation> clientPlayerObjects = new HashMap<>();
		if(!gameEventsGotten){
			player = gameObjects.get("PLAYER" + userID);
			clientPlayerObjects.put("PLAYER" + userID, player.getInformation());
		}
		try {
			clientPlayerObjects = GameConnection.getInstance().updateGameObjects(userID, getActions(), clientPlayerObjects, gameID);
			Iterator<Entry<String, GameObjectInformation>> it = clientPlayerObjects.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, GameObjectInformation> entry = it.next();
				gameObjects.get(entry.getKey()).setGameInformation(entry.getValue());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				animateGameObject(gameObjects.get("PLAYER" + user.getID()));
			}
		}
	}

	@Override
	protected void updateServerGame() {
		// GameInfo
		if(currentGameTime <= gameInfoTime){
			
		}
		// Countdown
		if(currentGameTime <= countdownTime){
			if(!showingCountDown){
				showingCountDown = true;
				addClientEvent(EVENT_SHOW_COUNTDOWN);
			}
		}
		// Game
		if(currentGameTime > countdownTime && currentGameTime <= (countdownTime + gameTime)){
			if(!playing){
				showingCountDown = false;
				playing = true;
				gameObjects.get("BACKGROUND").setCurrentAnimationType(animationStandOpen);
				addClientEvent(EVENT_START_GAME);
				for (Team team : playmode.getTeams()) {
					for (User user : team.getUser()) {
						((HorseGO)(gameObjects.get("PLAYER" + user.getID()))).setRunning(true);;
					}
				}
			}
			int playerCounter = 0;
			GameObject goalLine = gameObjects.get("GOALLINE");
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					if(!finishedPlayerIDs.contains(user.getID())){
						HorseGO player = (HorseGO)gameObjects.get("PLAYER" + user.getID());
						if(player.correspondsWith(goalLine)){
							finishedPlayerIDs.add(user.getID());
							player.setRunning(false);
							player.setCurrentAnimationType(animationStand);
							scoreList.addScoreFor(user, (500 / finishedPlayerIDs.size()));
						}
					}
					playerCounter++;
				}
			}
			if(playerCounter == finishedPlayerIDs.size()){
				addClientEvent(EVENT_MATCH_OVER);
				endMatch();
			}
		}
		// Add Scores up
		if(currentGameTime > (countdownTime + gameTime) && currentGameTime <= (countdownTime + gameTime + addScoreTime)){
			if(!showingAddingScore){
				showingAddingScore = true;
				running = false;
				for (Team team : playmode.getTeams()) {
					for (User user : team.getUser()) {
						if(!finishedPlayerIDs.contains(user.getID())){
							HorseGO player = (HorseGO)gameObjects.get("PLAYER" + user.getID());
							finishedPlayerIDs.add(user.getID());
							player.setRunning(false);
							
						}
					}
				}
				addClientEvent(EVENT_SHOW_COUNTDOWN);
			}
		}
		// Show Scores only
		if(currentGameTime > (countdownTime + gameTime + addScoreTime) && currentGameTime <= (countdownTime + gameTime + addScoreTime + showScoreTime)){
			
		}
		// Match over
		if(currentGameTime > (countdownTime + gameTime + addScoreTime + showScoreTime)){
			
		}
		currentGameTime++;
	}

	@Override
	protected boolean executeGameEvents(Integer[] events) {
		if(events == null){
			events = new Integer[0];
		}
		for(Integer event : events){
			switch (event) {
			case EVENT_START_GAME:{
				break;
			}
			case EVENT_SHOW_COUNTDOWN:{
				break;
			}
			case EVENT_MATCH_OVER:{
				endMatch();
				break;
			}
			default:
				break;
			}
		}
		return events.length > 0;
	}

	@Override
	protected void startServerLoop() {
		Runnable thread = new Runnable(){

			@Override
			public void run() {
				while(running){
					try {
						Thread.sleep(GAMEINTERVAL);
						updateServerGame();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		(new Thread(thread)).start();
	}

	@Override
	protected Integer[] getActions() {
		return new Integer[0];
	}

	@Override
	public void performClientActions(int userID, Integer[] actions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endMatch() {
		currentGameTime = countdownTime + gameTime + 1;
	}
}