package data;

import java.io.Serializable;

import client.InvalidParameterException;

public class LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final char BAD_LOGIN = 0;
	public static final char PROFESSOR = 'P';
	public static final char STUDENT = 'S';
	private int userId;
	private String password;
	
	public LoginCredentials(String userId, char[] password) throws InvalidParameterException {
		if (userId == null || userId.length() != 6)
			throw new InvalidParameterException("User ID must be 6 digits!");
		if (password == null || password.length == 0)
			throw new InvalidParameterException("Password cannot be empty!");		
		this.password = new String(password);
		try {
			this.userId = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Invalid User ID!");
		}
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getPassword() {
		return password;
	}
}
