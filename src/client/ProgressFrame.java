package client;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressFrame extends JFrame{
	
	private JLabel progressLabel;
	private JProgressBar progressBar;
	
	public ProgressFrame(){
		setLayout(null);
		
		progressLabel = new JLabel();
		progressLabel.setSize(100, 25);
		progressLabel.setLocation(5, 5);
		add(progressLabel);
		
		progressBar = new JProgressBar();
		progressBar.setSize(100, 25);
		progressBar.setLocation(5, 35);
		add(progressBar);
		
		setSize(new Dimension(400, 125));
		Point frameLocation = MouseInfo.getPointerInfo().getLocation(); 
		frameLocation.setLocation(frameLocation.getX() - (this.getWidth() / 2), frameLocation.getY() - (this.getHeight() / 2));
		setLocation(frameLocation);
		setResizable(false);
		setVisible(true);
	}
	
	public void setText(String text){
		progressLabel.setText(text);
	}
	
	public void setMinimum(int n){
		progressBar.setMinimum(n);
	}
	
	public void setMaximum(int m){
		progressBar.setMaximum(m);
	}
	
	public void setContent(String text, int value){
		setText(text);
		setValue(value);
	}
	
	public void setValue(int value){
		progressBar.setValue(value);
	}
}