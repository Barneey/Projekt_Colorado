package client;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class ContentPanel extends JPanel{
	
	private JRootPane rootContent;
	private JPanel contentPanelContent;
	
	public ContentPanel(){
		super();
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rootContent = new JRootPane();
		add(rootContent);
		createContent();
		setContent(contentPanelContent);
	}
	
	private void createContent(){
		if(contentPanelContent == null){
			contentPanelContent = new JPanel();
		}
		// TODO Create Method
		contentPanelContent.add(new JLabel("Dummy"));
	}
	
	
	public void setContent(JPanel content){
		if(content != null){
			rootContent.setContentPane(content);
			revalidate();
		}
	}
}