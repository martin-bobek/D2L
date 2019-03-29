package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

import data.ChatMessage;
import data.TableRow;
import response.ClientInterface;
import response.LoginResponse;
import response.Response;
/**
 * This class handles the socket connection to the server.
 * Is part of the clients model.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ServerConnection implements ClientInterface {
	/**
	 * The socket connection to the server.
	 */
	private Socket socket;
	/**
	 * The stream used to receive objects from the server.
	 */
	private ObjectInputStream input;
	/**
	 * The stream used to send objects to the server.
	 */
	private ObjectOutputStream output;
	/**
	 * A handle to the table model for the GUI, allowing changes 
	 * to be made directly to the GUI.
	 */
	private TableModel list;
	/**
	 * A handle to the chat area for the GUI, allowing changes
	 * to be made directly to the GUI.
	 */
	private JTextArea chat;
	/**
	 * A handle to the file helper, allowing received files to be 
	 * directly sent to the file system.
	 */
	private FileHelper files;
	/**
	 * A handle to the locking mechanism for the controller. Is unlocked 
	 * when certain critical operations finish.
	 */
	private AtomicBoolean lock;
	
	/**
	 * Creates a new connection to the server.
	 * @throws IOException Failed to send serialized messages.
	 */
	public ServerConnection() throws IOException {
		socket = new Socket("localhost", 9898);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}
	
	/**
	 * Adds a handle to the table model.
	 * @param table The table model.
	 */
	public void addTable(TableModel table) {
		list = table;
	}
	
	/**
	 * Adds a handle to the file helper.
	 * @param fileHelper The file helper.
	 */
	public void addFileHelper(FileHelper fileHelper) {
		files = fileHelper;
	}
	
	/**
	 * Adds a handle to the chat area.
	 * @param text The chat text area.
	 */
	public void addChatText(JTextArea text) {
		chat = text;
	}
	
	/**
	 * Adds a handle to the controllers locking mechanism.
	 * @param lock The locking mechanism.
	 */
	public void addLock(AtomicBoolean lock) {
		this.lock = lock;
	}
	
	/**
	 * Waits for a response from the server and triggers the performAction method.
	 * @throws ClassNotFoundException An unexpected class was received on the socket.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void receiveResponse() throws ClassNotFoundException, IOException {
		Response response = (Response)input.readObject();
		response.performAction(this);
	}
	
	/**
	 * Appends rows to the main table.
	 */
	public void updateTable(ArrayList<? extends TableRow> rows) {
		for (TableRow element : rows)
			list.addElement(element);
	}
	
	/**
	 * Appends messages to the chat.
	 */
	public void appendChat(ArrayList<ChatMessage> messages) {
		for (ChatMessage message : messages)
			chat.append(message.toString() + '\n');
	}
	
	/**
	 * Downloads a file to the users file system into a location set earlier.
	 */
	public void downloadFile(byte[] content) throws IOException {
		files.downloadFile(content);
	}
	
	/**
	 * Awaits a login response from the server.
	 * @return The response object.
	 * @throws ClassNotFoundException An unexpected class was received on the socket.
	 * @throws IOException Failed to send serialized messages.
	 */
	public LoginResponse getLoginResponse() throws ClassNotFoundException, IOException {
		return (LoginResponse)input.readObject();
	}
	
	/**
	 * Sends an object to the server.
	 * @param obj The object to be sent.
	 * @throws IOException Failed to send serialized messages.
	 */
	public void sendObject(Serializable obj) throws IOException {
		output.reset();
		output.writeObject(obj);
	}

	/**
	 * Unlocks the hold on the controller.
	 */
	public void unlock() {
		lock.set(false);
	}
}
