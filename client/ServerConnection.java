package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

import data.ChatMessage;
import data.TableRow;
import response.ClientInterface;
import response.LoginResponse;
import response.Response;

public class ServerConnection implements ClientInterface {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private TableModel list;
	private JTextArea chat;
	private FileHelper files;
	
	public ServerConnection() throws IOException {
		socket = new Socket("localhost", 9898);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void addTable(TableModel table) {
		list = table;
	}
	
	public void addFileHelper(FileHelper fileHelper) {
		files = fileHelper;
	}
	
	public void addChatText(JTextArea text) {
		chat = text;
	}
	
	public void receiveResponse() throws ClassNotFoundException, IOException {
		Response response = (Response)input.readObject();
		response.performAction(this);
	}
	
	public void updateTable(ArrayList<? extends TableRow> rows) {
		for (TableRow element : rows)
			list.addElement(element);
	}
	
	public void appendChat(ArrayList<ChatMessage> messages) {
		for (ChatMessage message : messages)
			chat.append(message.toString());
	}
	
	public void downloadFile(byte[] content) throws IOException {
		files.downloadFile(content);
	}
	
	public LoginResponse getLoginResponse() throws ClassNotFoundException, IOException {
		return (LoginResponse)input.readObject();
	}
	
	public void sendObject(Serializable obj) throws IOException {
		output.reset();
		output.writeObject(obj);
	}
}
