package clientMessage;

import java.io.IOException;
import java.io.Serializable;

public interface Response extends Serializable {
	public void performAction(ClientInterface client) throws IOException;
}
