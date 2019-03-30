package shared.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import shared.request.AssignmentUpdate;
import shared.request.Request;

/**
 * A class used to hold information about an assignment.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class Assignment implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates the assignment is currently ungraded.
	 */
	public static final int NO_GRADE = -1;
	/**
	 * Indicates the course is newly created and not yet added to the database.
	 */
	public static final int NEW_ID = -1;
	/**
	 * The assignment name data column.
	 */
	private static final int NAME_COL = 0;
	/**
	 * The due date data column.
	 */
	private static final int DUE_COL = 1;
	/**
	 * The is assignment active data column.
	 */
	private static final int ACTIVE_COL = 2;
	/**
	 * The grade data column.
	 */
	private static final int GRADE_COL = 3;
	
	/**
	 * The row properties used to render this class in the professor GUI table.
	 */
	public static final RowProperties PROF_ROW_PROPERTIES = new RowProperties(
			new String[] { "Name", "Due", "Active" }, 
			new Class<?>[] { String.class, String.class, Boolean.class }, 
			new int[] { NAME_COL, DUE_COL, ACTIVE_COL});
	/**
	 * The row properties used to render this calss in the student GUI table.
	 */
	public static final RowProperties STUDENT_ROW_PROPERTIES = new RowProperties(
			new String [] { "Name", "Due", "Grade" },
			new Class<?>[] { String.class, String.class, String.class },
			new int[] { NAME_COL, DUE_COL, GRADE_COL });
	
	/**
	 * The assignment's id.
	 */
	private int id;
	/**
	 * The title of the assignment.
	 */
	private String title;
	/**
	 * A field used to indicate whether the course is active or
	 * submitted, depending on context.
	 */
	private boolean activeSubmitted;
	/**
	 * The due date of the assignment.
	 */
	private Date dueDate;
	/**
	 * The file extension for the assignment.
	 */
	private String extension;
	/**
	 * The grade received for the assignment.
	 */
	private int grade;
	/**
	 * The content of the assignment file.
	 */
	private byte[] content;
	
	/**
	 * Creates a new course initializing all the parameters.
	 * @param id The assignment id.
	 * @param title The assignment title.
	 * @param activeSubmitted Whether the course is active/submitted.
	 * @param dueDate The due date of the assignment.
	 * @param extension The file extension for the assignment file.
	 * @param grade The grade received for the assignment.
	 */
	public Assignment(int id, String title, boolean activeSubmitted, Date dueDate, String extension, int grade) {
		this.id = id;
		this.title = title;
		this.activeSubmitted = activeSubmitted;
		this.dueDate = dueDate;
		this.extension = extension;
		this.grade = grade;
	}
	
	/**
	 * Creates a brand new assignment.
	 * @param title The title of the assignment.
	 * @param due The due date of the assignment.
	 * @param extension The file extension for the assignment.
	 * @param content The content of the assignment file.
	 */
	public Assignment(String title, Date due, String extension, byte[] content) {
		this(NEW_ID, title, false, due, extension, NO_GRADE);
		this.content = content;
	}

	/**
	 * Gets the assignment id.
	 * @return Assignment id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the assignment id.
	 * @param id The new assignment id.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/** 
	 * Gets the assignment's title.
	 * @return Assignment title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Checks if the assignment is active.
	 * @return true if the assignment is active.
	 */
	public boolean getActive() {
		return activeSubmitted;
	}
	
	/**
	 * Sets if the student has submitted something for the assignment.
	 * @param submitted true indicates the student has submitted.
	 */
	public void setSubmitted(boolean submitted) {
		grade = NO_GRADE;
		activeSubmitted = submitted;
	}
	
	/**
	 * Gets if the student has submitted something for the assignment.
	 * @return true if the student has submitted.
	 */
	public boolean getSubmitted() {
		return activeSubmitted;
	}
	
	/**
	 * Gets the due date.
	 * @return Due date.
	 */
	public Date getDueDate() {
		return dueDate;
	}
	
	/**
	 * Gets the file extension for the course.
	 * @return The file extension.
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Gets the content of the assignment file.
	 * @return The assignment content file.
	 */
	public byte[] getFile() {
		return content;
	}

	/**
	 * Gets the value of a data column.
	 * @param index The column number.
	 */
	public Object getColumn(int index) {
		if (index == NAME_COL)
			return title;
		if (index == DUE_COL) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
			return format.format(dueDate);
		} if (index == ACTIVE_COL)
			return activeSubmitted;
		if (index == GRADE_COL) {
			if (!activeSubmitted)
				return "Unsubmitted";
			if (grade == NO_GRADE)
				return "Ungraded";
			return grade + "%";
		}
		return null;
	}

	/**
	 * Sets the value of a data column.	
	 * @param value The new column value.
	 * @param index The column index.
	 */
	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			activeSubmitted = (boolean)value;
	}
	
	/**
	 * Creates an update request for this particular object.
	 */
	public Request createRequest() {
		return new AssignmentUpdate(this);
	}
}
