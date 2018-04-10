package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;

public class FileRequest implements Request {
	private static final long serialVersionUID = 1L;
	private final int id;

	public FileRequest(Assignment assignment) {
		id = assignment.getId();
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		server.sendFile('A', id);
	}	
}
