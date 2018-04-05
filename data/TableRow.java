package data;

public interface TableRow {
	public static final Class<?> STRING = String.class;
	public static final Class<?> CHECKBOX = Boolean.class;
	
	public int getNumColumns();
	public Object getColumn(int index);
	public void setColumn(Object value, int index);
	public String getColumnName(int index);
	public Class<?> getColumnType(int index);
	public boolean getColumnEditable(int index);
}
