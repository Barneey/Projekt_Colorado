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
	private BufferedImage imgBackground;
//	private BufferedImage[] imgBalls;
//	private BufferedImage[] imgPlayers;
	private boolean imagesLoaded;
	private int[] score;
	private Dimension fieldSize;
	private Point fieldStart;
	
	public SoccerMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		int teamNumber = this.playmode.getTeams().length;
//		gameObjects.put("BALL", new GameObject(getWidth()/2 - 20/2, getHeight()/2 - 20/2, new Dimension(20,20)));
		gameObjects.put("BALL", new GameObject(fieldStart.x + fieldSize.width / 2, fieldStart.y + fieldSize.height / 2, new Dimension(20,20)));
		gameObjects.put("BACKGROUND", new GameObject(0, 0, new Dimension(getWidth(), getHeight())));
		fieldSize = new Dimension(626, 307);
		fieldStart = new Point(47, 49);
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
		if(playmode.getTitel().equals("TEST")){
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
		gameObjects.get("").
//		imgBalls = new BufferedImage[SoccerImageLoader.BALLS.length];
//		for (int i = 0; i < SoccerImageLoader.BALLS.length; i++) {
//			imgBalls[i] = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BALLS[i]), new Dimension(20,20));
//		}
//		imgBackground = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BACKGROUND), getSize());
//		imgPlayers = new BufferedImage[1];
//		imgPlayers[0] = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.PLAYERR0), new Dimension(30, 30));
		imagesLoaded = true;
	}
	
	@Override
	public void paint(Graphics g) {
		if(!imagesLoaded){
			loadImages();
		}
		// Clear screen
		offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		offscreenGraphics = offscreen.getGraphics();
		
		offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
		// Draw components
		drawGameObject(gameObjects.get("BACKGROUND"), imgBackground);
		drawGameObject(gameObjects.get("BALL"), imgBalls[0]);
		drawGameObject(gameObjects.get("PLAYER1"), imgPlayers[0]);
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
				Thread.sleep(20);
				GameObject player = gameObjects.get("PLAYER1");
				player.setLocation(player.getX()+1, player.getY());
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}