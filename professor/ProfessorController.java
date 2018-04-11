package professor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFileChooser;
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
import data.Submission;
import data.Updatable;
import serverMessage.AssignmentRequest;
import serverMessage.CourseRequest;
import serverMessage.StudentRequest;
import serverMessage.SubmissionRequest;
import serverMessage.SubmissionUpdate;
import view.ProfessorView;
import serverMessage.AssignmentUpdate;
import serverMessage.CourseUpdate;
import serverMessage.EmailRequest;
import serverMessage.FileRequest;

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
		server.addFileHelper(fileHelper);
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
		addDropboxHandler();
		addDropboxBackHandler();
		addDownloadHandler();
		addGradeHandler();
		addEmailHandler();
		addCancelHandler();
		addSendHandler();
	}
	
	private void addCancelHandler() {
		view.addCancelListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(ProfessorView.STUDENT_PAGE);
			}
		});
	}
	
	private void addSendHandler() {
		view.addSendListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String subject = view.getSubject();
				String content = view.getContent();
				try {
				if (content.isEmpty())
					JOptionPane.showMessageDialog(view, "The content area cannot be empty!");
				else if (subject.isEmpty())
					JOptionPane.showMessageDialog(view, "The subject line cannot be empty!");
				else
					server.sendObject(new EmailRequest(subject, content));
				} catch (IOException ex) {
					connectionLost();
				}
				view.selectPage(ProfessorView.STUDENT_PAGE);
			}
		});
	}
	
	private void addEmailHandler() {
		view.addEmailListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearEmail();
				view.selectPage(ProfessorView.EMAIL_PAGE);
			}
		});
	}
	
	private void addGradeHandler() {
		view.addGradeListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = view.getSelected();
				Submission submission = (Submission)table.getRow(row);
				try {
					Integer grade = showGradeDialog();
					if (grade == null)
						locked.set(false);
					else {
						submission.setGrade(grade);
						table.updateRow(row);
						server.sendObject(new SubmissionUpdate(submission));
					}
				} catch (IOException ex) {
					connectionLost();
				}
			}
		});
	}
	
	private void addDownloadHandler() {
		view.addDownloadListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true)) 
					return;
				Submission submission = (Submission)table.getRow(view.getSelected());
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(submission.getName() + submission.getExtension()));
				if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) {
					locked.set(false);
					return;
				}
				fileHelper.setPath(chooser.getSelectedFile());
				try {
					server.sendObject(new FileRequest(submission));
				} catch (IOException ex) {
					connectionLost();
				}
			}
		});
	}
	
	private void addDropboxBackHandler() {
		view.addDropboxBackHandler(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.reset(Assignment.PROF_ROW_PROPERTIES);
				try {
					server.sendObject(new AssignmentRequest());
				} catch (IOException ex) {
					connectionLost();
				}
			}
		});
	}
	
	private void addDropboxHandler() {
		view.addDropboxHandler(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Assignment assignment = (Assignment)table.getRow(view.getSelected());
				view.selectPage(ProfessorView.DROPBOX_PAGE);
				view.setAdditionalText(assignment.getTitle(), ProfessorView.DROPBOX_PAGE);
				table.reset(Submission.ROW_PROPERTIES);
				try {
					server.sendObject(new SubmissionRequest(assignment));
				} catch (IOException e1) {
					connectionLost();
				}
			}
		});
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
					connectionLost();
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
	
	private Integer showGradeDialog() {
		int grade;
		while (true) {
			String gradeStr = JOptionPane.showInputDialog(view, "Grade (%):", "Grade Submission", JOptionPane.PLAIN_MESSAGE);
			if (gradeStr == null)
				return null;
			try {
				grade = Integer.parseInt(gradeStr);
				if (grade < 0)
					JOptionPane.showMessageDialog(view, "Grade must be positive!");
				else if (grade >= 1000)
					JOptionPane.showMessageDialog(view, "Grade has a maximum of 3 digits");
				else
					return grade;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(view, "Grade must be a number!");
			}
		}
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
	
	private void connectionLost() {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
