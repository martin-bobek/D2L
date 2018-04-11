package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.TableModel;
import data.Assignment;

public class StudentView extends View {
	private static final long serialVersionUID = 1L;
	public static final int NO_SELECTION = -1;
	public static final int COURSE_PAGE = 0;
	public static final int ASSIGNMENT_PAGE = 3;
	
	private JButton courseViewBtn, assignmentBackBtn, downloadBtn, submitBtn, emailBtn, chatBtn;
	private TableModel tableModel;

	public StudentView(String name, TableModel table) {
		super("Student Client", table, 
			new String[] { "Courses", "Compose", "Chat", "Assignments" });
		setAdditionalText(name, COURSE_PAGE);
		tableModel = table;
	}
	
	public void itemSelected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(true);
		} else if (page == ASSIGNMENT_PAGE) {
			downloadBtn.setEnabled(true);
			submitBtn.setEnabled(!((Assignment)tableModel.getRow(getSelected())).getSubmitted());
		}
	}
	
	public void itemDeselected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(false);
		} else if (page == ASSIGNMENT_PAGE) {
			downloadBtn.setEnabled(false);
			submitBtn.setEnabled(false);
		}
	}
	
	public void addChatListener(ActionListener listener) {
		chatBtn.addActionListener(listener);
	}
	
	public void addEmailListener(ActionListener listener) {
		emailBtn.addActionListener(listener);
	}
	
	public void addSubmitListener(ActionListener listener) {
		submitBtn.addActionListener(listener);
	}
	
	public void addDownloadListener(ActionListener listener) {
		downloadBtn.addActionListener(listener);
	}
	
	public void addAssignmentBackListener(ActionListener listener) {
		assignmentBackBtn.addActionListener(listener);
	}
	
	public void addCourseViewListener(ActionListener listener) {
		courseViewBtn.addActionListener(listener);
	}
	
	void layoutButtonPanels() {
		super.layoutButtonPanels();
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
	}
	
	private JPanel createAssignmentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(downloadBtn = new JButton("Download"));
		panel.add(submitBtn = new JButton("Submit"));
		panel.add(chatBtn = new JButton("Chat"));
		panel.add(emailBtn = new JButton("Email"));
		panel.add(assignmentBackBtn = new JButton("Back"));
		return panel;
	}

	private JPanel createCourseButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(courseViewBtn = new JButton("View"));
		courseViewBtn.setEnabled(false);
		return panel;
	}
}
