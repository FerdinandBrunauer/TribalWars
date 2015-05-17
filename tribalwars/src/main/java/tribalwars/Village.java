package tribalwars;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import browser.CaptchaException;
import browser.SessionException;

public class Village {

	private Account account;
	private String id = "";
	private String name = "";
	private int x = 0;
	private int y = 0;
	private int holz = 0;
	private int lehm = 0;
	private int eisen = 0;
	private int speicherKapazitaet = 0;
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
	private int speicher;
	private int versteck = 0;
	private int wall = 0;
	private Date nextAttackReturn = new Date();
	private Date nextBuildingOrder = new Date();

	public Village(Account account, String id, String name, int x, int y) {
		this.account = account;
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
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

	public void completeRefresh() throws IOException, CaptchaException, SessionException {
		Document document = account.getBrowser().GET("http://" + account.getWelt() + account.getWeltNummer() + ".die-staemme.de/game.php?village=" + id + "&screen=overview");
		completeRefresh(document);
	}
	public void completeRefresh(Document document) throws IOException, CaptchaException, SessionException {
		holz = Integer.parseInt(document.getElementById("wood").html());
		lehm = Integer.parseInt(document.getElementById("stone").html());
		eisen = Integer.parseInt(document.getElementById("iron").html());
		speicherKapazitaet = Integer.parseInt(document.getElementById("storage").html());

		String input = document.head().html();
		hauptgebaeude = getBuildingFromString(input, "main");
		kaserne = getBuildingFromString(input, "barracks");
		stall = getBuildingFromString(input, "stable");
		werkstatt = getBuildingFromString(input, "garage");
		adelshof = getBuildingFromString(input, "snob");
		schmiede = getBuildingFromString(input, "smith");
		versammlungsplatz = getBuildingFromString(input, "place");
		marktplatz = getBuildingFromString(input, "market");
		holzfaeller = getBuildingFromString(input, "wood");
		lehmgrube = getBuildingFromString(input, "stone");
		eisenmine = getBuildingFromString(input, "iron");
		bauernhof = getBuildingFromString(input, "farm");
		speicher = getBuildingFromString(input, "storage");
		versteck = getBuildingFromString(input, "hide");
		wall = getBuildingFromString(input, "wall");
		input = document.getElementById("content_value").html();
		speertraeger = getUnitFromString(input, "Speer");
		schwertkaempfer = getUnitFromString(input, "Schwert");
		axtkaempfer = getUnitFromString(input, "Axt");
		bogenschuetzen = getUnitFromString(input, "Bogensch\u00fctze");
		spaeher = getUnitFromString(input, "Sp\u00e4h");
		leichteKavallerie = getUnitFromString(input, "LKAV");
		berittenerBogenschuetze = getUnitFromString(input, "BBOgen");
		schwereKavallerie = getUnitFromString(input, "SKAV");
		rammboecke = getUnitFromString(input, "Ramme");
		katapult = getUnitFromString(input, "Kata");

		Element troops = document.getElementById("show_outgoing_units");
		Elements attacks = troops.getElementsByTag("tr");
		Element attack;
		for (int i = 1; i < attacks.size(); i++) {
			attack = attacks.get(i);
			if (attack.html().contains("data-command-type=\"return\"")) {
				nextAttackReturn = new Date(getAttackReturnTime(attack.html()));
				break;
			}
		}

		// TODO Building is not working
		/* String nextBuilding = Utils.calculateNextBuilding(2, this);
		if (nextBuilding != null) {
			document = account.getBrowser().GET("http://de" + account.getWelt() + ".die-staemme.de/game.php?village=" + id + "&screen=main");
			Element buildRow = document.getElementById("main_buildrow_" + nextBuilding);
			if (buildPossible(buildRow.html())) {
				String hWert = getHValueForBuild(buildRow.html());
				System.out.println(account.getBrowser().GET("http://de" + account.getWelt() + ".die-staemme.de/game.php?village=" + id + "&ajaxaction=upgrade_building&h=" + hWert + "&type=main&screen=main&&client_time=" + getTimestamp()));
				System.out.println("H-Wert: \"" + hWert + "\"");
				// http://de116.die-staemme.de/game.php?village=17105&ajaxaction=upgrade_building&h=a6b1f2a0&type=main&screen=main&&client_time=1430752527

				Database.logBuilding(name, x, y, nextBuilding, getBuildToLevel(buildRow.html())); // TODO get nice buildingname from database
			} else {
				// TODO read time
			}
		} else {
			nextBuildingOrder = new Date(2000000000); // no more buildings to buy
		} */
	}

	public static String getHValue(String tableRow) throws IOException {
		Pattern pattern = Pattern.compile(";h=([a-zA-Z0-9]{7,9})&amp;");
		Matcher matcher = pattern.matcher(tableRow);
		matcher.find();
		try {
			return matcher.group(1);
		} catch (Exception e) {
			throw new IOException("Konnte H - Wert für den Bau eines Gebäudes nicht finden!");
		}
	}

	public static boolean buildPossible(String tableRow) {
		Pattern pattern = Pattern.compile("class=\\\"(cost_wood|cost_stone|cost_iron) warn\\\"");
		Matcher matcher = pattern.matcher(tableRow);
		return !matcher.find();
	}

	public static long getAttackReturnTime(String input) {
		Pattern pattern = Pattern.compile("data-endtime=\\\"(\\d{10})\\\"");
		Matcher matcher = pattern.matcher(input);
		matcher.find();
		try {
			return Long.parseLong(matcher.group(1) + "000");
		} catch (Exception e) {
			return 0L;
		}
	}

	public static int getBuildingFromString(String input, String jsonKey) {
		Pattern pattern = Pattern.compile("\\\"" + jsonKey + "\\\"\\:\\\"(\\d{1,2})\\\"");
		Matcher matcher = pattern.matcher(input);
		matcher.find();
		try {
			return Integer.parseInt(matcher.group(1));
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getUnitFromString(String input, String jsonKey) {
		Pattern pattern = Pattern.compile("\\\"shortname\\\":\\\"" + jsonKey + "\\\",\\\"count\\\":\\\"(\\d{1,5})\\\"");
		Matcher matcher = pattern.matcher(input);
		matcher.find();
		try {
			return Integer.parseInt(matcher.group(1));
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean farmPossible(FarmVorlage[] vorlagen) throws IOException, CaptchaException, SessionException {
		Date now = new Date();
		if (now.after(nextAttackReturn)) {
			completeRefresh();
		}

		for (FarmVorlage vorlage : vorlagen) {
			if(vorlage.getSpeertraeger() <= speertraeger &&
			vorlage.getSchwertkaempfer() <= schwertkaempfer &&
			vorlage.getAxtkaempfer() <= axtkaempfer &&
			vorlage.getBogenschuetzen() <= bogenschuetzen &&
			vorlage.getSpaeher() <= spaeher &&
			vorlage.getLeichteKavallerie() <= leichteKavallerie &&
			vorlage.getBerittenerBogenschuetze() <= berittenerBogenschuetze &&
			vorlage.getSchwereKavallerie() <= schwereKavallerie &&
			vorlage.getRammboecke() <= rammboecke &&
			vorlage.getKatapult() <= katapult) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canFarm(FarmVorlage vorlage) {
		if(vorlage.getSpeertraeger() <= speertraeger &&
		vorlage.getSchwertkaempfer() <= schwertkaempfer &&
		vorlage.getAxtkaempfer() <= axtkaempfer &&
		vorlage.getBogenschuetzen() <= bogenschuetzen &&
		vorlage.getSpaeher() <= spaeher &&
		vorlage.getLeichteKavallerie() <= leichteKavallerie &&
		vorlage.getBerittenerBogenschuetze() <= berittenerBogenschuetze &&
		vorlage.getSchwereKavallerie() <= schwereKavallerie &&
		vorlage.getRammboecke() <= rammboecke &&
		vorlage.getKatapult() <= katapult) {
			return true;
		} else {
			return false;
		}
	}
	
	public void removeUnit(FarmVorlage vorlage) {
		speertraeger -= vorlage.getSpeertraeger();
		schwertkaempfer -= vorlage.getSchwertkaempfer();
		axtkaempfer -= vorlage.getAxtkaempfer();
		bogenschuetzen -= vorlage.getBogenschuetzen(); 
		spaeher -= vorlage.getSpaeher();
		leichteKavallerie -= vorlage.getLeichteKavallerie();
		berittenerBogenschuetze -= vorlage.getBerittenerBogenschuetze();
		schwereKavallerie -= vorlage.getSchwereKavallerie();
		rammboecke -= vorlage.getRammboecke();
		katapult -= vorlage.getKatapult();
	}
	
	public int getUnitCount(Unit unit) {
		switch (unit) {
		case Speer:
			return getSpeertraeger();
		case Schwert:
			return getSchwertkaempfer();
		case Axt:
			return getAxtkaempfer();
		case Bogen:
			return getBogenschuetzen();
		case Spaeher:
			getSpaeher();
		case LKAV:
			return getLeichteKavallerie();
		case BBOGEN:
			return getBogenschuetzen();
		case SKAV:
			return getSchwereKavallerie();
		case Rammen:
			return getRammboecke();
		case Kata:
			return getKatapult();
		}
		// TODO log message
		return 0;
	}

	public static String getTimestamp() {
		return (System.currentTimeMillis() + "").substring(0, (System.currentTimeMillis() + "").length() - 3);
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

	public int getSpeicherKapazitaet() {
		return speicherKapazitaet;
	}
}
