package data;

import java.io.Serializable;

/**
 * Represents a message in the chat. Holds the contents of the message and the name of the user
 * who submitted it.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * The name of the user who created the message.
	 * The content of the message.
	 */
	private String username;
	private String content;
	
	/**
	 * Creates a new chat message object.
	 * @param username The name of the user who submitted the message.
	 * @param content The content of the message.
	 */
	public ChatMessage(String username, String content) {
		this.username = username;
		this.content = content;
	}
	
	/**
	 * Produces a formatted string which will be shown in the chat window.
	 */
	public String toString() {
		return username + ": " + content;
	}
}
