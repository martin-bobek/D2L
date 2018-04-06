package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import data.Assignment;
import data.Course;
import data.Updatable;
import message.RequestAssignments;
import message.RequestCourses;
import message.RequestStudents;
import message.UpdateAssignment;
import message.UpdateCourse;

class ProfessorController {
	private ProfessorView view;
	private TableModel table;
	private ServerConnection server;
	private FileHelper fileHelper;
	private AtomicBoolean locked = new AtomicBoolean(true);
	
	ProfessorController(ProfessorView view, TableModel table, ServerConnection server, FileHelper helper) {
		this.view = view;
		server.addTable(table);
		this.table = table;
		this.server = server;
		fileHelper = helper;
		subscribeHandlers();
	}
	
	void runClient() throws IOException, ClassNotFoundException {
		view.setVisible(true);
		server.sendObject(new RequestCourses());
		while (true) {
			server.receiveList();
			locked.set(false);
		}
	}
	
	private void subscribeHandlers() {
		addViewHandler();
		addTableChangedHandler();
		addCreateCourseHandler();
		addSelectionHandler();
		addCreateAssignmentHandler();
		addAssignmentBackHandler();
		addStudentsHandler();
		addStudentsBackHandler();
	}
	
	private void addStudentsBackHandler() {
		view.addStudentsBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.clear();
				try {
					server.sendObject(new RequestAssignments());
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}
	
	private void addStudentsHandler() {
		view.addStudentsListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.STUDENT_PAGE);
				table.clear();
				try {
					server.sendObject(new RequestStudents(false));
				} catch (IOException e1) {
					lostConnection();
				}
			}
		});
	}

	private void addAssignmentBackHandler() {
		view.addAssignmentBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.COURSE_PAGE);
				table.clear();
				try {
					server.sendObject(new RequestCourses());
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}

	private void addTableChangedHandler() {
		table.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == TableModelEvent.ALL_COLUMNS)
					return;
				Updatable row = (Updatable)table.getRow(e.getFirstRow());
				try {
					server.sendObject(row.createRequest());
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}
	
	private void addCreateCourseHandler() {
		view.addCreateCourseListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				try {
					Course course = showCourseDialog();
					if (course == null)
						locked.set(false);
					else
						server.sendObject(new UpdateCourse(course));
				} catch (InvalidParameterException ex) {
					locked.set(false);
					JOptionPane.showMessageDialog(view, ex.getMessage());
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}
	
	private void addCreateAssignmentHandler() {
		view.addCreateAssignmentListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				try {
					Assignment assignment = AssignmentDialog.showAssignmentDialog(view, fileHelper);
					if (assignment == null)
						locked.set(false);
					else
						server.sendObject(new UpdateAssignment(assignment));
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}
	
	private void addViewHandler() {
		view.addViewListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				Course course = (Course)table.getRow(view.getSelected());
				table.clear();
				try {
					server.sendObject(new RequestAssignments(course));
				} catch (IOException ex) {
					lostConnection();
				}
			}
		});
	}
	
	private void addSelectionHandler() {
		view.addSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (view.getSelected() == ProfessorView.NO_SELECTION)
					view.itemDeselected();
				else
					view.itemSelected();
			}
		});
	}
	
	private Course showCourseDialog() throws InvalidParameterException {
		String name = JOptionPane.showInputDialog(view, "Course Name:", "Create Course", JOptionPane.PLAIN_MESSAGE);
		if (name == null)
			return null;
		if (name.isEmpty())
			throw new InvalidParameterException("Course name cannot be empty!");
		return new Course(name);
	}
	
	/*
	private Assignment showAssignmentDialog() throws InvalidParameterException {
		String name = JOptionPane.showInputDialog(view, "Assignment Name:", "Create Assignment", JOptionPane.PLAIN_MESSAGE);
		if (name == null)
			return null;
		if (name.isEmpty())
			throw new InvalidParameterException("Assignment name cannot be empty!");
		return new Assignment(name);
	}
	*/
	
	private void lostConnection() {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
