package response;

import java.io.IOException;
import java.util.ArrayList;

import data.ChatMessage;
import data.TableRow;

/** 
 * The functions client must implement as an interface for the response functions.
 * Necessary so that the response package can be used server side without access to 
 * the client packages.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface ClientInterface {
	/**
	 * Adds new rows to the main table in the GUI.
	 * @param rows The rows to be inserted.
	 */
	public void updateTable(ArrayList<? extends TableRow> rows);
	/**
	 * Passes in a file to be dealt with by the client.
	 * @param content The contents of the file.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void downloadFile(byte[] content) throws IOException;
	/**
	 * Appends new messages to the chat.
	 * @param messages The messages to append.
	 */
	public void appendChat(ArrayList<ChatMessage> messages);
	/**
	 * Unlocks the controller. 
	 * For certain critical operations it is necessary to wait for 
	 * the current response to be delivered before handling any more events from
	 * the GUI. This indicates that the critical operation has finished.
	 */
	public void unlock();
}
