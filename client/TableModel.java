package client;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import data.TableRow;

class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;	
	private ArrayList<TableRow> rows;
	
	TableModel() {
		rows = new ArrayList<TableRow>();
	}
	
	void updateRow(int row) {
		fireTableRowsUpdated(row, row);
	}
	
	void clear() {
		rows.clear();
		fireTableStructureChanged();
	}
	
	void addElement(TableRow row) {
		rows.add(row);
		if (rows.size() == 1)
			fireTableStructureChanged();
		fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
	}
	
	TableRow getRow(int row) {
		return rows.get(row);
	}
	
	public int getColumnCount() {
		if (rows.isEmpty())
			return 0;
		return rows.get(0).getNumColumns();
	}
	
	public int getRowCount() {
		return rows.size();
	}

	public String getColumnName(int col) {
		return rows.get(0).getColumnName(col);
	}
	
	public Object getValueAt(int rowIndex, int colIndex) {
		return rows.get(rowIndex).getColumn(colIndex);
	}
	
	public Class<?> getColumnClass(int col) {
		return rows.get(0).getColumnType(col);	
	}
}
