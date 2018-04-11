package request;

import java.io.IOException;
import java.sql.SQLException;

import data.Course;

public class CourseUpdate implements Request {
	private static final long serialVersionUID = 1L;
	private final Course course;

	public CourseUpdate(Course course) {
		this.course = course;
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (course.getId() == Course.NEW_ID)
			server.createCourse(course);
		else
			server.updateCourse(course);
	}
}
