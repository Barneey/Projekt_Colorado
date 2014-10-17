package server_client.matches.horserace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

import client.GameConnection;
import server_client.Playmode;
import server_client.Team;
import server_client.User;
import server_client.matches.GameObject;
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
	private int playerAnimationTimer;
	private transient BufferedImage[] backgroundStand;
	private transient BufferedImage[] backgroundStandOpen;
	private transient BufferedImage[] horseStand;
	private transient BufferedImage[] horseMove;

	private final static int GAMEINTERVAL = 40;
	
	
	public HorseRaceMatch(int matchType, Playmode playmode) {
		super(matchType, playmode);
		
		this.animationStand = "STAND";
		this.animationStandOpen = "STAND_OPEN";
		this.animationMove = "MOVE";
		
		gameObjects.put("BACKGROUND", new GameObject(0, 0, this.getSize()));
		gameObjects.put("GOALLINE", new GameObject(655, 0, new Dimension(100, this.getHeight())));
		int playerCounter = 0;
		for (Team team : playmode.getTeams()) {
			for (User user : team.getUser()) {
				gameObjects.put("PLAYER" + user.getID(), new GameObject(5, playerCounter * 40 + 25, new Dimension(40,15)));
				playerCounter++;
			}
		}
		imagesLoaded = false;
	}

	@Override
	public void run() {
		showingGameInfo = true;
		repaint();
		running = true;
//		int counter = 0;
		while(running){
			try {
//				counter++;
//				if(counter >= 10){
//					counter = 0;
//					renewImages();
//				}
				Thread.sleep(GAMEINTERVAL);
				updateGameObjects();
//				if(skipRepaintingCounter > 0){
//					skipRepaintingCounter--;
//				}else{
					repaint();
//				}
				requestFocusInWindow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		try {
//			this.scoreList = GameConnection.getInstance().getScoreList(gameID, userID); 
//			this.showingScore = true;
//			this.onlyDisplayScore = false;
//			while(showingScoreCounter < showingScoreMax){
//				Thread.sleep(GAMEINTERVAL);
//				repaint();
//				this.showingScoreCounter++;
//			}
//			this.onlyDisplayScore = true;
//			for(int i = 0; i < 50; i++){
//				Thread.sleep(GAMEINTERVAL);
//				repaint();
//			}
//			this.showingScore = false;
//			this.over = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics g) {
		if(imagesLoaded){
			loadImages();
		}
		// Clear screen
		offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
		// Draw background
		drawGameObject(gameObjects.get("BACKGROUND"));
		if(showingGameInfo){
			if(showingGameInfoCounter < showingGameInfoMax){
				showingGameInfoCounter++;
				drawString("Use the Arrow-Keys and Space to move the ball into the opponent goal", Color.YELLOW, new Font(Font.SANS_SERIF, Font.BOLD, 12), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 100);
			}
			if(showingGameInfoCounter > showingGameInfoMax){
				// Draw countdown
			}
		}
		// Draw score
		if(showingScore){
			Score[] currentScore = scoreList.getOrderedScore();
			for (int i = 0; i < currentScore.length; i++) {
				drawString(i+1 + ". " + currentScore[i].toString(), Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 15), VERTICAL_ALIGN_CENTER, NO_ALIGN, 0, 20*i+100);
			}
			if(!onlyDisplayScore){
				scoreList.calculateNewScores(showingScoreCounter, showingScoreMax);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void executeGameEvents(Integer[] events) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void startUpdating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Integer[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void performClientActions(int userID, Integer[] actions) {
		// TODO Auto-generated method stub
		
	}

}
