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
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
	static final int EMAIL_PAGE = 4;
	private static final int NUM_PAGES = 5;
	private static final String HEADERS[] = { "Courses", "Assignments", "Students", "Dropbox", "Compose" };
	private String additionalText[] = { "", "", "", "", "" };
	
	private int page = COURSE_PAGE;
	private JPanel buttonPanels[] = new JPanel[NUM_PAGES];
	private JLabel header;
	private JButton viewBtn, createCourseBtn, createAssignmentBtn, assignmentBackBtn, dropboxBtn, emailBtn, sendBtn, 
			studentsBtn, studentsBackBtn, searchBtn, clearSearchBtn, dropboxBackBtn, gradeBtn, downloadBtn, cancelBtn;
	private JCheckBox allStudentsChk;
	private JPanel emailEditor, tablePanel;
	private JTable table;
	private JTextField subjectTxt;
	private JTextArea emailContent;

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
	
	String getSubject() {
		return subjectTxt.getText();
	}
	
	String getContent() {
		return emailContent.getText();
	}
	
	void clearEmail() {
		subjectTxt.setText("");
		emailContent.setText("");
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
	
	void addEmailListener(ActionListener listener) {
		emailBtn.addActionListener(listener);
	}
	
	void addCancelListener(ActionListener listener) {
		cancelBtn.addActionListener(listener);
	}
	
	void addSendListener(ActionListener listener) {
		sendBtn.addActionListener(listener);
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
		emailEditor = createEmailEditor();
	}
	
	private JPanel createEmailEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		panel.add(createEmailHeader(), BorderLayout.NORTH);
		panel.add(new JScrollPane(emailContent = new JTextArea()), BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createEmailHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("  Subject: "), BorderLayout.WEST);
		panel.add(subjectTxt = new JTextField(), BorderLayout.CENTER);
		return panel;
	}
	
	private void layoutButtonPanels() {
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
		buttonPanels[STUDENT_PAGE] = createStudentButtons();
		buttonPanels[DROPBOX_PAGE] = createDropboxButtons();
		buttonPanels[EMAIL_PAGE] = createEmailButtons();
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
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
		return tablePanel;
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
	
	private JPanel createEmailButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(sendBtn = new JButton("Send"));
		panel.add(cancelBtn = new JButton("Cancel"));
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
