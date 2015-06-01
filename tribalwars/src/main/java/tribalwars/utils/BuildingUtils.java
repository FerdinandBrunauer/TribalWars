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

	public static String getFullBuildingname(String shortName) {
		switch (shortName) {
		case "main":
			return "Hauptgeb\u00E4ude";
		case "barracks":
			return "Kaserne";
		case "stable":
			return "Stall";
		case "garage":
			return "Werkstatt";
		case "snob":
			return "Adelshof";
		case "smith":
			return "Schmiede";
		case "place":
			return "Versammlungsplatz";
		case "market":
			return "Marktplatz";
		case "wood":
			return "Holzf\u00E4ller";
		case "stone":
			return "Lehmgrube";
		case "iron":
			return "Eisenmine";
		case "farm":
			return "Bauernhof";
		case "storage":
			return "Speiczher";
		case "hide":
			return "Versteck";
		case "wall":
			return "Wall";
		case "statue":
			return "Statue";
		default:
			return null;
		}
	}

}
