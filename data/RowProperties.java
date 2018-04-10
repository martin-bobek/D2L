package data;

public class RowProperties {
	private final String[] names;
	private final Class<?>[] types;
	private final int[] map;
	
	public RowProperties(String[] names, Class<?>[] types, int[] map) {
		this.names = names;
		this.types = types;
		this.map = map;
	}
	
	public int getNumColumns() {
		return names.length;
	}
	
	public String getName(int col) {
		return names[col];
	}
	
	public Class<?> getType(int col) {
		return types[col];
	}
	
	public int columnMap(int col) {
		return map[col];
	}
}
