package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Submission implements TableRow {
	private static final long serialVersionUID = 1L;
	public static final int NEW_ID = -1;
	public static final int NO_GRADE = -1;
	private static final int STUDENT_COL = 0;
	private static final int TIMESTAMP_COL = 1;
	private static final int GRADE_COL = 2;
	
	public static final RowProperties ROW_PROPERTIES = new RowProperties(
			new String[] { "Student", "Timestamp", "Grade" },
			new Class<?>[] { String.class, String.class, String.class },
			new int[] { STUDENT_COL, TIMESTAMP_COL, GRADE_COL });
	
	private int id;
	private int assignmentId;
	private final String name;
	private final String extension;
	private final Date timestamp;
	private final byte[] content;
	private int grade;
	private final String studentName;
	
	private Submission(int id, String name, String extension, byte[] content, Date timestamp, String studentName, int grade) {
		this.id = id;
		this.name = name;
		this.extension = extension;
		this.content = content;
		this.timestamp = timestamp;
		this.studentName = studentName;
		this.grade = grade;
	}
	
	public Submission(int id, String name, String extension, Date timestamp, String studentName, int grade) {
		this(id, name, extension, null, timestamp, studentName, grade);
	}
	
	public Submission(String name, String extension, byte[] content, Date timestamp) {
		this(NEW_ID, name, extension, content, timestamp, null, NO_GRADE);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}
	
	public int getAssignmentId() {
		return assignmentId;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public byte[] getFile() {
		return content;
	}
	
	public Object getColumn(int index) {
		if (index == STUDENT_COL)
			return studentName;
		if (index == TIMESTAMP_COL) {
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy - HH:mm:ss");
			return format.format(timestamp);
		} if (index == GRADE_COL) {
			if (grade == NO_GRADE)
				return "Ungraded";
			return grade + "%";
		}
		return null;
	}

	public void setColumn(Object value, int index) {
		// TODO Auto-generated method stub
		
	}
}
