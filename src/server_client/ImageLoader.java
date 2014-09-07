package server_client;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageLoader {

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