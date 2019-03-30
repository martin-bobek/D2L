package client.view;

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
import javax.swing.text.DefaultCaret;

import client.client.TableModel;

/**
 * An abstract class which implements all the shared functionalities of the student and professor GUI's.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
abstract public class View extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * A constant used to select the home page.
	 */
	private static final int HOME_PAGE = 0;
	/**
	 * A constant used to select the email page.
	 */
	public static final int EMAIL_PAGE = 1;
	/**
	 * A constant used to select the chat page.
	 */
	public static final int CHAT_PAGE = 2;
	/**
	 * A list of all the headers for the GUI.
	 */
	private final String[] headers;
	/**
	 * Additional text in the headers added based on dynamic data.
	 */
	private final String[] addText;
	
	/**
	 * The currently selected page.
	 */
	int page = HOME_PAGE;
	/**
	 * All the panels of buttons for the pages.
	 */
	JPanel[] buttonPanels;
	/**
	 * A handle to the header label.
	 */
	JLabel header;
	/**
	 * Handles to all the shared buttons in the GUI.
	 */
	private JButton sendBtn, cancelBtn, chatBackBtn, chatSubmitBtn;
	/**
	 * Handles to all the shared text fields.
	 */
	private JTextField subjectTxt, messageTxt;
	/**
	 * Handles to the two the text areas in the GUI.
	 */
	private JTextArea contentArea, chatArea;
	/**
	 * The three main center panels used by both GUI's.
	 */
	private JPanel emailEditor, tablePanel, chatPanel;
	/**
	 * A handle to the main JTable object.
	 */
	private JTable table;
	
	/**
	 * Creates a new view object, initializing all fields and laying out the shared components of the frame.
	 * @param title The title of the GUI.
	 * @param table The table model for the main table.
	 * @param username The user's name.
	 * @param headers The headers for the pages in the GUI.
	 */
	View(String title, TableModel table, String username, String[] headers) {
		super(title);
		this.headers = headers;
		buttonPanels = new JPanel[headers.length];
		this.addText = new String[headers.length];
		for (int i = 0; i < headers.length; i++)
			this.addText[i] = "";
		setAdditionalText(username, HOME_PAGE);
		layoutFrame();
		this.table.setModel(table);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 300));
	}
	
	/**
	 * Sets the additional text in the headers.
	 * @param text The line of additional text.
	 * @param page The page for which the text should be set.
	 */
	public void setAdditionalText(String text, int page) {
		if (text.length() > 35)
			addText[page] = " - " + text.substring(0, 33) + "...";
		else
			addText[page] = " - " + text;
	}
	
	/**
	 * Selects which page the view should show.
	 * @param page The page number.
	 */
	public void selectPage(int page) {
		header.setText(headers[page] + addText[page]);
		specialTransitions(page);
		remove(buttonPanels[this.page]);
		add(buttonPanels[page], BorderLayout.SOUTH);
		repaint();
		this.page = page;
		itemDeselected();
	}
	
	/**
	 * Performs transitions between pages where the content of the center 
	 * area needs to change.
	 * @param page The new page number.
	 */
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
	
	/**
	 * No item is selected in the table.
	 */
	public abstract void itemDeselected();
	/**
	 * An item is selected in the table.
	 */
	public abstract void itemSelected();
	
	/**
	 * Sets the contents of the chat message field.
	 * @param str The new contents.
	 */
	public void setChatMessage(String str) {
		messageTxt.setText(str);
	}
	
	/**
	 * Gets a handler for the main chat text area.
	 * @return The chat text area.
	 */
	public JTextArea getChatArea() {
		return chatArea;
	}
	
	/**
	 * Gets the chat message entered by the user.
	 * @return New chat message.
	 */
	public String getChatMessage() {
		return messageTxt.getText();
	}
	
	/**
	 * Gets the index of the item currently selected in the table.
	 * @return The row number.
	 */
	public int getSelected() {
		return table.getSelectedRow();
	}
	
	/**
	 * Adds an event listener for the table selection event.
	 * @param listener The new listener.
	 */
	public void addSelectionListener(ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}
	
	/**
	 * Adds an event listener for the cancel button click event.
	 * @param listener The new listener.
	 */
	public void addCancelListener(ActionListener listener) {
		cancelBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the send button click event.
	 * @param listener The new listener.
	 */
	public void addSendListener(ActionListener listener) {
		sendBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the chat send button click event.
	 * @param listener The new listener.
	 */
	public void addChatSubmitListener(ActionListener listener) {
		chatSubmitBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the chat back button click event.
	 * @param listener The new listener.
	 */
	public void addChatBackListener(ActionListener listener) {
		chatBackBtn.addActionListener(listener);
	}
	
	/**
	 * Gets the text in the subject line of the email.
	 * @return The subject.
	 */
	public String getSubject() {
		return subjectTxt.getText();
	}
		
	/**
	 * Gets the content of the email.
	 * @return The content.
	 */
	public String getContent() {
		return contentArea.getText();
	}
	
	/**
	 * Clears the new email message.
	 */
	public void clearMessage() {
		subjectTxt.setText("");
		contentArea.setText("");
	}
	
	/**
	 * Clears the chat window.
	 */
	public void clearChat() {
		messageTxt.setText("");
		chatArea.setText("");
	}
	
	/**
	 * Lays out all the shared button panels.
	 */
	void layoutButtonPanels() {
		buttonPanels[EMAIL_PAGE] = createEmailButtons();
		buttonPanels[CHAT_PAGE] = createChatButtons();
	}

	/**
	 * Lays out the whole frame.
	 */
	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
		emailEditor = createEmailEditor();
		chatPanel = createChatArea();
	}
	
	/**
	 * Creates the table text area.
	 * @return The new laid out panel.
	 */
	private JPanel createTextArea() {
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		return tablePanel;
	}
	
	/**
	 * Creates the header for the GUI.
	 * @return The new header label.
	 */
	private JLabel createHeader() {
		header = new JLabel(headers[page] + addText[page], SwingConstants.CENTER);
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));
		return header;
	}
	
	/**
	 * Creates the email editor panel.
	 * @return The newly laid out panel.
	 */
	private JPanel createEmailEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		panel.add(createEmailHeader(), BorderLayout.NORTH);
		panel.add(new JScrollPane(contentArea = new JTextArea()), BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * Creates the text area of the chat.
	 * @return The newly laid out panel.
	 */
	private JPanel createChatArea() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		JScrollPane scroller = new JScrollPane(chatArea = new JTextArea());
		DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panel.add(scroller, BorderLayout.CENTER);
		chatArea.setEditable(false);
		panel.add(messageTxt = new JTextField(), BorderLayout.SOUTH);
		return panel;
	}

	 /**
	  * Creates the email writer button panel.
	  * @return The newly laid out panel.
	  */
	private JPanel createEmailButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(sendBtn = new JButton("Send"));
		panel.add(cancelBtn = new JButton("Cancel"));
		return panel;
	}
	
	/**
	 * Creates the chat display button panel.
	 * @return The newly laid out panel.
	 */
	private JPanel createChatButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(chatSubmitBtn = new JButton("Send"));
		panel.add(chatBackBtn = new JButton("Back"));
		return panel;
	}
	
	/**
	 * Creates a the subject line for the email editor.
	 * @return The newly laid out panel.
	 */
	private JPanel createEmailHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Subject: "), BorderLayout.WEST);
		panel.add(subjectTxt = new JTextField(), BorderLayout.CENTER);
		return panel;
	}
}
