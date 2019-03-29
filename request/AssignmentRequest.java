package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Course;

/**
 * A request to the server to send the assignments for a course.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class AssignmentRequest implements Request {
	private static final long serialVersionUID = 1L;
	private static final int ALREADY_SELECTED = -1;
	/** 
	 * The course ID of the course for which the assignments should be retrieved.
	 */
	private final int courseId;

	/**
	 * Creates a new request for assignments, where the course has already been previously selected.
	 */
	public AssignmentRequest() {
		courseId = ALREADY_SELECTED;
	}
	
	/**
	 * Creates a new request for assignments, where a new course was just selected by the user.
	 * @param select The course which the user would like to view.
	 */
	public AssignmentRequest(Course select) {
		courseId = select.getId();
	}
	
	/**
	 * Tells the server which course the client is currently looking at and asks for all the assignments
	 * for that course to be sent to the client.
	 * @param server A handle to the server.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException {
		if (courseId != ALREADY_SELECTED)
			server.selectCourse(courseId);
		server.sendAssignments();
	}
}
