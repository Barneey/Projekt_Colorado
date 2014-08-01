package client;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	private static ImageLoader instance;
	
	private ImageLoader(){
		
	}
	
	public static ImageLoader getInstance(){
		if(instance == null){
			instance = new ImageLoader();
		}
		return instance;
	}

	public BufferedImage loadRCoins(){
		try {
			return ImageIO.read(new File("rCoins.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage loadVCoins(){
		try {
			return ImageIO.read(new File("vCoins.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage loadRefreshButtonInactive(){
		try {
			return ImageIO.read(new File("refreshButton_inaktive.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Image loadRefreshButtonActive(){
		try {
			return Toolkit.getDefaultToolkit().createImage("refreshButton_aktive.gif");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}