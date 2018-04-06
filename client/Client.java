package client;

import javax.swing.JOptionPane;

import data.LoginCredentials;
import helper.ServerConnection;
import helper.TableModel;

class Client {
	public static void main(String[] args) {
		
		ProfessorView view = null;
		try {
			ServerConnection server = new ServerConnection();
			LoginDialog login = new LoginDialog(server);			// TODO - Change login to be id based instead of email based
			char type = login.login();
			if (type == LoginCredentials.PROFESSOR) {
				TableModel table = new TableModel();
				view = new ProfessorView(table);
				ProfessorController controller = new ProfessorController(view, table, server);
				controller.runClient();
			} else {
				JOptionPane.showMessageDialog(view, "User type " + type + " not supported!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Fatal Error: " + e.getMessage());
			System.exit(1);
		}
	}
}
