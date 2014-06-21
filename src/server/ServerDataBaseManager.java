package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JList;

import serverUtil.ServerResultFrame;

public class ServerDataBaseManager {
// Create Table
// Add entry
// Drop Table
	
	public static final int QUERY = 0;
	public static final int UPDATE = 1;
	
	public void initializeDataBase(JList<String> log){
		ArrayList<String> alstLog = new ArrayList<>();
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + ServerDataBase.DBNAME + ";create=true";
			alstLog.add("Connecting...");
			log.setListData(alstLog.toArray(new String[0]));
			Connection connection = DriverManager.getConnection(url);
			alstLog.add("Creating Table users...");
			log.setListData(alstLog.toArray(new String[0]));
			Statement statement = connection.createStatement();
			
			String createUserTable = "CREATE TABLE users ("
									+ "uid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
									+ "Juser VARCHAR(24),"
									+ "Jpassword VARCHAR(100),"
									+ "JlastLoggin TIMESTAMP)";
			statement.executeUpdate(createUserTable);
			alstLog.add("Table users created");
			log.setListData(alstLog.toArray(new String[0]));
			
			// INSERT INTO USERS(JUSER, JPASSWORD, JLASTLOGGIN) VALUES ('root', 'root', '1970-01-01 00:00:00');
			//


			alstLog.add("Creating Table chatChannels...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createChatChannels = "CREATE TABLE chatChannels ("
						+ "channelID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
						+ "channelName VARCHAR(100))";
			statement.executeUpdate(createChatChannels);
			alstLog.add("Table chatChannels created");
			log.setListData(alstLog.toArray(new String[0]));
			
			
			alstLog.add("Creating Table chatMessages...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String sql = "CREATE TABLE chatmessages ("
						+ "messageid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
						+ "user_uid INTEGER REFERENCES USERS (uid),"
						+ "channel_cid INTEGER REFERENCES CHATCHANNELS (channelID),"
						+ "message VARCHAR(100),"
						+ "messagedate TIMESTAMP)";
			statement.executeUpdate(sql);
			alstLog.add("Table chatMessages created");
			log.setListData(alstLog.toArray(new String[0]));
			
			statement.close();
			connection.close();
			alstLog.add("Connection closed");
			log.setListData(alstLog.toArray(new String[0]));
			
		} catch (Exception e) {
			new ServerResultFrame(e.getMessage());
		}
	}
	
	public void deleteDataBase(JList<String> log){
		ArrayList<String> alstLog = new ArrayList<>();

		try {
			alstLog.add("Loading driver...");
			log.setListData(alstLog.toArray(new String[0]));
			
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			alstLog.add("Driver loaded");
			log.setListData(alstLog.toArray(new String[0]));
			alstLog.add("Deleting Tables...");
			log.setListData(alstLog.toArray(new String[0]));
			Connection connection = DriverManager.getConnection("jdbc:derby:" + ServerDataBase.DBNAME + ";");
			Statement statement = connection.createStatement();
			String readAllTablesSQL = "SELECT * FROM SYS.SYSTABLES";
			ResultSet allTables = statement.executeQuery(readAllTablesSQL);
			ArrayList<String> tables = new ArrayList<>();
			while(allTables.next()){
				if(allTables.getString("TABLETYPE").equals("T")){
					String tablename = allTables.getString("TABLENAME");
					tables.add(tablename);
				}
			}
			for (String table : tables) {
				dropTable(statement, table);
				alstLog.add("Table \"" + table +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			alstLog.add("Shutting down data base...");
			log.setListData(alstLog.toArray(new String[0]));
			alstLog.add("Data base shut down");
			log.setListData(alstLog.toArray(new String[0]));
		} catch (Exception e) {
			new ServerResultFrame(e.getMessage());
		}
	}

	public void deleteDataBaseManually(JList<String> log){
		ArrayList<String> alstLog = new ArrayList<>();

		try {
			alstLog.add("Loading driver...");
			log.setListData(alstLog.toArray(new String[0]));
			
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			alstLog.add("Driver loaded");
			log.setListData(alstLog.toArray(new String[0]));
			alstLog.add("Deleting Tables...");
			log.setListData(alstLog.toArray(new String[0]));
			Connection connection = DriverManager.getConnection("jdbc:derby:" + ServerDataBase.DBNAME + ";");
			Statement statement = connection.createStatement();
			
			String readAllTablesSQL = "SELECT * FROM SYS.SYSTABLES";
			ResultSet allTables = statement.executeQuery(readAllTablesSQL);
			ArrayList<String> allExistingUserTables = new ArrayList<>();
			while(allTables.next()){
				if(allTables.getString("TABLETYPE").equals("T")){
					String tablename = allTables.getString("TABLENAME");
					allExistingUserTables.add(tablename);
				}
			}
			String chatmessages = "CHATMESSAGES";
			if(allExistingUserTables.contains(chatmessages)){
				dropTable(statement, chatmessages);
				alstLog.add("Table \"" + chatmessages +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String chatchannels = "CHATCHANNELS";
			if(allExistingUserTables.contains(chatchannels)){
				dropTable(statement, chatchannels);
				alstLog.add("Table \"" + chatmessages +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String users = "USERS";
			if(allExistingUserTables.contains(users)){
				dropTable(statement, users);
				alstLog.add("Table \"" + users +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			alstLog.add("Shutting down data base...");
			log.setListData(alstLog.toArray(new String[0]));
			alstLog.add("Data base shut down");
			log.setListData(alstLog.toArray(new String[0]));
			} catch (Exception e) {
				new ServerResultFrame(e.getMessage());
			}
		}
	
	private void dropTable(Statement s, String tablename) throws SQLException{
		s.execute("DROP TABLE " + tablename);
	}

	public void enter(String sqlstatement, int type){
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + ServerDataBase.DBNAME;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			switch (type) {
			case QUERY:
				ResultSet rs = statement.executeQuery(sqlstatement);
				new ServerResultFrame(rs);
				break;
			case UPDATE:
				statement.executeUpdate(sqlstatement);
				new ServerResultFrame("Query Executed");
				break;
			default:
				break;
			}
			statement.close();
			connection.close();
		}
		catch(Exception e){
			new ServerResultFrame(e.getMessage());
		}
	}
	
	public User verifyUser(User user){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + ServerDataBase.DBNAME;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String sqlStatement = "SELECT * FROM users WHERE juser='" + user.getName() + "' AND jpassword='" + new String(user.getPassword()) + "'";
			ResultSet rs = statement.executeQuery(sqlStatement);
			if (rs.next()) {
				user.setValidLogin(true);
				user.setLastLogin(rs.getDate("jlastloggin"));
			}
			statement.close();
			connection.close();
		} catch (Exception e) {
			new ServerResultFrame(e.getMessage());
		}
		return user;
	}
}
