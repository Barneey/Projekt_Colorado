package client;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ContentPanel extends JPanel{
	
	JPanel content;
	
	public ContentPanel(){
		super();
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		createContent();
		add(content);
		
	}
	
	private void createContent(){
		content = new JPanel();
		content.add(new JLabel("dasdsd"));
	}
	
	public void setContent(JPanel content){
		this.content = content;
	}
}