package tribalwars;

public class Village {

	private String id = "undefined";
	private String name = "undefined";
	private int x = 0;
	private int y = 0;
	private int holz = 0;
	private int lehm = 0;
	private int eisen = 0;
	private int speertraeger = 0;
	private int schwertkaempfer = 0;
	private int axtkaempfer = 0;
	private int bogenschuetzen = 0;
	private int spaeher = 0;
	private int leichteKavallerie = 0;
	private int berittenerBogenschuetze = 0;
	private int schwereKavallerie = 0;
	private int rammboecke = 0;
	private int katapult = 0;
	private int hauptgebaeude = 0;
	private int kaserne = 0;
	private int stall = 0;
	private int werkstatt = 0;
	private int adelshof = 0;
	private int schmiede = 0;
	private int versammlungsplatz = 0;
	private int marktplatz = 0;
	private int holzfaeller = 0;
	private int lehmgrube = 0;
	private int eisenmine = 0;
	private int bauernhof = 0;
	private int speicher = 0;
	private int versteck = 0;
	private int wall = 0;

	public Village() {

	}

	public Village(int hauptgebaeude, int kaserne, int stall, int werkstatt, int adelshof, int schmiede, int versammlungsplatz, int marktplatz, int holzfaeller, int lehmgrube, int eisenmine, int bauernhof, int speicher, int versteck, int wall) {
		this.hauptgebaeude = hauptgebaeude;
		this.kaserne = kaserne;
		this.stall = stall;
		this.werkstatt = werkstatt;
		this.adelshof = adelshof;
		this.schmiede = schmiede;
		this.versammlungsplatz = versammlungsplatz;
		this.marktplatz = marktplatz;
		this.holzfaeller = holzfaeller;
		this.lehmgrube = lehmgrube;
		this.eisenmine = eisenmine;
		this.bauernhof = bauernhof;
		this.speicher = speicher;
		this.versteck = versteck;
		this.wall = wall;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHolz() {
		return holz;
	}

	public int getLehm() {
		return lehm;
	}

	public int getEisen() {
		return eisen;
	}

	public int getSpeicher() {
		return speicher;
	}

	public int getSpeertraeger() {
		return speertraeger;
	}

	public int getSchwertkaempfer() {
		return schwertkaempfer;
	}

	public int getAxtkaempfer() {
		return axtkaempfer;
	}

	public int getBogenschuetzen() {
		return bogenschuetzen;
	}

	public int getSpaeher() {
		return spaeher;
	}

	public int getLeichteKavallerie() {
		return leichteKavallerie;
	}

	public int getBerittenerBogenschuetze() {
		return berittenerBogenschuetze;
	}

	public int getSchwereKavallerie() {
		return schwereKavallerie;
	}

	public int getRammboecke() {
		return rammboecke;
	}

	public int getKatapult() {
		return katapult;
	}

	public int getHauptgebaeude() {
		return hauptgebaeude;
	}

	public int getKaserne() {
		return kaserne;
	}

	public int getStall() {
		return stall;
	}

	public int getWerkstatt() {
		return werkstatt;
	}

	public int getAdelshof() {
		return adelshof;
	}

	public int getSchmiede() {
		return schmiede;
	}

	public int getVersammlungsplatz() {
		return versammlungsplatz;
	}

	public int getMarktplatz() {
		return marktplatz;
	}

	public int getHolzfaeller() {
		return holzfaeller;
	}

	public int getLehmgrube() {
		return lehmgrube;
	}

	public int getEisenmine() {
		return eisenmine;
	}

	public int getBauernhof() {
		return bauernhof;
	}

	public int getVersteck() {
		return versteck;
	}

	public int getWall() {
		return wall;
	}

}
