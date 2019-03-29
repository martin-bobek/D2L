package client;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import data.RowProperties;
import data.TableRow;

/**
 * The model for the table in the main GUI's. Updates to this model are immediately
 * visible in the JTable display in the GUI.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	/**
	 * The list of rows to be displayed in the table.
	 */
	private ArrayList<TableRow> rows;
	/**
	 * The properties of the current row objects being displayed.
	 */
	private RowProperties properties;
	
	/**
	 * Creates a new table model.
	 */
	public TableModel() {
		rows = new ArrayList<TableRow>();
	}
	
	/**
	 * Is used to indicate the contents of a row have updated, causing 
	 * the GUI to update.
	 * @param row The row which has been modified.
	 */
	public void updateRow(int row) {
		fireTableRowsUpdated(row, row);
	}
	
	/**
	 * Clears the table, removing all rows.
	 */
	public void clear() {
		int size = rows.size();
		rows.clear();
		if (size != 0)
			fireTableRowsDeleted(0, size - 1);
	}
	
	/**
	 * Clears all rows and changes the table structure to reflect
	 * the new properties passed in.
	 * @param properties The new table properties to be drawn.
	 */
	public void reset(RowProperties properties) {
		rows.clear();
		this.properties = properties;
		fireTableStructureChanged();
	}
	
	/**
	 * Inserts an element into the table.
	 * @param row The row where the element should be inserted.
	 */
	void addElement(TableRow row) {
		rows.add(row);
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}
	
	/**
	 * Gets the contents of the table at a particular row.
	 * @param row The row number of the desired contents.
	 * @return The row grabbed from the table.
	 */
	public TableRow getRow(int row) {
		return rows.get(row);
	}
	
	/**
	 * Gets the number of columns.
	 * Used to interface with the GUI.
	 * @return The number of columns.
	 */
	public int getColumnCount() {
		if (properties == null)
			return 0;
		return properties.getNumColumns();
	}
	
	/**
	 * Gets the number of columns.
	 * Used to interface with the GUI.
	 * @return The number of rows.
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * Gets the name of the column.
	 * Used to interface with the GUI.
	 * @param col The column number.
	 * @return The name of the column.
	 */
	public String getColumnName(int col) {
		return properties.getName(col);
	}
	
	/**
	 * Gets the render type of the column.
	 * Used to interface with the GUI.
	 * @param col The column number.
	 * @return The object type of the column.
	 */
	public Class<?> getColumnClass(int col) {
		return properties.getType(col);
	}
	
	/**
	 * Checks if a cell is editable.
	 * Used to interface with the GUI.
	 * @param row The row number.
	 * @param col The column number.
	 * @return true if the cell is editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return properties.getType(col) == Boolean.class;
	}
	
	/**
	 * Gets the value of a particular cell.
	 * Used to interface with the GUI.
	 * @param row The row number.
	 * @param col The column number.
	 * @return The value in the cell.
	 */
	public Object getValueAt(int row, int col) {
		return rows.get(row).getColumn(properties.columnMap(col));
	}
	
	/**
	 * Sets the value of a cell, automatically updating the GUI.
	 * @param value The new value for the cell.
	 * @param row The row number.
	 * @param col The column number.
	 */
	public void setValueAt(Object value, int row, int col) {
		rows.get(row).setColumn(value, properties.columnMap(col));
		fireTableCellUpdated(row, col);
	}
}
