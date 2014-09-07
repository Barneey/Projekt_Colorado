package server_client.matches.soccer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import server_client.matches.GameObject;
import server_client.matches.Match;


public class SoccerMatch extends Match{
	
	private SoccerImageLoader sImgLdr;
	private BufferedImage imgBackground;
	private BufferedImage[] imgBalls;
	private boolean imagesLoaded;
	
	public SoccerMatch(int matchType) {
		super(matchType);
//		Creating the gameObjects
		gameObjects.put("BALL", new GameObject(100, 100));
		gameObjects.put("BACKGROUND", new GameObject(0, 0));
		imagesLoaded = false;
	}

	public void loadImages(){
		if(sImgLdr == null){
			sImgLdr = new SoccerImageLoader();
		}
//		imgBall = sImgLdr.loadBufferedImage(SoccerImageLoader.BALL);
		imgBalls = new BufferedImage[SoccerImageLoader.BALLS.length];
		for (int i = 0; i < SoccerImageLoader.BALLS.length; i++) {
			imgBalls[i] = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BALLS[i]), new Dimension(20,20));
		}
		imgBackground = sImgLdr.scaleBufferedImage(sImgLdr.loadBufferedImage(SoccerImageLoader.BACKGROUND), getSize());
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
		drawGameObject(gameObjects.get("BACKGROUND"), imgBackground);
		drawGameObject(gameObjects.get("BALL"), imgBalls[0]);
		g.drawImage(offscreen, 0, 0, this);
	}
	
	private void drawGameObject(GameObject gameObject, Image image){
		if(gameObject != null && image != null){
			offscreenGraphics.drawImage(image, gameObject.getX(), gameObject.getY(), this);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}