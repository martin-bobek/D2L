package serverMessage;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;

import javax.mail.MessagingException;

public interface Request extends Serializable {
	public void performAction(ServerInterface server) throws IOException, SQLException, ParseException, MessagingException;
}
