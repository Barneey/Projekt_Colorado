package server_client.matches.soccer;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import client.GameConnection;
import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObject;
import server_client.matches.GameObjectInformation;
import server_client.matches.Match;


public class SoccerMatch extends Match{
	
	private SoccerImageLoader sImgLdr;
	private boolean imagesLoaded;
	private int[] score;
	private Dimension fieldSize;
	private Point fieldStart;
	private String animationStand;
	private String animationMove;
	private GameObject player;
	private Set<Integer> pressedKeys;
	private int playerAnimationTimer;
	private transient BufferedImage[] ballStand;
	private transient BufferedImage[] ballMove;
	private transient BufferedImage[] backgroundStand;
	private transient BufferedImage[] playerStand;
	private transient BufferedImage[] playerMove;
	
	public SoccerMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		this.pressedKeys = new HashSet<>();
		this.userID = -1;
		this.playerAnimationTimer = 6;
		this.animationStand = "STAND";
		this.animationMove = "MOVE";
		this.fieldSize = new Dimension(626, 307);
		this.fieldStart = new Point(47, 49);
		GameObject ball = new GameObject(fieldStart.x + fieldSize.width / 2 - (20/2), fieldStart.y + fieldSize.height / 2 - (20/2), new Dimension(20,20));
		ball.setSpeedReduction(0.1);
		this.gameObjects.put("BALL", ball);
		this.gameObjects.put("BACKGROUND", new GameObject(0, 0, new Dimension(getWidth(), getHeight())));
		// Check playmode and create Objects
		if(playmode.getTitel().equals("TEAM")){
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
		}
		if(playmode.getTitel().equals("Test")){
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					GameObject newPlayer = new GameObject(fieldStart.x + fieldSize.width / 2, fieldStart.y + fieldSize.height / 2, new Dimension(21,21));
					newPlayer.setAnimationCounterMax(playerAnimationTimer);
					gameObjects.put("PLAYER" + user.getID(), newPlayer);
				}
			}
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
		// Draw components
		drawGameObject(gameObjects.get("BACKGROUND"));
		drawGameObject(gameObjects.get("BALL"));
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject player = gameObjects.get("PLAYER" + user.getID());
				drawGameObject(player);
				drawString((user.getNick().length() >= 10 ? user.getNick().substring(0, 9) : user.getNick()), Color.CYAN, new Font(Font.SERIF, Font.PLAIN, 10), new Point(player.getX(), player.getY() - 5));
			}
		}
		drawString(score[0] + ":"+score[1], Color.RED, new Font(Font.SANS_SERIF, Font.PLAIN, 20), new Point(getWidth() / 2 - 14, 40));
		g.drawImage(offscreen, 0, 0, this);
	}

	protected void showGameInfo(){
		repaint();
	}
	
	protected void updateGameObjects(){
		player = gameObjects.get("PLAYER" + userID);
		HashMap<String, GameObjectInformation> clientPlayerObjects = new HashMap<>();
		clientPlayerObjects.put("PLAYER" + userID, player.getInformation());
		try {
			clientPlayerObjects = GameConnection.getInstance().updateGameObjects(clientPlayerObjects, gameID);
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
		GameObject ball = gameObjects.get("BALL");
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject animatingPlayer = gameObjects.get("PLAYER" + user.getID());
				animateGameObject(animatingPlayer);
				if(animatingPlayer.correspondsWith(ball)){
					ball.setViewDegree(animatingPlayer.getViewDegree());
					ball.setSpeed(6);
					ball.setCurrentAnimationType(animationMove);
				}
			}
		}
		animateGameObject(ball);
		if(ball.getSpeed() <= 0.0){
			ball.setCurrentAnimationType(animationStand);
		}
	}
	
	@Override
	public void run() {
		showGameInfo();
		int counter = 0;
		while(true){
			try {
				counter++;
				if(counter >= 10){
					counter = 0;
					renewImages();
				}
//				renewImages();
				Thread.sleep(40);
				updateGameObjects();
				repaint();
				requestFocusInWindow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
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
		// TODO Auto-generated method stub
		
	}
	
	private void setMovementForPlayer(){
		if(player == null){
			player = gameObjects.get("PLAYER" + userID);
		}
		if(pressedKeys.size() == 0){
			player.setSpeed(0);
			player.setCurrentAnimationType(animationStand);
		}
		if(pressedKeys.size() == 1){
			if(pressedKeys.contains(KeyEvent.VK_LEFT)){
				player.setViewDegree(180);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_UP)){
				player.setViewDegree(270);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_RIGHT)){
				player.setViewDegree(0);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_DOWN)){
				player.setViewDegree(90);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
		}
		if(pressedKeys.size() == 2){
			if(pressedKeys.contains(KeyEvent.VK_LEFT) && pressedKeys.contains(KeyEvent.VK_UP)){
				player.setViewDegree(225);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_UP) && pressedKeys.contains(KeyEvent.VK_RIGHT)){
				player.setViewDegree(315);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_RIGHT) && pressedKeys.contains(KeyEvent.VK_DOWN)){
				player.setViewDegree(45);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
			if(pressedKeys.contains(KeyEvent.VK_DOWN) && pressedKeys.contains(KeyEvent.VK_LEFT)){
				player.setViewDegree(135);
				player.setSpeed(3);
				player.setCurrentAnimationType(animationMove);
			}
		}
	}
}