package shared.response;

import java.io.Serializable;

/**
 * The response sent after a login attempt. The response is sent to the login 
 * dialog, not the main client GUI so it does not need to implement the Response interface. 
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class LoginResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 
	 * Used to indicate a login attempt was not successful.
	 */
	private static final char BAD_LOGIN = 0;
	/**
	 * Indicates the user logged in as a student.
	 */
	public static final char PROFESSOR = 'P';
	/**
	 * Indicates the user logged in as a professor.
	 */
	public static final char STUDENT = 'S';
	/**
	 * The type of login, indicating whether it was successful 
	 * and what type of user logged in.
	 */
	private final char type;
	/**
	 * The name of the user who just logged in.
	 */
	private final String name;
	
	/**
	 * Creates a new successful login response.
	 * @param type The user's type.
	 * @param name The user's name.
	 */
	public LoginResponse(char type, String name) {
		this.type = type;
		this.name = name;
	}
	
	/**
	 * Creates a new bad login, indicating the credentials entered by the user were wrong.
	 */
	public LoginResponse() {
		this(BAD_LOGIN, null);
	}
	
	/**
	 * Indicates that the login was  successful.
	 * @return true if successfully logged in.
	 */
	public boolean success() {
		return type != BAD_LOGIN;
	}
	
	/**
	 * Indicates if a student or professor logged in.
	 * @return PROFESSOR if a professor logged in. STUDENT if a student logged in.
	 */
	public char getType() {
		return type;
	}
	
	/** 
	 * returns the name of the user/
	 * @return The name of the user.
	 */
	public String getName() {
		return name;
	}
}
