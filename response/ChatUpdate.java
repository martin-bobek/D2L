package response;

import java.io.IOException;
import java.util.ArrayList;

import data.ChatMessage;

/** 
 * Used to deliver new chat messages. Can be sent on request or as a push
 * notification without request.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ChatUpdate implements Response {
	private static final long serialVersionUID = 1L;
	/**
	 * The messages to be delivered.
	 */
	private final ArrayList<ChatMessage> messages;
	
	/**
	 * Creates a new update delivering a single message.
	 * @param message The message to be delivered.
	 */
	public ChatUpdate(ChatMessage message) {
		messages = new ArrayList<ChatMessage>();
		messages.add(message);
	}
	
	/**
	 * Creates a new update delivering many messages.
	 * @param messages The messages to be delivered.
	 */
	public ChatUpdate(ArrayList<ChatMessage> messages) {
		this.messages = messages;
	}

	/**
	 * Appends the messages stored in the Update object to the table in the GUI. 
	 */
	public void performAction(ClientInterface client) throws IOException {
		client.appendChat(messages);
	}
}
