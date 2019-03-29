package data;

import request.Request;
import request.StudentUpdate;

/**
 * A class used to hold information about a student.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class Student implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	/**
	 * The student id data column.
	 */
	private static final int ID_COL = 0;
	/**
	 * The student name data column.
	 */
	private static final int NAME_COL = 1;
	/**
	 * The is enrolled data column.
	 */
	private static final int ENROLLED_COL = 2;
	
	/**
	 * The row properties used to display the student object in the GUI table.
	 */
	public static final RowProperties ROW_PROPERTIES = new RowProperties( 
			new String[] { "Id", "Name", "Enrolled" },
			new Class<?>[] { String.class, String.class, Boolean.class }, 
			new int[] { ID_COL, NAME_COL, ENROLLED_COL });
	
	/**
	 * The student's id.
	 */
	private int id;
	/**
	 * The student's first name.
	 */
	private String firstName;
	/**
	 * The student's last name.
	 */
	private String lastName;
	/**
	 * True if the student is enrolled in the current course.
	 */
	private boolean enrolled;

	/**
	 * Creates a new student object, initializing all fields.
	 * @param id The student's id.
	 * @param firstName The student's first name.
	 * @param lastName The student's last name.
	 * @param enrolled true if the student is enrolled in the current course.
	 */
	public Student(int id, String firstName, String lastName, boolean enrolled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrolled = enrolled;
	}
	
	/**
	 * Gets the student's id.
	 * @return Student's id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Checks if the student is enrolled in the current course.
	 * @return true if the student is enrolled.
	 */
	public boolean getEnrolled() {
		return enrolled;
	}

	/**
	 * Gets the value of a data column.
	 * @param index The data column number.
	 */
	public Object getColumn(int index) {
		if (index == ID_COL)
			return Integer.toString(id);
		if (index == NAME_COL)
			return firstName + " " + lastName;
		if (index == ENROLLED_COL)
			return enrolled;
		return null;
	}

	/**
	 * Sets the value of a data column.
	 * @param value The new value for the column.
	 * @param index The column number.
	 */
	public void setColumn(Object value, int index) {
		if (index == 2)
			enrolled = (boolean)value;
	}
	
	/**
	 * Creates a new update request for this 
	 * particular object which will be sent to the server.
	 */
	public Request createRequest() {
		return new StudentUpdate(this);
	}
}
