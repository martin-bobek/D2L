package shared.request;

import java.io.IOException;
import java.sql.SQLException;

import shared.data.Course;

/**
 * A message to the server to create/update a course.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class CourseUpdate implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The course which was created or updated.
	 */
	private final Course course;

	/**
	 * Creates a new message with the desired course. 
	 * @param course The course which was updated/modified.
	 */
	public CourseUpdate(Course course) {
		this.course = course;
	}
	
	/**
	 * If the course is new, asks the server to create a new record.
	 * Otherwise asks the server to update the existing record.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (course.getId() == Course.NEW_ID)
			server.createCourse(course);
		else
			server.updateCourse(course);
	}
}
