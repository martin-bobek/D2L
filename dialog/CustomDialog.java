package dialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

abstract class CustomDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	CustomDialog(JFrame owner, String header) {
		super(owner, header, true);
		layoutDialog();
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}
	
	abstract void layoutDialog();
	
	void runDialog() {
		setVisible(true);
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			dispose();
		}
	}
	
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
