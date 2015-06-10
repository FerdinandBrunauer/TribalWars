package tribalwars.utils;

import java.util.regex.Pattern;

import logger.Logger;

public class VillagenameUtils {

	private static Pattern pattern = Pattern.compile("\\d{3}\\s-\\s\\w{4}\\s-\\sBanana");

	public static void main(String[] args) {
		String name;
		name = generateVillagename(1, VillageTroupType.OFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(2, VillageTroupType.DEFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(3, VillageTroupType.DEFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(4, VillageTroupType.OFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(5, VillageTroupType.DEFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(6, VillageTroupType.OFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(7, VillageTroupType.DEFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(8, VillageTroupType.DEFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(9, VillageTroupType.OFF, true, true, true);
		System.out.println(name);
		name = generateVillagename(10, VillageTroupType.OFF, true, true, true);
		System.out.println(name);
	}

	public static VillageTroupType getVillageTroupType(String villagename) {
		try {
			checkVillagename(villagename);

			String[] split = villagename.split(" ");
			int chatValue = split[2].charAt(0) - 97;
			if ((chatValue % 2) == 0) {
				return VillageTroupType.OFF;
			} else {
				return VillageTroupType.DEFF;
			}
		} catch (Exception ignore) {
			return VillageTroupType.UNDEFINED;
		}
	}

	public static boolean getVillageBuildBuildings(String villagename) {
		try {
			checkVillagename(villagename);

			String[] split = villagename.split(" ");
			int chatValue = split[2].charAt(1) - 97;
			if ((chatValue % 2) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ignore) {
			return false;
		}
	}

	public static boolean getVillageBuildTroops(String villagename) {
		try {
			checkVillagename(villagename);

			String[] split = villagename.split(" ");
			int chatValue = split[2].charAt(2) - 97;
			if ((chatValue % 2) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ignore) {
			return false;
		}
	}

	public static boolean getVillageDoFarming(String villagename) {
		try {
			checkVillagename(villagename);

			String[] split = villagename.split(" ");
			int chatValue = split[2].charAt(3) - 97;
			if ((chatValue % 2) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ignore) {
			return false;
		}
	}

	public static String generateVillagename(int counter, VillageTroupType type, boolean buildBuildings, boolean buildTroops, boolean farm) {
		return "000".substring((counter + "").length()) + counter + " - " + generateRandomChar((type == VillageTroupType.OFF) ? true : false) + generateRandomChar(buildBuildings) + generateRandomChar(buildTroops) + generateRandomChar(farm) + " - Banana";
	}

	private static void checkVillagename(String villagename) {
		if (!pattern.matcher(villagename).find()) {
			Logger.logMessage("Unbekannter Dorfname! Name: \"" + villagename + "\"");
			throw new IllegalArgumentException("Unbekannter Dorfname! Name: \"" + villagename + "\"");
		}
	}

	private static char generateRandomChar(boolean moduloZero) {
		int charValue;
		while (true) {
			charValue = (int) (Math.random() * 25);
			if (moduloZero) {
				if ((charValue % 2) == 0) {
					break;
				}
			} else {
				if ((charValue % 2) != 0) {
					break;
				}
			}
		}
		return (char) (charValue + 97);
	}

	public enum VillageTroupType {
		OFF,
		DEFF,
		UNDEFINED
	}

}
