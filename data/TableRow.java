package data;

import java.io.Serializable;

public interface TableRow extends Serializable {
	public Object getColumn(int index);
	public void setColumn(Object value, int index);
}
