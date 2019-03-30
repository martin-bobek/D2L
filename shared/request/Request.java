package shared.request;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

/**
 * The interface for all messages sent to the server.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface Request extends Serializable {
	/**
	 * When the message is delivered, the server calls this method, which makes the desired requests
	 * to the server through ServerInterface.
	 * @param server A handle to the server.
	 * @throws IOException Failed to send serialized messages.
	 * @throws SQLException Failed to communicate with SQL server.
	 * @throws ParseException An unexpected class was received on the socket.
	 * @throws MessagingException Failed to send email message.
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException;
}
