package server;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

class EmailSender {
	private Session session;
	
	void startSession(Authenticator authenticator) {
		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		session = Session.getInstance(properties, authenticator);
	}
}
