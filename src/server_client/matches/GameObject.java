package server_client.matches;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import server_client.matches.soccer.SoccerImageLoader;

public class GameObject implements Serializable{
	
	private Point location;
	private int viewDegree;
	private double speed;
	private Dimension size;
	private Hashtable<String, BufferedImage[]> animations;
	private String currentAnimationType;
	private int animationCounter;
	private int animationCounterMax;
	private transient Image currentImage;
	
	public GameObject(int x, int y, Dimension size){
		this.location = new Point(x, y);
		this.setSize(size);
		this.viewDegree = 0;
		this.setSpeed(0.0);
		this.animations = new Hashtable<>();
		this.currentAnimationType = "";
		this.animationCounter = 0;
		this.animationCounterMax = 100;
		this.currentImage = null;
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
		int difference = viewDegree - this.viewDegree;
		if(difference != 0){
			rotateImages(difference);
		}
		this.viewDegree = viewDegree;
	}
	
	private void rotateImages(int rotateDegree){
		Hashtable<String, BufferedImage[]> copyAnimations = new Hashtable<>();
		Iterator<Entry<String, BufferedImage[]>> it = animations.entrySet().iterator();
		SoccerImageLoader sImgLdr = new SoccerImageLoader();
		while(it.hasNext()){
			Entry<String, BufferedImage[]> entry = it.next();
			ArrayList<BufferedImage> tempImages = new ArrayList<>();
			for (BufferedImage bf : entry.getValue()) {
				BufferedImage tempImage = sImgLdr.rotateBufferedImage(bf, rotateDegree);
				tempImages.add(tempImage);
			}
			copyAnimations.put(entry.getKey(), tempImages.toArray(new BufferedImage[0]));
		}
		animations = copyAnimations;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getAnimationCounter() {
		return animationCounter;
	}

	public void setAnimationCounter(int animationCounter) {
		this.animationCounter = animationCounter;
	}

	public String getCurrentAnimationType() {
		return currentAnimationType;
	}

	public void setCurrentAnimationType(String currentAnimationType) {
		this.currentAnimationType = currentAnimationType;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public synchronized void addAnimation(String string, BufferedImage[] images) {
		if(this.currentAnimationType.equals("")){
			this.currentAnimationType = string;
		}
		if(this.animations == null){
			this.animations = new Hashtable<>();
		}
		if(images == null){
			images = new BufferedImage[0];
		}
		this.animations.put(string, images);
	}

	public Image getCurrentImage() {
		BufferedImage[] images = this.animations.get(currentAnimationType);
		if((images = this.animations.get(currentAnimationType)) == null || images.length == 0){
			return currentImage;
		}
		currentImage = images[(int)Math.floor(images.length / (double)this.animationCounterMax * this.animationCounter)];
		return currentImage;
	}

	public void animate() {
		location.setLocation(speed * Math.cos(Math.toRadians(viewDegree)) + location.x, speed * Math.sin(Math.toRadians(viewDegree)) + location.y);
		this.animationCounter++;
		if(this.animationCounter >= this.animationCounterMax){
			this.animationCounter = 0;
		}
	}

	public int getAnimationCounterMax() {
		return animationCounterMax;
	}

	public void setAnimationCounterMax(int animationCounterMax) {
		this.animationCounterMax = animationCounterMax;
	}
}