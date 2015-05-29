package datastore.memoryObjects;

public class BuildingPattern {

	private final long ID;
	private final String name;

	public BuildingPattern(long ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	public long getID() {
		return this.ID;
	}

	public String getName() {
		return this.name;
	}

}
