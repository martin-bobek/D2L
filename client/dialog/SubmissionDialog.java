package client.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.client.FileHelper;
import client.client.InvalidParameterException;
import client.view.StudentView;
import shared.data.Submission;

/**
 * A custom submission dialog which is used to allow the users to submit a file for an assignment.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class SubmissionDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * A handle to the file helper used to upload files.
	 */
	private final FileHelper fileHelper;
	/**
	 * Handles to the buttons in the dialog.
	 */
	private JButton browseBtn, submitBtn;
	/**
	 * A handle to the user input text field.
	 */
	private JTextField fileTxt;
	/**
	 * The submission object being constructed by the Dialog.
	 */
	private Submission submission;

	/**
	 * Creates a new dialog, laying out the window and subscribing handlers.
	 * @param owner The owner of the dialog window.
	 * @param helper The file helper used to upload files.
	 */
	private SubmissionDialog(JFrame owner, FileHelper helper) {
		super(owner, "Upload Submission");
		fileHelper = helper;
		addHandlers();
	}
	
	/**
	 * Creates a new submission dialog, and returns when the user cancels or finishes entering inputs.
	 * @param owner The owner of the dialog window.
	 * @param helper The file helper used to upload files.
	 * @return The submission constructed from user inputs. Null if the user cancels.
	 */
	public static Submission showSubmissionDialog(StudentView owner, FileHelper helper) {
		SubmissionDialog dialog = new SubmissionDialog(owner, helper);
		dialog.runDialog();
		return dialog.submission;
	}
	
	/**
	 * Subscribes all the event handlers. 
	 */
	void addHandlers() {
		super.addHandlers();
		addSubmitHandler();
		addBrowseHandler();
	}
	
	/**
	 * Adds a handler for the submit button. Validates user input, uploads the selected file,
	 * and constructs the new user submission.
	 */
	private void addSubmitHandler() {
		final Thread thread = Thread.currentThread();
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					validateInput();
					fileHelper.setPath(new File(fileTxt.getText()));
					submission = new Submission(limitLength(fileHelper.getName()), 
							fileHelper.getExtension(), fileHelper.uploadFile(), new Date());
					thread.interrupt();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(getOwner(), "File Error: " + ex.getMessage());
				} catch (InvalidParameterException ex) {
					JOptionPane.showMessageDialog(getOwner(), ex.getMessage());
				}
			}
		});
	}
	
	/** 
	 * Adds an event handler for the Browse button. Clicking the button
	 * opens a file chooser window, allowing the user to select a file. 
	 */
	private void addBrowseHandler() {
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION)
					fileTxt.setText(chooser.getSelectedFile().toString());
			}
		});
	}
	
	/**
	 * Lays out the components in the GUI.
	 */
	void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createFileRow());
		add(createSubmitRow());
	}
	
	/**
	 * Creates the file selection row.
	 * @return The laid out panel.
	 */
	private JPanel createFileRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("File"));
		panel.add(fileTxt = new JTextField(17));
		return panel;
	}
	
	/**
	 * Creates the button row.
	 * @return The new laid out panel.
	 */
	private JPanel createSubmitRow() {
		JPanel panel = new JPanel();
		panel.add(browseBtn = new JButton("Browse"));
		panel.add(submitBtn = new JButton("Submit"));
		return panel;
	}
	
	/**
	 * Validates the user inputs.
	 * @throws InvalidParameterException Not all inputs are specified.
	 */
	private void validateInput() throws InvalidParameterException {
		if (fileTxt.getText().isEmpty())
			throw new InvalidParameterException("A file must be specified!");
	}
	
	/**
	 * Cuts off the end off a string if it is too long.
	 * @param str The input string. 
	 * @return The formatted string. 
	 */
	private String limitLength(String str) {
		if (str.length() > 50)
			return str.substring(0, 50);
		return str;
	}
}
