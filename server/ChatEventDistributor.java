package server;

import java.util.Vector;

/**
 * A class used to notify all subscribed clients that a user has submitted a chat message.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class ChatEventDistributor {
	/**
	 * All the subscribed clients.
	 */
	private Vector<ClientHandler> clients;
	
	/**
	 * Creates a new event distributor for the chat system.
	 */
	ChatEventDistributor() {
		clients = new Vector<ClientHandler>();
	}
	
	/**
	 * Notifies all subscribed clients that a message has been delivered.
	 * @param messageId The message id.
	 * @param courseId The course to which the message belongs.
	 */
	void notifyMessage(int messageId, int courseId) {
		synchronized(clients) {
			for (ClientHandler client : clients)
				client.newMessage(messageId, courseId);
		}
	}
	
	/**
	 * Subscribes a client to receive notifications.
	 * @param client A handle to the client.
	 */
	void subscribe(ClientHandler client) {
		clients.addElement(client);
	}
	
	/**
	 * Unsubscribes a client.
	 * @param client A handle to the client.
	 */
	void unsubsibe(ClientHandler client) {
		clients.remove(client);
	}
}
