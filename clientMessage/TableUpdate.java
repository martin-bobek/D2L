package clientMessage;

import java.util.ArrayList;

import data.TableRow;

public class TableUpdate implements Response {
	private static final long serialVersionUID = 1L;
	private ArrayList<? extends TableRow> rows;
	
	public TableUpdate(ArrayList<? extends TableRow> rows) {
		this.rows = rows;
	}
	
	public void performAction(ClientInterface client) {
		client.updateTable(rows);
	}
}
