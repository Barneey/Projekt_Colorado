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

import org.apache.derby.tools.sysinfo;

public class ImageLoader {

	public static final String RCOINS = "images/menu/rCoins.png";
	public static final String VCOINS = "images/menu/vCoins.png";
	public static final String REFRESH_BUTTON_INAKTIVE = "images/menu/refreshButton_inaktive.png";
	public static final String REFRESH_BUTTON_AKTIVE = "images/menu/refreshButton_aktive.gif";
	public static final String GREEM_MAINFRAME_OVERLAY = "images/layouts/green/mainframeOverlay.png";
	public static final String GREEN_BUTTON_ACTIVE = "images/layouts/green/button/jbutton_active.png";
	public static final String GREEN_BUTTON_HOVER = "images/layouts/green/button/jbutton_hover.png";
	public static final String GREEN_BUTTON_INACTIVE = "images/layouts/green/button/jbutton_inactive.png";
	public static final String GREEN_BUTTON_PRESSED = "images/layouts/green/button/jbutton_pressed.png";
	public static final String GREEN_MAINFRAME_HEADER = "images/layouts/green/mainFrameHeader.png";
	
	public BufferedImage loadBufferedImage(String image){
		try {
			return ImageIO.read(new File(image));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	

	public BufferedImage scaleBufferedImage(BufferedImage image, Dimension size){
		int imageWidth = image.getWidth();
		double scalingFactorWidth = (double)size.getWidth() / imageWidth;
		int imageHeight = image.getHeight();
		double scalingFactorHeight = (double)size.getHeight() / imageHeight;
		AffineTransform at = new AffineTransform();
		at.scale(scalingFactorWidth, scalingFactorHeight);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(image, null);
	}
	
	public Image loadImage(String image){
		try {
			return Toolkit.getDefaultToolkit().createImage(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ImageIcon loadIcon(String image){
		try {
			return new ImageIcon(ImageIO.read(new File(image)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}