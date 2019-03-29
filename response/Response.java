package response;

import java.io.IOException;
import java.io.Serializable;

/**
 * An interface which is implemented by all messages and data coming from the server. 
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface Response extends Serializable {
	/**
	 * When the message is delivered, the client calls this method, which enacts the changes
	 * in the client through ClientInterface.
	 * @param client - The client which the message is delivered to.
	 * @throws IOException Failed to communicate with client.
	 */
	public void performAction(ClientInterface client) throws IOException;
}
