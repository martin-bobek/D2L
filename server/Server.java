package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * The main class for the server. 
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class Server {
	/**
	 * Accepts new requests from clients to connect to the server.
	 */
	private final ServerSocket serverSocket;
	/**
	 * The thread pool used to service the clients.
	 */
	private final ExecutorService pool;
	/**
	 * An event distributor used to notify subscribed clients that a chat message has been submitted.
	 */
	private final ChatEventDistributor chatEvents;
	
	/**
	 * Creates a new server. Port 9898 is reserved for server client communication.
	 * @throws IOException Failed to send serialized messages.
	 */
	Server() throws IOException {
		serverSocket = new ServerSocket(9898);
		pool = Executors.newCachedThreadPool();
		chatEvents = new ChatEventDistributor();
		System.out.println("Server running...");
	}
	
	/**
	 * Waits for new clients to connect and creates a new thread to service each client.
	 */
	void runServer() {
		while (true) {
			try {
				pool.execute(new ClientHandler(serverSocket.accept(), chatEvents));
			} catch (IOException | SQLException e) {
				System.err.println("Error 1: " + e.getMessage());
			}
		}
	}
	
	/**
	 * The main function which creates a new server object and then runs the server.
	 * @param args Command line arguments, not used.
	 */
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.runServer();
		} catch (IOException e) {
			System.err.println("Fatal Error: " + e.getMessage());
		}
	}
}
