package message;

import java.io.IOException;
import java.sql.SQLException;

import data.Course;

public class RequestAssignments implements Request {
	private static final long serialVersionUID = 1L;
	private static final int ALREADY_SELECTED = -1;
	private final int courseId;

	public RequestAssignments() {
		courseId = ALREADY_SELECTED;
	}
	
	public RequestAssignments(Course select) {
		courseId = select.getId();
	}
	
	public void performAction(RequestHandler server) throws IOException, SQLException {
		if (courseId != ALREADY_SELECTED)
			server.selectCourse(courseId);
		server.sendAssignments();
	}
}
