package tribalwars.storage;

public enum Unit {
	Speertraeger("spear", "Speertr\u00E4ger", 10, 15, 45, 20, UnitType.Infantry, 25, 18 * 60),
	Schwertkaempfer("sword", "Schwertk\u00E4mpfer", 25, 50, 15, 40, UnitType.Infantry, 15, 22 * 60),
	Axtkaempfer("axe", "Axtk\u00E4mpfer", 40, 10, 5, 10, UnitType.Infantry, 10, 18 * 60),
	Bogenschuetzen("archer", "Bogensch\u00FCtzen", 15, 50, 40, 5, UnitType.Archer, 10, 18 * 60),
	Spaeher("spy", "Sp\u00E4her", 0, 2, 1, 2, UnitType.Cavalry, 0, 9 * 60),
	LeichteKavallerie("light", "Leichte Kavallerie", 130, 30, 40, 30, UnitType.Cavalry, 80, 10 * 60),
	BeritteneBogenschuetzen("marcher", "Berittener Bogensch\u00FCtze", 120, 40, 30, 50, UnitType.Archer, 50, 10 * 60),
	SchwereKavallerie("heavy", "Schwere Kavallerie", 150, 200, 80, 180, UnitType.Cavalry, 50, 11 * 60),
	Rammboecke("ram", "Rammb\u00F6cke", 2, 20, 50, 20, UnitType.Infantry, 0, 30 * 60),
	Katapulte("catapult", "Katapult", 100, 100, 50, 100, UnitType.Infantry, 0, 30 * 60),
	Adelsgeschlechter("snob", "Adelsgeschlecht", 30, 100, 50, 100, UnitType.Infantry, 0, 35 * 60),
	Paladin("knight", "Paladin", 150, 250, 400, 150, UnitType.Cavalry, 100, 10 * 60);

	private final String shortName;
	private final String longName;
	private final int attackStrength;
	private final int defence;
	private final int defenceCavalry;
	private final int defenceArcher;
	private final UnitType unitType;
	private final int capacity;
	private final int timePerField;

	private Unit(String shortName, String longName, int attackStrength, int defence, int defenceCavalry, int defenceArcher, UnitType unitType, int capacity, int timePerField) {
		this.shortName = shortName;
		this.longName = longName;
		this.attackStrength = attackStrength;
		this.defence = defence;
		this.defenceCavalry = defenceCavalry;
		this.defenceArcher = defenceArcher;
		this.unitType = unitType;
		this.capacity = capacity;
		this.timePerField = timePerField;
	}

	public String getShortName() {
		return shortName;
	}

	public String getLongName() {
		return longName;
	}

	public int getAttackStrength() {
		return attackStrength;
	}

	public int getDefence() {
		return defence;
	}

	public int getDefenceCavalry() {
		return defenceCavalry;
	}

	public int getDefenceArcher() {
		return defenceArcher;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getTimePerField() {
		return timePerField;
	}

	public static Unit fromString(String shortName) {
		if (shortName != null) {
			for (Unit b : Unit.values()) {
				if (shortName.equalsIgnoreCase(b.shortName)) {
					return b;
				}
			}
		}
		return null;
	}

	public enum UnitType {
		Infantry,
		Cavalry,
		Archer
	}

}
