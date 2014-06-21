package serverUtil;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ServerStatusPanel extends JPanel{

	
	private BufferedImage redLamp;
	private BufferedImage greenLamp;
	private BufferedImage currentImage;
	
	public ServerStatusPanel(){
		super();
		try {
			redLamp = ImageIO.read(new File("redLamp.png"));
			greenLamp = ImageIO.read(new File("greenLamp.png"));
			currentImage = redLamp;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
   @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentImage, 0, 0,10, 10,  null);
   }
   
   public void setStatus(boolean status){
	   currentImage = status ? greenLamp : redLamp;
	   repaint();
   }
}