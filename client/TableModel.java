package client;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import data.RowProperties;
import data.TableRow;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;	
	private ArrayList<TableRow> rows;
	private RowProperties properties;
	
	public TableModel() {
		rows = new ArrayList<TableRow>();
	}
	
	public void updateRow(int row) {
		fireTableRowsUpdated(row, row);
	}
	
	public void clear() {
		int size = rows.size();
		rows.clear();
		if (size != 0)
			fireTableRowsDeleted(0, size - 1);
	}
	
	public void reset(RowProperties properties) {
		rows.clear();
		this.properties = properties;
		fireTableStructureChanged();
	}
	
	void addElement(TableRow row) {
		rows.add(row);
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}
	
	public TableRow getRow(int row) {
		return rows.get(row);
	}
	
	public int getColumnCount() {
		if (properties == null)
			return 0;
		return properties.getNumColumns();
	}
	
	public int getRowCount() {
		return rows.size();
	}

	public String getColumnName(int col) {
		return properties.getName(col);
	}
	
	public Class<?> getColumnClass(int col) {
		return properties.getType(col);
	}
	
	public boolean isCellEditable(int row, int col) {
		return properties.getType(col) == Boolean.class;
	}
	
	public Object getValueAt(int row, int col) {
		return rows.get(row).getColumn(properties.columnMap(col));
	}
	
	public void setValueAt(Object value, int row, int col) {
		rows.get(row).setColumn(value, properties.columnMap(col));
		fireTableCellUpdated(row, col);
	}
}
