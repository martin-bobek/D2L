package data;

import message.Request;
import message.UpdateCourse;

public class Course implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final int NEW_ID = -1;
	public static final RowProperties ROW_PROPERTIES = new RowProperties(2, 
			new String[] { "Name", "Active" }, 
			new Class<?>[] { RowProperties.STRING, RowProperties.CHECKBOX }, 
			new boolean[] { false, true });
	private static final int NAME_COL = 0;
	private static final int ACTIVE_COL	= 1;
	private int id;
	private String name;
	private boolean active;
	
	public Course(int id, String name, boolean active) {
		this.id = id;
		this.name = name;
		this.active = active;
	}
	
	public Course(String name) {
		this(NEW_ID, name, false);
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
		return null;
	}
	
	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			active = (boolean)value;
	}
	
	public Request createRequest() {
		return new UpdateCourse(this);
	}
}
