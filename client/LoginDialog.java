package client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import data.LoginCredentials;
import helper.ServerConnection;

class LoginDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField userIdTxt;
	private JPasswordField passwordTxt;
	private JButton loginBtn;
	private ServerConnection server;
	private AtomicBoolean locked = new AtomicBoolean(false);

	LoginDialog(ServerConnection server) {
		super("Login");
		this.server = server;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutDialog();
		pack();
		addLoginHandler();
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	char login() throws ClassNotFoundException, IOException {
		char response;
		while (true) {
			response = server.getLoginResponse();
			if (response != LoginCredentials.BAD_LOGIN) {
				dispose();
				return response;
			}
			JOptionPane.showMessageDialog(this, "Invalid username or password!");
			locked.set(false);
		}
	}
	
	private void addLoginHandler() {
		final JFrame this_ = this;
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				try {
					server.sendObject(validateInput());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this_, "Lost connection to server!");
					System.exit(1);
				} catch (InvalidParameterException ex) {
					JOptionPane.showMessageDialog(this_, ex.getMessage());
					locked.set(false);
				}
			}
		});
	}
	
	private void layoutDialog() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(createFields(), BorderLayout.CENTER);
		panel.add(createButton(), BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
	}
	
	private JPanel createFields() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("User ID:"), gridConstraints(0, 0));
		panel.add(userIdTxt = new JTextField(15), gridConstraints(1, 0));
		panel.add(new JLabel("Password:"), gridConstraints(0, 1));
		panel.add(passwordTxt = new JPasswordField(15), gridConstraints(1, 1));
		return panel;
	}
	
	private JPanel createButton() {
		JPanel panel = new JPanel();
		panel.add(loginBtn = new JButton("Login"));
		return panel;
	}
	
	private GridBagConstraints gridConstraints(int x, int y) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.NONE;
		return constraints;
	}
	
	private LoginCredentials validateInput() throws InvalidParameterException {
		String userId = userIdTxt.getText();
		char[] password = passwordTxt.getPassword();
		if (userId == null || userId.length() != 6)
			throw new InvalidParameterException("User ID must be 6 digits!");
		if (password == null || password.length == 0)
			throw new InvalidParameterException("Password cannot be empty!");		
		try {
			return new LoginCredentials(Integer.parseInt(userId), new String(password));
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Invalid User ID!");
		}
	}
}
