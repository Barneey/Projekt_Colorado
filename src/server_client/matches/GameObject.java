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
import java.util.Random;

import server_client.matches.soccer.SoccerImageLoader;

public class GameObject implements Serializable{
	
	private GameObjectInformation information;
	private Dimension size;
	private Hashtable<String, BufferedImage[]> animations;
	private int animationCounter;
	private int animationCounterMax;
	private transient Image currentImage;
	public static final int X_OFFSET_LEFT = 0;
	public static final int X_OFFSET_MIDDLE = 1;
	public static final int X_OFFSET_RIGHT = 2;
	public static final int Y_OFFSET_OVER = 3;
	public static final int Y_OFFSET_MIDDLE = 4;
	public static final int Y_OFFSET_BELOW = 5;
	
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
	
	public void resetLocation(){
		this.information.resetLocation();
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
	
	public void setX(int x){
		this.information.getLocation().x = x;
	}
	
	public int getY(){
		return this.information.getLocation().y;
	}
	
	public void setY(int y){
		this.information.getLocation().y = y;
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
	
	public void animate(boolean relocate) {
		if(relocate){
			relocate();
		}
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

	public void relocate() {
		information.setLocation(information.getSpeed() * Math.cos(Math.toRadians(information.getViewDegree())) + getX(), information.getSpeed() * Math.sin(Math.toRadians(this.information.getViewDegree())) + getY());
		calculateSpeed();
	}

	public boolean isCloseTo(GameObject go, int offset) {
		return (this.getX() + this.size.width + offset) >= (go.getX()) &&
				(this.getX() - offset <= (go.getX() + go.getSize().width)) &&
				(this.getY() + this.size.height + offset) >= (go.getY()) &&
				(this.getY() - offset) <= (go.getY() + go.getSize().height);
	}

	public void outOfBoundsCorrection(GameObject background) {
		if(this.getX() < background.getX()){
			this.setX(background.getX());
		}
		if((this.getX() + this.getSize().width) > (background.getX() + background.getSize().getWidth())){
			this.setX((int)(background.getX() + background.getSize().getWidth() - this.getSize().getWidth()));
		}
		if(this.getY() < background.getY()){
			this.setY(background.getY());
		}
		if((this.getY() + this.getSize().height) > (background.getY() + background.getSize().getHeight())){
			this.setY((int)(background.getY() + background.getSize().getHeight() - this.getSize().getHeight()));
		}
	}
	
	public void relativeTo(GameObject go, int xOffset, int yOffset){
		this.setViewDegree(go.getViewDegree());
		switch (xOffset) {
		case X_OFFSET_LEFT:
			this.setX((int)(go.getLocation().getX() - this.getSize().width - 4));
			break;
		case X_OFFSET_MIDDLE:
			this.setX(go.getX());
			break;
		case X_OFFSET_RIGHT:
			this.setX((int)(go.getLocation().getX() + go.getSize().width + 4));
			break;
		default:
			break;
		}
		switch (yOffset) {
		case Y_OFFSET_OVER:
			this.setY((int)(go.getLocation().getY() - this.getSize().height - 4));
			break;
		case Y_OFFSET_MIDDLE:
			this.setY(go.getY());
			break;
		case Y_OFFSET_BELOW:
			this.setY((int)(go.getLocation().getY() + go.getSize().height + 4));
			break;
		default:
			break;
		}
	}
	
	public void positionAnywhereIn(GameObject go) {
		Random generator = new Random();
		int xOffset = generator.nextInt(go.getSize().width);
		int yOffset = generator.nextInt(go.getSize().height);
		this.setLocation(go.getX() + xOffset, go.getY() + yOffset);
	}
}