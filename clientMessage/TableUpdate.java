package clientMessage;

import java.util.ArrayList;

import data.TableRow;

public class TableUpdate implements Response {
	private static final long serialVersionUID = 1L;
	private ArrayList<? extends TableRow> rows;
	
	public TableUpdate(TableRow row) {
		ArrayList<TableRow> rowArray = new ArrayList<TableRow>();
		rowArray.add(row);
		rows = rowArray;
	}
	
	public TableUpdate(ArrayList<? extends TableRow> rows) {
		this.rows = rows;
	}
	
	public void performAction(ClientInterface client) {
		client.updateTable(rows);
	}
}
