package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Assignment;
import data.FileContent;
import helper.FileHelper;

class AssignmentDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final FileHelper fileHelper;
	private JTextField nameTxt, fileTxt;
	private JButton uploadBtn;
	private FileContent file;
	private Assignment assignment;
	
	private AssignmentDialog(JFrame owner, FileHelper helper) {
		super(owner, "Upload Assignment", true);
		fileHelper = helper;
		layoutDialog();
		addHandlers();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
	
	static Assignment showAssignmentDialog(JFrame owner, FileHelper helper) {
		AssignmentDialog dialog = new AssignmentDialog(owner, helper);
		dialog.setVisible(true);
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			dialog.dispose();
		}
		return dialog.assignment;
	}
	
	private void addHandlers() {
		addUploadHandler();
		addCloseHandler();
	}
	
	private void addUploadHandler() {
		final Thread thread = Thread.currentThread();
		uploadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!filled()) {
					JOptionPane.showMessageDialog(getOwner(), "Empty fields are not allowed!");
					return;
				}
				try {
					file = fileHelper.uploadFile(new File(fileTxt.getText()));
					assignment = new Assignment(nameTxt.getText(), file);
					thread.interrupt();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(getOwner(), "File Error: " + ex.getMessage());
				}
			}
		});
	}
	
	private void addCloseHandler() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		final Thread thread = Thread.currentThread();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				thread.interrupt();
			}
		});
	}
	
	private void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createNameRow());
		add(createFileRow());
		add(createSubmitRow());
		pack();
	}
	
	private JPanel createNameRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("Name"));
		panel.add(nameTxt = new JTextField(20));
		return panel;
	}
	
	private JPanel createFileRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("File"));
		panel.add(fileTxt = new JTextField(20));
		return panel;
	}
	
	private JPanel createSubmitRow() {
		JPanel panel = new JPanel();
		panel.add(uploadBtn = new JButton("Upload"));
		return panel;
	}
	
	private boolean filled() {
		return !nameTxt.getText().isEmpty() &&
			   !fileTxt.getText().isEmpty();
	}
}
