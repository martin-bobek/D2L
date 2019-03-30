package server;

import shared.response.LoginResponse;

/**
 * Holds user data from the user after a successful login.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class LoginData {
	/**
	 * The user's email address.
	 */
	private final String email;
	/**
	 * The password used to access the user's email account.
	 */
	private final String password;
	/**
	 * The login response to be sent to the server.
	 */
	private final LoginResponse login;
	
	/**
	 * Created when login fails.
	 */
	LoginData() {
		this(new LoginResponse(), null, null);
	}
	
	/**
	 * Initializes all parameters of the users login information.
	 * @param login The login response object to be sent back to the client.
	 * @param email The user's email address.
	 * @param password The password for the user's email.
	 */
	LoginData(LoginResponse login, String email, String password) {
		this.email = email;
		this.password = password;
		this.login = login;
	}
	
	/**
	 * Gets the user's email address.
	 * @return Email address.
	 */
	String getEmail() {
		return email;
	}
	
	/**
	 * Gets the password to access the user's email.
	 * @return Email password.
	 */
	String getPassword() {
		return password;
	}
	
	/**
	 * Gets the login response.
	 * @return Login response.
	 */
	LoginResponse getLogin() {
		return login;
	}
}
