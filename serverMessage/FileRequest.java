package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;
import data.Submission;

public class FileRequest implements Request {
	private static final long serialVersionUID = 1L;
	private final int id;
	private final char type;

	public FileRequest(Assignment assignment) {
		type = 'A';
		id = assignment.getId();
	}
	
	public FileRequest(Submission submission) {
		type = 'S';
		id = submission.getId();
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		server.sendFile(type, id);
	}	
}
