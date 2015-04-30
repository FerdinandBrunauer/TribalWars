public class DatabaseDefaultvalueGenerator {

	private static String[][] rohstoffe = { { "Holzfäller", "3" }, { "Lehmgrube", "3" }, { "Eisenmine", "3" }, { "Lehmgrube", "6" }, { "Holzfäller", "6" }, { "Lehmgrube", "10" }, { "Holzfäller", "10" }, { "Eisenmine", "7" }, { "Hauptgebäude", "5" }, { "Kaserne", "5" }, { "Holzfäller", "15" },
			{ "Lehmgrube", "15" }, { "Eisenmine", "12" }, { "Hauptgebäude", "10" }, { "Wall", "10" }, { "Holzfäller", "25" }, { "Lehmgrube", "25" }, { "Eisenmine", "25" }, { "Kaserne", "10" }, { "Schmiede", "5" }, { "Holzfäller", "30" }, { "Lehmgrube", "30" }, { "Eisenmine", "30" },
			{ "Hauptgebäude", "20" }, { "Speicher", "30" }, { "Bauernhof", "30" }, { "Versammlungsplatz", "1" }, { "Marktplatz", "10" }, { "Wall", "20" }, { "Schmiede", "15" }, { "Stall", "5" }, { "Schmiede", "20" }, { "Adelshof", "1" }, { "Stall", "10" }, { "Kaserne", "25" }, { "Stall", "20" },
			{ "Werkstatt", "15" }, { "Marktplatz", "20" }, { "Marktplatz", "22" } };

	private static String[][] deff = { { "Hauptgebäude", "1" }, { "Versammlungsplatz", "1" }, { "Lehmgrube", "1" }, { "Holzfäller", "1" }, { "Eisenmine", "1" }, { "Lehmgrube", "2" }, { "Holzfäller", "2" }, { "Lehmgrube", "3" }, { "Holzfäller", "3" }, { "Eisenmine", "2" }, { "Holzfäller", "4" },
			{ "Lehmgrube", "4" }, { "Speicher", "1" }, { "Holzfäller", "5" }, { "Lehmgrube", "5" }, { "Holzfäller", "6" }, { "Eisenmine", "3" }, { "Lehmgrube", "6" }, { "Speicher", "2" }, { "Hauptgebäude", "3" }, { "Kaserne", "1" }, { "Speicher", "3" }, { "Kaserne", "2" }, { "Holzfäller", "7" },
			{ "Marktplatz", "1" }, { "Speicher", "4" }, { "Holzfäller", "9" }, { "Lehmgrube", "7" }, { "Holzfäller", "10" }, { "Lehmgrube", "8" }, { "Holzfäller", "11" }, { "Speicher", "5" }, { "Bauernhof", "2" }, { "Lehmgrube", "9" }, { "Eisenmine", "4" }, { "Holzfäller", "12" },
			{ "Lehmgrube", "10" }, { "Speicher", "6" }, { "Bauernhof", "3" }, { "Kaserne", "5" }, { "Wall", "5" }, { "Holzfäller", "13" }, { "Speicher", "7" }, { "Lehmgrube", "11" }, { "Eisenmine", "6" }, { "Bauernhof", "4" }, { "Hauptgebäude", "5" }, { "Schmiede", "2" }, { "Holzfäller", "14" },
			{ "Lehmgrube", "12" }, { "Bauernhof", "5" }, { "Speicher", "9" }, { "Holzfäller", "15" }, { "Eisenmine", "10" }, { "Lehmgrube", "13" }, { "Speicher", "10" }, { "Hauptgebäude", "10" }, { "Bauernhof", "8" }, { "Speicher", "11" }, { "Holzfäller", "16" }, { "Lehmgrube", "15" },
			{ "Eisenmine", "14" }, { "Kaserne", "12" }, { "Schmiede", "5" }, { "Wall", "10" }, { "Marktplatz", "5" }, { "Bauernhof", "11" }, { "Speicher", "12" }, { "Lehmgrube", "16" }, { "Holzfäller", "17" }, { "Eisenmine", "15" }, { "Schmiede", "8" }, { "Marktplatz", "6" }, { "Schmiede", "10" },
			{ "Kaserne", "13" }, { "Stall", "3" }, { "Holzfäller", "18" }, { "Lehmgrube", "17" }, { "Speicher", "16" }, { "Bauernhof", "16" }, { "Kaserne", "14" }, { "Wall", "15" }, { "Lehmgrube", "18" }, { "Holzfäller", "19" }, { "Lehmgrube", "19" }, { "Hauptgebäude", "15" }, { "Wall", "20" },
			{ "Bauernhof", "18" }, { "Speicher", "17" }, { "Holzfäller", "20" }, { "Lehmgrube", "22" }, { "Holzfäller", "21" }, { "Eisenmine", "18" }, { "Bauernhof", "21" }, { "Speicher", "19" }, { "Holzfäller", "22" }, { "Marktplatz", "10" }, { "Kaserne", "18" }, { "Lehmgrube", "24" },
			{ "Stall", "8" }, { "Schmiede", "15" }, { "Kaserne", "19" }, { "Speicher", "22" }, { "Bauernhof", "22" }, { "Lehmgrube", "25" }, { "Bauernhof", "23" }, { "Kaserne", "20" }, { "Holzfäller", "24" }, { "Eisenmine", "21" }, { "Hauptgebäude", "20" }, { "Schmiede", "20" },
			{ "Adelshof", "1" }, { "Kaserne", "21" }, { "Holzfäller", "25" }, { "Lehmgrube", "26" }, { "Bauernhof", "25" }, { "Eisenmine", "22" }, { "Speicher", "25" }, { "Stall", "13" }, { "Holzfäller", "26" }, { "Kaserne", "24" }, { "Holzfäller", "27" }, { "Lehmgrube", "27" },
			{ "Holzfäller", "28" }, { "Lehmgrube", "28" }, { "Eisenmine", "26" }, { "Speicher", "27" }, { "Bauernhof", "27" }, { "Marktplatz", "15" }, { "Stall", "18" }, { "Lehmgrube", "29" }, { "Holzfäller", "29" }, { "Eisenmine", "27" }, { "Bauernhof", "28" }, { "Eisenmine", "28" },
			{ "Speicher", "28" }, { "Werkstatt", "2" }, { "Kaserne", "25" }, { "Speicher", "29" }, { "Lehmgrube", "30" }, { "Holzfäller", "30" }, { "Eisenmine", "30" }, { "Bauernhof", "30" }, { "Stall", "20" }, { "Werkstatt", "5" } };

	private static String[][] off = { { "Hauptgebäude", "1" }, { "Versammlungsplatz", "1" }, { "Holzfäller", "1" }, { "Lehmgrube", "1" }, { "Eisenmine", "1" }, { "Holzfäller", "2" }, { "Lehmgrube", "2" }, { "Holzfäller", "3" }, { "Bauernhof", "1" }, { "Lehmgrube", "3" }, { "Eisenmine", "2" },
			{ "Bauernhof", "2" }, { "Holzfäller", "4" }, { "Lehmgrube", "4" }, { "Hauptgebäude", "2" }, { "Speicher", "1" }, { "Hauptgebäude", "3" }, { "Kaserne", "1" }, { "Speicher", "2" }, { "Bauernhof", "3" }, { "Holzfäller", "5" }, { "Lehmgrube", "5" }, { "Eisenmine", "3" },
			{ "Bauernhof", "4" }, { "Kaserne", "3" }, { "Speicher", "4" }, { "Hauptgebäude", "4" }, { "Bauernhof", "5" }, { "Hauptgebäude", "5" }, { "Schmiede", "3" }, { "Hauptgebäude", "6" }, { "Speicher", "6" }, { "Eisenmine", "5" }, { "Bauernhof", "6" }, { "Hauptgebäude", "7" },
			{ "Holzfäller", "10" }, { "Lehmgrube", "10" }, { "Bauernhof", "7" }, { "Marktplatz", "2" }, { "Hauptgebäude", "8" }, { "Kaserne", "4" }, { "Bauernhof", "8" }, { "Hauptgebäude", "9" }, { "Kaserne", "5" }, { "Wall", "5" }, { "Eisenmine", "6" }, { "Schmiede", "4" }, { "Bauernhof", "9" },
			{ "Schmiede", "5" }, { "Bauernhof", "10" }, { "Hauptgebäude", "10" }, { "Stall", "1" }, { "Bauernhof", "11" }, { "Stall", "3" }, { "Eisenmine", "7" }, { "Stall", "5" }, { "Bauernhof", "12" }, { "Holzfäller", "15" }, { "Lehmgrube", "15" }, { "Eisenmine", "10" }, { "Schmiede", "10" },
			{ "Werkstatt", "3" }, { "Eisenmine", "15" }, { "Hauptgebäude", "15" }, { "Bauernhof", "14" }, { "Wall", "20" }, { "Kaserne", "15" }, { "Holzfäller", "16" }, { "Lehmgrube", "16" }, { "Holzfäller", "17" }, { "Lehmgrube", "17" }, { "Bauernhof", "15" }, { "Holzfäller", "18" },
			{ "Lehmgrube", "18" }, { "Holzfäller", "19" }, { "Lehmgrube", "19" }, { "Holzfäller", "20" }, { "Lehmgrube", "20" }, { "Hauptgebäude", "20" }, { "Holzfäller", "21" }, { "Lehmgrube", "21" }, { "Holzfäller", "22" }, { "Lehmgrube", "22" }, { "Bauernhof", "16" }, { "Kaserne", "20" },
			{ "Stall", "10" }, { "Marktplatz", "10" }, { "Schmiede", "14" }, { "Eisenmine", "17" }, { "Bauernhof", "17" }, { "Holzfäller", "23" }, { "Lehmgrube", "23" }, { "Bauernhof", "18" }, { "Holzfäller", "24" }, { "Lehmgrube", "24" }, { "Holzfäller", "25" }, { "Bauernhof", "19" },
			{ "Lehmgrube", "25" }, { "Holzfäller", "26" }, { "Lehmgrube", "26" }, { "Eisenmine", "20" }, { "Werkstatt", "5" }, { "Schmiede", "20" }, { "Holzfäller", "27" }, { "Lehmgrube", "27" }, { "Eisenmine", "21" }, { "Stall", "15" }, { "Eisenmine", "22" }, { "Bauernhof", "20" },
			{ "Holzfäller", "28" }, { "Lehmgrube", "28" }, { "Adelshof", "1" }, { "Bauernhof", "30" }, { "Holzfäller", "29" }, { "Lehmgrube", "29" }, { "Holzfäller", "30" }, { "Marktplatz", "20" }, { "Lehmgrube", "30" }, { "Eisenmine", "25" }, { "Kaserne", "25" }, { "Stall", "20" },
			{ "Eisenmine", "30" }, { "Speicher", "30" } };

	public static void main(String[] args) {
		int counter = 1;
		for (String[] actualBuilding : rohstoffe) {
			System.out.println("INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, " + counter++ + ", " + replaceBuildingToID(actualBuilding[0]) + ", " + actualBuilding[1] + ");");
		}

		System.err.println("================================================================================================");

		counter = 1;
		for (String[] actualBuilding : deff) {
			System.out.println("INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, " + counter++ + ", " + replaceBuildingToID(actualBuilding[0]) + ", " + actualBuilding[1] + ");");
		}

		System.err.println("================================================================================================");

		counter = 1;
		for (String[] actualBuilding : off) {
			System.out.println("INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, " + counter++ + ", " + replaceBuildingToID(actualBuilding[0]) + ", " + actualBuilding[1] + ");");
		}
	}

	private static int replaceBuildingToID(String building) {
		switch (building) {
		case "Hauptgebäude":
			return 1;
		case "Kaserne":
			return 2;
		case "Stall":
			return 3;
		case "Werkstatt":
			return 4;
		case "Adelshof":
			return 5;
		case "Schmiede":
			return 6;
		case "Versammlungsplatz":
			return 7;
		case "Marktplatz":
			return 8;
		case "Holzfäller":
			return 9;
		case "Lehmgrube":
			return 10;
		case "Eisenmine":
			return 11;
		case "Bauernhof":
			return 12;
		case "Speicher":
			return 13;
		case "Wall":
			return 15;
		default:
			System.err.println(building);
			System.exit(1);
		}
		return 0;
	}

}