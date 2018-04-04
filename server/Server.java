package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

class Server {
	private ServerSocket serverSocket;
	private ExecutorService pool;
	
	Server() throws IOException {
		serverSocket = new ServerSocket(9898);
		pool = Executors.newCachedThreadPool();
		System.out.println("Server running...");
	}
	
	void runServer() {
		while (true) {
			try {
				pool.execute(new ClientHandler(serverSocket.accept()));
			} catch (IOException | SQLException e) {
				System.err.println("Error 1: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.runServer();
		} catch (IOException e) {
			System.err.println("Fatal Error: " + e.getMessage());
		}
	}
}
