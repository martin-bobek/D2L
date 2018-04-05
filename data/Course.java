package data;

import java.io.Serializable;

public class Course implements Serializable, TableRow {
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
	
	public String toString() {
		return name + "   -   " + (active ? "active" : "inactive");
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
		if (index == 0)
			return STRING;
		if (index == 1)
			return CHECKBOX;
		return null;
	}
}
