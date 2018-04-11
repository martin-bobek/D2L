package student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Controller;
import client.FileHelper;
import client.ServerConnection;
import client.TableModel;
import data.Assignment;
import data.Course;
import data.Submission;
import serverMessage.AssignmentRequest;
import serverMessage.CourseRequest;
import serverMessage.EmailRequest;
import serverMessage.FileRequest;
import serverMessage.SubmissionUpdate;
import view.StudentView;

public class StudentController implements Controller {
	private StudentView view;
	private TableModel table;
	private ServerConnection server;
	private FileHelper fileHelper = new FileHelper();
	private AtomicBoolean locked = new AtomicBoolean(true);
	
	
	public StudentController(StudentView view, TableModel table, ServerConnection server) {
		table.reset(Course.STUDENT_ROW_PROPERTIES);
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
		addSelectionHandler();
		addCourseViewHandler();
		addAssignmentBackHandler();
		addDownloadHandler();
		addSubmitHandler();
		addEmailHandler();
		addSendHandler();
		addCancelHandler();
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
				else
					server.sendObject(new EmailRequest(subject, content));
				} catch (IOException ex) {
					connectionLost();
				}
				view.selectPage(StudentView.ASSIGNMENT_PAGE);
			}
		});
	}
	
	private void addEmailHandler() {
		view.addEmailListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearEmail();
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
				view.selectPage(StudentView.COURSE_PAGE);
				table.reset(Course.STUDENT_ROW_PROPERTIES);
				try {
					server.sendObject(new CourseRequest());
				} catch (IOException ex) {
					connectionLost();
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
					connectionLost();
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
	
	private void connectionLost() {
		JOptionPane.showMessageDialog(view, "Lost connection to server!");
		System.exit(1);
	}
}
