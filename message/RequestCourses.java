package message;

import java.io.IOException;
import java.sql.SQLException;

public class RequestCourses implements Request {
	private static final long serialVersionUID = 1L;
	
	public void performAction(RequestHandler server) throws IOException, SQLException {
		server.sendCourses();
	}
}
