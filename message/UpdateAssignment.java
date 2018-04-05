package message;

import java.io.IOException;
import java.sql.SQLException;

import data.Assignment;
import server.ClientHandler;

public class UpdateAssignment implements Request {
	private static final long serialVersionUID = 1L;
	private Assignment assignment;
	
	public UpdateAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public void performAction(ClientHandler server) throws IOException, SQLException {
		if (assignment.getId() == Assignment.NEW_ID) 
			server.createAssignment(assignment);
		else
			server.updateAssignment(assignment);
	}
}
