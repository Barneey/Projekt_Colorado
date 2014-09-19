package server_client.matches;

import java.awt.Point;
import java.io.Serializable;

public class GameObjectInformation implements Serializable{

	private Point location;
	private Point defaultLocation;
	private int viewDegree;
	private double speed;
	private String currentAnimationType;
	private double speedReduction;

	public GameObjectInformation(int x, int y, int viewDegree){
		this.location = new Point(x, y);
		this.defaultLocation = new Point(x, y);
		this.viewDegree = viewDegree;
		this.currentAnimationType = "";
		this.speedReduction = 0.0;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(int x, int y) {
		this.location.setLocation(x, y);
	}
	
	public void resetLocation(){
		this.setLocation(defaultLocation.x, defaultLocation.y);
	}
	
	public void setLocation(double x, double y){
		this.location.setLocation(x, y);
	}
	
	public int getViewDegree() {
		return viewDegree;
	}
	public void setViewDegree(int viewDegree) {
		this.viewDegree = viewDegree;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getCurrentAnimationType() {
		return currentAnimationType;
	}

	public void setCurrentAnimationType(String currentAnimationType) {
		this.currentAnimationType = currentAnimationType;
	}

	public double getSpeedReduction() {
		return speedReduction;
	}

	public void setSpeedReduction(double speedReduction) {
		this.speedReduction = speedReduction;
	}
}