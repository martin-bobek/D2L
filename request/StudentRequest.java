package request;

import java.io.IOException;
import java.sql.SQLException;

/**
 * A request to get a list of students from the server.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class StudentRequest implements Request {
	private static final long serialVersionUID = 1L;
	private static final int NONE = 0;
	public static final int ID = 1;
	public static final int NAME = 2;
	/**
	 * The search parameter used to select students.
	 */
	private final Object parameter;
	/**
	 * The type of search to be performed (NONE, ID, or NAME).
	 */
	private final int type;
	/**
	 * Indicates that all students (not just enrolled) are desired.
	 */
	private boolean all;
	
	/**
	 * Makes a request for students.
	 * @param type The type of search to be performed (ID or NAME).
	 * @param parameter The search parameter for that type of search.
	 */
	public StudentRequest(int type, Object parameter) {
		this.type = type;
		this.parameter = parameter;
	}
	
	/**
	 * Creates a request for students with no search parameter applied.
	 */
	public StudentRequest() {
		parameter = null;
		type = NONE;
		all = false;
	}
	
	/**
	 * Sets if only enrolled students are desired.
	 * @param all true indicates all (not necessarily enrolled) students are desired.
	 */
	public void setAll(boolean all) {
		this.all = all;
	}
	
	/**
	 * Makes the correct request to the server to deliver the desired students.
	 * @param server A handle to the server.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (all)
			server.sendAllStudents(type, parameter);
		else
			server.sendEnrolledStudents(type, parameter);
	}
}
