package tribalwars.storage;

public enum Unit {
	Speertraeger("spear", 10, 25, 18),
	Schwertkaempfer("sword", 25, 15, 22),
	Axtkaempfer("axe", 40, 10, 18),
	Bogenschuetzen("archer", 15, 10, 18),
	Spaeher("spy", 0, 0, 9),
	LeichteKavallerie("light", 130, 80, 10),
	BeritteneBogenschuetzen("marcher", 120, 50, 10),
	SchwereKavallerie("heavy", 150, 50, 11),
	Rammboecke("ram", 2, 0, 30),
	Katapulte("catapult", 100, 0, 30),
	Adelsgeschlechter("snob", 30, 0, 35),
	Paladin("knight", 150, 100, 10);

	private final String text;
	private final int attackStrength;
	private final int capacity;
	private final int timePerField;

	Unit(String text, int attackStrength, int capacity, int timePerField) {
		this.text = text;
		this.attackStrength = attackStrength;
		this.capacity = capacity;
		this.timePerField = timePerField;
	}

	public String getText() {
		return this.text;
	}

	public int getAttackStrength() {
		return this.attackStrength;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getTimePerField() {
		return this.timePerField;
	}

	public static Unit fromString(String text) {
		if (text != null) {
			for (Unit b : Unit.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
