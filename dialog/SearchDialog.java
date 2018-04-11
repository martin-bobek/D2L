package dialog;

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

import client.InvalidParameterException;
import request.StudentRequest;

public class SearchDialog extends CustomDialog {
	private static final long serialVersionUID = 1L;
	private JRadioButton idRdio, nameRdio;
	private JTextField searchTxt;
	private JButton searchBtn;
	private StudentRequest request;
	
	private SearchDialog(JFrame owner) {
		super(owner, "Search Students");
		addHandlers();
	}
	
	public static StudentRequest showSearchDialog(JFrame owner) {
		SearchDialog dialog = new SearchDialog(owner);
		dialog.runDialog();
		return dialog.request;
	}
	
	void addHandlers() {
		super.addHandlers();
		addSearchHandler();
	}
	
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
	
	void layoutDialog() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(createSearchType());
		add(createSearchParameter());
		add(createSearchButton());
	}
	
	private JPanel createSearchType() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		panel.add(idRdio = createRadioButton("Student ID", group));
		panel.add(nameRdio = createRadioButton("Last Name", group));
		return panel;
	}
	
	private JRadioButton createRadioButton(String label, ButtonGroup group) {
		JRadioButton button = new JRadioButton(label);
		group.add(button);
		return button;
	}
	
	private JPanel createSearchParameter() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Search:"));
		panel.add(searchTxt = new JTextField(10));
		return panel;
	}
	
	private JPanel createSearchButton() {
		JPanel panel = new JPanel();
		panel.add(searchBtn = new JButton("Search"));
		return panel;
	}
	
	private void validateInput() throws InvalidParameterException {
		if (!idRdio.isSelected() && !nameRdio.isSelected())
			throw new InvalidParameterException("A search type must be selected!");
		if (searchTxt.getText().isEmpty())
			throw new InvalidParameterException("Search parameter must be filled!");
	}
}
