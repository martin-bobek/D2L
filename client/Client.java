package client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import data.LoginCredentials;
import professor.ProfessorController;
import professor.ProfessorView;
import student.StudentController;
import student.StudentView;

class Client {
	public static void main(String[] args) {
		
		JFrame view = null;
		try {
			Controller controller = null;
			ServerConnection server = new ServerConnection();
			LoginDialog login = new LoginDialog(server);
			char type = login.login();
			TableModel table = new TableModel();
			if (type == LoginCredentials.PROFESSOR) {
				ProfessorView pView = new ProfessorView(table);
				controller = new ProfessorController(pView, table, server);
				view = pView;
			} else if(type == LoginCredentials.STUDENT) {
				StudentView sView = new StudentView(table);
				controller = new StudentController(sView, table, server);
				view = sView;
			} else {
				JOptionPane.showMessageDialog(view, "User type " + type + " not supported!");
				return;
			}
			controller.runClient();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Fatal Error: " + e.getMessage());
			System.exit(1);
		}
	}
}
