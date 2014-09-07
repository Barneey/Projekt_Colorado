package server_client.matches.soccer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import server_client.matches.GameObject;
import server_client.matches.Match;


public class SoccerMatch extends Match{
	
	private SoccerImageLoader sImgLdr;
	private BufferedImage imgBall;
	private boolean imagesLoaded;
	
	public SoccerMatch(int matchType) {
		super(matchType);
		// Position of the ball at gamestart
		gameObjects.put("BALL", new GameObject(100, 100));
		imagesLoaded = false;
	}

	public void loadImages(){
		if(sImgLdr == null){
			sImgLdr = new SoccerImageLoader();
		}
		imgBall = sImgLdr.loadBufferedImage(SoccerImageLoader.BALL);
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
		GameObject ball = gameObjects.get("BALL");
		offscreenGraphics.drawImage(imgBall, ball.getX(), ball.getY(), null);
		g.drawImage(offscreen, 0, 0, this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}