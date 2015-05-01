package tribalwars;

import java.util.List;

import datastore.Database;
import datastore.memoryObjects.VorlageItem;

public class Utils {

	public static String calculateNextBuilding(long vorlageID, Village village) {
		List<VorlageItem> vorlage = Database.getBuildingPatternContent(vorlageID);

		for (VorlageItem item : vorlage) {
			if (getBuildingLevelByName(village, item.getBuildingName()) < item.getLevel()) {
				return item.getBuildingName();
			}
		}

		return null;
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
