package server;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class ServerDataBase {
	private static ServerDataBase instance = null;
	public static final String DBNAME = "JGameCollectionDB";

	protected ServerDataBase() {

	}
	
	public static ServerDataBase getInstance() {
		if (instance == null) {
			instance = new ServerDataBase();
		}
		return instance;
	}
}