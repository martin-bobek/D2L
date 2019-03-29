package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.ServerConnection;
import client.TableModel;
import data.Assignment;
import data.Course;
import data.Submission;
import dialog.SubmissionDialog;
import request.AssignmentRequest;
import request.ChatSubmit;
import request.CourseRequest;
import request.EmailRequest;
import request.FileRequest;
import request.SubmissionUpdate;
import request.SubscribeChat;
import view.StudentView;

/**
 * The controller for the student GUI.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class StudentController extends Controller {
	/**
	 * A handle to the student view.
	 */
	private StudentView view;
	
	/**
	 * Creates a new student controller. All helper and model classes are created and initialized.
	 * @param view The main view.
	 * @param table The table model for the view.
	 * @param server The server connection.
	 */
	public StudentController(StudentView view, TableModel table, ServerConnection server) {
		super(table, server);
		server.addChatText(view.getChatArea());
		table.reset(Course.STUDENT_ROW_PROPERTIES);
		this.view = view;
		subscribeHandlers();
		view.setVisible(true);
	}
	
	/**
	 * Subscribes all the handler for view events.
	 */
	void subscribeHandlers() {
		addSelectionHandler();
		addCourseViewHandler();
		addAssignmentBackHandler();
		addDownloadHandler();
		addSubmitHandler();
		addEmailHandler();
		addSendHandler();
		addCancelHandler();
		addChatHandler();
		addChatBackHandler();
		addChatSubmitHandler();
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
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
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
				view.selectPage(StudentView.CHAT_PAGE);
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
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
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
						view.selectPage(StudentView.ASSIGNMENT_PAGE);
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
				view.selectPage(StudentView.EMAIL_PAGE);
			}
		});
	}
	
	/**
	 * Creates a handler for the submit button event. Creates a new dialog to
	 * obtain information about the submitted file from the user. Then submits
	 * the file to the server.
	 */
	private void addSubmitHandler() {
		view.addSubmitListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = view.getSelected();
				Assignment assignment = (Assignment)table.getRow(row);
				Submission submission = SubmissionDialog.showSubmissionDialog(view, fileHelper);
				if (submission == null)
					return;
				submission.setAssignmentId(assignment.getId());
				assignment.setSubmitted(true);
				table.updateRow(row);
				view.itemDeselected();
				try {
					server.sendObject(new SubmissionUpdate(submission));
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
				Assignment assignment = (Assignment)table.getRow(view.getSelected());
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(assignment.getTitle() + assignment.getExtension()));
				if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION)
					return;
				fileHelper.setPath(chooser.getSelectedFile());
				try {
					server.sendObject(new FileRequest(assignment));
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the assignment back submit button event.
	 * Selects the course page and asks the server to send all the 
	 * courses to display.
	 */
	private void addAssignmentBackHandler() {
		view.addAssignmentBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				view.selectPage(StudentView.COURSE_PAGE);
				table.reset(Course.STUDENT_ROW_PROPERTIES);
				try {
					server.sendObject(new CourseRequest());
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
	/**
	 * Creates a handler for the course view button event. Selects the
	 * assignment page and asks the server to send all the assignments to display.
	 */
	private void addCourseViewHandler() {
		view.addCourseViewListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Course course = (Course)table.getRow(view.getSelected());
				view.setAdditionalText(course.getName(), StudentView.ASSIGNMENT_PAGE);
				view.setAdditionalText(course.getName(), StudentView.CHAT_PAGE);
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
				table.reset(Assignment.STUDENT_ROW_PROPERTIES);
				try {
					server.sendObject(new AssignmentRequest(course));
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}

	/**
	 * Creates a handler for the table selection event. Gets the GUI to enable/disable
	 * the appropriate buttons in response to selection of entries in the table.
	 */
	private void addSelectionHandler() {
		view.addSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (view.getSelected() == StudentView.NO_SELECTION)
					view.itemDeselected();
				else
					view.itemSelected();
			}
		});
	}
}
