package tribalwars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tribalwars.storage.Building;
import tribalwars.storage.Unit;
import tribalwars.utils.RegexUtils;
import browser.CaptchaException;
import browser.SessionException;
import datastore.Database;
import datastore.memoryObject.Farm;

public class Village {

	private final long id;
	private String dorfname;
	public final int x;
	private final int y;
	private int holz = 0;
	private int lehm = 0;
	private int eisen = 0;
	private int speicher = 0;
	private int population = 0;
	private int maximalPopulation = 0;
	private ArrayList<String> needResearch = new ArrayList<String>();
	private HashMap<Unit, Integer> units = new HashMap<Unit, Integer>();
	private HashMap<Building, Integer> buildings = new HashMap<Building, Integer>();
	private Date nextBuildingbuildPossible = new Date();
	private Date nextTroupBuildBarracksPossible = new Date();
	private Date nextTroupBuildStablePossible = new Date();
	private Date nextTroupBuildWorkshopPossible = new Date();
	private Date nextFarmattackPossible = new Date();

	public Village(long id, String dorfname, int x, int y) {
		this.id = id;
		this.dorfname = dorfname;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Village) {
			Village village = (Village) object;
			if (village.getID() == getID()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	};

	private void villageOverview() throws IOException, SessionException, CaptchaException {
		Account account = Account.getInstance();

		account.document = Jsoup.parse(Account.getInstance().browser.get("https://" + account.getWorldPrefix() + account.getWorldNumber() + ".die-staemme.de/game.php?village=" + getID() + "&screen=overview"));

		JSONObject headerJson = RegexUtils.getJSONFromHead(account.document.head().html());
		JSONObject playerJson = headerJson.getJSONObject("player");
		JSONObject villageJson = headerJson.getJSONObject("village");
		JSONObject buildingsJson = villageJson.getJSONObject("buildings");

		account.setNewReport((playerJson.getInt("new_report") > 0) ? true : false);
		account.setPremium(playerJson.getBoolean("premium"));
		account.setAccountManager(playerJson.getBoolean("account_manager"));

		// Gebäude
		this.buildings.put(Building.Hauptgebaeude, buildingsJson.getInt("main"));
		this.buildings.put(Building.Kaserne, buildingsJson.getInt("barracks"));
		this.buildings.put(Building.Stall, buildingsJson.getInt("stable"));
		this.buildings.put(Building.Werkstatt, buildingsJson.getInt("garage"));
		this.buildings.put(Building.Adelshof, buildingsJson.getInt("snob"));
		this.buildings.put(Building.Schmiede, buildingsJson.getInt("smith"));
		this.buildings.put(Building.Versammlungsplatz, buildingsJson.getInt("place"));
		this.buildings.put(Building.Statue, buildingsJson.getInt("statue"));
		this.buildings.put(Building.Marktplatz, buildingsJson.getInt("market"));
		this.buildings.put(Building.Holzfaeller, buildingsJson.getInt("wood"));
		this.buildings.put(Building.Lehmgrube, buildingsJson.getInt("stone"));
		this.buildings.put(Building.Eisenmine, buildingsJson.getInt("iron"));
		this.buildings.put(Building.Bauernhof, buildingsJson.getInt("farm"));
		this.buildings.put(Building.Speicher, buildingsJson.getInt("storage"));
		this.buildings.put(Building.Versteck, buildingsJson.getInt("hide"));
		this.buildings.put(Building.Wall, buildingsJson.getInt("wall"));

		// Truppen
		if (account.hasPremium()) {
			Elements troupRows = account.document.getElementById("show_units").getElementsByTag("tr");
			troupRows.remove(troupRows.size() - 1); // rekruit Link

			this.units.clear();
			for (Element troupRow : troupRows) {
				Unit unit = Unit.fromString(troupRow.getElementsByTag("a").get(0).attr("data-unit"));
				int count = Integer.parseInt(troupRow.getElementsByTag("strong").get(0).html());
				this.units.put(unit, count);
			}
			troupRows = null;
		} else {
			throw new IOException("Truppen auslesen ohne Premium noch nicht implementiert!");
			// wahrscheinlich ist nur kein rekrutieren eintrag vorhanden, Kontrollieren bitte!
		}

		// Rohstoffe, Bauernhofplätze, Speicher
		setDorfname(villageJson.getString("name"));
		setHolz(villageJson.getInt("wood"));
		setLehm(villageJson.getInt("stone"));
		setEisen(villageJson.getInt("iron"));
		setSpeicher(villageJson.getInt("storage_max"));
		setPopulation(villageJson.getInt("pop"));
		setMaximalPopulation(villageJson.getInt("pop_max"));

		// TODO first return for next possible Attack

		// save memory
		headerJson = null;
		playerJson = null;
		villageJson = null;
		buildingsJson = null;
	}

	public void sendFarmTroops() throws IOException, SessionException, CaptchaException {
		villageOverview();

		ArrayList<Farm> farms = Database.getFarms(getID());
		Collections.sort(farms, new Comparator<Farm>() {
			@Override
			public int compare(Farm o1, Farm o2) {
				return Double.compare(o1.getResourcesPerDistance(), o2.getResourcesPerDistance());
			}
		});

		// TODO sendFarmTroops
	}

	public long getID() {
		return this.id;
	}

	public void setDorfname(String dorfname) {
		this.dorfname = dorfname;
	}

	public String getDorfname() {
		return this.dorfname;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getHolz() {
		return this.holz;
	}

	public int getLehm() {
		return this.lehm;
	}

	public int getEisen() {
		return this.eisen;
	}

	public int getSpeicher() {
		return this.speicher;
	}

	public void setHolz(int holz) {
		this.holz = holz;
	}

	public void setLehm(int lehm) {
		this.lehm = lehm;
	}

	public void setEisen(int eisen) {
		this.eisen = eisen;
	}

	public void setSpeicher(int speicher) {
		this.speicher = speicher;
	}

	public boolean isNextBuildingbuildPossible() {
		return new Date().after(this.nextBuildingbuildPossible);
	}

	public void setNextBuildingbuildPossible(Date nextBuildingbuildPossible) {
		this.nextBuildingbuildPossible = nextBuildingbuildPossible;
	}

	public Date getNextBuildingbuildPossible() {
		return this.nextBuildingbuildPossible;
	}

	public boolean isNextTroupBuildBarracksPossible() {
		return new Date().after(this.nextTroupBuildBarracksPossible);
	}

	public void setNextTroupBuildBarracksPossible(Date nextTroupBuildBarracksPossible) {
		this.nextTroupBuildBarracksPossible = nextTroupBuildBarracksPossible;
	}

	public Date getNextTroupBuildBarracks() {
		return this.nextTroupBuildBarracksPossible;
	}

	public boolean isNextTroupBuildStablePossible() {
		return new Date().after(this.nextTroupBuildStablePossible);
	}

	public void setNextTroupBuildStablePossible(Date nextTroupBuildStablePossible) {
		this.nextTroupBuildStablePossible = nextTroupBuildStablePossible;
	}

	public Date getNextTroupBuildStable() {
		return this.nextTroupBuildStablePossible;
	}

	public boolean isNextTroupBuildWorkshopPossible() {
		return new Date().after(this.nextTroupBuildWorkshopPossible);
	}

	public void setNextTroupBuildWorkshopPossible(Date nextTroupBuildWorkshopPossible) {
		this.nextTroupBuildWorkshopPossible = nextTroupBuildWorkshopPossible;
	}

	public Date getNextTroupBuildWorkshop() {
		return this.nextTroupBuildWorkshopPossible;
	}

	public boolean isNextFarmattackPossible() {
		return new Date().after(this.nextFarmattackPossible);
	}

	public void setNextFarmattackPossible(Date nextFarmattackPossible) {
		this.nextFarmattackPossible = nextFarmattackPossible;
	}

	public int getPopulation() {
		return this.population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getMaximalPopulation() {
		return this.maximalPopulation;
	}

	public void setMaximalPopulation(int maximalPopulation) {
		this.maximalPopulation = maximalPopulation;
	}

	public void addResearchOrder(String unit) {
		if (!this.needResearch.contains(unit)) {
			this.needResearch.add(unit);
		}
	}

	public String getNextResearchOrder() {
		if (this.needResearch.size() > 0) {
			return this.needResearch.remove(0);
		} else {
			return null;
		}
	}

	public boolean hasToResearch() {
		return this.needResearch.size() > 0;
	}

}
