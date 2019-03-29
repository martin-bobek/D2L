package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;
import data.Submission;

/**
 * A request to the server to deliver a particular file.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class FileRequest implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The id of the file requested.
	 */
	private final int id;
	/**
	 * The type of file to retrieve (assignment/submission)
	 */
	private final char type;

	/**
	 * Creates a request for an assignment file.
	 * @param assignment The assignment for which to retrieve the file.
	 */
	public FileRequest(Assignment assignment) {
		type = 'A';
		id = assignment.getId();
	}
	
	/**
	 * Creates a request for a submission file.
	 * @param submission The submission for which to retrieve the file.
	 */
	public FileRequest(Submission submission) {
		type = 'S';
		id = submission.getId();
	}
	
	/**
	 * Asks the server to send the file to the client.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		server.sendFile(type, id);
	}	
}
