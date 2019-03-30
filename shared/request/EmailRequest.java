package shared.request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

/**
 * A request to send an email.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class EmailRequest implements Request {
	private static final long serialVersionUID = 1L;
	/**
	 * The subject of the email.
	 */
	private final String subject;
	/**
	 * The content of the email.
	 */
	private final String content;
	
	/**
	 * Creates a new request to send an email.
	 * @param subject The subject of the message.
	 * @param content The content of the message.
	 */
	public EmailRequest(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}

	/**
	 * Asks the server to send the email.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.sendEmail(subject, content);
	}
}
