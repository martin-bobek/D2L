package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

class ServerConnection {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private DefaultListModel<Serializable> list;
	
	ServerConnection() throws IOException {
		socket = new Socket("localhost", 9898);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		list = new DefaultListModel<Serializable>();
	}
	
	DefaultListModel<Serializable> getList() {
		return list;
	}
	
	void clearList() {
		list.clear();
	}
	
	void receiveList() throws ClassNotFoundException, IOException {
		@SuppressWarnings("unchecked")
		ArrayList<Serializable> queryResult = (ArrayList<Serializable>)input.readObject();
		for (Serializable element : queryResult)
			list.addElement(element);
	}
	
	char getLoginResponse() throws ClassNotFoundException, IOException {
		return (Character)input.readObject();
	}
	
	void sendObject(Serializable obj) throws IOException {
		output.writeObject(obj);
	}
}
