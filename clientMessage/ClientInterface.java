package clientMessage;

import java.io.IOException;
import java.util.ArrayList;

import data.TableRow;

public interface ClientInterface {
	public void updateTable(ArrayList<? extends TableRow> rows);
	public void downloadFile(byte[] content) throws IOException;
}
