package serverMessage;

import java.io.IOException;
import java.sql.SQLException;

public class CourseRequest implements Request {
	private static final long serialVersionUID = 1L;
	
	public void performAction(ServerInterface server) throws IOException, SQLException {
		server.sendCourses();
	}
}
