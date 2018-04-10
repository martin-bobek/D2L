package data;

import java.text.SimpleDateFormat;
import java.util.Date;

import serverMessage.Request;
import serverMessage.AssignmentUpdate;

public class Assignment implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final int NO_GRADE = -1;
	public static final int NEW_ID = -1;
	private static final int NAME_COL = 0;
	private static final int DUE_COL = 1;
	private static final int ACTIVE_COL = 2;
	private static final int GRADE_COL = 3;
	
	public static final RowProperties PROF_ROW_PROPERTIES = new RowProperties(
			new String[] { "Name", "Due", "Active" }, 
			new Class<?>[] { String.class, String.class, Boolean.class }, 
			new int[] { NAME_COL, DUE_COL, ACTIVE_COL});
	public static final RowProperties STUDENT_ROW_PROPERTIES = new RowProperties(
			new String [] { "Name", "Due", "Grade" },
			new Class<?>[] { String.class, String.class, String.class },
			new int[] { NAME_COL, DUE_COL, GRADE_COL });
	
	private int id;
	private String title;
	private boolean activeSubmitted;
	private Date dueDate;
	private String extension;
	private byte[] content;
	
	public Assignment(int id, String title, boolean activeSubmitted, Date dueDate, String extension) {
		this.id = id;
		this.title = title;
		this.activeSubmitted = activeSubmitted;
		this.dueDate = dueDate;
		this.extension = extension;
	}
	
	public Assignment(String title, Date due, String extension, byte[] content) {
		this(NEW_ID, title, false, due, extension);
		this.content = content;
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
		return activeSubmitted;
	}
	
	public void setSubmitted(boolean submitted) {
		activeSubmitted = submitted;
	}
	
	public boolean getSubmitted() {
		return activeSubmitted;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public byte[] getFile() {
		return content;
	}

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
			return "Ungraded";
		}
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			activeSubmitted = (boolean)value;
	}
	
	public Request createRequest() {
		return new AssignmentUpdate(this);
	}
}
