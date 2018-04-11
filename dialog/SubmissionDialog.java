package dialog;

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

import client.FileHelper;
import client.InvalidParameterException;
import data.Submission;
import view.StudentView;

public class SubmissionDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;
	private final FileHelper fileHelper;
	private JButton browseBtn, submitBtn;
	private JTextField fileTxt;
	private Submission submission;

	private SubmissionDialog(JFrame owner, FileHelper helper) {
		super(owner, "Upload Submission");
		fileHelper = helper;
		addHandlers();
	}
	
	public static Submission showSubmissionDialog(StudentView owner, FileHelper helper) {
		SubmissionDialog dialog = new SubmissionDialog(owner, helper);
		dialog.runDialog();
		return dialog.submission;
	}
	
	void addHandlers() {
		super.addHandlers();
		addSubmitHandler();
		addBrowseHandler();
	}
	
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
	
	private void addBrowseHandler() {
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION)
					fileTxt.setText(chooser.getSelectedFile().toString());
			}
		});
	}
	
	void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createFileRow());
		add(createSubmitRow());
	}
	
	private JPanel createFileRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("File"));
		panel.add(fileTxt = new JTextField(17));
		return panel;
	}
	
	private JPanel createSubmitRow() {
		JPanel panel = new JPanel();
		panel.add(browseBtn = new JButton("Browse"));
		panel.add(submitBtn = new JButton("Submit"));
		return panel;
	}
	
	private void validateInput() throws InvalidParameterException {
		if (fileTxt.getText().isEmpty())
			throw new InvalidParameterException("A file must be specified!");
	}
	
	private String limitLength(String str) {
		if (str.length() > 50)
			return str.substring(0, 50);
		return str;
	}
}