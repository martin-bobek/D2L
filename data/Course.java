package data;

import serverMessage.Request;
import serverMessage.CourseUpdate;

public class Course implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final int NEW_ID = -1;
	private static final int NAME_COL = 0;
	private static final int ACTIVE_COL	= 1;
	private static final int PROF_COL = 2;
	
	public static final RowProperties PROF_ROW_PROPERTIES = new RowProperties( 
			new String[] { "Name", "Active" }, 
			new Class<?>[] { RowProperties.STRING, RowProperties.CHECKBOX }, 
			new boolean[] { false, true },
			new int[] { NAME_COL, ACTIVE_COL });
	public static final RowProperties STUDENT_ROW_PROPERTIES = new RowProperties(
			new String[] { "Name", "Professor" }, 
			new Class<?>[] { RowProperties.STRING, RowProperties.STRING }, 
			new boolean[] { false, false }, 
			new int[] { NAME_COL, PROF_COL });
	
	private int id;
	private String name;
	private boolean active;
	private String profName;
	
	public Course(int id, String name, boolean active, String profName) {
		this.id = id;
		this.name = name;
		this.active = active;
		this.profName = profName;
	}
	
	public Course(String name) {
		this(NEW_ID, name, false, null);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Object getColumn(int index) {
		if (index == NAME_COL)
			return id + ": " + name;
		if (index == ACTIVE_COL)
			return active;
		if (index == PROF_COL)
			return profName;
		return null;
	}
	
	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			active = (boolean)value;
	}
	
	public Request createRequest() {
		return new CourseUpdate(this);
	}
}
