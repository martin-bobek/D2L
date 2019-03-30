package client.controller;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import client.client.FileHelper;
import client.client.ServerConnection;
import client.client.TableModel;
import client.view.View;
import shared.request.CourseRequest;

/**
 * An abstract controller class with all the shared functionalities of the student and professor controller classes.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public abstract class Controller {
	/**
	 * The table model used to update the JTable in the view.
	 */
	final TableModel table;
	/**
	 * A connection to the server.
	 */
	final ServerConnection server;
	/**
	 * A helper class used to download and upload files.
	 */
	final FileHelper fileHelper;
	/**
	 * A locking mechanism used to prevent any new operations while waiting for 
	 * critical responses from the server.
	 */
	final AtomicBoolean locked;
	
	/**
	 * Creates a new controller object, adding all the model and helper classes.
	 * @param table The tabel model.
	 * @param server A connection to the server.
	 */
	Controller(TableModel table, ServerConnection server) {
		locked = new AtomicBoolean(true);
		server.addLock(locked);
		server.addTable(table);
		server.addFileHelper(fileHelper = new FileHelper());
		this.table = table;
		this.server = server;
	}
	
	/**
	 * Runs the client by sending out a course request (to fill the first page) then
	 * waiting for and servicing standard responses from the server.
	 * @throws IOException Failed to send serialized messages.
	 * @throws ClassNotFoundException An unexpected class was received on the socket.
	 */
	public void runClient() throws IOException, ClassNotFoundException {
		server.sendObject(new CourseRequest());
		while (true)
			server.receiveResponse();
	}
	
	/**
	 * If the connection is lost, is used to show an error dialog and shuts down the client.
	 * @param view The main view of the GUI.
	 */
	void connectionLost(View view) {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
