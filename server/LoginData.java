package server;

import response.LoginResponse;

class LoginData {
	private final String email;
	private final String password;
	private final LoginResponse login;
	
	LoginData() {
		this(new LoginResponse(), null, null);
	}
	
	LoginData(LoginResponse login, String email, String password) {
		this.email = email;
		this.password = password;
		this.login = login;
	}
	
	String getEmail() {
		return email;
	}
	
	String getPassword() {
		return password;
	}
	
	LoginResponse getLogin() {
		return login;
	}
}
