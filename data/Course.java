package data;

import message.Request;
import message.UpdateCourse;

public class Course implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = { "Name", "Active" };
	public static final int NEW_ID = -1;
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
	
	public int getNumColumns() {
		return 2;
	}

	public Object getColumn(int index) {
		if (index == 0)
			return name;
		if (index == 1)
			return active;
		return null;
	}
	
	public String getColumnName(int index) {
		return COLUMNS[index];
	}

	public Class<?> getColumnType(int index) {
		if (index == 1)
			return CHECKBOX;
		return STRING;
	}
	
	public boolean getColumnEditable(int index) {
		return index == 1;
	}

	public void setColumn(Object value, int index) {
		if (index == 1)
			active = (boolean)value;
	}
	
	public Request createRequest() {
		return new UpdateCourse(this);
	}
}
