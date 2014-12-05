package server_client.matches.soccer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import client.GameConnection;
import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObject;
import server_client.matches.GameObjectInformation;
import server_client.matches.Match;
import server_client.matches.Score;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class SoccerMatch extends Match{
	
	private SoccerImageLoader sImgLdr;
	private boolean imagesLoaded;
	private int[] score;
	private Dimension fieldSize;
	private Point fieldStart;
	private String animationStand;
	private String animationMove;
	private GameObject player;
	private int lastContactUserID;
	private Set<Integer> pressedKeys;
	private int playerAnimationTimer;
	private double ballSpeedRun;
	private double ballSpeedShoot;
	private double ballSpeedReduction;
	private transient BufferedImage[] ballStand;
	private transient BufferedImage[] ballMove;
	private transient BufferedImage[] backgroundStand;
	private transient BufferedImage[] playerStand;
	private transient BufferedImage[] playerMove;
	private final static int EVENT_GOAL_TEAM_1 = 1;
	private final static int EVENT_GOAL_TEAM_2 = 2;
	private final static int EVENT_RESET_BALL = 3;
	private final static int EVENT_THROW_IN_TOP_TEAM1 = 4;
	private final static int EVENT_THROW_IN_TOP_TEAM2 = 5;
	private final static int EVENT_THROW_IN_BOTTOM_TEAM1 = 6;
	private final static int EVENT_THROW_IN_BOTTOM_TEAM2 = 7;
	private final static int EVENT_CORNER_TOP_TEAM_1 = 8;
	private final static int EVENT_CORNER_BOTTOM_TEAM_1 = 9;
	private final static int EVENT_CORNER_TOP_TEAM_2 = 10;
	private final static int EVENT_CORNER_BOTTOM_TEAM_2 = 11;
	private final static int EVENT_KICKOFF_TEAM_1 = 12;
	private final static int EVENT_KICKOFF_TEAM_2 = 13;
	private final static int ACTION_SHOOT = 1;
	private final static int TEAM_1 = 0;
	private final static int TEAM_2 = 1;
	private final static int TOP = 0;
	private final static int BOTTOM = 1;
	private final static int GAMEINTERVAL = 40;
	private boolean goal;
	private int goalDisplayCounter;
	private int goalDisplayCounterMax;
	private int skipRepaintingCounter;
	private int gameTimerMax;
	private int currentGameTime;
	
	public SoccerMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		this.pressedKeys = new HashSet<>();
		this.lastContactUserID = -1;
		this.playerAnimationTimer = 6;
		this.ballSpeedRun = 5.0;
		this.ballSpeedShoot = 9.0;
		this.ballSpeedReduction = 0.2;
		this.animationStand = "STAND";
		this.animationMove = "MOVE";
		this.fieldSize = new Dimension(626, 307);
		this.fieldStart = new Point(47, 49);
		this.goal = false;
		this.goalDisplayCounter = 0;
		this.goalDisplayCounterMax = 30;
		this.skipRepaintingCounter = 0;
		this.gameTimerMax = (int)(0.5 * 60 * 1000 / GAMEINTERVAL);
		this.currentGameTime = 0;
		GameObject ball = new GameObject(fieldStart.x + fieldSize.width / 2 - (20/2), fieldStart.y + fieldSize.height / 2 - (20/2), new Dimension(20,20));
		ball.setSpeedReduction(ballSpeedReduction);
		ball.setAnimationCounterMax(8);
		this.gameObjects.put("BALL", ball);
		this.gameObjects.put("BACKGROUND", new GameObject(0, 0, new Dimension(getWidth(), getHeight())));
		this.gameObjects.put("GOAL1", new GameObject(45, 162, new Dimension(3, 82)));
		this.gameObjects.put("GOAL2", new GameObject(674, 162, new Dimension(3, 82)));
		this.gameObjects.put("TOUCH_TOP", new GameObject(40, 48, new Dimension(640, 3)));
		this.gameObjects.put("TOUCH_BOTTOM", new GameObject(40, 358, new Dimension(640, 3)));
		this.gameObjects.put("GOAL_LINE_TEAM1_TOP", new GameObject(40, 48, new Dimension(3, 112)));
		this.gameObjects.put("GOAL_LINE_TEAM1_BOTTOM", new GameObject(40, 244, new Dimension(3, 112)));
		this.gameObjects.put("GOAL_LINE_TEAM2_TOP", new GameObject(680, 48, new Dimension(3, 112)));
		this.gameObjects.put("GOAL_LINE_TEAM2_BOTTOM", new GameObject(680, 244, new Dimension(3, 112)));
		// Check playmode and create Objects
		switch (this.matchType) {
		case TEAM:{
			User[] user = playmode.getTeams()[0].getUser();
			int xOffset = fieldSize.width / 30;
			int yOffset = fieldSize.height / ((user.length + 1) / 2);
			for(int i = 0; i < user.length; i++){
				GameObject newPlayer = new GameObject(fieldStart.x + xOffset + (i / 2 == 0 ? xOffset : 0), fieldStart.y + yOffset + yOffset * (i/2), new Dimension(21,21));
				newPlayer.setAnimationCounterMax(playerAnimationTimer);
				gameObjects.put("PLAYER" + user[i].getID(), newPlayer);
			}
			user = playmode.getTeams()[1].getUser();
			for(int i = 0; i < user.length; i++){
				GameObject newPlayer = new GameObject(fieldStart.x + fieldSize.width - (xOffset + (i / 2 == 0 ? xOffset : 0)), fieldStart.y + fieldSize.height - (yOffset + yOffset * (i/2)), new Dimension(21,21));
				newPlayer.setAnimationCounterMax(playerAnimationTimer);
				gameObjects.put("PLAYER" + user[i].getID(), newPlayer);
			}
			break;
		}
		case TEST:{
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					GameObject newPlayer = new GameObject(fieldStart.x + fieldSize.width / 2, fieldStart.y + fieldSize.height / 2, new Dimension(21,21));
					newPlayer.setAnimationCounterMax(playerAnimationTimer);
					gameObjects.put("PLAYER" + user.getID(), newPlayer);
				}
			}
			break;
		}
		default:
			break;
		}
		imagesLoaded = false;
		score = new int[2];
		score[0] = 0;
		score[1] = 0;
	}

	public void loadImages(){
		if(sImgLdr == null){
			sImgLdr = new SoccerImageLoader();
		}
		
		GameObject ball = gameObjects.get("BALL");
		if(ballStand == null){
			ballStand = new BufferedImage[0];
		}
		ball.addAnimation(animationStand, ballStand);

		if(ballMove == null){
			ballMove = new BufferedImage[SoccerImageLoader.BALL_MOVE.length];
		}
		for (int i = 0; i < SoccerImageLoader.BALL_MOVE.length; i++) {
			ballMove[i] = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BALL_MOVE[i]), ball.getSize());
		}
		ball.addAnimation(animationMove, ballMove);
		ball.setCurrentImage(ballMove[0]);
		
		GameObject background = gameObjects.get("BACKGROUND");
		if(backgroundStand == null){
			backgroundStand = new BufferedImage[1];
		}
		backgroundStand[0] = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BACKGROUND), background.getSize());
		background.addAnimation(animationStand, backgroundStand);
		
		boolean firstRun = true;
		if(playerStand == null){
			playerStand = new BufferedImage[1];
		}
		if(playerMove == null){
			playerMove = new BufferedImage[3];
		}
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject playerObject = gameObjects.get("PLAYER" + user.getID());
				if(firstRun){
					firstRun = false;
					playerStand[0] = (sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYER_STAND), playerObject.getSize()));
					for(int i = 0; i < SoccerImageLoader.PLAYER_MOVE.length; i++){
						playerMove[i] = (sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYER_MOVE[i]), playerObject.getSize()));
					}
				}
				playerObject.addAnimation(animationStand, playerStand);
				playerObject.addAnimation(animationMove, playerMove);
			}
		}
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		imagesLoaded = true;
	}
	
	protected void startServerLoop(){
		
		Runnable thread = new Runnable(){

			@Override
			public void run() {
				while(running){
					try {
						Thread.sleep(GAMEINTERVAL);
						updateServerGame();
						currentGameTime++;
						if(currentGameTime >= gameTimerMax-1){
							running = false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Match over
				if(matchType == TEAM){
					if(score[TEAM_1] == score[TEAM_2]){
						for (Team team : playmode.getTeams()) {
							for (User user : team.getUser()) {
								scoreList.addScoreFor(user, 150);
							}
						}
					}else if(score[TEAM_1] > score[TEAM_2]){
						for (User user : playmode.getTeams()[TEAM_1].getUser()) {
							scoreList.addScoreFor(user, 400);
						}
					}else if(score[TEAM_2] > score[TEAM_1]){
						for (User user : playmode.getTeams()[TEAM_2].getUser()) {
							scoreList.addScoreFor(user, 400);
						}
					}
				}else if(matchType == TEST){
					for (Team team : playmode.getTeams()) {
						for (User user : team.getUser()) {
							scoreList.addScoreFor(user, 1337);
						}
					}
				}
				addClientEvent(EVENT_MATCH_OVER);
				
			}
		};
		(new Thread(thread)).start();
	}
	
	private void renewImages(){
		
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				boolean firstRun = true;
				for (Team team : playmode.getTeams()) {
					for (User user : team.getUser()) {
						GameObject playerObject = gameObjects.get("PLAYER" + user.getID());
						if(firstRun){
							firstRun = false;
							playerStand[0] = (sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYER_STAND), playerObject.getSize()));
							for(int i = 0; i < SoccerImageLoader.PLAYER_MOVE.length; i++){
								playerMove[i] = (sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYER_MOVE[i]), playerObject.getSize()));
							}
						}
						BufferedImage[] tempPlayerStand = new BufferedImage[playerStand.length];
						for (int i = 0; i < playerStand.length; i++) {
							tempPlayerStand[i] = sImgLdr.rotateBufferedImage(playerStand[i], playerObject.getViewDegree());
						}
						BufferedImage[] tempPlayerMove = new BufferedImage[playerMove.length];
						for (int i = 0; i < playerMove.length; i++) {
							tempPlayerMove[i] = sImgLdr.rotateBufferedImage(playerMove[i], playerObject.getViewDegree());
						}
						
						playerObject.addAnimation(animationStand, tempPlayerStand);
						playerObject.addAnimation(animationMove, tempPlayerMove);
					}
				}
			}
		};
		(new Thread(thread)).start();
		
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
		// Draw gameInfo
		if(showingGameInfo){
			if(showingGameInfoCounter < showingGameInfoMax){
				showingGameInfoCounter++;
				drawString("Use the Arrow-Keys and Space to move the ball into the opponent goal", Color.YELLOW, new Font(Font.SANS_SERIF, Font.BOLD, 12), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 100);
			}else{
				showingGameInfo = false;
			}
		}
		// Draw score
		if(showingAddingScore){
			Score[] currentScore = scoreList.getOrderedScore();
			for (int i = 0; i < currentScore.length; i++) {
				drawString(i+1 + ". " + currentScore[i].toString(), Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 15), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20*i+100);
			}
			if(!showingOnlyScore){
				scoreList.calculateNewScores(showingScoreCounter, showingScoreMax);
			}
		}
		// Draw components
		drawGameObject(gameObjects.get("BALL"));
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
		drawString(score[0] + ":" + score[1], Color.RED, new Font(Font.SANS_SERIF, Font.PLAIN, 20), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 30);
		int ms = currentGameTime * GAMEINTERVAL;
		int seconds = (ms / 1000) % 60;
		String sSeconds = seconds / 10 + "" + seconds % 10;
		int minutes = (ms / (1000*60)) % 60;
		drawString("Time: " + minutes + ":" + sSeconds , null, null, VERTICAL_ALIGN_RIGHT, NO_ALIGN, -20, 30);
		if(goal){
			goalDisplayCounter++;
			int colorValue = (100 + (155 * goalDisplayCounter / goalDisplayCounterMax));
			drawString("G O A L", new Color(colorValue,colorValue,colorValue), new Font(Font.SANS_SERIF, Font.PLAIN, 40), VERTICAL_ALIGN_CENTER, NO_ALIGN, -4, 130);
			if(goalDisplayCounter >= goalDisplayCounterMax){
				goal = false;
				goalDisplayCounter = 0;
			}
		}
		g.drawImage(offscreen, 0, 0, this);
	}

	protected void updateGameObjects(){
		HashMap<String, GameObjectInformation> clientPlayerObjects = new HashMap<>();
		player = gameObjects.get("PLAYER" + userID);
		clientPlayerObjects.put("PLAYER" + userID, player.getInformation());
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
		GameObject background = gameObjects.get("BACKGROUND");
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				animateGameObject(player);
				player.outOfBoundsCorrection(background);
			}
		}
		animateGameObject(gameObjects.get("BALL"), false);
		try{
			executeGameEvents(GameConnection.getInstance().getGameEvents(gameID, userID));
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	protected Integer[] getActions() {
		ArrayList<Integer> alstActions = new ArrayList<>();
		if(pressedKeys.contains(KeyEvent.VK_SPACE)){
			alstActions.add(ACTION_SHOOT);
		}
		return alstActions.toArray(new Integer[0]);
	}

	protected void updateServerGame(){
		GameObject ball = gameObjects.get("BALL");
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				if(player.correspondsWith(ball)){
					ball.setViewDegree(player.getViewDegree());
					ball.setSpeed(ballSpeedRun);
					ball.setCurrentAnimationType(animationMove);
					this.lastContactUserID = user.getID();
				}
			}
		}
		if(ball.getSpeed() <= 0.0){
			ball.setCurrentAnimationType(animationStand);
		}else{
			ball.relocate();
		}
		GameObject goal1 = gameObjects.get("GOAL1");
		if(ball.correspondsWith(goal1)){
			addClientEvent(EVENT_GOAL_TEAM_2);
			resetPlayerPositions();
			ball.setLocation(301, 193);
			ball.setSpeed(0.0);
			ball.setCurrentAnimationType(animationStand);
			if(this.matchType == TEAM){
				for (User user : playmode.getTeams()[TEAM_2].getUser()) {
					this.scoreList.addScoreFor(user, 80);
				}
			}else if(this.matchType == TEST){
				for (Team team : playmode.getTeams()) {
					for (User user : team.getUser()) {
						this.scoreList.addScoreFor(user, 80);
					}
				}
			}
		}else{
			GameObject goal2 = gameObjects.get("GOAL2");
			if(ball.correspondsWith(goal2)){
				addClientEvent(EVENT_GOAL_TEAM_1);
				resetPlayerPositions();
				ball.setLocation(401, 193);
				ball.setSpeed(0.0);
				ball.setCurrentAnimationType(animationStand);
				if(this.matchType == TEAM){
					for (User user : playmode.getTeams()[TEAM_1].getUser()) {
						this.scoreList.addScoreFor(user, 80);
					}
				}else if(this.matchType == TEST){
					for (Team team : playmode.getTeams()) {
						for (User user : team.getUser()) {
							this.scoreList.addScoreFor(user, 80);
						}
					}
				}
			}else{
				GameObject touchTop = gameObjects.get("TOUCH_TOP");
				if(ball.correspondsWith(touchTop)){
					switch (matchType) {
					case TEST:
						addClientEvent(EVENT_RESET_BALL);
						resetBallPosition();
						break;
					case TEAM:
						if(lastContactFromTeam(TEAM_1)){
							addClientEvent(EVENT_THROW_IN_TOP_TEAM2);
							positionPlayerThrowInTopForTeam(TEAM_2);
						}else if(lastContactFromTeam(TEAM_2)){
							addClientEvent(EVENT_THROW_IN_TOP_TEAM1);
							positionPlayerThrowInTopForTeam(TEAM_1);
						}else{
							addClientEvent(EVENT_RESET_BALL);
							resetBallPosition();
						}
						break;
					default:
						break;
					}
				}else{
					GameObject touchBottom = gameObjects.get("TOUCH_BOTTOM");
					if(ball.correspondsWith(touchBottom)){
						switch (matchType) {
						case TEST:
							addClientEvent(EVENT_RESET_BALL);
							resetBallPosition();
							break;
						case TEAM:
							if(lastContactFromTeam(TEAM_1)){
								addClientEvent(EVENT_THROW_IN_BOTTOM_TEAM2);
								positionPlayerThrowInBottomForTeam(TEAM_2);
							}else if(lastContactFromTeam(TEAM_2)){
								addClientEvent(EVENT_THROW_IN_BOTTOM_TEAM1);
								positionPlayerThrowInBottomForTeam(TEAM_1);
							}else{
								addClientEvent(EVENT_RESET_BALL);
								resetBallPosition();
							}
							break;
						default:
							break;
						}
					}else{
						GameObject goalLineTeam1Top = gameObjects.get("GOAL_LINE_TEAM1_TOP");
						if(ball.correspondsWith(goalLineTeam1Top)){
							switch (matchType) {
							case TEST:
								addClientEvent(EVENT_RESET_BALL);
								resetBallPosition();
								break;
							case TEAM:
								if(lastContactFromTeam(TEAM_1)){
									addClientEvent(EVENT_CORNER_TOP_TEAM_2);
									positionCorner(TEAM_2, TOP);
								}else if(lastContactFromTeam(TEAM_2)){
									addClientEvent(EVENT_KICKOFF_TEAM_1);
									positionPlayerKickOff(TEAM_1);
								}else{
									addClientEvent(EVENT_RESET_BALL);
									resetBallPosition();
								}
								break;
							default:
								break;
							}
						}else{
							GameObject goalLineTeam1Bottom = gameObjects.get("GOAL_LINE_TEAM1_BOTTOM");
							if(ball.correspondsWith(goalLineTeam1Bottom)){
								switch (matchType) {
								case TEST:
									addClientEvent(EVENT_RESET_BALL);
									resetBallPosition();
									break;
								case TEAM:
									if(lastContactFromTeam(TEAM_1)){
										addClientEvent(EVENT_CORNER_BOTTOM_TEAM_2);
										positionCorner(TEAM_2, BOTTOM);
									}else if(lastContactFromTeam(TEAM_2)){
										addClientEvent(EVENT_KICKOFF_TEAM_1);
										positionPlayerKickOff(TEAM_1);
									}else{
										addClientEvent(EVENT_RESET_BALL);
										resetBallPosition();
									}
									break;
								default:
									break;
								}
							}else{
								GameObject goalLineTeam2Top = gameObjects.get("GOAL_LINE_TEAM2_TOP");
								if(ball.correspondsWith(goalLineTeam2Top)){
									switch (matchType) {
									case TEST:
										addClientEvent(EVENT_RESET_BALL);
										resetBallPosition();
										break;
									case TEAM:
										if(lastContactFromTeam(TEAM_2)){
											addClientEvent(EVENT_CORNER_TOP_TEAM_1);
											positionCorner(TEAM_1, TOP);
										}else if(lastContactFromTeam(TEAM_1)){
											addClientEvent(EVENT_KICKOFF_TEAM_2);
											positionPlayerKickOff(TEAM_2);
										}else{
											addClientEvent(EVENT_RESET_BALL);
											resetBallPosition();
										}
										break;
									default:
										break;
									}
								}else{
									GameObject goalLineTeam2Bottom = gameObjects.get("GOAL_LINE_TEAM2_BOTTOM");
									if(ball.correspondsWith(goalLineTeam2Bottom)){
										switch (matchType) {
										case TEST:
											addClientEvent(EVENT_RESET_BALL);
											resetBallPosition();
											break;
										case TEAM:
											if(lastContactFromTeam(TEAM_2)){
												addClientEvent(EVENT_CORNER_BOTTOM_TEAM_1);
												positionCorner(TEAM_1, BOTTOM);
											}else if(lastContactFromTeam(TEAM_1)){
												addClientEvent(EVENT_KICKOFF_TEAM_2);
												positionPlayerKickOff(TEAM_2);
											}else{
												addClientEvent(EVENT_RESET_BALL);
												resetBallPosition();
											}
											break;
										default:
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void positionPlayerKickOff(int teamNumber) {
		switch (teamNumber) {
		case TEAM_1:{
			Random generator = new Random();
			int kickOffPlayerID = playmode.getTeams()[teamNumber].getUser()[generator.nextInt(playmode.getTeams()[teamNumber].getUser().length)].getID();
			GameObject ball = gameObjects.get("BALL");
			ball.setSpeed(0.0);
			ball.setViewDegree(0);
			ball.setLocation(103, 194);
			ball.setCurrentAnimationType(animationStand);
			GameObject kickOffPlayerArea = new GameObject(235, 56, new Dimension(251,295));
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					GameObject player = gameObjects.get("PLAYER" + user.getID());
					player.setSpeed(0.0);
					player.setCurrentAnimationType(animationStand);
					if(user.getID() == kickOffPlayerID){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_LEFT, GameObject.Y_OFFSET_MIDDLE);
					}else{
						player.positionAnywhereIn(kickOffPlayerArea);
					}
				}
			}
			break;
			}
		case TEAM_2:
			Random generator = new Random();
			int kickOffPlayerID = playmode.getTeams()[teamNumber].getUser()[generator.nextInt(playmode.getTeams()[teamNumber].getUser().length)].getID();
			GameObject ball = gameObjects.get("BALL");
			ball.setSpeed(0.0);
			ball.setViewDegree(180);
			ball.setLocation(603, 194);
			ball.setCurrentAnimationType(animationStand);
			GameObject kickOffPlayerArea = new GameObject(235, 56, new Dimension(251,295));
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					GameObject player = gameObjects.get("PLAYER" + user.getID());
					player.setSpeed(0.0);
					player.setCurrentAnimationType(animationStand);
					if(user.getID() == kickOffPlayerID){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_RIGHT, GameObject.Y_OFFSET_MIDDLE);
					}else{
						player.positionAnywhereIn(kickOffPlayerArea);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private void positionCorner(int teamNumber, int side) {
		Random generator = new Random();
		int throwInPlayerID = playmode.getTeams()[teamNumber].getUser()[generator.nextInt(playmode.getTeams()[teamNumber].getUser().length)].getID();
		GameObject ball = gameObjects.get("BALL");
		ball.setSpeed(0.0);
		ball.setCurrentAnimationType(animationStand);
		GameObject cornerPlayerArea = new GameObject(-1, -1, new Dimension(145, 180));
		if(teamNumber == TEAM_1){
			cornerPlayerArea.setLocation(520, 118);
			if(side == TOP){
				ball.setLocation(647, 60);
				ball.setViewDegree(135);
			}else if(side == BOTTOM){
				ball.setLocation(647, 327);
				ball.setViewDegree(225);
			}else{
				ball.resetLocation();
			}
		}else if(teamNumber == TEAM_2){
			cornerPlayerArea.setLocation(60, 118);
			if(side == TOP){
				ball.setLocation(60, 60);
				ball.setViewDegree(45);
			}else if(side == BOTTOM){
				ball.setLocation(60, 327);
				ball.setViewDegree(315);
			}else{
				ball.resetLocation();
			}
		}
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				player.setSpeed(0.0);
				player.setCurrentAnimationType(animationStand);
				if(user.getID() == throwInPlayerID){
					if(teamNumber == TEAM_2 && side == TOP){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_LEFT, GameObject.Y_OFFSET_OVER);
					}else if(teamNumber == TEAM_2 && side == BOTTOM){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_LEFT, GameObject.Y_OFFSET_BELOW);
					}else if(teamNumber == TEAM_1 && side == TOP){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_RIGHT, GameObject.Y_OFFSET_OVER);
					}else if(teamNumber == TEAM_1 && side == BOTTOM){
						player.positionRelativeTo(ball, GameObject.X_OFFSET_RIGHT, GameObject.Y_OFFSET_BELOW);
					}else{
						player.positionRelativeTo(ball, GameObject.X_OFFSET_MIDDLE, GameObject.Y_OFFSET_MIDDLE);
					}
				}else{
					player.positionAnywhereIn(cornerPlayerArea);
				}
			}
		}
	}
	
	private void positionPlayerThrowInTopForTeam(int i) {
		Random generator = new Random();
		int throwInPlayerID = playmode.getTeams()[i].getUser()[generator.nextInt(playmode.getTeams()[i].getUser().length)].getID();
		GameObject ball = gameObjects.get("BALL");
		ball.setSpeed(0.0);
		ball.setViewDegree(90);
		ball.setY(52);
		ball.setCurrentAnimationType(animationStand);
		GameObject throwInPlayerArea = new GameObject(ball.getLocation().x - ((130-ball.getSize().width)/2), ball.getLocation().y + 60, new Dimension(130, 60));
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				player.setSpeed(0.0);
				player.setCurrentAnimationType(animationStand);
				if(user.getID() == throwInPlayerID){
					player.positionRelativeTo(ball, GameObject.X_OFFSET_MIDDLE, GameObject.Y_OFFSET_OVER);
				}else{
					player.positionAnywhereIn(throwInPlayerArea);
				}
			}
		}
	}
	
	private void positionPlayerThrowInBottomForTeam(int i) {
		Random generator = new Random();
		int throwInPlayerID = playmode.getTeams()[i].getUser()[generator.nextInt(playmode.getTeams()[i].getUser().length)].getID();
		GameObject ball = gameObjects.get("BALL");
		ball.setSpeed(0.0);
		ball.setViewDegree(270);
		ball.setY(333);
		ball.setCurrentAnimationType(animationStand);
		GameObject throwInPlayerArea = new GameObject(ball.getLocation().x - ((130-ball.getSize().width)/2), ball.getLocation().y - 60, new Dimension(130, 60));
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				if(user.getID() == throwInPlayerID){
					player.setSpeed(0.0);
					player.setCurrentAnimationType(animationStand);
					player.positionRelativeTo(ball, GameObject.X_OFFSET_MIDDLE, GameObject.Y_OFFSET_BELOW);
				}else{
					player.positionAnywhereIn(throwInPlayerArea);
				}
			}
		}
	}

	private boolean lastContactFromTeam(int i) {
		for (User user : playmode.getTeams()[i].getUser()) {
			if(user.getID() == lastContactUserID){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean executeGameEvents(Integer[] events) {
		if(events == null){
			events = new Integer[0];
		}
		for (Integer integer : events) {
			switch (integer) {
			case EVENT_GOAL_TEAM_1:
				goalTeamOne();
				break;
			case EVENT_GOAL_TEAM_2:
				goalTeamTwo();
				break;
			case EVENT_RESET_BALL:
				resetBallPosition();
				break;
			case EVENT_THROW_IN_TOP_TEAM1:
				positionPlayerThrowInTopForTeam(TEAM_1);
				skipRepaintingCounter = 2;
				break;
			case EVENT_THROW_IN_TOP_TEAM2:
				positionPlayerThrowInTopForTeam(TEAM_2);
				skipRepaintingCounter = 2;
				break;
			case EVENT_THROW_IN_BOTTOM_TEAM1:
				positionPlayerThrowInBottomForTeam(TEAM_1);
				skipRepaintingCounter = 2;
				break;
			case EVENT_CORNER_TOP_TEAM_1:
				positionCorner(TEAM_1, TOP);
				skipRepaintingCounter = 2;
				break;
			case EVENT_CORNER_TOP_TEAM_2:
				positionCorner(TEAM_2, TOP);
				skipRepaintingCounter = 2;
				break;
			case EVENT_CORNER_BOTTOM_TEAM_1:
				positionCorner(TEAM_1, BOTTOM);
				skipRepaintingCounter = 2;
				break;
			case EVENT_CORNER_BOTTOM_TEAM_2:
				positionCorner(TEAM_2, BOTTOM);
				skipRepaintingCounter = 2;
				break;
			case EVENT_KICKOFF_TEAM_1:
				positionPlayerKickOff(TEAM_1);
				skipRepaintingCounter = 2;
				break;
			case EVENT_KICKOFF_TEAM_2:
				positionPlayerKickOff(TEAM_2);
				skipRepaintingCounter = 2;
				break;
			case EVENT_MATCH_OVER:{
				endMatch();
			}
			default:
				break;
			}
		}
		return events.length > 0;
	}
	
	public static synchronized void playSound() {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		    	InputStream in = new FileInputStream("sounds/goal.wav");
		    	AudioStream as = new AudioStream(in);         
		    	AudioPlayer.player.start(as);            
		      } catch (Exception e) {
		    	  e.printStackTrace();
		      }
		    }
		  }).start();
		}
	
	private void goalTeamOne(){
		score[0]++;
		goal=true;
		playSound();
		resetPlayerPositions();
	}
	
	private void goalTeamTwo(){
		score[1]++;
		goal=true;
		playSound();
		resetPlayerPositions();
	}
	
	private void resetPlayerPositions(){
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				gameObjects.get("PLAYER" + user.getID()).resetLocation();
			}
		}
	}

	private void resetBallPosition(){
		GameObject ball = gameObjects.get("BALL");
		ball.resetLocation();
		ball.setSpeed(0.0);
		ball.setCurrentAnimationType(animationStand);
	}
	
	@Override
	public void run() {
		showingGameInfo = true;
		repaint();
		running = true;
		int counter = 0;
		while(running){
			try {
				counter++;
				if(counter >= 10){
					counter = 0;
					renewImages();
				}
				Thread.sleep(GAMEINTERVAL);
				currentGameTime++;
				updateGameObjects();
				if(skipRepaintingCounter > 0){
					skipRepaintingCounter--;
				}else{
					repaint();
				}
				requestFocusInWindow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			this.scoreList = GameConnection.getInstance().getScoreList(gameID, userID); 
			this.showingAddingScore = true;
			this.showingOnlyScore = false;
			while(showingScoreCounter < showingScoreMax){
				Thread.sleep(GAMEINTERVAL);
				repaint();
				this.showingScoreCounter++;
			}
			this.showingOnlyScore = true;
			for(int i = 0; i < 50; i++){
				Thread.sleep(GAMEINTERVAL);
				repaint();
			}
			this.showingAddingScore = false;
			this.over = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void keyPressed(KeyEvent e) {
//		if(e.getKeyCode() == KeyEvent.VK_SPACE){
//			System.out.println(MouseInfo.getPointerInfo().getLocation().x + " " + MouseInfo.getPointerInfo().getLocation().y);
//		}
		if(player == null){
			player = gameObjects.get("PLAYER" + userID);
		}
		pressedKeys.add(e.getKeyCode());
		setMovementForPlayer();
	}
			
	public void keyReleased(KeyEvent e) {
		if(player == null){
			player = gameObjects.get("PLAYER" + userID);
		}
		pressedKeys.remove(e.getKeyCode());
		setMovementForPlayer();
	}

	public void keyTyped(KeyEvent e) {
	}
	
	private void setMovementForPlayer(){
		if(player == null){
			player = gameObjects.get("PLAYER" + userID);
		}
		if(pressedKeys.size() == 0){
			player.setSpeed(0);
			player.setCurrentAnimationType(animationStand);
		}else if(pressedKeys.contains(KeyEvent.VK_LEFT) && pressedKeys.contains(KeyEvent.VK_UP)){
			player.setViewDegree(225);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_UP) && pressedKeys.contains(KeyEvent.VK_RIGHT)){
			player.setViewDegree(315);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_RIGHT) && pressedKeys.contains(KeyEvent.VK_DOWN)){
			player.setViewDegree(45);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_DOWN) && pressedKeys.contains(KeyEvent.VK_LEFT)){
			player.setViewDegree(135);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_LEFT)){
			player.setViewDegree(180);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_UP)){
			player.setViewDegree(270);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_RIGHT)){
			player.setViewDegree(0);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else if(pressedKeys.contains(KeyEvent.VK_DOWN)){
			player.setViewDegree(90);
			player.setSpeed(3);
			player.setCurrentAnimationType(animationMove);
		}else{
			player.setSpeed(0.0);
			player.setCurrentAnimationType(animationStand);
		}
	}

	@Override
	public void performClientActions(int userID, Integer[] actions) {
		GameObject player = gameObjects.get("PLAYER" + userID);
		for (Integer integer : actions) {
			switch (integer) {
			case ACTION_SHOOT:{
				GameObject ball = gameObjects.get("BALL");
				int offset = 3;
				if(player.isCloseTo(ball, offset)){
					ball.setViewDegree(player.getViewDegree());
					ball.setSpeed(ballSpeedShoot);
					ball.setCurrentAnimationType(animationMove);
				}
				break;
				}
			default:
				break;
			}
		}
	}

	@Override
	protected void endMatch() {
		running = false;
	}
}