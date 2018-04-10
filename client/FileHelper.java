package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import data.FileContent;

public class FileHelper {
	private File file;
	
	public void setPath(File file) {
		this.file = file;
	}
	
	public FileContent uploadFile() throws IOException {
		byte[] content = new byte[validateLength()];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		input.read(content);
		input.close();
		String name = file.toString();
		return new FileContent(content, name.substring(name.lastIndexOf('.')));
	}
	
	public void downloadFile(byte[] content) throws IOException {
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(this.file));
		output.write(content);
		output.close();
	}
	
	int validateLength() throws IOException {
		long length = file.length();
		if (length > Integer.MAX_VALUE)
			throw new IOException("File too long!");
		return (int)length;
	}
}
