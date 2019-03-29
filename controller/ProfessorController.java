package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import client.ServerConnection;
import client.TableModel;
import data.Assignment;
import data.Course;
import data.Student;
import data.Submission;
import data.Updatable;
import dialog.AssignmentDialog;
import dialog.SearchDialog;
import request.AssignmentRequest;
import request.AssignmentUpdate;
import request.ChatSubmit;
import request.CourseRequest;
import request.CourseUpdate;
import request.EmailRequest;
import request.FileRequest;
import request.StudentRequest;
import request.SubmissionRequest;
import request.SubmissionUpdate;
import request.SubscribeChat;
import view.ProfessorView;

/**
 * The controller for the main professor GUI.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class ProfessorController extends Controller {
	/**
	 * A handle to the main professor view.
	 */
	private ProfessorView view;
	
	/**
	 * The current search parameters used to view students.
	 */
	private StudentRequest search = new StudentRequest();
	
	/**
	 * Create a new professor controller, initializing all helper and model classes.
	 * @param view The main professor view.
	 * @param table The table model.
	 * @param server The connection to the server.
	 */
	public ProfessorController(ProfessorView view, TableModel table, ServerConnection server) {
		super(table, server);
		server.addChatText(view.getChatArea());
		table.reset(Course.PROF_ROW_PROPERTIES);
		this.view = view;
		subscribeHandlers();
		view.setVisible(true);
	}
	
	/**
	 * Subscribes all the handlers to all the view events.
	 */
	void subscribeHandlers() {
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
		addChatSubmitHandler();
		addChatBackHandler();
		addChatHandler();
	}
	
	/**
	 * Creates a handler for the chat submit button event. Validates input 
	 * and sends the new message to the server.
	 */
	private void addChatSubmitHandler() {
		view.addChatSubmitListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = view.getChatMessage();
				if (message.isEmpty())
					return;
				try {
					if (message.length() > 255)
						JOptionPane.showMessageDialog(view, "Chat messages must be less than 255 characters!");
					else {
						server.sendObject(new ChatSubmit(message));
						view.setChatMessage("");
					}
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the chat back button event. Changes the view
	 * to the assignment page.
	 */
	private void addChatBackHandler() {
		view.addChatBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
			}
		});
	}
	
	/**
	 * Creates a handler for the chat button event. Changes the
	 * view to the chat page and request the server to deliver all messages
	 * for the chat.
	 */
	private void addChatHandler() {
		view.addChatListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(ProfessorView.CHAT_PAGE);
				view.clearChat();
				try {
					server.sendObject(new SubscribeChat());
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the cancel button event. Selects the assignment page.
	 */
	private void addCancelHandler() {
		view.addCancelListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(ProfessorView.STUDENT_PAGE);
			}
		});
	}
	
	/**
	 * Creates a handler for the send button event.
	 * validates the content of the email and makes a request
	 * for the server to send the email.
	 */
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
				else {
					server.sendObject(new EmailRequest(subject, content));
					view.selectPage(ProfessorView.STUDENT_PAGE);
				}
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the email submit button event.
	 * Clears the previous email message and selects the email page.
	 */
	private void addEmailHandler() {
		view.addEmailListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearMessage();
				view.selectPage(ProfessorView.EMAIL_PAGE);
			}
		});
	}
	
	/**
	 * Creates a handler for the grade button event. Creates a new
	 * dialog allowing the professor to enter a new grade then makes 
	 * a request to the server to update the grade.
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the download button event. Opens a new file picker view
	 * to select a save location. Then retrieves the file from the server.
	 */
	private void addDownloadHandler() {
		view.addDownloadListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Submission submission = (Submission)table.getRow(view.getSelected());
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(submission.getName() + submission.getExtension()));
				if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION)
					return;
				fileHelper.setPath(chooser.getSelectedFile());
				try {
					server.sendObject(new FileRequest(submission));
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the dropbox button event. Selects the
	 * assignment page and request the server to send all the assignments. 
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the dropbox button event. Selects the
	 * dropbox page and request the server to send all the student submissions. 
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the clear search button event. Clears an
	 * search restrictions set by the user.
	 */
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
					connectionLost(view);
				}
			}
		});
	}

	/**
	 * Creates a handler for the search button event. Displays the search 
	 * dialog allowing the user to enter search parameters.  
	 */
	private void addSearchHandler() {
		view.addSearchListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				StudentRequest newSearch = SearchDialog.showSearchDialog(view); 
				if (newSearch == null) {
					locked.set(false);
					return;
				}
				search = newSearch;
				view.setClearSearchEnabled(true);
				search.setAll(view.getAllStudents());
				table.clear();
				try {
					server.sendObject(search);
				} catch (IOException e1) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the all students checkbox event.
	 * Changes whether only enrolled or all students are displayed. 
	 */
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
					connectionLost(view);
				}
			}			
		});
	}

	/**
	 * Creates a handler for the students back button event. Selects
	 * the assignment page and asks the server to send all assignments. 
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the students button event. Selects the 
	 * student page and asks the server to send all the students.
	 */
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
					connectionLost(view);
				}
			}
		});
	}

	/**
	 * Creates a handler for the assignment back button event. Selects the 
	 * course page and asks the server to send all the courses.
	 */
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
					connectionLost(view);
				}
			}
		});
	}

	/**
	 * Creates a handler for the table changed event. Sends a request to 
	 * the server to update the modified table entry in the database.
	 */
	private void addTableChangedHandler() {
		table.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == TableModelEvent.ALL_COLUMNS)
					return;
				Updatable row = (Updatable)table.getRow(e.getFirstRow());
				try {
					server.sendObject(row.createRequest());
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the create course button event. Opens a 
	 * new dialog allowing the user to create a course. Then sends this
	 * new course to the server.
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the create assignment button event. Opens
	 * a new dialog allowing the user to upload a file for the assignment
	 * then sends this file and assignment to the server.
	 */
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
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the view button event. Selects the assignment
	 * page and makes a request to the server to send all the assignments.
	 */
	private void addViewHandler() {
		view.addViewListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Course course = (Course)table.getRow(view.getSelected());
				view.setAdditionalText(course.getName(), ProfessorView.ASSIGNMENT_PAGE);
				view.setAdditionalText(course.getName(), ProfessorView.STUDENT_PAGE);
				view.setAdditionalText(course.getName(), ProfessorView.CHAT_PAGE);
				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
				table.reset(Assignment.PROF_ROW_PROPERTIES);
				try {
					server.sendObject(new AssignmentRequest(course));
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the table selection event. Enables/disables
	 * buttons in the GUI to reflect the selection state of the table.
	 */
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
	
	/**
	 * Creates a dialog used to get a new grade from the user. The function
	 * then validates the inputs.
	 * @return An integer representing the new grade.
	 */
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
	
	/**
	 * Creates a dialog used to get a new course from the user. The function
	 * then validates the inputs.
	 * @return An course object representing the new course.
	 */
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
}
