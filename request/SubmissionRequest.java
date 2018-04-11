package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;

public class SubmissionRequest implements Request {
	private static final long serialVersionUID = 1L;
	private final int assignmentId;

	public SubmissionRequest(Assignment assignment) {
		assignmentId = assignment.getId();
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		server.sendSubmissions(assignmentId);
	}
}
