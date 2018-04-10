package data;

import java.text.SimpleDateFormat;
import java.util.Date;

import serverMessage.Request;
import serverMessage.AssignmentUpdate;

public class Assignment implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final int NEW_ID = -1;
	private static final int NAME_COL = 0;
	private static final int DUE_COL = 1;
	private static final int ACTIVE_COL = 2;
	private static final int GRADE_COL = 3;
	
	public static final RowProperties PROF_ROW_PROPERTIES = new RowProperties(
			new String[] { "Name", "Due", "Active" }, 
			new Class<?>[] { RowProperties.STRING, RowProperties.STRING, RowProperties.CHECKBOX }, 
			new boolean[] { false, false, true },
			new int[] { NAME_COL, DUE_COL, ACTIVE_COL});
	public static final RowProperties STUDENT_ROW_PROPERTIES = new RowProperties(
			new String [] { "Name", "Due", "Grade" },
			new Class<?>[] { RowProperties.STRING, RowProperties.STRING, RowProperties.STRING },
			new boolean[] { false, false, false },
			new int[] { NAME_COL, DUE_COL, GRADE_COL });
	
	private int id;
	private String title;
	private boolean active;
	private Date dueDate;
	private FileContent file;
	
	public Assignment(int id, String title, boolean active, Date dueDate, FileContent file) {
		this.id = id;
		this.title = title;
		this.active = active;
		this.dueDate = dueDate;
		this.file = file;
	}
	
	public Assignment(String title, FileContent file, Date due) {
		this(NEW_ID, title, false, due, file);
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
	
	public FileContent getFile() {
		return file;
	}
	
	public Date getDueDate() {
		return dueDate;
	}

	public Object getColumn(int index) {
		if (index == NAME_COL)
			return title;
		if (index == DUE_COL) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
			return format.format(dueDate);
		} if (index == ACTIVE_COL)
			return active;
		if (index == GRADE_COL)
			return "--";
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			active = (boolean)value;
	}
	
	public Request createRequest() {
		return new AssignmentUpdate(this);
	}
}
