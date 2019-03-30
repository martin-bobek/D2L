package client.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import client.client.InvalidParameterException;
import shared.request.StudentRequest;

/**
 * A custom dialog used to enter search parameters for viewing students.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018 
 */
public class SearchDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * Handlers for the radio buttons in the dialog.
	 */
	private JRadioButton idRdio, nameRdio;
	/**
	 * A handle for the user input field.
	 */
	private JTextField searchTxt;
	/**
	 * A handle to the button in the dialog.
	 */
	private JButton searchBtn;
	/**
	 * The StudentRequest being constructed by the dialog.
	 */
	private StudentRequest request;
	
	/**
	 * Creates a new search dialog, adding all event handlers.
	 * @param owner The owner of the dialog.
	 */
	private SearchDialog(JFrame owner) {
		super(owner, "Search Students");
		addHandlers();
	}
	
	/**
	 * Opens a new search dialog and returns once the user has entered the desired inputs or cancelled.
	 * @param owner The view which owns the dialog.
	 * @return The request holding the search parameters obtained from the user.
	 */
	public static StudentRequest showSearchDialog(JFrame owner) {
		SearchDialog dialog = new SearchDialog(owner);
		dialog.runDialog();
		return dialog.request;
	}
	
	/**
	 * Adds all the event handlers to the dialog.
	 */
	void addHandlers() {
		super.addHandlers();
		addSearchHandler();
	}
	
	/**
	 * Adds the event handler to the search button. It validates inputs and
	 * constructs a new Request object from the user's inputs.
	 */
	private void addSearchHandler() {
		final Thread thread = Thread.currentThread();
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					validateInput();
					if (idRdio.isSelected())
						request = new StudentRequest(StudentRequest.ID, Integer.parseInt(searchTxt.getText()));
					else
						request = new StudentRequest(StudentRequest.NAME, searchTxt.getText());
					thread.interrupt();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(getOwner(), "ID must be a number!");
				} catch (InvalidParameterException ex) {
					JOptionPane.showMessageDialog(getOwner(), ex.getMessage());
				}
			}
		});
		
	}
	/**
	 * Lays out the components of the dialog.
	 */
	void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createSearchType());
		add(createSearchParameter());
		add(createSearchButton());
	}
	
	/**
	 * Creates the row of radio buttons.
	 * @return The newly laid out panel.
	 */
	private JPanel createSearchType() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		panel.add(idRdio = createRadioButton("Student ID", group));
		panel.add(nameRdio = createRadioButton("Last Name", group));
		return panel;
	}
	
	/**
	 * Creates a single radio button.
	 * @param label The label for the button.
	 * @param group The group which the button is a part of.
	 * @return The new button.
	 */
	private JRadioButton createRadioButton(String label, ButtonGroup group) {
		JRadioButton button = new JRadioButton(label);
		group.add(button);
		return button;
	}
	
	/**
	 * Creates the line containing the search parameter.
	 * @return The newly laid out panel.
	 */
	private JPanel createSearchParameter() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Search:"));
		panel.add(searchTxt = new JTextField(10));
		return panel;
	}
	
	/**
	 * Creates the row containing the search button.
 	 * @return The newly laid out button.
	 */
	private JPanel createSearchButton() {
		JPanel panel = new JPanel();
		panel.add(searchBtn = new JButton("Search"));
		return panel;
	}
	
	/**
	 * Validates the user input, throwing an exception if invalid inputs are entered.
	 * @throws InvalidParameterException Not all inputs are specified.
	 */
	private void validateInput() throws InvalidParameterException {
		if (!idRdio.isSelected() && !nameRdio.isSelected())
			throw new InvalidParameterException("A search type must be selected!");
		if (searchTxt.getText().isEmpty())
			throw new InvalidParameterException("Search parameter must be filled!");
	}
}
