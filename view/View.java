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
	public static final int CHAT_PAGE = 2;
	private final String[] headers;
	private final String[] addText;
	
	int page = HOME_PAGE;
	JPanel[] buttonPanels;
	JLabel header;
	private JButton sendBtn, cancelBtn, chatBackBtn, chatSubmitBtn;
	private JTextField subjectTxt, messageTxt;
	private JTextArea contentArea, chatArea;
	private JPanel emailEditor, tablePanel, chatPanel;
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
		specialTransitions(page);
		remove(buttonPanels[this.page]);
		add(buttonPanels[page], BorderLayout.SOUTH);
		repaint();
		this.page = page;
		itemDeselected();
	}
	
	private void specialTransitions(int page) {
		if (page == EMAIL_PAGE) {
			remove(tablePanel);
			add(emailEditor, BorderLayout.CENTER);
		} else if (page == CHAT_PAGE) {
			remove(tablePanel);
			add(chatPanel, BorderLayout.CENTER);
		} else if (this.page == EMAIL_PAGE) {
			remove(emailEditor);
			add(tablePanel, BorderLayout.CENTER);
		} else if (this.page == CHAT_PAGE) {
			remove(chatPanel);
			add(tablePanel, BorderLayout.CENTER);
		}
	}
	
	public abstract void itemDeselected();
	public abstract void itemSelected();
	
	public void setChatMessage(String str) {
		messageTxt.setText(str);
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}
	
	public String getChatMessage() {
		return messageTxt.getText();
	}
	
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
	
	public void addChatSubmitListener(ActionListener listener) {
		chatSubmitBtn.addActionListener(listener);
	}
	
	public void addChatBackListener(ActionListener listener) {
		chatBackBtn.addActionListener(listener);
	}
	
	public String getSubject() {
		return subjectTxt.getText();
	}
	
	public String getContent() {
		return contentArea.getText();
	}
	
	public void clearMessage() {
		subjectTxt.setText("");
		contentArea.setText("");
	}
	
	public void clearChat() {
		messageTxt.setText("");
		chatArea.setText("");
	}
	
	void layoutButtonPanels() {
		buttonPanels[EMAIL_PAGE] = createEmailButtons();
		buttonPanels[CHAT_PAGE] = createChatButtons();
	}

	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
		emailEditor = createEmailEditor();
		chatPanel = createChatArea();
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
		panel.add(new JScrollPane(contentArea = new JTextArea()), BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createChatArea() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		panel.add(new JScrollPane(chatArea = new JTextArea()), BorderLayout.CENTER);
		chatArea.setEditable(false);
		panel.add(messageTxt = new JTextField(), BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createEmailButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(cancelBtn = new JButton("Cancel"));
		panel.add(sendBtn = new JButton("Send"));
		return panel;
	}
	
	private JPanel createChatButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(chatBackBtn = new JButton("Back"));
		panel.add(chatSubmitBtn = new JButton("Submit"));
		return panel;
	}
	
	private JPanel createEmailHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Subject: "), BorderLayout.WEST);
		panel.add(subjectTxt = new JTextField(), BorderLayout.CENTER);
		return panel;
	}
}
