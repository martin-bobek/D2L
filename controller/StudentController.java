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

public class StudentController extends Controller {
	private StudentView view;
	
	public StudentController(StudentView view, TableModel table, ServerConnection server) {
		super(table, server);
		server.addChatText(view.getChatArea());
		table.reset(Course.STUDENT_ROW_PROPERTIES);
		this.view = view;
		subscribeHandlers();
		view.setVisible(true);
	}
	
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
	
	
	private void addChatBackHandler() {
		view.addChatBackListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
			}
		});
	}
	
	private void addChatHandler() {
		view.addChatListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
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
	
	private void addCancelHandler() {
		view.addCancelListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
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
	
	private void addEmailHandler() {
		view.addEmailListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearMessage();
				view.selectPage(StudentView.EMAIL_PAGE);
			}
		});
	}
	
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
	
	private void addDownloadHandler() {
		view.addDownloadListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Assignment assignment = (Assignment)table.getRow(view.getSelected());
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(assignment.getTitle() + assignment.getExtension()));
				if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) {
					locked.set(false);
					return;
				}
				fileHelper.setPath(chooser.getSelectedFile());
				try {
					server.sendObject(new FileRequest(assignment));
				} catch (IOException ex) {
					connectionLost(view);
				}
			}
		});
	}
	
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
	
	private void addCourseViewHandler() {
		view.addCourseViewListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Course course = (Course)table.getRow(view.getSelected());
				view.setAdditionalText(course.getName(), StudentView.ASSIGNMENT_PAGE);
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
