package server_client.matches;

import java.awt.Point;
import java.io.Serializable;

public class GameObjectInformation implements Serializable{

	private Point location;
	private int viewDegree;
	private double speed;

	public GameObjectInformation(int x, int y, int viewDegree){
		this.location = new Point(x, y);
		this.viewDegree = viewDegree;
	}
	
	public Point getLocation() {
		return location;
	}
	public void setLocation(int x, int y) {
		this.location.setLocation(x, y);
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
}