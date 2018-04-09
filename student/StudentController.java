package student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Controller;
import client.FileHelper;
import client.ServerConnection;
import client.TableModel;
import data.Assignment;
import data.Course;
import message.RequestAssignments;
import message.RequestCourses;
import professor.ProfessorView;

public class StudentController implements Controller {
	private StudentView view;
	private TableModel table;
	private ServerConnection server;
	private FileHelper fileHelper = new FileHelper();
	private AtomicBoolean locked = new AtomicBoolean(true);
	
	
	public StudentController(StudentView view, TableModel table, ServerConnection server) {
		table.reset(Course.STUDENT_ROW_PROPERTIES);
		server.addTable(table);
		this.view = view;
		this.table = table;
		this.server = server;
		subscribeHandlers();
	}
	
	public void runClient() throws IOException, ClassNotFoundException {
		view.setVisible(true);
		server.sendObject(new RequestCourses());
		while (true) {
			server.receiveList();
			locked.set(false);
		}
	}
	
	private void subscribeHandlers() {
		addSelectionHandler();
		addCourseViewHandler();
	}
	
//	private void addViewHandler() {
//		view.addViewListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (!locked.compareAndSet(false, true))
//					return;
//				Course course = (Course)table.getRow(view.getSelected());
//				view.setAdditionalText(course.getName(), ProfessorView.ASSIGNMENT_PAGE);
//				view.setAdditionalText(course.getName(), ProfessorView.STUDENT_PAGE);
//				view.selectPage(ProfessorView.ASSIGNMENT_PAGE);
//				table.reset(Assignment.ROW_PROPERTIES);
//				try {
//					server.sendObject(new RequestAssignments(course));
//				} catch (IOException ex) {
//					lostConnection();
//				}
//			}
//		});
//	}
	
	private void addCourseViewHandler() {
		view.addCourseViewListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				Course course = (Course)table.getRow(view.getSelected());
				view.setAdditionalText(course.getName(), StudentView.ASSIGNMENT_PAGE);
				
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
