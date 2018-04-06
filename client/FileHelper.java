package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import data.FileContent;

class FileHelper {
	FileHelper() {
		
	}
	
	FileContent uploadFile(File file) throws IOException {
		byte[] content = new byte[validateLength(file)];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		input.read(content);
		input.close();
		String name = file.toString();
		return new FileContent(content, name.substring(name.lastIndexOf('.')));
	}
	
	int validateLength(File file) throws IOException {
		long length = file.length();
		if (length > Integer.MAX_VALUE)
			throw new IOException("File too long!");
		return (int)length;
	}
}
