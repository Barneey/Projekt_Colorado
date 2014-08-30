package client;

import javax.swing.JButton;



public class JGCButton extends JButton{

	private static ImageLoader imgLdr = new ImageLoader();

	public JGCButton(String text){
		super(text);
		setLayout(1);
	}
	
	public void setLayout(int layoutType){
		setIcon(imgLdr.loadIcon(ImageLoader.GREEN_BUTTON_ACTIVE));
		setRolloverIcon(imgLdr.loadIcon(ImageLoader.GREEN_BUTTON_HOVER));
		setPressedIcon(imgLdr.loadIcon(ImageLoader.GREEN_BUTTON_PRESSED));
		setDisabledIcon(imgLdr.loadIcon(ImageLoader.GREEN_BUTTON_INACTIVE));
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
	}
	// TODO IMAGE LOADER GETS METHODS TO SCALE IMAGES
	// TODO SCALE IMAGES WHEN PREFFERED SIZE IS CHANGED etc
}