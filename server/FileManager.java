package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class FileManager {
	static final char ASSIGNMENT = 'A';
	static final char SUBMISSION = 'S';
	private final String workingDir;
	private File path;
	
	FileManager(String workingDir) {
		this.workingDir = workingDir;
	}
	
	void setPath(char type, int id) {
		path = new File(workingDir + '\\' + type + '\\' + id);
	}
	
	void storeFile(byte[] file) throws IOException {
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(path));
		output.write(file);
		output.close();
	}
	
	byte[] retreiveFile() throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
		byte[] content = new byte[(int)path.length()];
		input.read(content);
		input.close();
		return content;
	}
}
