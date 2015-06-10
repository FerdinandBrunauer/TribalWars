package tribalwars.storage;

public enum Building {
	Hauptgebaeude("main"),
	Kaserne("barracks"),
	Stall("stable"),
	Werkstatt("garage"),
	Adelshof("snob"),
	Schmiede("smith"),
	Versammlungsplatz("place"),
	Statue("statue"),
	Marktplatz("market"),
	Holzfaeller("wood"),
	Lehmgrube("stone"),
	Eisenmine("iron"),
	Bauernhof("farm"),
	Speicher("storage"),
	Versteck("hide"),
	Wall("wall");

	private String text;

	Building(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static Building fromString(String text) {
		if (text != null) {
			for (Building b : Building.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
