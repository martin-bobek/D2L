package data;

import java.io.Serializable;

public class LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userId;
	private String password;
	
	public LoginCredentials(int userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getPassword() {
		return password;
	}
}
