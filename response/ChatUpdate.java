package response;

import java.io.IOException;
import java.util.ArrayList;

import data.ChatMessage;

public class ChatUpdate implements Response {
	private static final long serialVersionUID = 1L;
	private final ArrayList<ChatMessage> messages;
	
	public ChatUpdate(ChatMessage message) {
		messages = new ArrayList<ChatMessage>();
		messages.add(message);
	}
	
	public ChatUpdate(ArrayList<ChatMessage> messages) {
		this.messages = messages;
	}

	public void performAction(ClientInterface client) throws IOException {
		client.appendChat(messages);
	}
}
