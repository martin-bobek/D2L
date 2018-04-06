package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private static final String[] MONTHS = { "", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	private final FileHelper fileHelper;
	private JTextField nameTxt, fileTxt, dayTxt, yearTxt;
	private JComboBox<String> monthCmb;
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
				try {
					validateInput();
					file = fileHelper.uploadFile(new File(fileTxt.getText()));
					assignment = new Assignment(nameTxt.getText(), file, parseDate());
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
		add(createDateRow());
		add(createSubmitRow());
		pack();
	}
	
	private JPanel createNameRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("Name"));
		panel.add(nameTxt = new JTextField(17));
		return panel;
	}
	
	private JPanel createFileRow() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(new JLabel("File"));
		panel.add(fileTxt = new JTextField(17));
		return panel;
	}
	
	private JPanel createDateRow() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Due:"));
		panel.add(monthCmb = new JComboBox<String>(MONTHS));
		panel.add(new JLabel("Day"));
		panel.add(dayTxt = new JTextField(2));
		panel.add(new JLabel("Year"));
		panel.add(yearTxt = new JTextField(4));
		return panel;
	}
	
	private JPanel createSubmitRow() {
		JPanel panel = new JPanel();
		panel.add(uploadBtn = new JButton("Upload"));
		return panel;
	}
	
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
	
	private Date parseDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("ddMMMyyyy");
		format.setLenient(false);
		return format.parse(dayTxt.getText() + (String)monthCmb.getSelectedItem() + yearTxt.getText());
	}
}
