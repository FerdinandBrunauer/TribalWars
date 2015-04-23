package tribalwars.building;

import tribalwars.Village;
import tribalwars.building.event.BuildingEventListener;

public abstract class Building implements BuildingEventListener {

	/**
	 * This Constant defines, how long the Level is valid. If the level is NOT
	 * valid, then it will update automatically.
	 */
	private static final long LEVEL_UPDATE_TIMEOUT = 2 * 60 * 1000;

	private final Village owner;
	private long lastLevelUpdate = 0;

	private int level = -1;

	public Building(Village owner) {
		this.owner = owner;
	}

	public Village getOwner() {
		return this.owner;
	}

	/**
	 * Returns the Level of the Building
	 * 
	 * @return -1 if the Building does not exist or an Error while reading the
	 *         Information occurs
	 */
	public synchronized int getLevel() {
		if ((System.currentTimeMillis() - lastLevelUpdate) > LEVEL_UPDATE_TIMEOUT) {
			updateLevel();
		}

		return this.level;
	}

	protected void setLevel(int level) {
		this.level = level;
		lastLevelUpdate = System.currentTimeMillis();

	}

	/**
	 * This Method should not finish until the Level is updated!
	 */
	protected abstract void updateLevel();

}
