package message;

import java.io.IOException;
import java.sql.SQLException;

import data.Student;
import server.ClientHandler;

public class UpdateStudent implements Request {
	private static final long serialVersionUID = 1L;
	private final Student updated;

	public UpdateStudent(Student updated) {
		this.updated = updated;
	}
	
	public void performAction(ClientHandler server) throws IOException, SQLException {
		server.updateStudent(updated);
	}
}
