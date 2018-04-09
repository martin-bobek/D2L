package student;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;

import client.TableModel;

public class StudentView extends JFrame {
	private static final long serialVersionUID = 1L;
	static final int NO_SELECTION = -1;
	static final int COURSE_PAGE = 0;
	static final int ASSIGNMENT_PAGE = 1;
	private static final int NUM_PAGES = 2;
	private static final String[] HEADERS = { "Courses" };
	private String additionalText[] = { "", "", "" };
	
	private int page = COURSE_PAGE;
	private JPanel buttonPanels[] = new JPanel[NUM_PAGES];
	private JLabel header;
	private JButton courseViewBtn;
	private JTable table;

	public StudentView(TableModel table) {
		super("Student Client");
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
	
	int getSelected() {
		return table.getSelectedRow();
	}
	
	void itemSelected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(true);
		} 
	}
	
	void itemDeselected() {
		if (page == COURSE_PAGE) {
			courseViewBtn.setEnabled(false);
		} 
	}
	
	void addCourseViewListener(ActionListener listener) {
		courseViewBtn.addActionListener(listener);
	}
	
	void addSelectionListener(ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}
	
	private void layoutFrame() {
		layoutButtonPanels();
		add(createHeader(HEADERS[page]), BorderLayout.NORTH);
		add(createTextArea(), BorderLayout.CENTER);
		add(buttonPanels[page], BorderLayout.SOUTH);
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

	private void layoutButtonPanels() {
		buttonPanels[COURSE_PAGE] = createCourseButtons();
		buttonPanels[ASSIGNMENT_PAGE] = createAssignmentButtons();
	}
	
	private JPanel createAssignmentButtons() {
		return null;
	}

	private JPanel createCourseButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.add(courseViewBtn = new JButton("View"));
		courseViewBtn.setEnabled(false);
		return panel;
	}

	private JLabel createHeader(String title) {
		header = new JLabel(title, SwingConstants.CENTER);
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));
		return header;
	}
}
