package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.TableModel;
import data.Assignment;

/**
 * The student view in the client application.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class StudentView extends View {
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates no item is currently selected in the table.
	 */
	public static final int NO_SELECTION = -1;
	/**
	 * The course page number.
	 */
	public static final int COURSE_PAGE = 0;
	/**
	 * The assignment page number.
	 */
	public static final int ASSIGNMENT_PAGE = 3;
	
	/**
	 * Handles to all the buttons in the GUI.
	 */
	private JButton courseViewBtn, assignmentBackBtn, downloadBtn, submitBtn, emailBtn, chatBtn;
	/**
	 * The table model used for the main JTable in the GUI.
	 */
	private TableModel tableModel;

	/**
	 * Creates a new student view.
	 * @param name The name of the user.
	 * @param table The table model.
	 */
	public StudentView(String name, TableModel table) {
		super("Student Client", table, name, 
			new String[] { "Courses", "Compose", "Chat", "Assignments" });
		tableModel = table;
	}
	
	/**
	 * Responds to an item being selected in the table.
	 */
	public void itemSelected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(true);
		} else if (page == ASSIGNMENT_PAGE) {
			downloadBtn.setEnabled(true);
			submitBtn.setEnabled(!((Assignment)tableModel.getRow(getSelected())).getSubmitted());
		}
	}
	
	/**
	 * Responds to an item being deselected in the table.
	 */
	public void itemDeselected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(false);
		} else if (page == ASSIGNMENT_PAGE) {
			downloadBtn.setEnabled(false);
			submitBtn.setEnabled(false);
		}
	}
	
	/**
	 * Adds an event listener for the chat button click event.
	 * @param listener The new listener.
	 */
	public void addChatListener(ActionListener listener) {
		chatBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the email button click event.
	 * @param listener The new listener.
	 */
	public void addEmailListener(ActionListener listener) {
		emailBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the submit button click event.
	 * @param listener The new listener.
	 */
	public void addSubmitListener(ActionListener listener) {
		submitBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the download button click event.
	 * @param listener The new listener.
	 */
	public void addDownloadListener(ActionListener listener) {
		downloadBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the back button click event.
	 * @param listener The new listener.
	 */
	public void addAssignmentBackListener(ActionListener listener) {
		assignmentBackBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the course view button click event.
	 * @param listener The new listener.
	 */
	public void addCourseViewListener(ActionListener listener) {
		courseViewBtn.addActionListener(listener);
	}
	
	/**
	 * Lays out the components of the frame.
	 */
	void layoutButtonPanels() {
		super.layoutButtonPanels();
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
	}
	
	/**
	 * Lays out the button row for the assignment page.
	 * @return The newly laid out panel.
	 */
	private JPanel createAssignmentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(downloadBtn = new JButton("Download"));
		panel.add(submitBtn = new JButton("Submit"));
		panel.add(chatBtn = new JButton("Chat"));
		panel.add(emailBtn = new JButton("Email"));
		panel.add(assignmentBackBtn = new JButton("Back"));
		return panel;
	}

	/**
	 * Lays out the button row for the main course page.
	 * @return The newly laid out panel.
	 */
	private JPanel createCourseButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(courseViewBtn = new JButton("View"));
		courseViewBtn.setEnabled(false);
		return panel;
	}
}
