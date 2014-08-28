package client;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageLoader {

	public static final String RCOINS = "images/menu/rCoins.png";
	public static final String VCOINS = "images/menu/vCoins.png";
	public static final String REFRESH_BUTTON_INAKTIVE = "images/menu/refreshButton_inaktive.png";
	public static final String REFRESH_BUTTON_AKTIVE = "images/menu/refreshButton_aktive.gif";
	
	
	public ImageLoader(){
		
	}
	
	

	public BufferedImage loadBufferedImage(String image){
		try {
			return ImageIO.read(new File(image));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Image loadImage(String image){
		try {
			return Toolkit.getDefaultToolkit().createImage(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Image scaleImage(BufferedImage image, Dimension size){
		int imageWidth = image.getWidth();
		double scalingFactorWidth = (double)size.getWidth() / imageWidth;
		int imageHeight = image.getHeight();
		double scalingFactorHeight = (double)size.getHeight() / imageHeight;
		BufferedImage imageScaled = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scalingFactorWidth, scalingFactorHeight);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(image, imageScaled);
	}
	
	public Image loadMainFrameOverlay(){
		try {
			return ImageIO.read(new File("mainframeOverlay.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Icon loadButtonIcon(int layoutType) {
		try {
			return new ImageIcon(ImageIO.read(new File("images/layouts/green/button/jbutton_active.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Icon loadButtonRolloverIcon(int layoutType) {
		try {
			return new ImageIcon(ImageIO.read(new File("images/layouts/green/button/jbutton_hover.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Icon loadButtonPressedIcon(int layoutType) {
		try {
			return new ImageIcon(ImageIO.read(new File("images/layouts/green/button/jbutton_inactive.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Icon loadButtonDisabledIcon(int layoutType) {
		try {
			return new ImageIcon(ImageIO.read(new File("images/layouts/green/button/jbutton_pressed.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}