package serverMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

public class EmailRequest implements Request {
	private static final long serialVersionUID = 1L;
	private final String subject;
	private final String content;
	
	public EmailRequest(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}

	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.sendEmail(subject, content);
	}
}
