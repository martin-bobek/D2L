package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import client.TableModel;

abstract public class View extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int HOME_PAGE = 0;
	public static final int EMAIL_PAGE = 1;
	private final String[] headers;
	private final String[] addText;
	
	int page = HOME_PAGE;
	JPanel[] buttonPanels;
	JLabel header;
	private JButton sendBtn, cancelBtn;
	private JTextField subjectTxt;
	private JTextArea emailContent;
	private JPanel emailEditor, tablePanel;
	private JTable table;
	
	View(String title, TableModel table, String[] headers) {
		super(title);
		this.headers = headers;
		buttonPanels = new JPanel[headers.length];
		this.addText = new String[headers.length];
		for (int i = 0; i < headers.length; i++)
			this.addText[i] = "";
		layoutFrame();
		this.table.setModel(table);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 300));
	}
	
	public void setAdditionalText(String text, int page) {
		if (text.length() > 35)
			addText[page] = " - " + text.substring(0, 33) + "...";
		else
			addText[page] = " - " + text;
	}
	
	public void selectPage(int page) {
		header.setText(headers[page] + addText[page]);
		if (page == EMAIL_PAGE) {
			remove(tablePanel);
			add(emailEditor, BorderLayout.CENTER);
		} else if (this.page == EMAIL_PAGE) {
			remove(emailEditor);
			add(tablePanel, BorderLayout.CENTER);
		}
		remove(buttonPanels[this.page]);
		add(buttonPanels[page], BorderLayout.SOUTH);
		repaint();
		this.page = page;
		itemDeselected();
	}
	
	public abstract void itemDeselected();
	public abstract void itemSelected();
	
	public int getSelected() {
		return table.getSelectedRow();
	}
	
	public void addSelectionListener(ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}
	
	public void addCancelListener(ActionListener listener) {
		cancelBtn.addActionListener(listener);
	}
	
	public void addSendListener(ActionListener listener) {
		sendBtn.addActionListener(listener);
	}
	
	public String getSubject() {
		return subjectTxt.getText();
	}
	
	public String getContent() {
		return emailContent.getText();
	}
	
	public void clearEmail() {
		subjectTxt.setText("");
		emailContent.setText("");
	}
	
	void layoutButtonPanels() {
		buttonPanels[EMAIL_PAGE] = createEmailButtons();
	}
	
	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
		emailEditor = createEmailEditor();
	}
	
	private JPanel createTextArea() {
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		return tablePanel;
	}
	
	private JLabel createHeader() {
		header = new JLabel(headers[page] + addText[page], SwingConstants.CENTER);
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));
		return header;
	}
	
	private JPanel createEmailEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		panel.add(createEmailHeader(), BorderLayout.NORTH);
		panel.add(new JScrollPane(emailContent = new JTextArea()), BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createEmailButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(sendBtn = new JButton("Send"));
		panel.add(cancelBtn = new JButton("Cancel"));
		return panel;
	}
	
	private JPanel createEmailHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Subject: "), BorderLayout.WEST);
		panel.add(subjectTxt = new JTextField(), BorderLayout.CENTER);
		return panel;
	}
}
