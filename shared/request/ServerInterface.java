package shared.request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

import shared.data.Assignment;
import shared.data.Course;
import shared.data.Student;
import shared.data.Submission;

/**
 * The functions server must implement as an interface for the request
 * messages it receives from the client. Necessary so that the request
 * package can be used client side without server packages. 
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface ServerInterface {
	/**
	 * Tells the server which course the client is viewing.
	 * @param courseId The id of the course viewed by the client.
	 */
	public void selectCourse(int courseId);
	/**
	 * Ask the server to send all the courses visible to the user.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendCourses() throws IOException, SQLException;
	/**
	 * Updates an already existing course.
	 * @param updated The course to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateCourse(Course updated) throws SQLException;
	/**
	 * Creates an new course.
	 * @param course The newly created course.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createCourse(Course course) throws SQLException, IOException;
	/**
	 * Asks the server to send all the assignments for a course. 
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 */
	public void sendAssignments() throws IOException, SQLException, ParseException;
	/**
	 * Updates an already existing assignment.
	 * @param updated The assignment to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateAssignment(Assignment updated) throws SQLException;
	/**
	 * Creates a new assignment.
	 * @param assignment The newly created assignment.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createAssignment(Assignment assignment) throws SQLException, IOException;
	/**
	 * Asks the server to send only enrolled students 
	 * which fit the desired search criteria.
	 * @param type The type of search to be performed.
	 * @param parameter The search parameter used for the search.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendEnrolledStudents(int type, Object parameter) throws IOException, SQLException;
	/**
	 * Asks the server to send all students 
	 * which fit the desired search criteria.
	 * @param type The type of search to be performed.
	 * @param parameter The search parameter used for the search.
	 * @throws IOException Failed to send serialized objects.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendAllStudents(int type, Object parameter) throws IOException, SQLException;
	/**
	 * Updates an already existing student.
	 * @param updated The student to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateStudent(Student updated) throws SQLException;
	/**
	 * Creates a submission to an assignment drop box.
	 * @param submission The student submission.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createSubmission(Submission submission) throws SQLException, IOException;
	/**
	 * Asks the server to send all the submissions for an assignment.
	 * @param assignmentId The assignment id used to select submissions.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 */
	public void sendSubmissions(int assignmentId) throws IOException, SQLException, ParseException;
	/**
	 * Asks the server to send a particular file.
	 * @param type The type of the file (submission or assignment).
	 * @param id The id of the file.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void sendFile(char type, int id) throws IOException;
	/**
	 * Updates an already existing submission.
	 * @param submission The submission which was updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateSubmission(Submission submission) throws SQLException;
	/**
	 * Asks the server to send an email.
	 * @param subject The subject of the email.
	 * @param content The content of the email.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws MessagingException Failed to send email message.
	 */
	public void sendEmail(String subject, String content) throws SQLException, MessagingException;
	/**
	 * Subscribes the client to receive chat updates for a particular course. Responds with all
	 * the previous messages from this chat.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void subscribeChat() throws SQLException, IOException;
	/**
	 * Submits a new message to a chat.
	 * @param message The message sent by the client.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void submitMessage(String message) throws SQLException;
}
