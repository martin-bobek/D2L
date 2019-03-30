package shared.data;

/**
 * A class used to store the properties used by the table model to render the data
 * in an object. Necessary to make available by all object which implement TableRow.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public class RowProperties {
	/**
	 * The names of the columns.
	 */
	private final String[] names;
	/**
	 * The types of data stored in the columns.
	 */
	private final Class<?>[] types;
	/**
	 * Maps table column numbers to data object columns. Allows multiple
	 * render styles to be used for each data object.
	 */
	private final int[] map;
	
	/**
	 * Create a new row properties object, creating values for all properties.
	 * @param names The names of the columns.
	 * @param types The types of the columns.
	 * @param map The map of table columns to data columns.
	 */
	public RowProperties(String[] names, Class<?>[] types, int[] map) {
		this.names = names;
		this.types = types;
		this.map = map;
	}
	
	/**
	 * Gets the number of columns.
	 * @return The number of columns.
	 */
	public int getNumColumns() {
		return names.length;
	}
	
	/**
	 * Gets the name of a column.
	 * @param col The column number.
	 * @return The column's name.
	 */
	public String getName(int col) {
		return names[col];
	}
	
	/**
	 * Gets the type of a column.
	 * @param col The column number.
	 * @return The column's type.
	 */
	public Class<?> getType(int col) {
		return types[col];
	}
	
	/**
	 * Gets the data column number.
	 * @param col The table column number.
	 * @return The data column number.
	 */
	public int columnMap(int col) {
		return map[col];
	}
}
