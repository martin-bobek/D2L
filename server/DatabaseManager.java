package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Assignment;
import data.Course;
import data.LoginCredentials;
import data.Student;

class DatabaseManager {
	private static final String DATABASE = "ApplicationData";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	private static final String USER = "martin";	// TODO - Create proper account for application
	private static final String PASSWORD = "966567";
	
	private Connection connection;
	private ResultSet results;
	private int profId;
	private int courseId;
	
	DatabaseManager(int profId) throws SQLException {
		this.profId = profId;
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	void selectCourse(int courseId) {
		this.courseId = courseId;
	}
	
	char validateLogin(LoginCredentials credentials) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT ID, TYPE FROM USER WHERE EMAIL = ? AND PASSWORD = ?");
		statement.setString(1, credentials.getUsername());
		statement.setString(2, credentials.getPassword());
		results = statement.executeQuery();
		if (results.next()) {
			profId = results.getInt(1);
			return results.getString(2).charAt(0);
		}
		return LoginCredentials.BAD_LOGIN;
	}
	
	ArrayList<Course> getCourses() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT ID, NAME, ACTIVE FROM COURSE WHERE PROF_ID = ?");
		statement.setInt(1, profId);
		results = statement.executeQuery();
		ArrayList<Course> courses = new ArrayList<Course>();
		while (results.next())
			courses.add(new Course(results.getInt(1), results.getString(2), results.getBoolean(3)));
		return courses;
	}
	
	void updateCourse(Course updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE COURSE SET ACTIVE = ? WHERE ID = ?");
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}
	
	void createCourse(Course course) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO COURSE (PROF_ID, NAME, ACTIVE) VALUES (?, ?, ?)");
		statement.setInt(1, profId);
		statement.setString(2, course.getName());
		statement.setBoolean(3, false);
		statement.executeUpdate();
	}
	
	ArrayList<Assignment> getAssignments() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT ID, TITLE, ACTIVE, DUE_DATE FROM ASSIGNMENT WHERE COURSE_ID = ?");
		statement.setInt(1, courseId);
		results = statement.executeQuery();
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		while (results.next())
			assignments.add(new Assignment(results.getInt(1), results.getString(2), results.getBoolean(3), results.getString(4)));
		return assignments;
	}
	
	void updateAssignment(Assignment updated) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE ASSIGNMENT SET ACTIVE = ? WHERE ID = ?");
		statement.setBoolean(1, updated.getActive());
		statement.setInt(2, updated.getId());
		statement.executeUpdate();
	}

	void createAssignment(Assignment assignment) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO ASSIGNMENT (COURSE_ID, TITLE, PATH, ACTIVE, DUE_DATE) VALUES (?, ?, ?, ?, ?)");
		statement.setInt(1, courseId);
		statement.setString(2, assignment.getTitle());
		statement.setString(3, "");
		statement.setBoolean(4, assignment.getActive());
		statement.setString(5, assignment.getDueDate());
		statement.executeUpdate();
	}
	
	ArrayList<Student> getAllStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0b0 ELSE 0b1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' ORDER BY U.ID");
		statement.setInt(1, courseId);
		results = statement.executeQuery();
		ArrayList<Student> students = new ArrayList<Student>();
		while (results.next())
			students.add(new Student(results.getInt(1), results.getString(2), results.getString(3), results.getBoolean(4)));
		return students;
	}
	
	ArrayList<Student> getEnrolledStudents() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? ORDER BY U.ID");
		statement.setInt(1, courseId);
		results = statement.executeQuery();
		ArrayList<Student> students = new ArrayList<Student>();
		while (results.next())
			students.add(new Student(results.getInt(1), results.getString(2), results.getString(3), true));
		return students;
	}
	
	void createEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO STUDENT_ENROLLMENT (STUDENT_ID, COURSE_ID) SELECT ?, ? FROM STUDENT_ENROLLMENT WHERE NOT EXISTS (SELECT * FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?) LIMIT 1");
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.setInt(3, student.getId());
		statement.setInt(4, courseId);
		statement.executeUpdate();
	}
	
	void deleteEnrollment(Student student) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?");
		statement.setInt(1, student.getId());
		statement.setInt(2, courseId);
		statement.executeUpdate();
	}
	
	int getLastId() throws SQLException {
		results = connection.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
		results.next();
		return results.getInt(1);
	}
}
