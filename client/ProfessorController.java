package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Course;
import message.RequestAssignments;
import message.RequestCourses;
import message.UpdateCourse;

class ProfessorController {
	private ProfessorView view;
	private ServerConnection server;
	private AtomicBoolean locked = new AtomicBoolean(true);
	
	ProfessorController(ProfessorView view, ServerConnection server) {
		this.view = view;
		this.server = server;
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
		addCourseActiveHandler();
		addCreateCourseHandler();
		addSelectionHandler();
	}
	
	private void addCreateCourseHandler() {
		view.addCreateCourseHandler(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!locked.compareAndSet(false, true))
					return;
				try {
					Course course = showCourseDialog();
					server.sendObject(new UpdateCourse(course));
				} catch (InvalidParameterException ex) {
					locked.set(false);
					JOptionPane.showMessageDialog(view, ex.getMessage());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(view, "Lost connection to server!");
					System.exit(1);
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
				try {
					server.clearList();
					server.sendObject(new RequestAssignments());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(view, "Lost connection to server!");
					System.exit(1);
				}
			}
		});
	}
	
	private void addCourseActiveHandler() {
		view.addCourseActiveListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Course course = (Course)view.getSelectedItem();
				course.setActive(!course.getActive());
				view.updateSelectedItem(course);
				view.setCourseActiveButtonText(course.getActive());
				try {
					server.sendObject(new UpdateCourse(course));
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(view, "Lost connection to server!");
					System.exit(1);
				}
			}
		});
	}
	
	private void addSelectionHandler() {
		view.addSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (view.getSelectedItem() == null)
					view.itemDeselected();
				else
					view.itemSelected();
			}
		});
	}
	
	private Course showCourseDialog() throws InvalidParameterException {
		String name = JOptionPane.showInputDialog(view, "Course Name:", "Create Course", JOptionPane.PLAIN_MESSAGE);
		if (name == null || name.isEmpty())
			throw new InvalidParameterException("Course name cannot be empty!");
		return new Course(name);
	}
}
