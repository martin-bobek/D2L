package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Submission;

public class SubmissionUpdate implements Request {
	private static final long serialVersionUID = 1L;
	private final Submission submission;
	
	public SubmissionUpdate(Submission submission) {
		this.submission = submission;
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		if (submission.getId() == Submission.NEW_ID)
			server.createSubmission(submission);
		else
			server.updateSubmission(submission);
	}
}
