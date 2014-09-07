package client;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;



public class JGCButton extends JButton{

	private static ArrayList<JGCButton> allObjects;
	
	private static GUIImageLoader imgLdr = new GUIImageLoader();

	public JGCButton(String text){
		super(text);
		if(allObjects == null){
			allObjects = new ArrayList<>();
		}
		allObjects.add(this);
	}
	
	public JGCButton[] getAllObjects(){
		return allObjects.toArray(new JGCButton[0]);
	}
	
	public void setLayout(int layoutType){
		switch (layoutType) {
		case LookManager.GREEN_LAYOUT:
			Dimension size = (getSize().height == 0 ? getPreferredSize() : getSize());
			setIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(GUIImageLoader.GREEN_BUTTON_ACTIVE), size)));
			setRolloverIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(GUIImageLoader.GREEN_BUTTON_HOVER), size)));
			setPressedIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(GUIImageLoader.GREEN_BUTTON_PRESSED), size)));
			setDisabledIcon(new ImageIcon(imgLdr.scaleBufferedImage(imgLdr.loadBufferedImage(GUIImageLoader.GREEN_BUTTON_INACTIVE), size)));
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