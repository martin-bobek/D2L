package request;

import java.io.IOException;
import java.sql.SQLException;

import data.Assignment;

/**
 * A message to the server to update or create an assignment record.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class AssignmentUpdate implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The assignment which was created or updated.
	 */
	private final Assignment assignment;
	
	/**
	 * Creates a new update with the modified assignment.
	 * @param assignment The assignment which was created/modified.
	 */
	public AssignmentUpdate(Assignment assignment) {
		this.assignment = assignment;
	}

	/**
	 * If the assignment is new, asks the server to create a new record.
	 * Otherwise, asks the server to modify the existing record.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (assignment.getId() == Assignment.NEW_ID) 
			server.createAssignment(assignment);
		else
			server.updateAssignment(assignment);
	}
}
