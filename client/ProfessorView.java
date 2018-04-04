package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import data.Course;

class ProfessorView extends JFrame {
	private static final long serialVersionUID = 1L;
	static final int COURSE_PAGE = 0;
	static final int ASSIGNMENT_PAGE = 1;
	private static final int NUM_PAGES = 2;
	private static final String HEADERS[] = { "Courses", "Assignments" };
	
	private int page = COURSE_PAGE;
	private JPanel buttonPanels[] = new JPanel[NUM_PAGES];
	private JLabel header;
	private JButton viewBtn, courseActiveBtn, createCourseBtn;
	private JList<Serializable> list;

	ProfessorView(ServerConnection server) {
		super("Professor Client");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutFrame();
		list.setModel(server.getList());
		setMinimumSize(new Dimension(500, 500));
	}
	
	Serializable getSelectedItem() {
		return list.getSelectedValue();
	}
	
	void updateSelectedItem(Serializable item) {
		DefaultListModel<Serializable> model = (DefaultListModel<Serializable>)list.getModel();
		int index = list.getSelectedIndex();
		model.setElementAt(item, index);
	}
	
	void selectPage(int page) {
		header.setText(HEADERS[page]);
		remove(buttonPanels[this.page]);
		add("South", buttonPanels[page]);
		validate();
		this.page = page;
	}
	
	void itemSelected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(true);
			courseActiveBtn.setEnabled(true);
			setCourseActiveButtonText(((Course)list.getSelectedValue()).getActive());
		}
	}
	
	void itemDeselected() {
		if (page == COURSE_PAGE) {
			viewBtn.setEnabled(false);
			courseActiveBtn.setEnabled(false);
		}
	}
	
	void setCourseActiveButtonText(boolean courseActive) {
		courseActiveBtn.setText(courseActive ? "Deactivate" : "Activate");
	}
	
	void addCreateCourseHandler(ActionListener listener) {
		createCourseBtn.addActionListener(listener);
	}
	
	void addViewListener(ActionListener listener) {
		viewBtn.addActionListener(listener);
	}
	
	void addCourseActiveListener(ActionListener listener) {
		courseActiveBtn.addActionListener(listener);
	}
	
	void addSelectionListener(ListSelectionListener listener) {
		list.getSelectionModel().addListSelectionListener(listener);
	}
	
	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(HEADERS[page]), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
	}
	
	private void layoutButtonPanels() {
		buttonPanels[COURSE_PAGE] = createHomeButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
	}
	
	private JLabel createHeader(String title) {
		header = new JLabel(title, SwingConstants.CENTER);
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));
		return header;
	}
	
	private JPanel createTextArea() {
		list = new JList<Serializable>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel scrollList = new JPanel(new BorderLayout());
		scrollList.add(new JScrollPane(list), BorderLayout.CENTER);
		scrollList.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		return scrollList;
	}
	
	private JPanel createHomeButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(viewBtn = new JButton("View"));
		viewBtn.setEnabled(false);
		panel.add(courseActiveBtn = new JButton("Activate"));
		courseActiveBtn.setEnabled(false);
		panel.add(createCourseBtn = new JButton("Create"));
		return panel;
	}
	
	private JPanel createAssignmentButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(new JButton("Upload"));
		panel.add(new JButton("Activate"));
		panel.add(new JButton("Dropbox"));
		panel.add(new JButton("Students"));
		return panel;
	}
}
