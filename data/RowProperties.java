package data;

public class RowProperties {
	public static final Class<?> STRING = String.class;
	public static final Class<?> CHECKBOX = Boolean.class;
	
	private final int numColumns;
	private final String[] columnNames;
	private final Class<?>[] columnTypes;
	private final boolean[] columnEditable;
	
	public RowProperties(int numColumns, String[] columnNames, Class<?>[] columnTypes, boolean[] editable) {
		this.numColumns = numColumns;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.columnEditable = editable;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public Class<?> getColumnType(int col) {
		return columnTypes[col];
	}
	
	public boolean getColumnEditable(int col) {
		return columnEditable[col];
	}
}
