package data;

import helper.RowProperties;
import helper.TableRow;
import message.Request;
import message.UpdateAssignment;

public class Assignment implements TableRow, Updatable {
	private static final long serialVersionUID = 1L;
	public static final int NEW_ID = -1;
	public static final RowProperties ROW_PROPERTIES = new RowProperties(3,
			new String[] { "Name", "Due", "Active" }, 
			new Class<?>[] { RowProperties.STRING, RowProperties.STRING, RowProperties.CHECKBOX }, 
			new boolean[] { false, false, true });
	private static final int NAME_COL = 0;
	private static final int DUE_COL = 1;
	private static final int ACTIVE_COL = 2;
	private int id;
	private String title;
	private boolean active;
	private String dueDate;
	private FileContent file;
	
	public Assignment(int id, String title, boolean active, String dueDate) {
		this.id = id;
		this.title = title;
		this.active = active;
		this.dueDate = dueDate;
	}
	
	public Assignment(String title, FileContent file) {
		this(NEW_ID, title, false, "MAR 15 2017");
		this.file = file;
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
	
	public FileContent getFile() {
		return file;
	}

	public Object getColumn(int index) {
		if (index == NAME_COL)
			return title;
		if (index == DUE_COL)
			return dueDate;
		if (index == ACTIVE_COL)
			return active;
		return null;
	}

	public void setColumn(Object value, int index) {
		if (index == ACTIVE_COL)
			active = (boolean)value;
	}
	
	public Request createRequest() {
		return new UpdateAssignment(this);
	}
}
