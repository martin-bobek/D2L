package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.TableModel;

/**
 * The main professor view in the client application.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ProfessorView extends View {
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates the item is selected in the table.
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
	 * The student page number.
	 */
	public static final int STUDENT_PAGE = 4;
	/**
	 * The dropbox page number.
	 */
	public static final int DROPBOX_PAGE = 5;
	
	/**
	 * Handles to all the buttons in the professor view.
	 */
	private JButton viewBtn, createCourseBtn, createAssignmentBtn, assignmentBackBtn, dropboxBtn, emailBtn,  
			studentsBtn, studentsBackBtn, searchBtn, clearSearchBtn, dropboxBackBtn, gradeBtn, downloadBtn, chatBtn;
	/**
	 * A handle to the checkbox in the view.
	 */
	private JCheckBox allStudentsChk;

	/**
	 * Creates a new professor view.
	 * @param name The name of proessor.
	 * @param table The table model for the main JTable.
	 */
	public ProfessorView(String name, TableModel table) {
		super("Professor Client", table, name, 
			new String[] { "Courses", "Compose", "Chat", "Assignments", "Students", "Dropbox" });
		
	}
	
	/**
	 * Enables or disables the clear search button.
	 * @param enabled true if enabled.
	 */
	public void setClearSearchEnabled(boolean enabled) {
		clearSearchBtn.setEnabled(enabled);
	}
	
	/**
	 * Sets the value of the all students checkbox.
	 * @param all The value to be shown.
	 */
	public void setAllStudents(boolean all) {
		allStudentsChk.setSelected(all);
	}
	
	/**
	 * Gets the value of the all students checkbox.
	 * @return true if the box is selected.
	 */
	public boolean getAllStudents() {
		return allStudentsChk.isSelected();
	}
	
	/**
	 * Responds to a table row being selected.
	 */
	public void itemSelected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(true);
		} else if (page == ASSIGNMENT_PAGE) {
			dropboxBtn.setEnabled(true);
		} else if (page == DROPBOX_PAGE) {
			downloadBtn.setEnabled(true);
			gradeBtn.setEnabled(true);
		}
	}
	
	/**
	 * Responds to a table row being deselected. 
	 */
	public void itemDeselected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(false);
		} else if (page == ASSIGNMENT_PAGE) {
			dropboxBtn.setEnabled(false);
		} else if (page == DROPBOX_PAGE) {
			downloadBtn.setEnabled(false);
			gradeBtn.setEnabled(false);
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
	 * Adds an event listener for the download button click event.
	 * @param listener The new listener.
	 */
	public void addDownloadListener(ActionListener listener) {
		downloadBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the grade button click event.
	 * @param listener The new listener.
	 */
	public void addGradeListener(ActionListener listener) {
		gradeBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the dropbox back button click event.
	 * @param listener The new listener.
	 */
	public void addDropboxBackHandler(ActionListener listener) {
		dropboxBackBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the Dropbox button click event.
	 * @param listener The new listener.
	 */
	public void addDropboxHandler(ActionListener listener) {
		dropboxBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the clear search button click event.
	 * @param listener The new listener.
	 */
	public void addClearSearchListener(ActionListener listener) {
		clearSearchBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the search button click event.
	 * @param listener The new listener.
	 */
	public void addSearchListener(ActionListener listener) {
		searchBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the all students checkbox click event.
	 * @param listener The new listener.
	 */
	public void addAllStudentsListener(ActionListener listener) {
		allStudentsChk.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the students back button click event.
	 * @param listener The new listener.
	 */
	public void addStudentsBackListener(ActionListener listener) {
		studentsBackBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the students button click event.
	 * @param listener The new listener.
	 */
	public void addStudentsListener(ActionListener listener) {
		studentsBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the assignment back button click event.
	 * @param listener The new listener.
	 */
	public void addAssignmentBackListener(ActionListener listener) {
		assignmentBackBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the create course button click event.
	 * @param listener The new listener.
	 */
	public void addCreateCourseListener(ActionListener listener) {
		createCourseBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the Create Assignment button click event.
	 * @param listener The new listener.
	 */
	public void addCreateAssignmentListener(ActionListener listener) {
		createAssignmentBtn.addActionListener(listener);
	}
	
	/**
	 * Adds an event listener for the view button click event.
	 * @param listener The new listener.
	 */
	public void addViewListener(ActionListener listener) {
		viewBtn.addActionListener(listener);
	}
	
	/**
	 * lays out all the button rows.
	 */
	void layoutButtonPanels() {
		super.layoutButtonPanels();
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
		buttonPanels[STUDENT_PAGE] = createStudentButtons();
		buttonPanels[DROPBOX_PAGE] = createDropboxButtons();
	}
	
	/**
	 * Lays out the row of buttons for the course page.
	 * @return The new laid out panel. 
	 */
	private JPanel createCourseButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(viewBtn = new JButton("View"));
		viewBtn.setEnabled(false);
		panel.add(createCourseBtn = new JButton("Create"));
		return panel;
	}
	
	/**
	 * Lays out the row of buttons for the assignment page.
	 * @return The new laid out panel. 
	 */
	private JPanel createAssignmentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(dropboxBtn = new JButton("Dropbox"));
		panel.add(createAssignmentBtn = new JButton("Upload"));
		panel.add(chatBtn = new JButton("Chat"));
		panel.add(studentsBtn = new JButton("Students"));
		panel.add(assignmentBackBtn = new JButton("Back"));
		return panel;
	}
	
	/**
	 * Lays out the row of buttons for the student page.
	 * @return The new laid out panel. 
	 */
	private JPanel createStudentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		allStudentsChk = addCheckBox(panel, "All");
		panel.add(searchBtn = new JButton("Search"));
		panel.add(clearSearchBtn = new JButton("Clear Search"));
		panel.add(emailBtn = new JButton("Email"));
		panel.add(studentsBackBtn = new JButton("Back"));
		return panel;
	}
	
	/**
	 * Lays out the row of buttons for the dropbox page.
	 * @return The new laid out panel. 
	 */
	private JPanel createDropboxButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(downloadBtn = new JButton("Download"));
		panel.add(gradeBtn = new JButton("Grade"));
		panel.add(dropboxBackBtn = new JButton("Back"));
		return panel;
	}
	
	/**
	 * Creates a checkbox.
	 * @param panel The panel to which the checkbox is added.
	 * @param label The label beside the box.
	 * @return A handle to the newly create checkbox.
	 */
	private JCheckBox addCheckBox(JPanel panel, String label) {
		JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		JCheckBox checkBox = new JCheckBox();
		checkPanel.add(new JLabel(label));
		checkPanel.add(checkBox);
		panel.add(checkPanel);
		return checkBox;
	}
}
