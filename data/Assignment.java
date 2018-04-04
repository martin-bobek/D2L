package data;

import java.io.Serializable;

public class Assignment implements Serializable {
	private static final long serialVersionUID = 1L;
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
	
	public String toString() {
		return title + "   -   " + (active ? "active" : "inactive");
	}
}
