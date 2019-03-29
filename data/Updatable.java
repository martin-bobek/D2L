package data;

import request.Request;

/**
 * An interface implemented by TableRow's which can be updated from the GUI table.
 * Allows a message to be sent to the server when the table detects a change.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface Updatable {
	/**
	 * Creates a new update message to the server.
	 * @return The new update for the server.
	 */
	Request createRequest();
}
