package helper;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;	
	private ArrayList<TableRow> rows;
	private RowProperties properties;
	
	public TableModel() {
		rows = new ArrayList<TableRow>();
	}
	
	void updateRow(int row) {
		fireTableRowsUpdated(row, row);
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
		return properties.getColumnName(col);
	}
	
	public Object getValueAt(int rowIndex, int colIndex) {
		return rows.get(rowIndex).getColumn(colIndex);
	}
	
	public Class<?> getColumnClass(int col) {
		return properties.getColumnType(col);
	}
	
	public boolean isCellEditable(int row, int col) {
		return properties.getColumnEditable(col);
	}
	
	public void setValueAt(Object value, int row, int col) {
		rows.get(row).setColumn(value, col);
		fireTableCellUpdated(row, col);
	}
}
