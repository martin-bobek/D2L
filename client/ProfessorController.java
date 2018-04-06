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
import data.Student;
import data.Updatable;
import helper.FileHelper;
import helper.ServerConnection;
import helper.TableModel;
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
	
	private RequestStudents search = new RequestStudents();
	
	ProfessorController(ProfessorView view, TableModel table, ServerConnection server) {
		table.reset(Course.ROW_PROPERTIES);
		server.addTable(table);
		this.view = view;
		this.table = table;
		this.server = server;
		fileHelper = new FileHelper();
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
		addAllStudentsHandler();
		addSearchHandler();
		addClearSearchHandler();
	}
	
	private void addClearSearchHandler() {
		view.addClearSearchListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.setClearSearchEnabled(false);
				search = new RequestStudents();
				search.setAll(view.getAllStudents());
				table.clear();
				try {
					server.sendObject(search);
				} catch (IOException e1) {
					lostConnection();
				}
			}
		});
	}

	private void addSearchHandler() {
		view.addSearchListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				if ((search = SearchDialog.showSearchDialog(view)) == null) {
					locked.set(false);
					return;
				}
				view.setClearSearchEnabled(true);
				search.setAll(view.getAllStudents());
				table.clear();
				try {
					server.sendObject(search);
				} catch (IOException e1) {
					lostConnection();
				}
			}
		});
	}
	
	private void addAllStudentsHandler() {
		view.addAllStudentsListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				table.clear();
				search.setAll(view.getAllStudents());
				try {
					server.sendObject(search);
				} catch (IOException ex) {
					lostConnection();
				}
			}			
		});
	}

	private void addStudentsBackHandler() {
		view.addStudentsBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.reset(Assignment.ROW_PROPERTIES);
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
				table.reset(Student.ROW_PROPERTIES);
				search = new RequestStudents();
				view.setAllStudents(false);
				view.setClearSearchEnabled(false);
				try {
					server.sendObject(search);
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
				table.reset(Course.ROW_PROPERTIES);
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
				Course course = (Course)table.getRow(view.getSelected());
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.reset(Assignment.ROW_PROPERTIES);
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
	
	private void lostConnection() {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
