package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

public class ChatSubmit implements Request {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ChatSubmit(String message) {
		this.message = message;
	}

	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.submitMessage(message);
	}
}
