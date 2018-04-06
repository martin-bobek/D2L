package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import data.Assignment;
import data.Course;
import data.LoginCredentials;
import data.Student;
import message.Request;
import message.RequestHandler;
import message.RequestStudents;

public class ClientHandler implements Runnable, RequestHandler {
	private final static String FILE_STORAGE = "C:\\Users\\Martin\\Desktop\\AppStorage"; 
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private DatabaseManager database;
	private FileManager files;
	
	ClientHandler(Socket socket) throws IOException, SQLException {
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		database = new DatabaseManager();
		files = new FileManager(FILE_STORAGE);
	}
	
	public void run() {
		try {
			login();
			while (true)
				((Request)input.readObject()).performAction(this);
		} catch (SQLException | ClassNotFoundException | ParseException e) {
			System.err.println("Error 2: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Client disconnected.");
			return;
		}
	}
	
	public void selectCourse(int courseId) {
		database.selectCourse(courseId);
	}
	
	public void sendCourses() throws IOException, SQLException {
		output.writeObject(database.getCourses());
	}
	
	public void updateCourse(Course updated) throws SQLException {
		database.updateCourse(updated);
	}
	
	public void createCourse(Course course) throws SQLException, IOException {
		database.createCourse(course);
		course.setId(database.getLastId());
		ArrayList<Course> courseList = new ArrayList<Course>();
		courseList.add(course);
		output.writeObject(courseList);
	}
	
	public void sendAssignments() throws IOException, SQLException, ParseException {
		output.writeObject(database.getAssignments());
	}
	
	public void updateAssignment(Assignment updated) throws SQLException {
		database.updateAssignment(updated);
	}
	
	public void createAssignment(Assignment assignment) throws SQLException, IOException {
		database.createAssignment(assignment);
		assignment.setId(database.getLastId());
		files.setPath(FileManager.ASSIGNMENT, assignment.getId());
		files.storeFile(assignment.getFile());
		ArrayList<Assignment> assList = new ArrayList<Assignment>();
		assList.add(assignment);
		output.writeObject(assList);
	}
	
	public void sendEnrolledStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == RequestStudents.NAME)
			database.getEnrolledStudents((String)parameter);
		else if (type == RequestStudents.ID)
			database.getEnrolledStudents((int)parameter);
		else
			database.getEnrolledStudents();
		output.writeObject(database.getStudents(false));
	}
	
	public void sendAllStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == RequestStudents.NAME)
			database.getAllStudents((String)parameter);
		else if (type == RequestStudents.ID)
			database.getAllStudents((int)parameter);
		else
			database.getAllStudents();
		output.writeObject(database.getStudents(true));
	}
	
	public void updateStudent(Student updated) throws SQLException {
		if (updated.getEnrolled())
			database.createEnrollment(updated);
		else
			database.deleteEnrollment(updated);
	}
	
	private void login() throws ClassNotFoundException, SQLException, IOException {
		char response;
		do {
			LoginCredentials credentials = (LoginCredentials)input.readObject();
			response = database.validateLogin(credentials);
			output.writeObject(response);
		} while (response == LoginCredentials.BAD_LOGIN);
	}
}
