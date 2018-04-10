package professor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import client.Controller;
import client.FileHelper;
import client.ServerConnection;
import client.TableModel;
import data.Assignment;
import data.Course;
import data.Student;
import data.Updatable;
import serverMessage.AssignmentRequest;
import serverMessage.CourseRequest;
import serverMessage.StudentRequest;
import serverMessage.AssignmentUpdate;
import serverMessage.CourseUpdate;

public class ProfessorController implements Controller {
	private ProfessorView view;
	private TableModel table;
	private ServerConnection server;
	private FileHelper fileHelper = new FileHelper();
	private AtomicBoolean locked = new AtomicBoolean(true);
	
	private StudentRequest search = new StudentRequest();
	
	public ProfessorController(ProfessorView view, TableModel table, ServerConnection server) {
		table.reset(Course.PROF_ROW_PROPERTIES);
		server.addTable(table);
		this.view = view;
		this.table = table;
		this.server = server;
		subscribeHandlers();
	}
	
	public void runClient() throws IOException, ClassNotFoundException {
		view.setVisible(true);
		server.sendObject(new CourseRequest());
		while (true) {
			server.receiveResponse();
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
				search = new StudentRequest();
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
				table.reset(Assignment.PROF_ROW_PROPERTIES);
				try {
					server.sendObject(new AssignmentRequest());
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
				search = new StudentRequest();
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
				table.reset(Course.PROF_ROW_PROPERTIES);
				try {
					server.sendObject(new CourseRequest());
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
						server.sendObject(new CourseUpdate(course));
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
						server.sendObject(new AssignmentUpdate(assignment));
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
				view.setAdditionalText(course.getName(), ProfessorView.ASSIGNMENT_PAGE);
				view.setAdditionalText(course.getName(), ProfessorView.STUDENT_PAGE);
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.reset(Assignment.PROF_ROW_PROPERTIES);
				try {
					server.sendObject(new AssignmentRequest(course));
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
	
	private Course showCourseDialog() {
		while (true) {
			String name = JOptionPane.showInputDialog(view, "Course Name:", "Create Course", JOptionPane.PLAIN_MESSAGE);
			if (name == null)
				return null;
			else if (name.isEmpty())
				JOptionPane.showMessageDialog(view, "Course name cannot be empty!");
			else if (name.length() > 50)
				JOptionPane.showMessageDialog(view, "Course name cannot have more than 50 characters!");
			else
				return new Course(name);
		}
	}
	
	private void lostConnection() {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
