package response;

import java.io.IOException;
import java.util.ArrayList;

import data.ChatMessage;
import data.TableRow;

public interface ClientInterface {
	public void updateTable(ArrayList<? extends TableRow> rows);
	public void downloadFile(byte[] content) throws IOException;
	public void appendChat(ArrayList<ChatMessage> messages);
}
