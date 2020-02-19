package es.uned.common;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {


	private static final long serialVersionUID = -7810380232027294231L;
	private String nombre;
	private String nick;
	private String password;
	private long registerDate;
	private long lastLogin;
	
	public User(String name, String nick, String password) 
	{
		super();
		this.nombre = name;
		this.nick = nick;
		this.password = password;
		Date date = new Date();
		this.registerDate = date.getTime();
		this.lastLogin = date.getTime();
		
	}
	
	//getters
	
	public String getName() {
		return this.nombre;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public long getRegisterDate() {
		return this.registerDate;
	}
	
	public long getLastLogin() {
		return this.lastLogin;
	}
	
	
	
	//setters
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setLastLogin() {
		Date date = new Date();
		this.lastLogin = date.getTime();
	}
}
