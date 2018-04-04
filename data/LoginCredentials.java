package data;

import java.io.Serializable;

import client.InvalidParameterException;

public class LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final char BAD_LOGIN = 0;
	public static final char PROFESSOR = 'P';
	public static final char STUDENT = 'S';
	private String username;
	private String password;
	
	public LoginCredentials(String username, String password) throws InvalidParameterException {
		if (username == null || username.isEmpty())
			throw new InvalidParameterException("Username cannot be empty!");
		if (password == null || password.isEmpty())
			throw new InvalidParameterException("Password cannot be empty!");
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
