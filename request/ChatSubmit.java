package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

/**
 * Submits a message to a chat from the user.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ChatSubmit implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The message submitted by the user.
	 */
	private String message;
	
	/**
	 * Creates a new chat message submission.
	 * @param message The content of the message.
	 */
	public ChatSubmit(String message) {
		this.message = message;
	}

	/**
	 * Notifies the server that a message has been submitted to the chat.
	 * @param server A handle to the server.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.submitMessage(message);
	}
}
