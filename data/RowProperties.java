package data;

public class RowProperties {
	public static final Class<?> STRING = String.class;
	public static final Class<?> CHECKBOX = Boolean.class;
	
	private final int numColumns;
	private final String[] names;
	private final Class<?>[] types;
	private final boolean[] editable;
	private final int[] map;
	
	public RowProperties(String[] names, Class<?>[] types, boolean[] editable, int[] map) {
		numColumns = names.length;
		this.names = names;
		this.types = types;
		this.editable = editable;
		this.map = map;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public String getName(int col) {
		return names[col];
	}
	
	public Class<?> getType(int col) {
		return types[col];
	}
	
	public boolean getEditable(int col) {
		return editable[col];
	}
	
	public int columnMap(int col) {
		return map[col];
	}
}
