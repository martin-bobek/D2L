package message;

import java.io.IOException;
import java.sql.SQLException;

import server.ClientHandler;

public class RequestCourses implements Request {
	private static final long serialVersionUID = 1L;

	public void performAction(ClientHandler server) throws IOException, SQLException {
		server.sendCourses();
	}
}
