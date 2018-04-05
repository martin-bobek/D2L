package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import data.TableRow;

class ServerConnection {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private TableModel list;
	
	ServerConnection() throws IOException {
		socket = new Socket("localhost", 9898);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		list = new TableModel();
	}
	
	TableModel getList() {
		return list;
	}
	
	void clearList() {
		list.clear();
	}
	
	void receiveList() throws ClassNotFoundException, IOException {
		@SuppressWarnings("unchecked")
		ArrayList<TableRow> queryResult = (ArrayList<TableRow>)input.readObject();
		for (TableRow element : queryResult)
			list.addElement(element);
	}
	
	char getLoginResponse() throws ClassNotFoundException, IOException {
		return (Character)input.readObject();
	}
	
	void sendObject(Serializable obj) throws IOException {
		output.writeObject(obj);
	}
}
