package shared.data;

import shared.request.CourseUpdate;
import shared.request.Request;

/**
 * A class to hold information about a course.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class Course implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates the course is newly created and not yet added to the database.
	 */
	public static final int NEW_ID = -1;
	/**
	 * The course name data column.
	 */
	private static final int NAME_COL = 0;
	/**
	 * The course active data column.
	 */
	private static final int ACTIVE_COL	= 1;
	/**
	 * The professor name data column.
	 */
	private static final int PROF_COL = 2;
	
	/**
	 * The row properties used to render the object in the professor GUI table.
	 */
	public static final RowProperties PROF_ROW_PROPERTIES = new RowProperties( 
			new String[] { "Name", "Active" }, 
			new Class<?>[] { String.class, Boolean.class }, 
			new int[] { NAME_COL, ACTIVE_COL });
	/**
	 * The row properties used to render the object in the student GUI table.
	 */
	public static final RowProperties STUDENT_ROW_PROPERTIES = new RowProperties(
			new String[] { "Name", "Professor" }, 
			new Class<?>[] { String.class, String.class },  
			new int[] { NAME_COL, PROF_COL });
	
	/**
	 * The course id.
	 */
	private int id;
	/**
	 * The name of the course.
	 */
	private String name;
	/**
	 * The course is currently active and visible to students.
	 */
	private boolean active;
	/**
	 * The name of the course's professor.
	 */
	private String profName;
	
	/**
	 * Creates a new course, initializing all fields.
	 * @param id The id of the course.
	 * @param name The name of the course.
	 * @param active Whether the course is currently active.
	 * @param profName The name of the course's professor.
	 */
	public Course(int id, String name, boolean active, String profName) {
		this.id = id;
		this.name = name;
		this.active = active;
		this.profName = profName;
	}
	
	/**
	 * Creates a new course, setting only the courses name.
	 * @param name The courses name.
	 */
	public Course(String name) {
		this(NEW_ID, name, false, null);
	}
	
	/**
	 * Gets the course's id.
	 * @return Course id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the courses name. 
	 * @return Course name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets whether the course is currently active.
	 * @return true if the course is active.
	 */
	public boolean getActive() {
		return active;
	}
	
	/**
	 * Set's the courses id.
	 * @param id The new id.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Set's whether the course is active.
	 * @param active true indicates the course is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Gets the value of a data column.
	 * @param index The data column.
	 */
	public Object getColumn(int index) {
		if (index == NAME_COL)
			return id + ": " + name;
		if (index == ACTIVE_COL)
			return active;
		if (index == PROF_COL)
			return profName;
		return null;
	}
	
	/**
	 * Sets the value of a data column.
	 * @param value The new value.
	 * @param index The data column.
	 */
	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			active = (boolean)value;
	}
	
	/**
	 * Creates an update request for this particular object.
	 */
	public Request createRequest() {
		return new CourseUpdate(this);
	}
}
