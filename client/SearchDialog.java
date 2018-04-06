package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import message.RequestStudents;

public class SearchDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JRadioButton idRdio, nameRdio;
	private JTextField searchTxt;
	private JButton searchBtn;
	private RequestStudents request;
	
	private SearchDialog(JFrame owner) {
		super(owner, "Search Students", true);
		layoutDialog();
		addHandlers();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
	
	static RequestStudents showSearchDialog(JFrame owner) {
		SearchDialog dialog = new SearchDialog(owner);
		dialog.setVisible(true);
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			dialog.dispose();
		}
		return dialog.request;
	}
	
	private void addHandlers() {
		addSearchHandler();
		addCloseHandler();
	}
	
	private void addSearchHandler() {
		final Thread thread = Thread.currentThread();
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					validateInput();
					if (idRdio.isSelected())
						request = new RequestStudents(RequestStudents.ID, Integer.parseInt(searchTxt.getText()));
					else
						request = new RequestStudents(RequestStudents.NAME, searchTxt.getText());
					thread.interrupt();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(getOwner(), "ID must be a number!");
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
		add(createSearchType());
		add(createSearchParameter());
		add(createSearchButton());
		pack();
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
