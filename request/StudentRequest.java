package request;

import java.io.IOException;
import java.sql.SQLException;

public class StudentRequest implements Request {
	private static final long serialVersionUID = 1L;
	public static final int NONE = 0;
	public static final int ID = 1;
	public static final int NAME = 2;
	private final Object parameter;
	private final int type;
	private boolean all;
	
	public StudentRequest(int type, Object parameter) {
		this.type = type;
		this.parameter = parameter;
	}
	
	public StudentRequest() {
		parameter = null;
		type = NONE;
		all = false;
	}
	
	public void setAll(boolean all) {
		this.all = all;
	}
	
	public void performAction(ServerInterface server) throws IOException, SQLException {
		if (all)
			server.sendAllStudents(type, parameter);
		else
			server.sendEnrolledStudents(type, parameter);
	}
}
