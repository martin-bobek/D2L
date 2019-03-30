package shared.request;

import java.io.IOException;
import java.sql.SQLException;

/**
 * A request for all the courses visible to the particular user.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class CourseRequest implements Request {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Asks the server to send all the courses visible to the user.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException {
		server.sendCourses();
	}
}
