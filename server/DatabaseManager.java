package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import clientMessage.LoginResponse;
import data.Assignment;
import data.Course;
import data.LoginCredentials;
import data.Student;
import data.Submission;

class DatabaseManager implements SqlQueries {
	private static final String DATABASE = "ApplicationData";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	private static final String USER = "martin";	// TODO - Create proper account for application
	private static final String PASSWORD = "966567"; 
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyddMMHHmmss");
	private Connection connection;
	private ResultSet results;
	private int userId;
	private boolean isProf;
	private int courseId;
	
	DatabaseManager() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	void selectCourse(int courseId) {
		this.courseId = courseId;
	}
	
	LoginResponse validateLogin(LoginCredentials credentials) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(LOGIN);
		statement.setInt(1, credentials.getUserId());
		statement.setString(2, credentials.getPassword());
		results = statement.executeQuery();
		if (results.next()) {
			userId = credentials.getUserId();
			char userType = results.getString(1).charAt(0);
			isProf = userType == 'P'; 
			return new LoginResponse(userType, results.getString(2) + ' ' + results.getString(3));
		}
		return new LoginResponse();
	}
	
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
	
	void updateCourse(Course updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE);
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}
	
	void createCourse(Course course) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_COURSE);
		statement.setInt(1, userId);
		statement.setString(2, course.getName());
		statement.setBoolean(3, false);
		statement.executeUpdate();
	}
	
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
	
	void updateAssignment(Assignment updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_ASSIGNMENT);
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}

	void createAssignment(Assignment assignment) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT);
		statement.setInt(1, courseId);
		statement.setString(2, assignment.getTitle());
		statement.setString(3, assignment.getExtension());
		statement.setBoolean(4, assignment.getActive());
		statement.setString(5, dateFormat.format(assignment.getDueDate()));
		statement.executeUpdate();
	}
	
	void getAllStudents(String name) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_NAME_STUDENT);
		statement.setInt(1, courseId);
		statement.setString(2, name);
		results = statement.executeQuery();
	}
	
	void getAllStudents(int id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_ID_STUDENT);
		statement.setInt(1, courseId);
		statement.setInt(2, id);
		results = statement.executeQuery();
	}
	
	void getAllStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ALL_STUDENT);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
	}
	
	void getEnrolledStudents(String name) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_NAME_STUDENT);
		statement.setInt(1, courseId);
		statement.setString(2, name);
		results = statement.executeQuery();
	}
	
	void getEnrolledStudents(int id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_ID_STUDENT);
		statement.setInt(1, courseId);
		statement.setInt(2, id);
		results = statement.executeQuery();
	}
	
	void getEnrolledStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(GET_ENROLLED_STUDENT);
		statement.setInt(1, courseId);
		results = statement.executeQuery();
	}
	
	ArrayList<Student> getStudents(boolean all) throws SQLException {
		ArrayList<Student> students = new ArrayList<Student>();
		while (results.next())
			students.add(new Student(results.getInt(1), results.getString(2), results.getString(3), all ? results.getBoolean(4) : true ));
		return students;
	}
	
	void createEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ENROLLEMENT);
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.setInt(3, student.getId());
		statement.setInt(4, courseId);
		statement.executeUpdate();
	}
	
	void deleteEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(DELETE_ENROLLEMENT);
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.executeUpdate();
	}
	
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
	
	public void updateSubmission(Submission submission) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_SUBMISSION);
		statement.setInt(1, submission.getGrade());
		statement.setInt(2, submission.getId());
		statement.executeUpdate();
	}
	
	int getLastId() throws SQLException {
		results = connection.prepareStatement(GET_LAST_ID).executeQuery();
		results.next();
		return results.getInt(1);
	}
}
