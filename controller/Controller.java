package controller;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import client.FileHelper;
import client.ServerConnection;
import client.TableModel;
import request.CourseRequest;
import view.View;

public abstract class Controller {
	final TableModel table;
	final ServerConnection server;
	final FileHelper fileHelper;
	final AtomicBoolean locked;			/// TODO - The locking mechanism is broken by the chat messages
	
	Controller(TableModel table, ServerConnection server) {
		server.addTable(table);
		server.addFileHelper(fileHelper = new FileHelper());
		this.table = table;
		this.server = server;
		locked = new AtomicBoolean(true);
	}
	
	public void runClient() throws IOException, ClassNotFoundException {
		server.sendObject(new CourseRequest());
		while (true) {
			server.receiveResponse();
			locked.set(false);
		}
	}
	
	void connectionLost(View view) {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
