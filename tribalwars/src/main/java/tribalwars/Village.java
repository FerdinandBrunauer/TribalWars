package tribalwars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import browser.CaptchaException;
import browser.SessionException;

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

	/**
	 * Ruft die Dorfübersicht des Dorfes auf und gibt eine {@link HashMap} mit
	 * den aktuell vorhandenen Gebäudestufen zurück
	 *
	 * @param dorfID die DorfId des Dorfes
	 * @return {@link HashMap} mit den aktuellen Gebäudestufen
	 * @throws SessionException Wenn die Session abgelaufen ist und ein ReLogin
	 *             vorgenommen werden muss.
	 * @throws IOException Wenn die Verbindung zum Internet unterbrochen wird.
	 * @throws CaptchaException Wenn der Botschutz auftritt
	 */
	// TODO refactor village overview
	/*private HashMap<String, Integer> villageOverview(String dorfID) throws IOException, SessionException, CaptchaException {
		String head = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?village=" + dorfID + "&screen=overview")).head().html();
		HashMap<String, Integer> building = new HashMap<String, Integer>();

		JSONObject object = RegexUtils.getVillageJSONFromHead(head);
		JSONObject player = object.getJSONObject("player");
		JSONObject village = object.getJSONObject("village");
		JSONObject buildings = village.getJSONObject("buildings");

		this.newReport = (player.getInt("new_report") > 0) ? true : false;
		this.newMessage = (player.getInt("new_igm") > 0) ? true : false;
		this.premium = player.getBoolean("premium");
		this.accountManager = player.getBoolean("account_manager");

		building.put("main", buildings.getInt("main"));
		building.put("barracks", buildings.getInt("barracks"));
		building.put("stable", buildings.getInt("stable"));
		building.put("garage", buildings.getInt("garage"));
		building.put("snob", buildings.getInt("snob"));
		building.put("smith", buildings.getInt("smith"));
		building.put("place", buildings.getInt("place"));
		building.put("market", buildings.getInt("market"));
		building.put("wood", buildings.getInt("wood"));
		building.put("stone", buildings.getInt("stone"));
		building.put("iron", buildings.getInt("iron"));
		building.put("farm", buildings.getInt("farm"));
		building.put("storage", buildings.getInt("storage"));
		building.put("hide", buildings.getInt("hide"));
		building.put("wall", buildings.getInt("wall"));
		building.put("statue", buildings.getInt("statue"));

		Village actualVillage = this.villages.getVillage(buildings.getString("village"));
		actualVillage.setDorfname(village.getString("name"));
		actualVillage.setHolz(village.getInt("wood"));
		actualVillage.setLehm(village.getInt("stone"));
		actualVillage.setEisen(village.getInt("iron"));
		actualVillage.setSpeicher(village.getInt("storage_max"));
		actualVillage.setPopulation(village.getInt("pop"));
		actualVillage.setMaximalPopulation(village.getInt("pop_max"));

		head = null;
		village = null;
		player = null;
		buildings = null;

		System.gc();

		return building;
	}*/

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
