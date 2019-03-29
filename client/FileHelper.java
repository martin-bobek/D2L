package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is used to save and upload files to and from the
 * users computer. Part of the model component of the client.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class FileHelper {
	private File file;
	
	public void setPath(File file) {
		this.file = file;
	}
	
	public String getExtension() {
		String name = file.toString();
		int extStart = name.lastIndexOf('.');
		if (extStart == -1)
			return "";
		return name.substring(extStart);
	}
	
	public String getName() {
		String name = file.getName();
		int extStart = name.lastIndexOf('.');
		if (extStart == -1)
			return name;
		return name.substring(0, extStart);
	}
	
	public byte[] uploadFile() throws IOException {
		byte[] content = new byte[validateLength()];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		input.read(content);
		input.close();
		return content;
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
