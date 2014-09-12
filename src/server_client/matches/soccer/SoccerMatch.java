package server_client.matches.soccer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObject;
import server_client.matches.Match;


public class SoccerMatch extends Match{
	
	private SoccerImageLoader sImgLdr;
//	private BufferedImage imgBackground;
//	private BufferedImage[] imgBalls;
//	private BufferedImage[] imgPlayers;
	private boolean imagesLoaded;
	private int[] score;
	private Dimension fieldSize;
	private Point fieldStart;
	
	public SoccerMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		int teamNumber = this.playmode.getTeams().length;
		fieldSize = new Dimension(626, 307);
		fieldStart = new Point(47, 49);
		gameObjects.put("BALL", new GameObject(fieldStart.x + fieldSize.width / 2 - (20/2), fieldStart.y + fieldSize.height / 2 - (20/2), new Dimension(20,20)));
		gameObjects.put("BACKGROUND", new GameObject(0, 0, new Dimension(getWidth(), getHeight())));
		// Check playmode and create Objects
		if(playmode.getTitel().equals("TEAM")){
			User[] user = playmode.getTeams()[0].getUser();
			int xOffset = fieldSize.width / 30;
			int yOffset = fieldSize.height / ((user.length + 1) / 2);
			for(int i = 0; i < user.length; i++){
				gameObjects.put("PLAYER" + user[i].getID(), new GameObject(fieldStart.x + xOffset + (i / 2 == 0 ? xOffset : 0), fieldStart.y + yOffset + yOffset * (i/2), new Dimension(30,30)));
			}
			user = playmode.getTeams()[1].getUser();
			for(int i = 0; i < user.length; i++){
				gameObjects.put("PLAYER" + user[i].getID(), new GameObject(fieldStart.x + fieldSize.width - (xOffset + (i / 2 == 0 ? xOffset : 0)), fieldStart.y + fieldSize.height - (yOffset + yOffset * (i/2)), new Dimension(30,30)));
			}			
		}
		if(playmode.getTitel().equals("Test")){
			for (Team team : playmode.getTeams()) {
				for (User user : team.getUser()) {
					gameObjects.put("PLAYER" + user.getID(), new GameObject(fieldStart.x + fieldSize.width / 2, fieldStart.y + fieldSize.height / 2, new Dimension(30,30)));
				}
			}
		}
		imagesLoaded = false;
		score = new int[teamNumber];
		for (int i = 0; i < score.length; i++) {
			score[i]=0;
		}
	}

	public void loadImages(){
		if(sImgLdr == null){
			sImgLdr = new SoccerImageLoader();
		}
		GameObject ball = gameObjects.get("BALL");
		BufferedImage[] ballStand = {(sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BALLS[0]), ball.getSize()))};
		ball.addAnimation("STAND", ballStand);
		
		GameObject background = gameObjects.get("BACKGROUND");
		BufferedImage[] backgroundStand = {(sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BACKGROUND), background.getSize()))};
		background.addAnimation("STAND", backgroundStand);
		
		
		boolean firstRun = true;
		BufferedImage[] playerStand = new BufferedImage[1];;
		BufferedImage[] playerMove = new BufferedImage[0];
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				GameObject playerObject = gameObjects.get("PLAYER" + user.getID());
				if(firstRun){
					firstRun = false;
					playerStand[0] = (sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYER_STAND), playerObject.getSize()));
//					playerMove[0] = 
				}
				playerObject.addAnimation("STAND", playerStand);
				playerObject.addAnimation("MOVE", playerMove);
			}
		}
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		imagesLoaded = true;
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
				drawGameObject(gameObjects.get("PLAYER" + user.getID()));
			}
		}
		drawString(String.valueOf(score[0]), Color.RED, new Font(Font.SANS_SERIF, Font.PLAIN, 20), new Point(getWidth() / 2 - 120, 40));
		g.drawImage(offscreen, 0, 0, this);
	}

	protected void showGameInfo(){
		repaint();
	}
	
	@Override
	public void run() {
		showGameInfo();
		while(true){
			try {
				Thread.sleep(40);
				score[0] = score[0] + 1;
				GameObject player = gameObjects.get("PLAYER" + playmode.getTeams()[0].getUser()[0].getID());
				player.setLocation(player.getX()+1, player.getY());
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}