package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Course;
import data.LoginCredentials;
import message.Request;

public class ClientHandler implements Runnable {
	ObjectInputStream input;
	ObjectOutputStream output;
	DatabaseManager database;
	
	ClientHandler(Socket socket) throws IOException, SQLException {
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		database = new DatabaseManager(0);
	}
	
	public void run() {
		try {
			login();
			while (true)
				((Request)input.readObject()).performAction(this);
		} catch (SQLException | ClassNotFoundException e) {
			System.err.println("Error 2: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Client disconnected.");
			return;
		}
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
	
	public void sendAssignments() throws IOException, SQLException {
		output.writeObject(database.getAssignments());
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
