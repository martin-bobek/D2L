package client.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.client.FileHelper;
import client.client.InvalidParameterException;
import shared.data.Assignment;

/**
 * A custom assignment dialog, allowing professors to upload a new assignment.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class AssignmentDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * The three letter abbreviations for all the months.
	 */
	private static final String[] MONTHS = { "", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	/**
	 * The file helper used to upload the file.
	 */
	private final FileHelper fileHelper;
	/**
	 * Handles to all the input fields.
	 */
	private JTextField nameTxt, fileTxt, dayTxt, yearTxt;
	/**
	 * The combo box used to select months.
	 */
	private JComboBox<String> monthCmb;
	/**
	 * Handles to the buttons in the dialog.
	 */
	private JButton uploadBtn, browseBtn;
	/**
	 * The new assignment being created in the dialog.
	 */
	private Assignment assignment;
	
	/**
	 * Creates a new assignment dialog.
	 * @param owner The view which owns the dialog.
	 * @param helper The file helper used to download files.
	 */
	private AssignmentDialog(JFrame owner, FileHelper helper) {
		super(owner, "Upload Assignment");
		fileHelper = helper;
		addHandlers();
	}
	
	/**
	 * Creates a new assignment dialog, returning once the users finishes entering input
	 * or cancels.
	 * @param owner The view which owns the dialog.
	 * @param helper The file helper used to download files.
	 * @return The new assignment which the dialog created.
	 */
	public static Assignment showAssignmentDialog(JFrame owner, FileHelper helper) {
		AssignmentDialog dialog = new AssignmentDialog(owner, helper);
		dialog.runDialog();
		return dialog.assignment;
	}
	
	/**
	 * Adds all the event handlers.
	 */
	void addHandlers() {
		super.addHandlers();
		addUploadHandler();
		addBrowseHandler();
	}
	
	/**
	 * Adds an event handler for the Browse button. Causes a file browser
	 * to open, allowing user's to select a file.
	 */
	private void addBrowseHandler() {
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION)
					fileTxt.setText(chooser.getSelectedFile().toString());
			}
		});
	}
	
	/**
	 * Adds an event handler for the upload button. Validates inputs entered by the user,
	 * uploads the selected file, and constructs the new assignment.
	 */
	private void addUploadHandler() {
		final Thread thread = Thread.currentThread();
		uploadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					validateInput();
					fileHelper.setPath(new File(fileTxt.getText()));
					assignment = new Assignment(nameTxt.getText(), parseDate(), fileHelper.getExtension(), fileHelper.uploadFile());
					thread.interrupt();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(getOwner(), "File Error: " + ex.getMessage());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(getOwner(), "Invalid date!");
				} catch (InvalidParameterException ex) {
					JOptionPane.showMessageDialog(getOwner(), ex.getMessage());
				}
			}
		});
	}
	
	/**
	 * Lays out all the components in the dialog.
	 */
	void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createNameRow());
		add(createDateRow());
		add(createFileRow());
		add(createSubmitRow());
		pack();
	}
	
	/**
	 * Creates the row for naming the assignment.
	 * @return The newly laid out panel.
	 */
	private JPanel createNameRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("Name"));
		panel.add(nameTxt = new JTextField(16));
		return panel;
	}
	
	/**
	 * creates the row for specifying a file.
	 * @return The newly laid out panel.
	 */
	private JPanel createFileRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("File"));
		panel.add(fileTxt = new JTextField(17));
		return panel;
	}
	
	/**
	 * Creates the row for specifying the due date.
	 * @return The new laid out panel.
	 */
	private JPanel createDateRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("Due:"));
		panel.add(monthCmb = new JComboBox<String>(MONTHS));
		panel.add(new JLabel("Day"));
		panel.add(dayTxt = new JTextField(2));
		panel.add(new JLabel("Year"));
		panel.add(yearTxt = new JTextField(4));
		return panel;
	}
	
	/**
	 * Creates the button row for the dialog.
	 * @return The newly laid out panel.
	 */
	private JPanel createSubmitRow() {
		JPanel panel = new JPanel();
		panel.add(browseBtn = new JButton("Browse"));
		panel.add(uploadBtn = new JButton("Upload"));
		return panel;
	}
	
	/**
	 * Validates all user inputs, throwing an exception if an invalid input is entered.
	 * @throws InvalidParameterException Inputs are empty or too long.
	 */
	private void validateInput() throws InvalidParameterException {
		if (nameTxt.getText().isEmpty() ||
			   fileTxt.getText().isEmpty() ||
			   dayTxt.getText().isEmpty() || 
			   yearTxt.getText().isEmpty() ||
			   monthCmb.getSelectedIndex() == 0)
			throw new InvalidParameterException("Empty fields are not allowed!");
		if (nameTxt.getText().length() > 50)
			throw new InvalidParameterException("Name has a maximum of 50 characters!");
	}
	
	/**
	 * Parses the users due date input into a Date object.
	 * @return The parsed Date object.
	 * @throws ParseException Could not parse the users date.
	 */
	private Date parseDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("ddMMMyyyy");
		format.setLenient(false);
		return format.parse(dayTxt.getText() + (String)monthCmb.getSelectedItem() + yearTxt.getText());
	}
}
