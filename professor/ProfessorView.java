package professor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import client.TableModel;

public class ProfessorView extends JFrame {
	private static final long serialVersionUID = 1L;
	static final int NO_SELECTION = -1;
	static final int COURSE_PAGE = 0;
	static final int ASSIGNMENT_PAGE = 1;
	static final int STUDENT_PAGE = 2;
	static final int DROPBOX_PAGE = 3;
	private static final int NUM_PAGES = 4;
	private static final String HEADERS[] = { "Courses", "Assignments", "Students", "Dropbox" };
	private String additionalText[] = { "", "", "", "" };
	
	private int page = COURSE_PAGE;
	private JPanel buttonPanels[] = new JPanel[NUM_PAGES];
	private JLabel header;
	private JButton viewBtn, createCourseBtn, createAssignmentBtn, assignmentBackBtn, dropboxBtn, 
			studentsBtn, studentsBackBtn, searchBtn, clearSearchBtn, dropboxBackBtn, gradeBtn, downloadBtn;
	private JCheckBox allStudentsChk;
	private JTable table;

	public ProfessorView(String name, TableModel table) {
		super("Professor Client");
		setAdditionalText(name, COURSE_PAGE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutFrame();
		this.table.setModel(table);
		setMinimumSize(new Dimension(600, 300));
	}
	
	void setAdditionalText(String text, int page) {
		if (text.length() > 35)
			additionalText[page] = " - " + text.substring(0, 33) + "...";
		else
			additionalText[page] = " - " + text;
	}
	
	void setClearSearchEnabled(boolean enabled) {
		clearSearchBtn.setEnabled(enabled);
	}
	
	void setAllStudents(boolean all) {
		allStudentsChk.setSelected(all);
	}
	
	boolean getAllStudents() {
		return allStudentsChk.isSelected();
	}
	
	int getSelected() {
		return table.getSelectedRow();
	}
	
	void selectPage(int page) {
		header.setText(HEADERS[page] + additionalText[page]);
		remove(buttonPanels[this.page]);
		add(buttonPanels[page], BorderLayout.SOUTH);
		repaint();
		this.page = page;
		itemDeselected();
	}
	
	void itemSelected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(true);
		} else if (page == ASSIGNMENT_PAGE) {
			dropboxBtn.setEnabled(true);
		} else if (page == DROPBOX_PAGE) {
			downloadBtn.setEnabled(true);
			gradeBtn.setEnabled(true);
		}
	}
	
	void itemDeselected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(false);
		} else if (page == ASSIGNMENT_PAGE) {
			dropboxBtn.setEnabled(false);
		} else if (page == DROPBOX_PAGE) {
			downloadBtn.setEnabled(false);
			gradeBtn.setEnabled(false);
		}
	}
	
	void addDownloadListener(ActionListener listener) {
		downloadBtn.addActionListener(listener);
	}
	
	void addGradeListener(ActionListener listener) {
		gradeBtn.addActionListener(listener);
	}
	
	void addDropboxBackHandler(ActionListener listener) {
		dropboxBackBtn.addActionListener(listener);
	}
	
	void addDropboxHandler(ActionListener listener) {
		dropboxBtn.addActionListener(listener);
	}
	
	void addClearSearchListener(ActionListener listener) {
		clearSearchBtn.addActionListener(listener);
	}
	
	void addSearchListener(ActionListener listener) {
		searchBtn.addActionListener(listener);
	}
	
	void addAllStudentsListener(ActionListener listener) {
		allStudentsChk.addActionListener(listener);
	}
	
	void addStudentsBackListener(ActionListener listener) {
		studentsBackBtn.addActionListener(listener);
	}
	
	void addStudentsListener(ActionListener listener) {
		studentsBtn.addActionListener(listener);
	}
	
	void addAssignmentBackListener(ActionListener listener) {
		assignmentBackBtn.addActionListener(listener);
	}
	
	void addCreateCourseListener(ActionListener listener) {
		createCourseBtn.addActionListener(listener);
	}
	
	void addCreateAssignmentListener(ActionListener listener) {
		createAssignmentBtn.addActionListener(listener);
	}
	
	void addViewListener(ActionListener listener) {
		viewBtn.addActionListener(listener);
	}
	
	void addSelectionListener(ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}
	
	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(HEADERS[page] + additionalText[page]), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
	}
	
	private void layoutButtonPanels() {
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
		buttonPanels[STUDENT_PAGE] = createStudentButtons();
		buttonPanels[DROPBOX_PAGE] = createDropboxButtons();
	}

	private JLabel createHeader(String title) {
		header = new JLabel(title, SwingConstants.CENTER);
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));
		return header;
	}
	
	private JPanel createTextArea() {
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel scrollList = new JPanel(new BorderLayout());
		scrollList.add(new JScrollPane(table), BorderLayout.CENTER);
		scrollList.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		return scrollList;
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
