package data;

import message.Request;
import message.UpdateStudent;

public class Student implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = { "Id", "Name", "Enrolled" };
	private int id;
	private String firstName;
	private String lastName;
	private boolean enrolled;

	public Student(int id, String firstName, String lastName, boolean enrolled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrolled = enrolled;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean getEnrolled() {
		return enrolled;
	}
	
	public Request createRequest() {
		return new UpdateStudent(this);
	}

	public int getNumColumns() {
		return 3;
	}

	public Object getColumn(int index) {
		if (index == 0)
			return Integer.toString(id);
		if (index == 1)
			return firstName + " " + lastName;
		if (index == 2)
			return enrolled;
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == 2)
			enrolled = (boolean)value;
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
		if (index == 2)
			return true;
		return false;
	}
}
