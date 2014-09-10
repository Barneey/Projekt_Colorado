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
		if(scalingFactorHeight == 1.0 && scalingFactorWidth == 1.0){
			return image;
		}
		AffineTransform at = new AffineTransform();
		at.scale(scalingFactorWidth, scalingFactorHeight);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(image, null);
	}
	
	public BufferedImage rotateBufferedImage(BufferedImage bf, int rotateDegree) {
		double rotationRequired = Math.toRadians(rotateDegree);
		double locationX = bf.getWidth() / 2;
		double locationY = bf.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(bf, null);
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