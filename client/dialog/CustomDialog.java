package client.dialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
/**
 * An abstract custom dialog object used aas a template for custom dialogs used in the project.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
abstract class CustomDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new dialog, laying out components.
	 * @param owner The view which owns the dialog.
	 * @param header The header text of the dialog.
	 */
	CustomDialog(JFrame owner, String header) {
		super(owner, header, true);
		layoutDialog();
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
	
	/**
	 * Implemented by subclasses to specify the layout of the dialog.
	 */
	abstract void layoutDialog();
	
	/**
	 * Runs the dialog. Sets the dialog to visible and waits until the user 
	 * enters the required information.
	 */
	void runDialog() {
		setVisible(true);
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			dispose();
		}
	}
	
	/**
	 * Adds the default event handlers to the dialog.
	 */
	void addHandlers() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		final Thread thread = Thread.currentThread();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				thread.interrupt();
			}
		});
	}
}
