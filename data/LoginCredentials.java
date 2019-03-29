package data;

import java.io.Serializable;

/**
 * A class used to hold the login credentials given by the user at login.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * The user's id.
	 */
	private int userId;
	/**
	 * The user's password.
	 */
	private String password;
	
	/**
	 * Creates a new login credentials object, initializing all parameters.
	 * @param userId The user id.
	 * @param password The user's password.
	 */
	public LoginCredentials(int userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	/**
	 * Gets the user's id.
	 * @return id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Gets the user's password.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
}
