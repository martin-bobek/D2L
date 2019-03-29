package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

/**
 * Message used to subscribe to a particular courses chat. The server responds by sending all the
 * messages currently in the chat.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class SubscribeChat implements Request {
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates the the server that the client wants to view a particular chat.
	 * @param server A handle to the server. 
	 */
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.subscribeChat();
	}
}
