package client.client;

import javax.swing.JOptionPane;

import client.controller.Controller;
import client.controller.ProfessorController;
import client.controller.StudentController;
import client.view.ProfessorView;
import client.view.StudentView;
import client.view.View;
import shared.response.LoginResponse;

/**
 * The main class for the client application. Used to login and start the program.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class Client {
	/**
	 * The main function used to start the program. Creates a login window and attempts
	 * to authenticate the user. If authentication is successful, the appropriate GUI
	 * is created.
	 * @param args Command line args, not used.
	 */
	public static void main(String[] args) {
		View view = null;
		try {
			Controller controller = null;
			ServerConnection server = new ServerConnection();
			LoginResponse login = (new LoginDialog(server)).login();
			TableModel table = new TableModel();
			if (login.getType() == LoginResponse.PROFESSOR) {
				ProfessorView pView = new ProfessorView(login.getName(), table);
				controller = new ProfessorController(pView, table, server);
				view = pView;
			} else if(login.getType() == LoginResponse.STUDENT) {
				StudentView sView = new StudentView(login.getName(), table);
				controller = new StudentController(sView, table, server);
				view = sView;
			} else {
				JOptionPane.showMessageDialog(view, "User type " + login.getType() + " not supported!");
				return;
			}
			controller.runClient();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Fatal Error: " + e.getMessage());
			System.exit(1);
		}
	}
}
