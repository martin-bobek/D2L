package shared.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Submission implements TableRow {
	private static final long serialVersionUID = 1L;
	/**
	 * Used to indicate the submission is newly created.
	 */
	public static final int NEW_ID = -1;
	/**
	 * Used to indicate the submission is not graded.
	 */
	public static final int NO_GRADE = -1;
	/**
	 * The student name data column.
	 */
	private static final int STUDENT_COL = 0;
	/**
	 * The time stamp data column.
	 */
	private static final int TIMESTAMP_COL = 1;
	/**
	 * The grades data column.
	 */
	private static final int GRADE_COL = 2;
	
	/**
	 * The row properties for displaying submission objects.
	 */
	public static final RowProperties ROW_PROPERTIES = new RowProperties(
			new String[] { "Student", "Timestamp", "Grade" },
			new Class<?>[] { String.class, String.class, String.class },
			new int[] { STUDENT_COL, TIMESTAMP_COL, GRADE_COL });
	
	/**
	 * The submission id.
	 */
	private int id;
	/**
	 * The id of the assignment the submission is for.
	 */
	private int assignmentId;
	/**
	 * The name of the submission.
	 */
	private final String name;
	/**
	 * The extension for the file.
	 */
	private final String extension;
	/**
	 * The time stamp indicating when the assignment was submitted.
	 */
	private final Date timestamp;
	/**
	 * The file content for the assignment.
	 */
	private final byte[] content;
	/**
	 * The grade given to the assignment.
	 */
	private int grade;
	/**
	 * The name of the student who submitted the assignment.
	 */
	private final String studentName;
	
	/**
	 * Creates a new Submission, initializing all fields.
	 * @param id The submission id.
	 * @param name The name of the submission.
	 * @param extension The extension of the file.
	 * @param content The content of the file.
	 * @param timestamp The submission time.
	 * @param studentName The name of the student.
	 * @param grade The grade received.
	 */
	private Submission(int id, String name, String extension, byte[] content, Date timestamp, String studentName, int grade) {
		this.id = id;
		this.name = name;
		this.extension = extension;
		this.content = content;
		this.timestamp = timestamp;
		this.studentName = studentName;
		this.grade = grade;
	}
	
	/**
	 * Creates a new submission from the database entry.
	 * @param id The submission id.
	 * @param name The submission name.
	 * @param extension The extension of the file.
	 * @param timestamp The time stamp indicating when submission happened.
	 * @param studentName The name of the student.
	 * @param grade The grade given.
	 */
	public Submission(int id, String name, String extension, Date timestamp, String studentName, int grade) {
		this(id, name, extension, null, timestamp, studentName, grade);
	}
	
	/**
	 * Creates a new submission from user input.
	 * @param name The name of the submission.
	 * @param extension The file extension.
	 * @param content The file contents.
	 * @param timestamp The submission time.
	 */
	public Submission(String name, String extension, byte[] content, Date timestamp) {
		this(NEW_ID, name, extension, content, timestamp, null, NO_GRADE);
	}
	
	/**
	 * Sets the id of the submission.
	 * @param id The new id.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the submission's id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the submission's grade.
	 * @param grade The new grade.
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	/**
	 * Gets the submission's grade.
	 * @return The grade.
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Sets the id of the assignment.
	 * @param assignmentId The assignment id.
	 */
	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}
	
	/**
	 * gets the assignment Id.
	 * @return The assignment id.
	 */
	public int getAssignmentId() {
		return assignmentId;
	}
	
	/**
	 * Gets the file extension.
	 * @return The file extension.
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Gets the file name.
	 * @return The file name.
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * Gets the submission time stamp.
	 * @return The time.
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Get's the file content.
	 * @return The file content.
	 */
	public byte[] getFile() {
		return content;
	}
	
	/**
	 * Gets the value in a column.
	 * @param index The data column number.
	 */
	public Object getColumn(int index) {
		if (index == STUDENT_COL)
			return studentName;
		if (index == TIMESTAMP_COL) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy - HH:mm:ss");
			return format.format(timestamp);
		} if (index == GRADE_COL) {
			if (grade == NO_GRADE)
				return "Ungraded";
			return grade + "%";
		}
		return null;
	}

	/**
	 * Updates a column. Does not do anything.
	 * @param value The new value of the column
	 * @param index The data column number.
	 */
	public void setColumn(Object value, int index) {}
}
