package server_client.matches;

import java.awt.Point;

public class GameObject {
	
	private Point location;
//	private String imagePath;
//	private HashMap<String, Boolean> conditions;

	public GameObject(int x, int y){
		this.location = new Point(x, y);
//		this.imagePath = imagePath;
	}
	
//	public void setImagePath(String imagePath){
//		this.imagePath = imagePath;
//	}
//	
//	public String getImagePath(){
//		return this.imagePath;
//	}
	
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
}