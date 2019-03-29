package data;

import java.io.Serializable;

/**
 * Implemented by all data object which can be displayed in the table.
 * Provides necessary methods for the table model to be able to access 
 * the objects data.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
public interface TableRow extends Serializable {
	/**
	 * Gets the value at a particular column.
	 * @param index The column number.
	 * @return The value of the column.
	 */
	public Object getColumn(int index);
	/**
	 * Sets the value of a particular column.
	 * @param value The value to be set for the column.
	 * @param index The column number.
	 */
	public void setColumn(Object value, int index);
}
