package server;

import java.util.Vector;

class ChatEventDistributor {
	private Vector<ClientHandler> clients;
	
	ChatEventDistributor() {
		clients = new Vector<ClientHandler>();
	}
	
	void notifyMessage(int messageId, int courseId) {
		synchronized(clients) {
			for (ClientHandler client : clients)
				client.newMessage(messageId, courseId);
		}
	}
	
	void subscribe(ClientHandler client) {
		clients.addElement(client);
	}
	
	void unsubsibe(ClientHandler client) {
		clients.remove(client);
	}
}
