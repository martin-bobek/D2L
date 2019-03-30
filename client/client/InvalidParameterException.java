package client.client;

/**
 * This exception class is used to indicate an invalid parameter has been entered by the user.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new exception with a custom message.
	 * @param message The custom error message.
	 */
	public InvalidParameterException(String message) {
		super(message);
	}
}
