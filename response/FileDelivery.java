package response;

import java.io.IOException;

/**
 * Delivers a file to the client. Always sent on request.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class FileDelivery implements Response {
	private static final long serialVersionUID = 1L;
	/**
	 * An array of bytes storing the content of the file.
	 */
	private byte[] content;

	/**
	 * Creates a new update, storing the content of a particular file.
	 * @param content A list of bytes in the file.
	 */
	public FileDelivery(byte[] content) {
		this.content = content;
	}
	
	/**
	 * Passes the file to the client, which then is responsible for saving the
	 * file to the users computer.
	 */
	public void performAction(ClientInterface client) throws IOException {
		client.downloadFile(content);
	}
}
