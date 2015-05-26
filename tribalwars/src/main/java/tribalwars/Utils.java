package tribalwars;

import java.util.List;

import datastore.Database;
import datastore.memoryObjects.VorlageItem;

public class Utils {

	// private static int[] production = { 0, 30, 35, 41, 47, 55, 64, 74, 86, 100, 117, 136, 158, 184, 214, 249, 289, 337, 391, 455, 530, 616, 717, 833, 969, 1127, 1311, 1525, 1774, 2063, 2400 };

	public static String calculateNextBuilding(long vorlageID, Village village) {
		List<VorlageItem> vorlage = Database.getBuildingPatternContent(vorlageID);

		for (VorlageItem item : vorlage) {
			if (getBuildingLevelByName(village, item.getBuildingName()) < item.getLevel()) {
				return item.getBuildingName();
			}
		}

		return null;
	}

	public static int calculateLoot(long villageID) {
		return 0;
	}

	private static int getBuildingLevelByName(Village village, String building) {
		switch (building) {
		case "main":
			return village.getHauptgebaeude();
		case "barracks":
			return village.getKaserne();
		case "stable":
			return village.getStall();
		case "garage":
			return village.getWerkstatt();
		case "snob":
			return village.getAdelshof();
		case "smith":
			return village.getSchmiede();
		case "place":
			return village.getVersammlungsplatz();
		case "market":
			return village.getMarktplatz();
		case "wood":
			return village.getHolzfaeller();
		case "stone":
			return village.getLehmgrube();
		case "iron":
			return village.getEisenmine();
		case "farm":
			return village.getBauernhof();
		case "storage":
			return village.getSpeicher();
		case "hide":
			return village.getVersteck();
		case "wall":
			return village.getWall();
		default:
			System.err.println("Kein definiertes Gebäude \"" + building + "\"!");
			System.exit(1);
			return 0;
		}
	}

}
