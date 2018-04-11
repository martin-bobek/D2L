package request;

import java.io.IOException;
import java.sql.SQLException;

import data.Student;

public class StudentUpdate implements Request {
	private static final long serialVersionUID = 1L;
	private final Student updated;

	public StudentUpdate(Student updated) {
		this.updated = updated;
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException {
		server.updateStudent(updated);
	}
}
