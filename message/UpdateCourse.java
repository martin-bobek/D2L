package message;

import java.io.IOException;
import java.sql.SQLException;

import data.Course;
import server.ClientHandler;

public class UpdateCourse implements Request {
	private static final long serialVersionUID = 1L;
	private final Course course;

	public UpdateCourse(Course course) {
		this.course = course;
	}
	
	public void performAction(ClientHandler server) throws IOException, SQLException {
		if (course.getId() == Course.NEW_ID)
			server.createCourse(course);
		else
			server.updateCourse(course);
	}
}
