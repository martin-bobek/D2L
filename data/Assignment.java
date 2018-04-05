package data;

import message.Request;
import message.UpdateAssignment;

public class Assignment implements TableRow, Updatable {
	public static final int NEW_ID = -1;
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = { "Name", "Due", "Active" };
	private int id;
	private String title;
	private boolean active;
	private String dueDate;
	
	public Assignment(int id, String title, boolean active, String dueDate) {
		this.id = id;
		this.title = title;
		this.active = active;
		this.dueDate = dueDate;
	}
	
	public Assignment(String title) {
		this(NEW_ID, title, false, "MAR 15 2017");
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public Request createRequest() {
		return new UpdateAssignment(this);
	}

	public int getNumColumns() {
		return 3;
	}

	public Object getColumn(int index) {
		if (index == 0)
			return title;
		if (index == 1)
			return dueDate;
		if (index == 2)
			return active;
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == 2)
			active = (boolean)value;
	}

	public String getColumnName(int index) {
		return COLUMNS[index];
	}

	public Class<?> getColumnType(int index) {
		if (index == 2)
			return CHECKBOX;
		return STRING;
	}

	public boolean getColumnEditable(int index) {
		return index == 2;
	}
}
