package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.ParseException;

import clientMessage.FileDelivery;
import clientMessage.LoginResponse;
import clientMessage.TableUpdate;
import data.Assignment;
import data.Course;
import data.LoginCredentials;
import data.Student;
import data.Submission;
import serverMessage.Request;
import serverMessage.ServerInterface;
import serverMessage.StudentRequest;

public class ClientHandler implements Runnable, ServerInterface {
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
			while (true) {
				Request request = (Request)input.readObject();
				request.performAction(this);
			}
		} catch (SocketException e) {
			System.out.println("Client disconnected.");
		} catch (SQLException | ClassNotFoundException | ParseException | IOException e) {
			System.err.println("Error 2: " + e.getMessage());
		}
	}
	
	public void selectCourse(int courseId) {
		database.selectCourse(courseId);
	}
	
	public void sendCourses() throws IOException, SQLException {
		output.writeObject(new TableUpdate(database.getCourses()));
	}
	
	public void updateCourse(Course updated) throws SQLException {
		database.updateCourse(updated);
	}
	
	public void createCourse(Course course) throws SQLException, IOException {
		database.createCourse(course);
		course.setId(database.getLastId());
		output.writeObject(new TableUpdate(course));
	}
	
	public void sendAssignments() throws IOException, SQLException, ParseException {
		output.writeObject(new TableUpdate(database.getAssignments()));
	}
	
	public void updateAssignment(Assignment updated) throws SQLException {
		database.updateAssignment(updated);
	}
	
	public void createAssignment(Assignment assignment) throws SQLException, IOException {
		database.createAssignment(assignment);
		assignment.setId(database.getLastId());
		files.setPath(FileManager.ASSIGNMENT, assignment.getId());
		files.storeFile(assignment.getFile());
		output.writeObject(new TableUpdate(assignment));
	}
	
	public void sendEnrolledStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == StudentRequest.NAME)
			database.getEnrolledStudents((String)parameter);
		else if (type == StudentRequest.ID)
			database.getEnrolledStudents((int)parameter);
		else
			database.getEnrolledStudents();
		output.writeObject(new TableUpdate(database.getStudents(false)));
	}
	
	public void sendAllStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == StudentRequest.NAME)
			database.getAllStudents((String)parameter);
		else if (type == StudentRequest.ID)
			database.getAllStudents((int)parameter);
		else
			database.getAllStudents();
		output.writeObject(new TableUpdate(database.getStudents(true)));
	}
	
	public void updateStudent(Student updated) throws SQLException {
		if (updated.getEnrolled())
			database.createEnrollment(updated);
		else
			database.deleteEnrollment(updated);
	}
	
	public void updateSubmission(Submission submission) throws SQLException {
		database.updateSubmission(submission);
	}
	
	public void createSubmission(Submission submission) throws SQLException, IOException {
		database.createSubmission(submission);
		submission.setId(database.getLastId());
		files.setPath(FileManager.SUBMISSION, submission.getId());
		files.storeFile(submission.getFile());
	}
	
	public void sendSubmissions(int assignmentId) throws IOException, SQLException, ParseException {
		output.writeObject(new TableUpdate(database.getSubmissions(assignmentId)));
	}
	
	public void sendFile(char type, int id) throws IOException {
		files.setPath(type, id);
		byte[] content = files.retreiveFile();
		output.writeObject(new FileDelivery(content));
	}
	
	private void login() throws ClassNotFoundException, SQLException, IOException {
		LoginResponse response;
		do {
			LoginCredentials credentials = (LoginCredentials)input.readObject();
			response = database.validateLogin(credentials);
			output.writeObject(response);
		} while (!response.success());
	}
}
