package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.rowset.CachedRowSet;
import javax.swing.JList;

import com.sun.rowset.CachedRowSetImpl;

import serverUtil.ServerResultFrame;
import server_client.ChatChannel;
import server_client.ChatMessage;
import server_client.Playmode;
import server_client.User;

public class ServerDataBaseManager {
	
	public static final int QUERY = 0;
	public static final int UPDATE = 1;
	private String DBName;
	
	public ServerDataBaseManager(){
		DBName = ServerDataBase.getInstance().getDBName();
	}
	
	public void initializeDataBase(JList<String> log){
		ArrayList<String> alstLog = new ArrayList<>();
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName + ";create=true";
			alstLog.add("Connecting...");
			log.setListData(alstLog.toArray(new String[0]));
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();

			//			###CREATING TABLE USERS###
			
			alstLog.add("Creating Table users...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createUsers = "CREATE TABLE users ("
									+ "uid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
									+ "Juser VARCHAR(24) NOT NULL,"
									+ "Jpassword VARCHAR(100) NOT NULL,"
									+ "Jnick VARCHAR(24),"
									+ "level INTEGER DEFAULT 1,"
									+ "currentExp INTEGER DEFAULT 0,"
									+ "rCoins INTEGER DEFAULT 0,"
									+ "vCoins INTEGER DEFAULT 0,"
									+ "points INTEGER DEFAULT 0,"
									+ "JlastLoggin TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
			statement.executeUpdate(createUsers);
			alstLog.add("Table users created");
			log.setListData(alstLog.toArray(new String[0]));
			
			// INSERT INTO USERS(JUSER, JPASSWORD) VALUES ('root', 'root')

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
			
			//			###CREATING TABLE CHATCHANNELTYPES###
			
			alstLog.add("Creating Table chatChannelTypes...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createChatChannelTypes = "CREATE TABLE chatChannelTypes("
											+ "cctID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
											+ "cctype VARCHAR(255))";
			statement.executeUpdate(createChatChannelTypes);
			
			alstLog.add("Table chatChannelTypes created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###FILLING TABLE CHATCHANNELTYPES###
			
			alstLog.add("Filling Table chatChannelTypes");
			log.setListData(alstLog.toArray(new String[0]));
			
			String insertChatChannelTypePrivate = "INSERT INTO CHATCHANNELTYPES (cctype) VALUES ('private')";
			statement.execute(insertChatChannelTypePrivate);
			
			String insertChatChannelTypePermanent = "INSERT INTO CHATCHANNELTYPES (cctype) VALUES ('permanent')";
			statement.execute(insertChatChannelTypePermanent);
			
			String insertChatChannelTypeCustom = "INSERT INTO CHATCHANNELTYPES (cctype) VALUES ('custom')";
			statement.execute(insertChatChannelTypeCustom);
			
			alstLog.add("Table chatChannelTypes filled");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###CREATING TABLE CHATCHANNELS###
			
			alstLog.add("Creating Table chatChannels...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String createChatChannels = "CREATE TABLE chatChannels ("
										+ "channelID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
										+ "channelType INTEGER REFERENCES CHATCHANNELTYPES(cctID),"
										+ "channelName VARCHAR(100))";
			statement.executeUpdate(createChatChannels);
			alstLog.add("Table chatChannels created");
			log.setListData(alstLog.toArray(new String[0]));
			
			// 			###FILLING TABLE CHATCHANNELS
			
			alstLog.add("Filling Table chatChannels...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String insertChatChannelsHelp = "INSERT INTO CHATCHANNELS (channelType, channelName)" +
											"VALUES ((SELECT cctid FROM chatChannelTypes where cctype = 'permanent'), 'Help')";
			statement.executeUpdate(insertChatChannelsHelp);
			
			String insertChatChannelsJGame_DEU_1 = "INSERT INTO CHATCHANNELS (channelType, channelName)" +
													"VALUES ((SELECT cctid FROM chatChannelTypes where cctype = 'permanent'), 'JGame DEU-1')";
			statement.executeUpdate(insertChatChannelsJGame_DEU_1);
			
			alstLog.add("Table chatChannels filled");
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
			
//			###CREATING TABLE PLAYMODES###
			
			alstLog.add("Creating Table playmodes...");
			log.setListData(alstLog.toArray(new String[0]));
			String createPlaymodes = "CREATE TABLE playmodes ("
										+ "pmid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
										+ "titel VARCHAR(25),"
										+ "desctext VARCHAR(255),"
										+ "available boolean)";
			statement.executeUpdate(createPlaymodes);
			alstLog.add("Table playmodes created");
			log.setListData(alstLog.toArray(new String[0]));
			
			//			###FILLING TABLE PLAYMODES###
			
			alstLog.add("Filling Table playmodes...");
			log.setListData(alstLog.toArray(new String[0]));
			
			String insertPlaymodesFFA = "INSERT INTO playmodes (titel, desctext, available) VALUES ('FFA', 'Free for all - Up to 8 players fight against each other', true)";
			statement.execute(insertPlaymodesFFA);
			
			String insertPlaymodesTEAM = "INSERT INTO playmodes (titel, desctext, available) VALUES ('TEAM', 'Team vs Team - Two teams fight against each other', true)";
			statement.execute(insertPlaymodesTEAM);
			
			String insertPlaymodesUNDERDOG = "INSERT INTO playmodes (titel, desctext, available) VALUES ('UNDERDOG', 'The Underdog Challange - A single player competes against a group of player', true)";
			statement.execute(insertPlaymodesUNDERDOG);
			
			alstLog.add("Table playmodes filled");
			log.setListData(alstLog.toArray(new String[0]));
			
//			###CREATING TABLE PLAYMODETEAMS###
			
			alstLog.add("Creating Table playmodeTeams...");
			log.setListData(alstLog.toArray(new String[0]));
			String playmodeTeams = "CREATE TABLE playmodeteams ("
								+ "pmtID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
								+ "pmID INTEGER REFERENCES PLAYMODES (pmID),"
								+ "size INTEGER)";
			statement.executeUpdate(playmodeTeams);
			alstLog.add("Table playmodeTeams created");
			log.setListData(alstLog.toArray(new String[0]));
			
//			###FILLING TABLE PLAYMODETEAMS###
			
			alstLog.add("Filling Table playmodeTeams...");
			log.setListData(alstLog.toArray(new String[0]));
			
			for (int i = 0; i < 8; i++) {
				String insertPlaymodeTeamsFFA = "INSERT INTO playmodeteams (pmid, size) VALUES ((SELECT pmid FROM playmodes WHERE titel = 'FFA'), 1)";
				statement.execute(insertPlaymodeTeamsFFA);
			}
			
			for (int i = 0; i < 2; i++) {
				String insertPlaymodeTeamsTEAM = "INSERT INTO playmodeteams (pmid, size) VALUES ((SELECT pmid FROM playmodes WHERE titel = 'TEAM'), 3)";
				statement.execute(insertPlaymodeTeamsTEAM);
			}

			String insertPlaymodeTeamsUNDERDOGTeam = "INSERT INTO playmodeteams (pmid, size) VALUES ((SELECT pmid FROM playmodes WHERE titel = 'UNDERDOG'), 3)";
			statement.execute(insertPlaymodeTeamsUNDERDOGTeam);
			
			String insertPlaymodeTeamsUNDERDOGSolo = "INSERT INTO playmodeteams (pmid, size) VALUES ((SELECT pmid FROM playmodes WHERE titel = 'UNDERDOG'), 1)";
			statement.execute(insertPlaymodeTeamsUNDERDOGSolo);
			
			alstLog.add("Table playmodeTeams filled");
			log.setListData(alstLog.toArray(new String[0]));
			
			
//			###CREATING TABLE GAMES###
			
			alstLog.add("Creating Table games...");
			log.setListData(alstLog.toArray(new String[0]));
			String createGames = "CREATE TABLE games ("
								+ "gid INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
								+ "pmID INTEGER REFERENCES PLAYMODES (pmID),"
								+ "winner INTEGER,"
								+ "gamestart TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
			statement.executeUpdate(createGames);
			alstLog.add("Table games created");
			log.setListData(alstLog.toArray(new String[0]));
			
//			###CREATING TABLE GAMERUSERLRATIONSHIPS###
			
			alstLog.add("Creating gameUserRelationships games...");
			log.setListData(alstLog.toArray(new String[0]));
			String createGameUserRelationships = "CREATE TABLE gameUserRelationships ("
												+ "gurID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
												+ "gID INTEGER REFERENCES GAMES (gID),"
												+ "uID INTEGER REFERENCES USERS (uID),"
												+ "team INTEGER)";
			statement.executeUpdate(createGameUserRelationships);
			alstLog.add("Table gameUserRelationships created");
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
			Connection connection = DriverManager.getConnection("jdbc:derby:" + DBName + ";");
			Statement statement = connection.createStatement();
			String readAllTablesSQL = "SELECT * FROM SYS.SYSTABLES WHERE TYBLETYPE = 'T'";
			ResultSet allTables = statement.executeQuery(readAllTablesSQL);
			ArrayList<String> tables = new ArrayList<>();
			while(allTables.next()){
				String tablename = allTables.getString("TABLENAME");
				tables.add(tablename);
			}
			for (String table : tables) {
				dropTable(statement, table);
				alstLog.add("Table \"" + table +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			statement.close();
			connection.close();
			
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
			Connection connection = DriverManager.getConnection("jdbc:derby:" + DBName + ";");
			Statement statement = connection.createStatement();
			
			String readAllTablesSQL = "SELECT * FROM SYS.SYSTABLES WHERE TABLETYPE = 'T'";
			ResultSet allTables = statement.executeQuery(readAllTablesSQL);
			ArrayList<String> allExistingUserTables = new ArrayList<>();
			while(allTables.next()){
				String tablename = allTables.getString("TABLENAME");
				allExistingUserTables.add(tablename);
			}
			
			String playmodeTeams = "PLAYMODETEAMS";
			if(allExistingUserTables.contains(playmodeTeams)){
				dropTable(statement, playmodeTeams);
				alstLog.add("Table \"" + playmodeTeams +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String gameUserRelationships = "GAMEUSERRELATIONS";
			if(allExistingUserTables.contains(gameUserRelationships)){
				dropTable(statement, gameUserRelationships);
				alstLog.add("Table \"" + gameUserRelationships +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String games = "GAMES";
			if(allExistingUserTables.contains(games)){
				dropTable(statement, games);
				alstLog.add("Table \"" + games +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			String playmodes = "PLAYMODES";
			if(allExistingUserTables.contains(playmodes)){
				dropTable(statement, playmodes);
				alstLog.add("Table \"" + playmodes +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
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
			
			String chatChannelTypes = "CHATCHANNELTYPES";
			if(allExistingUserTables.contains(chatChannelTypes)){
				dropTable(statement, chatChannelTypes);
				alstLog.add("Table \"" + chatChannelTypes +  "\" deleted");
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
			
			String levelRanges = "LEVELRANGES";
			if(allExistingUserTables.contains(levelRanges)){
				dropTable(statement, levelRanges);
				alstLog.add("Table \"" + levelRanges +  "\" deleted");
				log.setListData(alstLog.toArray(new String[0]));
			}
			
			statement.close();
			connection.close();
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
			String url = "jdbc:derby:" + DBName;
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
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String sqlStatement = "SELECT * FROM users WHERE juser='" + user.getUsername() + "' AND jpassword='" + new String(user.getPassword()) + "'";
			ResultSet rs = statement.executeQuery(sqlStatement);
			if (rs.next()) {
				user.setId(rs.getInt("uid"));
				user.setValidLogin(true);
				user.setNick(rs.getString("jnick"));
				user.setLevel(rs.getInt("level"));
				user.setCurrentExp(rs.getInt("currentExp"));
				user.setrCoins(rs.getInt("rcoins"));
				user.setvCoins(rs.getInt("vcoins"));
				user.setPoints(rs.getInt("points"));
				user.setLastLogin(rs.getTimestamp("jlastloggin"));
			}
			statement.close();
			connection.close();
		} catch (Exception e) {
			new ServerResultFrame(e.getMessage());
		}
		return user;
	}	

	public boolean checkUniqueNickname(String nickname){
		boolean isUnique = false;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String sqlStatement = "SELECT * FROM users WHERE jnick='" + nickname + "'";
			ResultSet rs = statement.executeQuery(sqlStatement);
			isUnique = !rs.next();
			statement.close();
			connection.close();
		} catch (Exception e) {
			new ServerResultFrame(e.getMessage());
		}
		return isUnique; 
	}

	public void updateUser(User user){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();

			DateFormat dtfLastLogin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastLoginDate = dtfLastLogin.format(user.getLastLogin());

			String sqlStatement = "UPDATE users SET "
									+ "jnick='" + user.getNick() + "',"
									+ "level=" + user.getLevel() + ","
									+ "currentexp=" + user.getCurrentExp() + ","
									+ "rcoins=" + user.getrCoins() + ","
									+ "vcoins=" + user.getvCoins() + ","
									+ "jlastloggin='" + lastLoginDate + "' "
									+ "WHERE uid=" + user.getId();
			statement.executeUpdate(sqlStatement);
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			new ServerResultFrame(e.getMessage());
		}
	}

	public ChatChannel[] getAllChatChannels(){
		ChatChannel channels[] = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String sqlStatement = "SELECT channelID, cctype AS channelType, channelName "
								+ "FROM CHATCHANNELS, CHATCHANNELTYPES WHERE cctid = chatchannels.channelType "
								+ "AND (chatchannels.channelType = (SELECT CCTID FROM chatchannelTypes WHERE cctype = 'permanent') "
								+ "OR chatchannels.channelType = (SELECT CCTID FROM chatchannelTypes WHERE cctype = 'custom'))";
			
			ResultSet rs = statement.executeQuery(sqlStatement);
			ArrayList<ChatChannel> alstChannels = new ArrayList<>();
			while(rs.next()){
				ChatChannel channel = new ChatChannel(rs.getInt("channelID"), rs.getString("channelType"), rs.getString("channelName"));
				alstChannels.add(channel);
			}
			channels = alstChannels.toArray(new ChatChannel[0]);
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channels;
	}
	
	public ChatChannel createAndJoinChannel(ChatChannel chatChannel, User user){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String insertIntoChatChannels = "INSERT INTO chatchannels (channeltype, channelname) "
											+ "VALUES ((SELECT cctid FROM chatchanneltypes WHERE cctype = 'custom' ), '" + chatChannel.getChannelName() + "')";
			statement.executeUpdate(insertIntoChatChannels);
		
			String getChannel = "SELECT channelID "
								+ "FROM CHATCHANNELS "
								+ "WHERE channelname = '" + chatChannel.getChannelName() + "' "
								+ "AND channeltype = (SELECT cctid FROM chatchannelTypes WHERE cctype = 'custom')";

			ResultSet rs = statement.executeQuery(getChannel);
			if(rs.next()){
				chatChannel.setChannelID(rs.getInt("channelID"));
			}
			
			String joinUserInChannelUpdate = "INSERT INTO USERCHATRELATIONS (userID, channelID, relationshiptype) "
											+"VALUES (" + user.getId() + ", " + chatChannel.getChannelID() +", (SELECT ucrtID FROM UserChannelRelationshipTypes WHERE rtype = 'online' ))";

			statement.executeUpdate(joinUserInChannelUpdate);
			
			statement.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chatChannel;
	}
	
	public void joinUserInChannel(ChatChannel chatChannel, User user){
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String selectUserFromChannel = "SELECT * FROM USERCHATRELATIONS WHERE userID =" + user.getId() + " "
										+ "AND channelID = " + chatChannel.getChannelID();
			
			ResultSet rs = statement.executeQuery(selectUserFromChannel);
			
			if(!rs.next()){
				String joinUserInChannelUpdate = "INSERT INTO USERCHATRELATIONS (userID, channelID, relationshiptype) "
												+ "VALUES (" + user.getId() + ", " + chatChannel.getChannelID() +", (SELECT ucrtID FROM UserChannelRelationshipTypes WHERE rtype = 'online' ))";

				statement.executeUpdate(joinUserInChannelUpdate);
			}
			statement.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public User[] loadUsers(ChatChannel channel){
		User[] users = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String sqlStatement = "SELECT * FROM userchatrelations, users, userChannelRelationshipTypes "
								+ "WHERE userchatrelations.channelID = " + channel.getChannelID() + " "
								+ "AND userchatrelations.userid = users.uid "
								+ "AND userChannelRelationshipTypes.ucrtID = (SELECT ucrtid FROM userChannelRelationshipTypes WHERE rtype='online')";
			
			
			ResultSet rs = statement.executeQuery(sqlStatement);
			ArrayList<User> alstUsers = new ArrayList<>();
			while(rs.next()){
				User user = new User(rs.getString("juser"), null);
				user.setId(rs.getInt("uid"));
				user.setValidLogin(true);
				user.setNick(rs.getString("jnick"));
				user.setLevel(rs.getInt("level"));
				user.setCurrentExp(rs.getInt("currentExp"));
				user.setrCoins(rs.getInt("rcoins"));
				user.setvCoins(rs.getInt("vcoins"));
				user.setPoints(rs.getInt("points"));
				user.setLastLogin(rs.getTimestamp("jlastloggin"));
				alstUsers.add(user);
			}
			users = alstUsers.toArray(new User[0]);
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public ChatMessage[] loadMessages(ChatChannel channel, Date joinDate){
		ChatMessage[] chatMessages = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = dtf.format(joinDate);
			
			String sqlStatement = "SELECT jnick, uid, message, messagedate "
								+ "FROM chatmessages, users "
								+ "WHERE chatmessages.channel_cid = " + channel.getChannelID() + " "
								+ "AND chatmessages.user_uid = users.uid "
								+ "AND messagedate >= '" + formattedDate + "'";
			
			ResultSet rs = statement.executeQuery(sqlStatement);
			ArrayList<ChatMessage> alstMessages = new ArrayList<>();
			while(rs.next()){
				ChatMessage message = new ChatMessage(rs.getString("jnick"), rs.getInt("uid"), channel.getChannelID(), rs.getString("message"));
				message.setMessageDate(rs.getTimestamp("messagedate"));
				alstMessages.add(message);
			}
			chatMessages = alstMessages.toArray(new ChatMessage[0]);
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chatMessages;
	}
	
	public void addMessage(ChatMessage message){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = dtf.format(new Date());
			
			String sqlStatement = "INSERT INTO chatmessages (user_uid, channel_cid, message, messagedate) VALUES (" + message.getUserID() + ", " + message.getChannelCID() + ", '" + message.getMessage() + "', '" + currentDate + "')";
			
			statement.executeUpdate(sqlStatement);

			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void leaveChannel(ChatChannel channel, User user){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String deleteStatement = "DELETE FROM userchatrelations WHERE userID=" + user.getId() + " "
									+ "AND channelID = " + channel.getChannelID();
			
			statement.executeUpdate(deleteStatement);
			
			if(channel.getChannelType().equals("custom")){
				String selectStatement = "SELECT * FROM userchatrelations WHERE channelID = " + channel.getChannelID();
	
				ResultSet rs = statement.executeQuery(selectStatement);
				
				if(!rs.next()){
					String deleteMessagesStatement = "DELETE FROM chatmessages WHERE channel_cid = " + channel.getChannelID();
					statement.executeUpdate(deleteMessagesStatement);
					
					String deleteChannelStatement = "DELETE FROM chatchannels WHERE channelID = " + channel.getChannelID();
					statement.executeUpdate(deleteChannelStatement);
				}
			}
		
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void leaveAllChannels(User user){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String deleteStatement = "DELETE FROM userchatrelations WHERE userID=" + user.getId();
			
			statement.executeUpdate(deleteStatement);
		
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CachedRowSetImpl getRanking(){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String getRanking = "SELECT jnick AS Jamertag, Level, points FROM USERS ORDER BY POINTS DESC FETCH FIRST 100 ROWS ONLY";
			
			CachedRowSetImpl rowSet = new CachedRowSetImpl();
			rowSet.populate(statement.executeQuery(getRanking));
			
			statement.close();
			connection.close();
			return rowSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Playmode[] getPlaymodes(){
		Playmode[] playmodes = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			String url = "jdbc:derby:" + DBName;
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			
			String getRanking = "SELECT * FROM playmodes where available = true";
			
			ResultSet rs = statement.executeQuery(getRanking);
			ArrayList<Playmode> alstPlaymodes = new ArrayList<>();
			while(rs.next()){
				alstPlaymodes.add(new Playmode(rs.getInt("pmid"), rs.getString("titel"), rs.getString("descText"), null));
			}
			playmodes = alstPlaymodes.toArray(new Playmode[0]);
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return playmodes;
	}
}