package shared.response;

import java.util.ArrayList;

import shared.data.TableRow;
/**
 * Used to deliver new table entries in the GUI. Is only delivered after a request from the
 * the client.
 * @author Martin
 * @version 1.0
 * @since March 11, 2018
 */
public class TableUpdate implements Response {
	private static final long serialVersionUID = 1L;
	/**
	 * A list of the new table entries to be displayed.
	 */
	private ArrayList<? extends TableRow> rows;
	
	/**
	 * Creates a new update which delivers a single row.
	 * @param row The row to be delivered.
	 */
	public TableUpdate(TableRow row) {
		ArrayList<TableRow> rowArray = new ArrayList<TableRow>();
		rowArray.add(row);
		rows = rowArray;
	}
	
	/**
	 * Creates a new update which delivers many rows.
	 * @param rows A list of rows to be delivered.
	 */
	public TableUpdate(ArrayList<? extends TableRow> rows) {
		this.rows = rows;
	}
	
	/** 
	 * Adds the rows to the clients table. Then unlocks the client allowing new requests
	 * to be sent.
	 */
	public void performAction(ClientInterface client) {
		client.updateTable(rows);
		client.unlock();
	}
}
