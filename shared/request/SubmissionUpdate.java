package shared.request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import shared.data.Submission;

/**
 * A message used to indicate that a submission has been created or updated.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class SubmissionUpdate implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The submission which has been created/updated.
	 */
	private final Submission submission;
	
	/**
	 * Creates a new update with the desired submission.
	 * @param submission The submission which was updated.
	 */
	public SubmissionUpdate(Submission submission) {
		this.submission = submission;
	}
	
	/**
	 * If the submission is new, gets a server to add the submission to the database. Otherwise
	 * asks the server to update the current record.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		if (submission.getId() == Submission.NEW_ID)
			server.createSubmission(submission);
		else
			server.updateSubmission(submission);
	}
}
