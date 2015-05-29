package tribalwars;

import java.io.IOException;
import java.util.HashMap;

import logger.Logger;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tribalwars.utils.BuildingUtils;
import tribalwars.utils.RegexUtils;
import tribalwars.utils.VillagenameUtils;
import browser.CaptchaException;
import browser.SessionException;
import browser.WebBrowser;
import datastore.Database;
import datastore.memoryObjects.Village;

public class Account implements Runnable {

	private VillageList villages = new VillageList();
	private long lastLoginAttempt = 0;
	private String username;
	private String password;
	private String worldPrefix;
	private String worldNumber;
	private boolean newMessage = false;
	private WebBrowser browser;
	private Document document;

	public Account(String username, String password, String world, String worldNumber) {
		this.username = username;
		this.password = password;
		this.worldPrefix = world;
		this.worldNumber = worldNumber;

		new Thread(this, "GameLogic").start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				if ((System.currentTimeMillis() - this.lastLoginAttempt) > (5 * 60 * 1000)) {
					this.lastLoginAttempt = System.currentTimeMillis();
					gameLogic();
				}
			} catch (IOException e) {
				Logger.logMessage("Schwerer Fehler w\u00E4hrend des Ablaufes aufgetreten!", e);
				return;
			} catch (CaptchaException e) {
				Logger.logMessage("Botschutz aktiv!");
				return;
			} catch (SessionException e) {
				Logger.logMessage("Session abgelaufen. Relogin in 5 Minuten ...");
				try {
					Thread.sleep(5 * 60 * 1000);
				} catch (InterruptedException e1) {
					Logger.logMessage("Gamelogik wurde unterbrochen!", e1);
				}
				continue;
			} catch (InterruptedException e) {
				Logger.logMessage("Gamelogik wurde unterbrochen!", e);
				return;
			}

			try {
				Thread.sleep(100); // Save energy
			} catch (InterruptedException e) {
				Logger.logMessage("Gamelogik wurde unterbrochen!", e);
			}
		}
	}

	private void gameLogic() throws CaptchaException, SessionException, IOException, InterruptedException {
		this.browser = new WebBrowser();
		if (login()) {
			Logger.logMessage("Erfolgreich eingeloggt!");

			// Get villages
			analyzeVillages();
			// Get farms for actual villages
			for (Village village : this.villages) {
				Logger.logMessage(analyzeFarms(village.getX(), village.getY()) + " Farmen für Dorf " + village.getDorfname() + " (" + village.getX() + "|" + village.getY() + ") hinzugef\u00FCgt.");
			}
			// Refresh reports
			analyzeReports();

			while (true) {
				analyzeVillages(); // Ressourcen aktualisieren und überprüfen, ob der Spieler ein Dorf verloren hat.
				for (Village village : this.villages) {
					if (VillagenameUtils.getVillageDoFarming(village.getDorfname())) {
						if (village.isNextFarmattackPossible()) {
							// Farm	
							// TODO Farm
						}
					}
					if (VillagenameUtils.getVillageBuildTroops(village.getDorfname())) {
						// Baue Truppen
						// TODO Baue Truppen
						switch (VillagenameUtils.getVillageTroupType(village.getDorfname())) {
						case DEFF: {
							if (village.isNextTroupBuildBarracksPossible()) {

							}
							break;
						}
						case OFF: {
							if (village.isNextTroupBuildBarracksPossible()) {

							}
							if (village.isNextTroupBuildStablePossible()) {

							}
							if (village.isNextTroupBuildWorkshopPossible()) {

							}
							break;
						}
						default:
							// Nichts machen
							break;
						}
					}
					if (VillagenameUtils.getVillageBuildBuildings(village.getDorfname())) {
						if (village.isNextBuildingbuildPossible()) {
							// Baue Gebäude	
							HashMap<String, Integer> building = villageOverview(village.getID());
							String nextBuilding = BuildingUtils.calculateNextBuilding(2, building);
							if (nextBuilding != null) {
								// TODO Build
							} else {
								Logger.logMessage(village.getDorfname() + " ist vollst\u00E4ndig Ausgebaut! Bitte \u00E4ndern sie den Namen des Dorfes!");
							}
						}
					}

					Thread.sleep((long) ((Math.random() * 800) + 400)); // Pause zwischen 400 und 1200 millisekunden
				}
			}

		} else {
			throw new IOException("Ver\u00E4ndertes Loginsystem oder falsche Accountdaten!");
		}
	}

	private boolean login() throws IOException, CaptchaException, SessionException {
		this.browser.get("http://www.die-staemme.de/");
		this.document = Jsoup.parse(this.browser.post("http://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + this.username + "&password=" + this.password + "&clear=true"));
		Element passwordElement = this.document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		this.browser.post("http://www.die-staemme.de/index.php?action=login&server_" + this.worldPrefix + this.worldNumber, "user=" + this.username + "&password=" + passwordHash);
		this.document = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?screen=overview&intro"));
		return this.document.getElementById("menu_counter_profile") != null;
	}

	/**
	 * Ruft die Dörferübersicht auf und aktualisiert die aktuell vorhandenen
	 * Dörfer
	 */
	private void analyzeVillages() {
		// TODO analyze Villages (overview)
		// optimized logic
	}

	/**
	 * Durchsucht die Berichte nach möglichen neuen Berichten
	 */
	private void analyzeReports() {
		// TODO analyze Reports
		// TODO test where reports are, when not using farmassistant!

		// http://dep5.die-staemme.de/game.php?village=56872&mode=attack&group_id=-1&screen=report
		// http://dep5.die-staemme.de/game.php?village=56872&mode=attack&group_id=8382&screen=report
	}

	/**
	 * Fügt alle möglichen Farmen der Datenbank hinzu. Quelle ist der Dorffinder
	 * von de.twstats.com
	 * 
	 * @param x Ausgangsdorf X
	 * @param y Ausgangsdorf Y
	 * @return Die Anzahl der Farmen
	 */
	private int analyzeFarms(int x, int y) {
		Document document;
		Element widgetTable;
		Elements tableVillages, tableData;
		int counter = 0;

		try {
			document = Jsoup.connect("http://de.twstats.com/" + this.worldPrefix + this.worldNumber + "/index.php?page=village_locator&stage=3&source=village").data("village_coords", x + "|" + y).timeout(10 * 1000).post();
			widgetTable = document.getElementsByClass("widget").get(3);
			tableVillages = widgetTable.getElementsByTag("tr");
			tableVillages.remove(0); // Table header
			for (Element tableVillage : tableVillages) {
				tableData = tableVillage.getElementsByTag("td");

				if (tableData.get(6).getElementsByTag("a").get(0).html().compareTo("") == 0) {
					long id = RegexUtils.getIDFromTwStatsLink(tableData.get(5).getElementsByTag("a").get(0).attr("href"));
					int[] coords = RegexUtils.getCoordsFromVillagename(tableData.get(5).getElementsByTag("a").get(0).html());

					Database.insertVillage(id, coords[0], coords[1]);
					counter += 1;
				}
			}
		} catch (Exception e) {
			Logger.logMessage("Fehler beim aktualisieren der Farmen!", e);
			System.exit(4);
		}

		document = null;
		widgetTable = null;
		tableVillages = null;
		tableData = null;

		return counter;
	}

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
	private HashMap<String, Integer> villageOverview(String dorfID) throws IOException, SessionException, CaptchaException {
		String head = Jsoup.parse(browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?village=" + dorfID + "&screen=overview")).head().html();
		HashMap<String, Integer> building = new HashMap<String, Integer>();

		JSONObject village = RegexUtils.getJsonFromHead(head).getJSONObject("village");
		JSONObject buildings = village.getJSONObject("buildings");

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

		Village actualVillage = villages.getVillage(buildings.getString("village"));
		actualVillage.setDorfname(village.getString("name"));
		actualVillage.setHolz(village.getInt("wood"));
		actualVillage.setLehm(village.getInt("stone"));
		actualVillage.setEisen(village.getInt("iron"));
		actualVillage.setSpeicher(village.getInt("storage_max"));

		return building;
	}

	public Village[] getMyVillages() {
		return this.villages.toArray(new Village[this.villages.size()]);
	}

	public boolean hasNewMessage() {
		return this.newMessage;
	}

}
