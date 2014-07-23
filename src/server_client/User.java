package server_client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable{
	
	private static final long serialVersionUID = -6199635274651607854L;
	private int id;
	private String username;
	private char[] password;
	private boolean validLogin;
	private String nick;
	private int level;
	private int levelUpExp;
	private int currentExp;
	private int rCoins;
	private int vCoins;
	private int points;
	private Date lastLogin;
	
	public User(String name, char[] password){
		this.username = name;
		this.password = password;
		try {
			this.lastLogin = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			this.lastLogin = new Date();
		}
		this.validLogin = false;
		this.id = -1;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isValidLogin() {
		return validLogin;
	}

	public void setValidLogin(boolean validLogin) {
		this.validLogin = validLogin;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getrCoins() {
		return rCoins;
	}

	public void setrCoins(int rCoins) {
		this.rCoins = rCoins;
	}

	public int getvCoins() {
		return vCoins;
	}

	public void setvCoins(int vCoins) {
		this.vCoins = vCoins;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		this.levelUpExp = ((int)(Math.pow(this.level, 3.8/3.0) * 100 + 100) / 100) * 100;
	}

	public int getLevelUpExp() {
		return levelUpExp;
	}

	public int getCurrentExp() {
		return currentExp;
	}

	public void setCurrentExp(int currentExp) {
		this.currentExp = currentExp;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public String toString(){
		return this.nick;
	}
}