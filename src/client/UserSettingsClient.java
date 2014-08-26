package client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class UserSettingsClient {
	private static UserSettingsClient instance = null;
	private XStream xstream;
	private boolean savedUsername;
	private String sSavedUsername;
	private String username;
	private String sUsername;
	private Map<String, String> userSettings;
	private File fUserData;
	private String sDir;
	private boolean showTimestamp;
	private String sShowTimestamp;
	private int layoutType;
	private String sLayoutType;
	
	protected UserSettingsClient() {
		this.xstream = new XStream(new StaxDriver());
		this.sDir = System.getenv("APPDATA")+ "/JGameCollector" + JGSystem.VERSION;
		this.fUserData = new File(sDir + "/" + "settings.xml");
		username = "";
		sUsername = "username";
		savedUsername = false;
		sSavedUsername = "savedUSername";
		showTimestamp = true;
		sShowTimestamp = "showTimestamp";
		layoutType = LookManager.GREEN_LAYOUT;
		sLayoutType = "layoutType";
		loadUserSettings();
	}
	
	private void setUpUserSettings(){
		userSettings = new HashMap<String, String>();
		userSettings.put(sSavedUsername, String.valueOf(savedUsername));
		userSettings.put(sUsername, username);
		userSettings.put(sShowTimestamp, String.valueOf(showTimestamp));
		userSettings.put(sLayoutType, String.valueOf(layoutType));
	}
	
	private void loadUserSettings(){
		if(!this.fUserData.exists()){
			File fDir = new File(sDir);
			if(!fDir.exists()){
				fDir.mkdir();
			}
			try {

				this.fUserData.createNewFile();
				saveUserSettings();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Initialise or load the User Settings
		this.userSettings = (HashMap<String, String>)(this.xstream.fromXML(fUserData));
		this.username = userSettings.get(sUsername);
		this.savedUsername = Boolean.parseBoolean(userSettings.get(sSavedUsername));
		this.showTimestamp = Boolean.parseBoolean(userSettings.get(sShowTimestamp));
		this.layoutType = Integer.parseInt(userSettings.get(sLayoutType));
	}
	
	public boolean saveUserSettings(){
		setUpUserSettings();
	    OutputStream outputStream = null;
	    Writer writer = null;

	    try {
	        outputStream = new FileOutputStream(this.fUserData);
	        writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
	        this.xstream.toXML(userSettings, writer);
	    }
	    catch (Exception exp) {
	        return false;
	    }
	    return true;
	}
	
	public static UserSettingsClient getInstance(){
		if(instance == null){
			instance = new UserSettingsClient();
		}
		return instance;
	}

	public boolean isSavedUsername() {
		return savedUsername;
	}

	public void setSavedUsername(boolean savedUsername) {
		this.savedUsername = savedUsername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isShowTimestamp() {
		return showTimestamp;
	}

	public void setShowTimestamp(boolean showTimestamp) {
		this.showTimestamp = showTimestamp;
	}
	
	public int getLayoutType(){
		return this.layoutType;
	}
	
	public void setLayoutType(int layoutType){
		this.layoutType = layoutType;
	}
}