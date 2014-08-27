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
	
	
	public ImageLoader(){
		
	}
	
	private Image scaleImage(Image image, Dimension size){
		int imageWidth = biHeader.getWidth();
		double scalingFactorWidth = (double)this.getWidth() / imageWidth;
		int imageHeight = biHeader.getHeight();
		double scalingFactorHeight = (double)this.getHeight() / imageHeight;
		biHeaderScaled = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scalingFactorWidth, scalingFactorHeight);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		biHeaderScaled = scaleOp.filter(biHeader, biHeaderScaled);
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