package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JList;

import serverUtil.ServerResultFrame;
import server_client.User;

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

			//			###CREATING TABLE USERS###
			
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
			
			// INSERT INTO USERS(JUSER, JPASSWORD, JLASTLOGGIN) VALUES ('root', 'root', '1970-01-01 00:00:00')

			//			###CREATING TABLE USERUSERRELATIONSHIPTYPES###
			
			alstLog.add("Creating Table UserUserRelationshipTypes...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createUserUserRelationshipTypes = "CREATE TABLE UserUserRelationshipTypes ("
											+ "uurtid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
											+ "rtype VARCHAR(255))";
			statement.executeUpdate(createUserUserRelationshipTypes);
			alstLog.add("Table UserUserRelationshipTypes created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###FILLING TABLE USERUSERRELATIONSHIPTYPES###
			
			alstLog.add("Filling Table UserUserRelationshipTypes...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String insertRelationshipTypeFriends = "INSERT INTO UserUserRelationshipTypes (rtype) VALUES ('friends')";
			statement.execute(insertRelationshipTypeFriends);
			
			String insertRelationshipTypeBlocked = "INSERT INTO UserUserRelationshipTypes (rtype) VALUES ('blocked')";
			statement.execute(insertRelationshipTypeBlocked);
			
			String insertRelationshipTypeRequested = "INSERT INTO UserUserRelationshipTypes (rtype) VALUES ('requested')";
			statement.execute(insertRelationshipTypeRequested);
			
			alstLog.add("Table UserUserRelationshipTypes filled");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE USERUSERRELATIONSHIPS###
			
			alstLog.add("Creating Table UserUserRelationships...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createUserUserRelationships = "CREATE TABLE UserUserRelationships ("
											+ "urID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
											+ "initialUserID INTEGER REFERENCES USERS (uid),"
											+ "destinationUserID INTEGER REFERENCES USERS (uid),"
											+ "userUserRelationshipTypesID INTEGER REFERENCES USERUSERRELATIONSHIPTYPES (uurtid))";
			statement.executeUpdate(createUserUserRelationships);
			
			alstLog.add("Table UserUserRelationships created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE CHATCHANNELS###
			
			alstLog.add("Creating Table chatChannels...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createChatChannels = "CREATE TABLE chatChannels ("
						+ "channelID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
						+ "channelName VARCHAR(100))";
			statement.executeUpdate(createChatChannels);
			alstLog.add("Table chatChannels created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE USERCHANNELRELATIONSHIPTYPES###

			alstLog.add("Creating Table userChannelRelationshiptypes...");
			log.setListData(alstLog.toArray(new String[0]));
			String createUserChannelRelationshiptypes = "CREATE TABLE userChannelRelationshiptypes ("
														+ "ucrtID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
														+ "rtype VARCHAR(255))";
			statement.executeUpdate(createUserChannelRelationshiptypes);
			alstLog.add("Table userChannelRelationshiptypes created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###FILLING TABLE USERCHANNELRELATIONSHIPTYPES###
			
			alstLog.add("Filling Table userChannelRelationshiptypes...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String insertRelationshipTypeOnline = "INSERT INTO userChannelRelationshiptypes (rtype) VALUES ('online')";
			statement.execute(insertRelationshipTypeOnline);
			
			String insertRelationshipTypeOffline = "INSERT INTO userChannelRelationshiptypes (rtype) VALUES ('offline')";
			statement.execute(insertRelationshipTypeOffline);
			
			String insertRelationshipTypeMuted = "INSERT INTO userChannelRelationshiptypes (rtype) VALUES ('muted')";
			statement.execute(insertRelationshipTypeMuted);
			
			alstLog.add("Table userChannelRelationshiptypes filled");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE USERCHATRELATIONS###

			alstLog.add("Creating Table userChatRelations...");
			log.setListData(alstLog.toArray(new String[0]));
			String createUserChatRelations = "CREATE TABLE userChatRelations ("
												+ "ucrID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
												+ "userID INTEGER REFERENCES USERS (uID),"
												+ "channelID INTEGER REFERENCES CHATCHANNELS (channelID),"
												+ "relationshipType INTEGER REFERENCES USERCHANNELRELATIONSHIPTYPES (ucrtID))";
			statement.executeUpdate(createUserChatRelations);
			alstLog.add("Table userChatRelations created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE CHATMESSAGES###
			
			alstLog.add("Creating Table chatMessages...");
			log.setListData(alstLog.toArray(new String[0]));
			String createChatMessages = "CREATE TABLE chatmessages ("
										+ "messageid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
										+ "user_uid INTEGER REFERENCES USERS (uid),"
										+ "channel_cid INTEGER REFERENCES CHATCHANNELS (channelID),"
										+ "message VARCHAR(100),"
										+ "messagedate TIMESTAMP)";
			statement.executeUpdate(createChatMessages);
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
			
			String userChatRelations = "USERCHATRELATIONS";
			if(allExistingUserTables.contains(userChatRelations)){
				dropTable(statement, userChatRelations);
				alstLog.add("Table \"" + userChatRelations +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String userChannelRelationshipTypes = "USERCHANNELRELATIONSHIPTYPES";
			if(allExistingUserTables.contains(userChannelRelationshipTypes)){
				dropTable(statement, userChannelRelationshipTypes);
				alstLog.add("Table \"" + userChannelRelationshipTypes +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String chatchannels = "CHATCHANNELS";
			if(allExistingUserTables.contains(chatchannels)){
				dropTable(statement, chatchannels);
				alstLog.add("Table \"" + chatchannels +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}

			String userUserRelationships = "USERUSERRELATIONSHIPS";
			if(allExistingUserTables.contains(userUserRelationships)){
				dropTable(statement, userUserRelationships);
				alstLog.add("Table \"" + userUserRelationships +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}

			String userUserRelationshipType = "USERUSERRELATIONSHIPTYPES";
			if(allExistingUserTables.contains(userUserRelationshipType)){
				dropTable(statement, userUserRelationshipType);
				alstLog.add("Table \"" + userUserRelationshipType +  "\" deleted");
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
