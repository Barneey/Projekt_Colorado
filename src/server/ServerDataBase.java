package server;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class ServerDataBase {
	private static ServerDataBase instance = null;
	private final String DBNAME;

	protected ServerDataBase() {
		DBNAME = "JGameCollectionDB";
	}
	
	public static ServerDataBase getInstance() {
		if (instance == null) {
			instance = new ServerDataBase();
		}
		return instance;
	}
	
	public String getDBName(){
		return this.DBNAME;
	}
}