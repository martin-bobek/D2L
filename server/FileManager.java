package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A class to manage the servers stored files in a file system.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
class FileManager {
	/**
	 * Indicates a file is for an assignment.
	 */
	static final char ASSIGNMENT = 'A';
	/**
	 * Indicates a file is for a assignment submission.
	 */
	static final char SUBMISSION = 'S';
	/**
	 * The directory in which the servers files are stored.
	 */
	private final String workingDir;
	/**
	 * The file path to the current file being handled.
	 */
	private File path;
	
	/**
	 * Creates a new file manager, setting the working directory.
	 * @param workingDir The location where server data is stored.
	 */
	FileManager(String workingDir) {
		this.workingDir = workingDir;
	}
	
	/**
	 * Selects a file to work with.
	 * @param type The type of the file (Assignment or Submission).
	 * @param id The id of the file.
	 */
	void setPath(char type, int id) {
		path = new File(workingDir + '\\' + type + '\\' + id);
	}
	
	/**
	 * Stores a file in the currently selected path.
	 * @param file The contents of the file.
	 * @throws IOException Failed to send serialized messages.
	 */
	void storeFile(byte[] file) throws IOException {
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(path));
		output.write(file);
		output.close();
	}
	
	/**
	 * Retrieves the file at the currently selected path.
	 * @return The bytes in the file.
	 * @throws IOException Failed to send serialized messages.
	 */
	byte[] retreiveFile() throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
		byte[] content = new byte[(int)path.length()];
		input.read(content);
		input.close();
		return content;
	}
}
