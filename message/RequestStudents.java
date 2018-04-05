package message;

import java.io.IOException;
import java.sql.SQLException;

import server.ClientHandler;

public class RequestStudents implements Request {
	private static final long serialVersionUID = 1L;
	private final boolean onlyEnrolled;
	
	public RequestStudents(boolean onlyEnrolled) {
		this.onlyEnrolled = onlyEnrolled;
	}
	
	public void performAction(ClientHandler server) throws IOException, SQLException {
		if (onlyEnrolled)
			server.sendEnrolledStudents();
		else
			server.sendAllStudents();
	}
}
