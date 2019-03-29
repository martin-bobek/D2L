package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;

/**
 * A request to get submissions from the server.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class SubmissionRequest implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The id of the assignment for which to get submissions.
	 */
	private final int assignmentId;

	/**
	 * Creates a new request for submissions.
	 * @param assignment The assignment for which to get submissions.
	 */
	public SubmissionRequest(Assignment assignment) {
		assignmentId = assignment.getId();
	}
	
	/**
	 * Asks the server to send all the submissions for a particular assignment.
	 * @param server A handle to the server.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		server.sendSubmissions(assignmentId);
	}
}
