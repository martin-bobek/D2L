package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import data.FileContent;

class FileManager {
	static final char ASSIGNMENT = 'A';
	private final String workingDir;
	private File path;
	
	FileManager(String workingDir) {
		this.workingDir = workingDir;
	}
	
	void setPath(char type, int id) {
		path = new File(workingDir + '\\' + type + '\\' + id);
	}
	
	void storeFile(FileContent file) throws IOException {
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(path));
		output.write(file.getContent());
		output.close();
	}
}
