package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Course;

public class AssignmentRequest implements Request {
	private static final long serialVersionUID = 1L;
	private static final int ALREADY_SELECTED = -1;
	private final int courseId;

	public AssignmentRequest() {
		courseId = ALREADY_SELECTED;
	}
	
	public AssignmentRequest(Course select) {
		courseId = select.getId();
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		if (courseId != ALREADY_SELECTED)
			server.selectCourse(courseId);
		server.sendAssignments();
	}
}
