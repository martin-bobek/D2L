package data;

import java.io.Serializable;

public class FileContent implements Serializable {
	private static final long serialVersionUID = 1L;
	String filetype;
	private byte[] content;
	
	public FileContent(byte[] content, String filetype) {
		this.content = content;
		this.filetype = filetype;
	}

	public byte[] getContent() {
		return content;
	}

	public String getExtension() {
		return filetype;
	}
}
