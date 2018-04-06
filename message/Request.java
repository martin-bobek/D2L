package message;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;

public interface Request extends Serializable {
	public void performAction(RequestHandler server) throws IOException, SQLException, ParseException;
}
