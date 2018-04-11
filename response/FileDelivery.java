package response;

import java.io.IOException;

public class FileDelivery implements Response {
	private static final long serialVersionUID = 1L;
	private byte[] content;

	public FileDelivery(byte[] content) {
		this.content = content;
	}
	
	public void performAction(ClientInterface client) throws IOException {
		client.downloadFile(content);
	}
}
