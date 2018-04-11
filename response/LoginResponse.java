package response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final char BAD_LOGIN = 0;
	public static final char PROFESSOR = 'P';
	public static final char STUDENT = 'S';
	private final char type;
	private final String name;
	
	public LoginResponse(char type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public LoginResponse() {
		this(BAD_LOGIN, null);
	}
	
	public boolean success() {
		return type != BAD_LOGIN;
	}
	
	public char getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
