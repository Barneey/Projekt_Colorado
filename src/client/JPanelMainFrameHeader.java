package client;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class JPanelMainFrameHeader extends JPanel{
	
	private BufferedImage biHeader;
	private BufferedImage biHeaderScaled;
	
	public JPanelMainFrameHeader() {
		super();
		try {
			biHeader = ImageIO.read(new File("mainFrameHeader.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void scaleImage(){
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
	
   @Override
	protected void paintComponent(Graphics g) {
		scaleImage();
		super.paintComponent(g);
		g.drawImage(biHeaderScaled, 0, 0, null);        
	}

}
