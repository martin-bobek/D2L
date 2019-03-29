package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import data.Assignment;
import data.ChatMessage;
import data.Course;
import data.LoginCredentials;
import data.Student;
import data.Submission;
import request.Request;
import request.ServerInterface;
import request.StudentRequest;
import response.ChatUpdate;
import response.FileDelivery;
import response.TableUpdate;

/**
 * A runnable class used to service the incoming requests of a particular client.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ClientHandler implements Runnable, ServerInterface {
	/**
	 * The location where files are stored in the server.
	 */
	private final static String FILE_STORAGE = "C:\\Users\\Martin\\Desktop\\AppStorage";
	/**
	 * The input stream from the client.
	 */
	private ObjectInputStream input;
	/**
	 * The output stream to the server.
	 */
	private ObjectOutputStream output;
	/**
	 * The connection to the database.
	 */
	private DatabaseManager database;
	/**
	 * A helper class used to manage the servers files.
	 */
	private FileManager files;
	/**
	 * The helper class used to send email's from the server.
	 */
	private EmailSender email;
	/**
	 * A link to the chat event distributor.
	 */
	private ChatEventDistributor chatEvents;
	
	/**
	 * Creates a new client handler, creating connections to the newly joined client.
	 * All helper classes are created.
	 * @param socket The socket to the new client.
	 * @param chatEvents A handle to the chat event distributor.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	ClientHandler(Socket socket, ChatEventDistributor chatEvents) throws IOException, SQLException {
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		database = new DatabaseManager();
		files = new FileManager(FILE_STORAGE);
		email = new EmailSender();
		this.chatEvents = chatEvents;
	}
	
	/**
	 * Runs the client. Waits for login requests from the client until a valid request is entered.
	 * It then receives and services standard requests from the client.
	 */
	public void run() {
		try {
			login();
			chatEvents.subscribe(this);
			while (true) {
				Request request = (Request)input.readObject();
				request.performAction(this);
			}
		} catch (SocketException e) {
			System.out.println("Client disconnected.");
		} catch (SQLException | ClassNotFoundException | ParseException | IOException | MessagingException e) {
			System.err.println("Error 2: " + e.getMessage());
		} finally {
			chatEvents.unsubsibe(this);
		}
	}
	
	/**
	 * Is called by the chat event distributor when a new message arrives.
	 * @param messageId The message id.
	 * @param courseId The course for which the message was received.
	 */
	public void newMessage(int messageId, int courseId) {
		try {
			ChatMessage message = database.getMessage(messageId, courseId);
			if (message != null)
				output.writeObject(new ChatUpdate(message));
		} catch (SQLException | IOException e) {
			System.err.println("Error 3: " + e.getMessage());
		}
	}
	
	/**
	 * Returns all the messages for a particular chat.
	 */
	public void subscribeChat() throws SQLException, IOException {
		ChatUpdate update = new ChatUpdate(database.getAllMessages());
		output.writeObject(update);
	}
	
	/**
	 * Submits a message to the server, storing it in the database and notifying all 
	 * other clients.
	 */
	public void submitMessage(String message) throws SQLException {
		int courseId = database.addMessage(message);
		int messageId = database.getLastId();
		chatEvents.notifyMessage(messageId, courseId);
	}
	
	/**
	 * Tells the server which course the client is viewing.
	 * @param courseId The id of the course viewed by the client.
	 */
	public void selectCourse(int courseId) {
		database.selectCourse(courseId);
	}
	
	/**
	 * Ask the server to send all the courses visible to the user.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendCourses() throws IOException, SQLException {
		output.writeObject(new TableUpdate(database.getCourses()));
	}
	
	/**
	 * Updates an already existing course.
	 * @param updated The course to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateCourse(Course updated) throws SQLException {
		database.updateCourse(updated);
	}
	
	/**
	 * Creates an new course.
	 * @param course The newly created course.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createCourse(Course course) throws SQLException, IOException {
		database.createCourse(course);
		course.setId(database.getLastId());
		output.writeObject(new TableUpdate(course));
	}
	
	/**
	 * Asks the server to send all the assignments for a course. 
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected date format was encountered.
	 */
	public void sendAssignments() throws IOException, SQLException, ParseException {
		output.writeObject(new TableUpdate(database.getAssignments()));
	}
	
	/**
	 * Updates an already existing assignment.
	 * @param updated The assignment to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateAssignment(Assignment updated) throws SQLException {
		database.updateAssignment(updated);
	}
	
	/**
	 * Creates a new assignment.
	 * @param assignment The newly created assignment.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createAssignment(Assignment assignment) throws SQLException, IOException {
		database.createAssignment(assignment);
		assignment.setId(database.getLastId());
		files.setPath(FileManager.ASSIGNMENT, assignment.getId());
		files.storeFile(assignment.getFile());
		output.writeObject(new TableUpdate(assignment));
	}
	
	/**
	 * Asks the server to send only enrolled students 
	 * which fit the desired search criteria.
	 * @param type The type of search to be performed.
	 * @param parameter The search parameter used for the search.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendEnrolledStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == StudentRequest.NAME)
			database.getEnrolledStudents((String)parameter);
		else if (type == StudentRequest.ID)
			database.getEnrolledStudents((int)parameter);
		else
			database.getEnrolledStudents();
		output.writeObject(new TableUpdate(database.getStudents(false)));
	}
	
	/**
	 * Asks the server to send all students 
	 * which fit the desired search criteria.
	 * @param type The type of search to be performed.
	 * @param parameter The search parameter used for the search.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendAllStudents(int type, Object parameter) throws IOException, SQLException {
		if (type == StudentRequest.NAME)
			database.getAllStudents((String)parameter);
		else if (type == StudentRequest.ID)
			database.getAllStudents((int)parameter);
		else
			database.getAllStudents();
		output.writeObject(new TableUpdate(database.getStudents(true)));
	}
	
	/**
	 * Updates an already existing student.
	 * @param updated The student to be updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateStudent(Student updated) throws SQLException {
		if (updated.getEnrolled())
			database.createEnrollment(updated);
		else
			database.deleteEnrollment(updated);
	}
	
	/**
	 * Updates an already existing submission.
	 * @param submission The submission which was updated.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void updateSubmission(Submission submission) throws SQLException {
		database.updateSubmission(submission);
	}
	
	/**
	 * Creates a submission to an assignment drop box.
	 * @param submission The student submission.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void createSubmission(Submission submission) throws SQLException, IOException {
		database.createSubmission(submission);
		submission.setId(database.getLastId());
		files.setPath(FileManager.SUBMISSION, submission.getId());
		files.storeFile(submission.getFile());
	}
	
	/**
	 * Asks the server to send all the submissions for an assignment.
	 * @param assignmentId The assignment id used to select submissions.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 */
	public void sendSubmissions(int assignmentId) throws IOException, SQLException, ParseException {
		output.writeObject(new TableUpdate(database.getSubmissions(assignmentId)));
	}
	
	/**
	 * Asks the server to send a particular file.
	 * @param type The type of the file (submission or assignment).
	 * @param id The id of the file.
	 * @throws IOException Failed to send serialized messages.
	 */ 
	public void sendFile(char type, int id) throws IOException {
		files.setPath(type, id);
		byte[] content = files.retreiveFile();
		output.writeObject(new FileDelivery(content));
	}
	
	/**
	 * Attempts to validate the login credentials sent by the user.
	 * @throws ClassNotFoundException An unexpected class was received on the socket.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws IOException Failed to send serialized messages.
	 * @throws AddressException The email address entered is not valid.
	 */
	private void login() throws ClassNotFoundException, SQLException, IOException, AddressException {
		LoginData response;
		do {
			LoginCredentials credentials = (LoginCredentials)input.readObject();
			response = database.validateLogin(credentials);
			output.writeObject(response.getLogin());
		} while (!response.getLogin().success());
		email.startSession(response.getEmail(), response.getPassword());
	}

	/**
	 * Subscribes the client to receive chat updates for a particular course. Responds with all
	 * the previous messages from this chat.
	 * @throws SQLException Failed to communicate with SQL server.
	 */
	public void sendEmail(String subject, String content) throws SQLException, MessagingException {
		email.setContent(subject, content);
		email.setRecipients(database.getRecipients());
		email.sendMessage();
	}
}
