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
import response.LoginResponse;

/**
 * A small login dialog which gets a user ID and password from the user, and
 * attempts to authenticate the user by contacting the server.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class LoginDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * A handle to the userId text field.
	 */
	private JTextField userIdTxt;
	/**
	 * A handle to the password field.
	 */
	private JPasswordField passwordTxt;
	/**
	 * A handle to the login button.
	 */
	private JButton loginBtn;
	/**
	 * A connection to the server.
	 */
	private ServerConnection server;
	/**
	 * Used to lock the GUI while waiting for the server to respond for 
	 * some critical operations.
	 */
	private AtomicBoolean locked = new AtomicBoolean(false);

	/**
	 * Creates a new login GUI, laying out the window and subscribing all even listeners.
	 * @param server A handle to the server connection.
	 */
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
	
	/**
	 * Runs the login GUI, until the user has successfully logged in and authenticated.
	 * @return The login response from the server.
	 * @throws ClassNotFoundException An unexpected class was received on the socket.
	 * @throws IOException Failed to send serialized messages.
	 */
	LoginResponse login() throws ClassNotFoundException, IOException {
		LoginResponse response;
		while (true) {
			response = server.getLoginResponse();
			if (response.success()) {
				dispose();
				return response;
			}
			JOptionPane.showMessageDialog(this, "Invalid username or password!");
			locked.set(false);
		}
	}
	
	/**
	 * Adds a handler for the login button. If the GUI is not locked, sends the login credentials
	 * to the server. 
	 */
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
	
	/**
	 * Lays out the components in the overall frame. 
	 */
	private void layoutDialog() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(createFields(), BorderLayout.CENTER);
		panel.add(createButton(), BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates a panel containing the two user input fields.
	 * @return The laid out panel.
	 */
	private JPanel createFields() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("User ID:"), gridConstraints(0, 0));
		panel.add(userIdTxt = new JTextField(15), gridConstraints(1, 0));
		panel.add(new JLabel("Password:"), gridConstraints(0, 1));
		panel.add(passwordTxt = new JPasswordField(15), gridConstraints(1, 1));
		return panel;
	}
	
	/**
	 * Creates the button panel at the bottom of the GUI.
	 * @return The layed out panel.
	 */
	private JPanel createButton() {
		JPanel panel = new JPanel();
		panel.add(loginBtn = new JButton("Login"));
		return panel;
	}
	
	/**
	 * Creates a constraints object for the GridBag layout.
	 * @param x The column of the component.
	 * @param y The row of the component.
	 * @return The constraints object.
	 */
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
	
	/**
	 * Validates the input from the text fields, throwing an exception if the inputs are invalid.
	 * @return An object containing the validated inputs.
	 * @throws InvalidParameterException Inputs were empty or not of the correct format.
	 */
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
