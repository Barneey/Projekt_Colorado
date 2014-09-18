package server_client.matches;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import server_client.matches.soccer.SoccerImageLoader;

public class GameObject implements Serializable{
	
	private GameObjectInformation information;
	private Dimension size;
	private Hashtable<String, BufferedImage[]> animations;
	private int animationCounter;
	private int animationCounterMax;
	private transient Image currentImage;
	
	public GameObject(int x, int y, Dimension size){
		this.information = new GameObjectInformation(x, y, 0);
		this.setSize(size);
		this.setSpeed(0.0);
		this.animations = new Hashtable<>();
		this.animationCounter = 0;
		this.animationCounterMax = 100;
		this.currentImage = null;
	}
	
	public void setGameInformation(GameObjectInformation goi){
		this.information = goi;
	}
	
	public void setLocation(int x, int y){
		information.setLocation(x, y);
	}
	
	public Point getLocation(){
		return information.getLocation();
	}
	
	public int getX(){
		return this.information.getLocation().x;
	}
	
	public int getY(){
		return this.information.getLocation().y;
	}

	public int getViewDegree() {
		return this.information.getViewDegree();
	}

	public void setViewDegree(int viewDegree) {
		int difference = viewDegree - this.information.getViewDegree();
		if(difference != 0){
			rotateImages(difference);
		}
		this.information.setViewDegree(viewDegree);
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
		return information.getSpeed();
	}

	public void setSpeed(double speed) {
		this.information.setSpeed(speed);
	}

	public int getAnimationCounter() {
		return animationCounter;
	}

	public void setAnimationCounter(int animationCounter) {
		this.animationCounter = animationCounter;
	}

	public String getCurrentAnimationType() {
		return this.information.getCurrentAnimationType();
	}

	public void setCurrentAnimationType(String currentAnimationType) {
		this.information.setCurrentAnimationType(currentAnimationType);
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public synchronized void addAnimation(String string, BufferedImage[] images) {
		if(this.getCurrentAnimationType().equals("")){
			this.setCurrentAnimationType(string);
		}
		if(this.animations == null){
			this.animations = new Hashtable<>();
		}
		if(images == null){
			images = new BufferedImage[0];
		}
		this.animations.put(string, images);
	}

	public void setCurrentImage(BufferedImage image){
		this.currentImage = image;
	}
	
	public Image getCurrentImage() {
		BufferedImage[] images = null;
		if((images = this.animations.get(this.information.getCurrentAnimationType())) == null || images.length == 0){
			return currentImage;
		}
		currentImage = images[(int)Math.floor(images.length / (double)this.animationCounterMax * this.animationCounter)];
		return currentImage;
	}

	public boolean correspondsWith(GameObject go){
		return (this.getX() + this.size.width) >= (go.getX()) &&
				(this.getX() <= (go.getX() + go.getSize().width)) &&
				(this.getY() + this.size.height) >= (go.getY()) &&
				(this.getY()) <= (go.getY() + go.getSize().height);
	}
	
	public void animate() {
		information.setLocation(information.getSpeed() * Math.cos(Math.toRadians(information.getViewDegree())) + getX(), information.getSpeed() * Math.sin(Math.toRadians(this.information.getViewDegree())) + getY());
		calculateSpeed();
		this.animationCounter++;
		if(this.animationCounter >= this.animationCounterMax){
			this.animationCounter = 0;
		}
	}
	
	private void calculateSpeed(){
		this.information.setSpeed(this.information.getSpeed() - this.information.getSpeedReduction());
		if(this.information.getSpeed() < 0.0){
			this.information.setSpeed(0.0);
		}
	}

	public void setSpeedReduction(double reduction){
		this.information.setSpeedReduction(reduction);
	}
	
	public int getAnimationCounterMax() {
		return animationCounterMax;
	}

	public void setAnimationCounterMax(int animationCounterMax) {
		this.animationCounterMax = animationCounterMax;
	}

	public GameObjectInformation getInformation() {
		return this.information;
	}
}