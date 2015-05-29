package datastore.memoryObjects;

public class VorlageItem {

	private String buildingName;
	private int level;

	public VorlageItem(String buildingName, int level) {
		this.buildingName = buildingName;
		this.level = level;
	}

	public String getBuildingName() {
		return this.buildingName;
	}

	public int getLevel() {
		return this.level;
	}

}
