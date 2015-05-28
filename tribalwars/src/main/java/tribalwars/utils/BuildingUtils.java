package tribalwars.utils;

import java.util.HashMap;
import java.util.List;

import datastore.Database;
import datastore.memoryObjects.VorlageItem;

public class BuildingUtils {

	public static String calculateNextBuilding(long vorlageID, HashMap<String, Integer> buildings) {
		List<VorlageItem> vorlage = Database.getBuildingPatternContent(vorlageID);

		for (VorlageItem item : vorlage) {
			if (getBuildingLevelByName(buildings, item.getBuildingName()) < item.getLevel()) {
				return item.getBuildingName();
			}
		}

		return null;
	}

	private static int getBuildingLevelByName(HashMap<String, Integer> buildings, String building) {
		if (building.contains(building)) {
			return buildings.get(building);
		} else {
			return 0;
		}
	}

}
