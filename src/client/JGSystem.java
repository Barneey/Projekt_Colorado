package client;
import java.awt.Color;
import java.awt.Font;
import java.sql.DatabaseMetaData;

import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class JGSystem {
	private static JGSystem instance = null;
	public static final Font FONT_SMALL = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	public static final Font FONT_CHAT = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
	public static final Font FONT_BIG = new Font(Font.SERIF, Font.ITALIC, 22);
	public static final Font FONT_ERROR = new Font(Font.SERIF, Font.BOLD, 14);
	public static final Font FONT_WARN = new Font(Font.SERIF, Font.PLAIN, 13);
	public static final Color COLOR_TEXT = new Color(0.2f, 0.2f, 0.2f);
	public static final Color COLOR_ERROR = new Color(1.0f, 0.0f, 0.0f);
	public static final Color COLOR_WARN = new Color(0.7f, 0.3f, 0.0f);
	public static final String VERSION = "0.01 Alpha";
	public static final String NAME = "JGame Collection";

	protected JGSystem() {

	}

	public void exit(User user){
		 UserSettingsClient.getInstance().saveUserSettings();
		 DatabaseConnection.getInstance().leaveAllChannels(user);
		 try {
			 GameConnection.getInstance().leaveQueues(user);
			 GameConnection.getInstance().leaveGames(user.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static JGSystem getInstance() {
		if (instance == null) {
			instance = new JGSystem();
		}
		return instance;
	}
}