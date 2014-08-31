package client;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;



public class JGCButton extends JButton{

	private static ImageLoader imgLdr = new ImageLoader();

	public JGCButton(String text){
		super(text);
	}
	
	public void setLayout(int layoutType){
		switch (layoutType) {
		case LookManager.GREEN_LAYOUT:
			setIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(ImageLoader.GREEN_BUTTON_ACTIVE), getPreferredSize())));
			setRolloverIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(ImageLoader.GREEN_BUTTON_HOVER), getPreferredSize())));
			setPressedIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(ImageLoader.GREEN_BUTTON_PRESSED), getPreferredSize())));
			setDisabledIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(ImageLoader.GREEN_BUTTON_INACTIVE), getPreferredSize())));
			break;

		default:
			break;
		}
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
	}
	
	public void setPreferredSize(Dimension preferredSize){
		super.setPreferredSize(preferredSize);
		setLayout(UserSettingsClient.getInstance().getLayoutType());
	}
}