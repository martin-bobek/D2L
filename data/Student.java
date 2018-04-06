package data;

import message.Request;
import message.UpdateStudent;

public class Student implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final RowProperties ROW_PROPERTIES = new RowProperties(3, 
			new String[] { "Id", "Name", "Enrolled" },
			new Class<?>[] { RowProperties.STRING, RowProperties.STRING, RowProperties.CHECKBOX }, 
			new boolean[] { false, false, true });
	private static final int ID_COL = 0;
	private static final int NAME_COL = 1;
	private static final int ENROLLED_COL = 2;
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

	public Object getColumn(int index) {
		if (index == ID_COL)
			return Integer.toString(id);
		if (index == NAME_COL)
			return firstName + " " + lastName;
		if (index == ENROLLED_COL)
			return enrolled;
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == 2)
			enrolled = (boolean)value;
	}
	
	public Request createRequest() {
		return new UpdateStudent(this);
	}
}
