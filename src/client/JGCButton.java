package client;

import javax.swing.JButton;



public class JGCButton extends JButton{

	private static ImageLoader imgLdr = new ImageLoader();

	public JGCButton(String text){
		super(text);
		setLayout(1);
	}
	
	public void setLayout(int layoutType){
		setIcon(imgLdr.loadButtonIcon(layoutType));
		setRolloverIcon(imgLdr.loadButtonRolloverIcon(layoutType));
		setPressedIcon(imgLdr.loadButtonPressedIcon(layoutType));
		setDisabledIcon(imgLdr.loadButtonDisabledIcon(layoutType));
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
	}
	// TODO IMAGE LOADER GETS METHODS TO SCALE IMAGES
	// TODO SCALE IMAGES WHEN PREFFERED SIZE IS CHANGED etc
}