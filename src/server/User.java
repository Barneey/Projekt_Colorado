package server;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6199635274651607854L;
	private String name;
	private char[] password;
	private Date lastLogin;
	private boolean validLogin;
	private int id;
	
	public User(String name, char[] password){
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
