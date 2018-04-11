package request;

import java.io.IOException;
import java.sql.SQLException;

import data.Assignment;

public class AssignmentUpdate implements Request {
	private static final long serialVersionUID = 1L;
	private final Assignment assignment;
	
	public AssignmentUpdate(Assignment assignment) {
		this.assignment = assignment;
	}

	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (assignment.getId() == Assignment.NEW_ID) 
			server.createAssignment(assignment);
		else
			server.updateAssignment(assignment);
	}
}
