package shared.request;

import java.io.IOException;
import java.sql.SQLException;

import shared.data.Student;

/**
 * A message used to indicate that a student has been updated.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class StudentUpdate implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The updated student object.
	 */
	private final Student updated;

	/**
	 * Creates a new update request for the server.
	 * @param updated The student which has been updated.
	 */
	public StudentUpdate(Student updated) {
		this.updated = updated;
	}
	
	/**
	 * Asks the server to update the record for the student.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException {
		server.updateStudent(updated);
	}
}
