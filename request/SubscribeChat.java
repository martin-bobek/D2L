package request;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

public class SubscribeChat implements Request {
	private static final long serialVersionUID = 1L;

	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException {
		server.subscribeChat();
	}
}
