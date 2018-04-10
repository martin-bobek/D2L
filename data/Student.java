package data;

import serverMessage.Request;
import serverMessage.StudentUpdate;

public class Student implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	private static final int ID_COL = 0;
	private static final int NAME_COL = 1;
	private static final int ENROLLED_COL = 2;
	
	public static final RowProperties ROW_PROPERTIES = new RowProperties( 
			new String[] { "Id", "Name", "Enrolled" },
			new Class<?>[] { String.class, String.class, Boolean.class }, 
			new int[] { ID_COL, NAME_COL, ENROLLED_COL });
	
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
		return new StudentUpdate(this);
	}
}
