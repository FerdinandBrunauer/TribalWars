package tribalwars;

public class Village {

	private String id = "";
	private String name = "";
	private int x = 0;
	private int y = 0;
	private int holz = 0;
	private int lehm = 0;
	private int eisen = 0;
	private int speicher = 0;

	public Village(Account account, String id, String name, int x, int y) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
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

}
