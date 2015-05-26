package tribalwars;

public class FarmVorlage {
	
	private int farmSubmissionID;
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
	//private int paladin = 0;

	
	public FarmVorlage(int farmSubmissionID, int speertraeger, int schwertkaempfer, int axtkaempfer, int bogenschuetze, int spaeher, int lk, int bbs, int sk, int ramm, int kata) {
		this.farmSubmissionID = farmSubmissionID;
		this.speertraeger = speertraeger;
		this.schwertkaempfer = schwertkaempfer;
		this.axtkaempfer = axtkaempfer;
		this.bogenschuetzen = bogenschuetze;
		this.spaeher = spaeher;
		this.leichteKavallerie = lk;
		this.berittenerBogenschuetze = bbs;
		this.schwereKavallerie = sk;
		this.rammboecke = ramm;
		this.katapult = kata;
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
	
}
