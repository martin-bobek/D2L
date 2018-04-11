package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.TableModel;

public class ProfessorView extends View {
	private static final long serialVersionUID = 1L;
	public static final int NO_SELECTION = -1;
	public static final int COURSE_PAGE = 0;
	public static final int ASSIGNMENT_PAGE = 3;
	public static final int STUDENT_PAGE = 4;
	public static final int DROPBOX_PAGE = 5;
	
	private JButton viewBtn, createCourseBtn, createAssignmentBtn, assignmentBackBtn, dropboxBtn, emailBtn,  
			studentsBtn, studentsBackBtn, searchBtn, clearSearchBtn, dropboxBackBtn, gradeBtn, downloadBtn;
	private JCheckBox allStudentsChk;

	public ProfessorView(String name, TableModel table) {
		super("Professor Client", table,
			new String[] { "Courses", "Compose", "Chat", "Assignments", "Students", "Dropbox" });
		setAdditionalText(name, COURSE_PAGE);
	}
	
	public void setClearSearchEnabled(boolean enabled) {
		clearSearchBtn.setEnabled(enabled);
	}
	
	public void setAllStudents(boolean all) {
		allStudentsChk.setSelected(all);
	}
	
	public boolean getAllStudents() {
		return allStudentsChk.isSelected();
	}
	
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
	
	public void addEmailListener(ActionListener listener) {
		emailBtn.addActionListener(listener);
	}
	
	public void addDownloadListener(ActionListener listener) {
		downloadBtn.addActionListener(listener);
	}
	
	public void addGradeListener(ActionListener listener) {
		gradeBtn.addActionListener(listener);
	}
	
	public void addDropboxBackHandler(ActionListener listener) {
		dropboxBackBtn.addActionListener(listener);
	}
	
	public void addDropboxHandler(ActionListener listener) {
		dropboxBtn.addActionListener(listener);
	}
	
	public void addClearSearchListener(ActionListener listener) {
		clearSearchBtn.addActionListener(listener);
	}
	
	public void addSearchListener(ActionListener listener) {
		searchBtn.addActionListener(listener);
	}
	
	public void addAllStudentsListener(ActionListener listener) {
		allStudentsChk.addActionListener(listener);
	}
	
	public void addStudentsBackListener(ActionListener listener) {
		studentsBackBtn.addActionListener(listener);
	}
	
	public void addStudentsListener(ActionListener listener) {
		studentsBtn.addActionListener(listener);
	}
	
	public void addAssignmentBackListener(ActionListener listener) {
		assignmentBackBtn.addActionListener(listener);
	}
	
	public void addCreateCourseListener(ActionListener listener) {
		createCourseBtn.addActionListener(listener);
	}
	
	public void addCreateAssignmentListener(ActionListener listener) {
		createAssignmentBtn.addActionListener(listener);
	}
	
	public void addViewListener(ActionListener listener) {
		viewBtn.addActionListener(listener);
	}
	
	void layoutButtonPanels() {
		super.layoutButtonPanels();
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
		buttonPanels[STUDENT_PAGE] = createStudentButtons();
		buttonPanels[DROPBOX_PAGE] = createDropboxButtons();
	}
	
	private JPanel createCourseButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(viewBtn = new JButton("View"));
		viewBtn.setEnabled(false);
		panel.add(createCourseBtn = new JButton("Create"));
		return panel;
	}
	
	private JPanel createAssignmentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(dropboxBtn = new JButton("Dropbox"));
		panel.add(createAssignmentBtn = new JButton("Upload"));
		panel.add(studentsBtn = new JButton("Students"));
		panel.add(assignmentBackBtn = new JButton("Back"));
		return panel;
	}
	
	private JPanel createStudentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		allStudentsChk = addCheckBox(panel, "All");
		panel.add(searchBtn = new JButton("Search"));
		panel.add(clearSearchBtn = new JButton("Clear Search"));
		panel.add(emailBtn = new JButton("Email"));
		panel.add(studentsBackBtn = new JButton("Back"));
		return panel;
	}
	
	private JPanel createDropboxButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(downloadBtn = new JButton("Download"));
		panel.add(gradeBtn = new JButton("Grade"));
		panel.add(dropboxBackBtn = new JButton("Back"));
		return panel;
	}
	
	private JCheckBox addCheckBox(JPanel panel, String label) {
		JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		JCheckBox checkBox = new JCheckBox();
		checkPanel.add(new JLabel(label));
		checkPanel.add(checkBox);
		panel.add(checkPanel);
		return checkBox;
	}
}
