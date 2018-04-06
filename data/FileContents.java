package data;

import java.io.Serializable;

public class FileContents implements Serializable {
	private static final long serialVersionUID = 1L;
	String filetype;
	private byte[] content;
	
	public FileContents(byte[] content, String filetype) {
		this.content = content;
		this.filetype = filetype;
	}
}
