package data;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String content;
	
	public ChatMessage(String username, String content) {
		this.username = username;
		this.content = content;
	}
	
	public String toString() {
		return username + ": " + content + '\n';
	}
}
