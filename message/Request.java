package message;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import server.ClientHandler;

public interface Request extends Serializable {
	public void performAction(ClientHandler server) throws IOException, SQLException;
}
