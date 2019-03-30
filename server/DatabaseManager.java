package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import shared.data.Assignment;
import shared.data.ChatMessage;
import shared.data.Course;
import shared.data.LoginCredentials;
import shared.data.Student;
import shared.data.Submission;
import shared.response.LoginResponse;

/**
 * This class is used to connect the client handler to the database.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class DatabaseManager implements SqlQueries {
	/**
	 * The database name.
	 */
	private static final String DATABASE = "ApplicationData";
	/**
	 * The URL used to access the database.
	 */
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	/**
	 * The name of the user.
	 */
	private static final String USER = "martin";	// TODO - Create proper account for application
	/**
	 * The password used to access the database.
	 */
	private static final String PASSWORD = "966567";
	/**
	 * Used to format due dates for the assignment table.
	 */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
	/**
	 * Used to format submission time stamps for the submission table.
	 */
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyddMMHHmmss");
	/**
	 * A connection to the database.
	 */
	private Connection connection;
	/**
	 * The results received from database queries.
	 */
	private ResultSet results;
	/**
	 * The id of the current user.
	 */
	private int userId;
	/**
	 * Indicates whether the user is a professor or student.
	 */
	private boolean isProf;
	/**
	 * The id of the currently selected course in the client.
	 */
	private int courseId;
	
	/**
	 * Create a new connection to the database.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	DatabaseManager() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	/**
	 * Selects which course the user is currently viewing.
	 * @param courseId The course id.
	 */
	void selectCourse(int courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * Gets the user information for a pair of login credentials, 
	 * if it exists.
	 * @param credentials The credentials entered at the client.
	 * @return The login data for the user.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	LoginData validateLogin(LoginCredentials credentials) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(LOGIN);
		statement.setInt(1, credentials.getUserId());
		statement.setString(2, credentials.getPassword());
		results = statement.executeQuery();
		if (results.next()) {
			userId = credentials.getUserId();
			char userType = results.getString(1).charAt(0);
			isProf = userType == 'P'; 
			return new LoginData(new LoginResponse(userType, results.getString(2) + ' ' + results.getString(3)), 
					results.getString(4), results.getString(5));
		}
		return new LoginData();
	}
	
	/**
	 * Gets all the courses the user can see.
	 * @return A list of courses.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ArrayList<Course> getCourses() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(isProf ? GET_PROF_COURSE : GET_STUDENT_COURSE);
		statement.setInt(1, userId);
		results = statement.executeQuery();
		ArrayList<Course> courses = new ArrayList<Course>();
		while (results.next())
			courses.add(new Course(results.getInt(1), results.getString(2), 
					isProf ? results.getBoolean(3) : true, 
					isProf ? null : results.getString(3)));
		return courses;
	}
	
	/**
	 * Updates the active state of a course.
	 * @param updated The course to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void updateCourse(Course updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE);
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}
	
	/**
	 * Creates a new course.
	 * @param course The new course.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void createCourse(Course course) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_COURSE);
		statement.setInt(1, userId);
		statement.setString(2, course.getName());
		statement.setBoolean(3, false);
		statement.executeUpdate();
	}
	
	/**
	 * Gets all the assignments visible to the user.
	 * @return A list of assignments.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 */
	ArrayList<Assignment> getAssignments() throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement(isProf ? GET_PROF_ASSIGNMENT : GET_STUDENT_ASSIGNMENT);
		if (!isProf)
			statement.setInt(1, userId);
		statement.setInt(isProf ? 1 : 2, courseId);
		results = statement.executeQuery();
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		while (results.next())
			assignments.add(new Assignment(results.getInt(1), results.getString(2), results.getBoolean(3), 
					dateFormat.parse(results.getString(4)), isProf ? null : results.getString(5), isProf ? 0 : results.getInt(6)));
		return assignments;
	}
	
	/**
	 * Updates the active state of an assignment.
	 * @param updated The assignment to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void updateAssignment(Assignment updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_ASSIGNMENT);
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}

	/**
	 * Creates a new assignment record.
	 * @param assignment The new assignment.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void createAssignment(Assignment assignment) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT);
		statement.setInt(1, courseId);
		statement.setString(2, assignment.getTitle());
		statement.setString(3, assignment.getExtension());
		statement.setBoolean(4, assignment.getActive());
		statement.setString(5, dateFormat.format(assignment.getDueDate()));
		statement.executeUpdate();
	}
	
	/**
	 * Gets all the students with a particular name.
	 * @param name The name to search for.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getAllStudents(String name) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_NAME_STUDENT);
		statement.setInt(1, courseId);
		statement.setString(2, name);
		results = statement.executeQuery();
	}
	
	/**
	 * Gets all the students with a particular id number.
	 * @param id The id number to search for.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getAllStudents(int id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_ID_STUDENT);
		statement.setInt(1, courseId);
		statement.setInt(2, id);
		results = statement.executeQuery();
	}
	
	/**
	 * Gets all the students.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getAllStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_STUDENT);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
	}
	
	/**
	 * Gets all students enrolled in the current course with a particular name.
	 * @param name The name to search for.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getEnrolledStudents(String name) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_NAME_STUDENT);
		statement.setInt(1, courseId);
		statement.setString(2, name);
		results = statement.executeQuery();
	}
	
	/**
	 * Gets all students enrolled in the current course with a particular id. 
	 * @param id The id to search for.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getEnrolledStudents(int id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_ID_STUDENT);
		statement.setInt(1, courseId);
		statement.setInt(2, id);
		results = statement.executeQuery();
	}
	
	/**
	 * Gets all the students enrolled in the current course.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void getEnrolledStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_STUDENT);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
	}
	
	/**
	 * Returns the students obtained from the server in the previous query.
	 * @param all Indicates if all students or only enrolled students were queried.
	 * @return The list of students.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ArrayList<Student> getStudents(boolean all) throws SQLException {
		ArrayList<Student> students = new ArrayList<Student>();
		while (results.next())
			students.add(new Student(results.getInt(1), results.getString(2), results.getString(3), all ? results.getBoolean(4) : true ));
		return students;
	}
	
	/**
	 * Enrolls a student to a course.
	 * @param student The student to enroll.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void createEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ENROLLEMENT);
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.setInt(3, student.getId());
		statement.setInt(4, courseId);
		statement.executeUpdate();
	}
	
	/**
	 * Unenrolls a student from a course.
	 * @param student The student to unenroll.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void deleteEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(DELETE_ENROLLEMENT);
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.executeUpdate();
	}
	
	/**
	 * Creates a new submission record.
	 * @param submission The new submission.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	void createSubmission(Submission submission) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_SUBMISSION);
		statement.setInt(1, submission.getAssignmentId());
		statement.setInt(2, userId);
		statement.setString(3, submission.getExtension());
		statement.setString(4, submission.getName());
		statement.setString(5, timestampFormat.format(submission.getTimestamp()));
		statement.setInt(6, Submission.NO_GRADE);
		statement.executeUpdate();
	}
	
	/**
	 * Gets all the submission records for an assignment.
	 * @param assignmentId The assignment id.
	 * @return A list of all the submissions.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 */
	ArrayList<Submission> getSubmissions(int assignmentId) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement(GET_SUBMISSION);
		statement.setInt(1, assignmentId);
		results = statement.executeQuery();
		ArrayList<Submission> submissions = new ArrayList<Submission>();
		while (results.next())
			submissions.add(new Submission(results.getInt(1), results.getString(2), results.getString(3), 
					timestampFormat.parse(results.getString(4)), results.getString(5) + ' ' + results.getString(6), results.getInt(7)));
		return submissions;
	}
	
	/**
	 * Updates the grade of a submission.
	 * @param submission The submission to update.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateSubmission(Submission submission) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_SUBMISSION);
		statement.setInt(1, submission.getGrade());
		statement.setInt(2, submission.getId());
		statement.executeUpdate();
	}
	
	/**
	 * Gets the id of the last insertion.
	 * @return The insertion id.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	int getLastId() throws SQLException {
		results = connection.prepareStatement(GET_LAST_ID).executeQuery();
		results.next();
		return results.getInt(1);
	}

	/**
	 * Gets all the recipients for an email sent by the user.
	 * @return A list of email addresses.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ArrayList<String> getRecipients() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(isProf ? PROF_RECIPIENTS : STUDENT_RECIPIENT);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
		ArrayList<String> emails = new ArrayList<String>();
		while (results.next())
			emails.add(results.getString(1));
		return emails;
	}

	/**
	 * Gets a particular message from the database if its course id matches the
	 * course viewed by the client. 
	 * @param messageId The message id.
	 * @param courseId The course id to which the message belongs.
	 * @return The message or null.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ChatMessage getMessage(int messageId, int courseId) throws SQLException {
		if (courseId != this.courseId)
			return null;
		PreparedStatement statement = connection.prepareStatement(CHAT_ID_MESSAGE);
		statement.setInt(1, messageId);
		results = statement.executeQuery();
		if (!results.next())
			return null;
		return new ChatMessage(results.getString(1) + ' ' + results.getString(2), results.getString(3));
	}

	/**
	 * Gets all the chat messages for a particular course.
	 * @return A list of all the messages, in order.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ArrayList<ChatMessage> getAllMessages() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CHAT_ALL_MESSAGES);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
		ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
		while (results.next())
			messages.add(new ChatMessage(results.getString(1) + ' ' + results.getString(2), results.getString(3)));
		return messages;
	}

	/**
	 * Adds a message to the database.
	 * @param message The content of the message.
	 * @return The id of the course for which the message was sent.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	int addMessage(String message) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SUBMIT_MESSAGE);
		statement.setInt(1, userId);
		statement.setInt(2, courseId);
		statement.setString(3, message);
		statement.executeUpdate();
		return courseId;
	}
}
