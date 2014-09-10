package server_client.matches;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import server_client.matches.soccer.SoccerImageLoader;

public class GameObject implements Serializable{
	
	private Point location;
	private int viewDegree;
	private double speed;
	private Dimension size;
	private transient HashMap<String, BufferedImage[]> animations;
	private String currentAnimation;
	private int animationCounter;
	
	public GameObject(int x, int y, Dimension size){
		this.location = new Point(x, y);
		this.setSize(size);
		this.viewDegree = 0;
		this.setSpeed(0.0);
		this.setCurrentAnimation("");
		this.animationCounter = 0;
	}
	
	public void setObjectData(GameObject o){
		this.location = o.getLocation();
		this.viewDegree = o.getViewDegree();
		this.speed = o.getSpeed();
		this.animationCounter = o.getAnimationCounter();
	}
	
	public void setLocation(int x, int y){
		location.setLocation(x, y);
	}
	
	public Point getLocation(){
		return this.location;
	}
	
	public int getX(){
		return this.location.x;
	}
	
	public int getY(){
		return this.location.y;
	}

	public int getViewDegree() {
		return viewDegree;
	}

	public void setViewDegree(int viewDegree) {
		int difference = this.viewDegree - viewDegree;
		if(difference != 0){
			rotateImages(difference);
		}
		this.viewDegree = viewDegree;
	}
	
	public void rotateImages(int rotateDegree){
		Iterator<Entry<String, BufferedImage[]>> it = animations.entrySet().iterator();
		SoccerImageLoader sImgLdr = new SoccerImageLoader();
		while(it.hasNext()){
			Entry<String, BufferedImage[]> entry = it.next();
			for (BufferedImage bf : entry.getValue()) {
				bf = sImgLdr.rotateBufferedImage(bf, rotateDegree);
			}
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
//	
//	
//	public void addAnimation(String name, ){
//		
//	}


	public int getAnimationCounter() {
		return animationCounter;
	}

	public void setAnimationCounter(int animationCounter) {
		this.animationCounter = animationCounter;
	}

	public String getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(String currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}
}