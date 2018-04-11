package client;

import javax.swing.JOptionPane;

import clientMessage.LoginResponse;
import professor.ProfessorController;
import student.StudentController;
import view.ProfessorView;
import view.StudentView;
import view.View;

class Client {
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
			e.printStackTrace();
			JOptionPane.showMessageDialog(view, "Fatal Error: " + e.getMessage());
			System.exit(1);
		}
	}
}
