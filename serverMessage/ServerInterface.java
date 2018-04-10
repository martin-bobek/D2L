package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import data.Assignment;
import data.Course;
import data.Student;
import data.Submission;

public interface ServerInterface {
	public void selectCourse(int courseId);
	public void sendCourses() throws IOException, SQLException;
	public void updateCourse(Course updated) throws SQLException;
	public void createCourse(Course course) throws SQLException, IOException;
	public void sendAssignments() throws IOException, SQLException, ParseException;
	public void updateAssignment(Assignment updated) throws SQLException;
	public void createAssignment(Assignment assignment) throws SQLException, IOException;
	public void sendEnrolledStudents(int type, Object parameter) throws IOException, SQLException;
	public void sendAllStudents(int type, Object parameter) throws IOException, SQLException;
	public void updateStudent(Student updated) throws SQLException;
	public void createSubmission(Submission submission) throws SQLException, IOException;
	public void sendSubmissions(int assignmentId) throws IOException, SQLException, ParseException;
	public void sendFile(char type, int id) throws IOException;
}
