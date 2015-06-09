package tribalwars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tribalwars.utils.RegexUtils;
import tribalwars.utils.VillagenameUtils;
import browser.CaptchaException;
import browser.SessionException;
import browser.WebBrowser;
import datastore.Database;

public class Account implements Runnable {

	private VillageList villages = new VillageList();
	private long lastLoginAttempt = 0;
	private long lastFarmRefresh = 0;
	private String username;
	private String password;
	private String worldPrefix;
	private String worldNumber;
	private boolean premium = false;
	private boolean accountManager = false;
	private boolean newReport = false;
	private WebBrowser browser;
	private Document document;

	private static Account myInstance = null;

	public static Account getInstance() throws IOException {
		if (myInstance == null) {
			throw new IOException("Account wurde noch nicht erstellt!");
		} else {
			return myInstance;
		}
	}

	public Account(String username, String password, String world, String worldNumber) {
		this.username = username;
		this.password = password;
		this.worldPrefix = world;
		this.worldNumber = worldNumber;

		new Thread(this, "GameLogic").start();
		myInstance = this;
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
			Logger.logMessage("Premium: \"" + this.hasPremium() + "\"");
			Logger.logMessage("Account-Manager: \"" + this.hasAccountManager() + "\"");

			// Get villages
			analyzeVillages();

			// Neue berichte seit dem letzten mal einlesen
			Logger.logMessage("Aktualisiere Berichte");
			Logger.logMessage(analyzeReports() + " Berichte gelesen!");

			while (true) {
				// Get farms for actual villages
				if ((System.currentTimeMillis() - this.lastFarmRefresh) > (60 * 60 * 1000)) { // Jede Stunde aktuelle Farmen holen
					Logger.logMessage("Aktualisiere Farmen");
					for (Village village : this.villages) {
						Logger.logMessage(analyzeFarms(village) + " Farmen für Dorf " + village.getDorfname() + " (" + village.getX() + "|" + village.getY() + ") hinzugef\u00FCgt.");
					}
					this.lastFarmRefresh = System.currentTimeMillis();
				}

				analyzeVillages(); // Ressourcen aktualisieren und überprüfen, ob der Spieler ein Dorf verloren hat.

				// Refresh reports
				if (this.hasNewReport()) {
					Logger.logMessage("Aktualisiere Berichte");
					Logger.logMessage(analyzeReports() + " Berichte gelesen!");
				}

				for (Village village : this.villages) {
					if (VillagenameUtils.getVillageDoFarming(village.getDorfname())) {
						if (village.isNextFarmattackPossible()) {
							// Farm
							// TODO Farm
						}
					}

					if (VillagenameUtils.getVillageBuildTroops(village.getDorfname())) {
						if (this.hasAccountManager()) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							// TODO refactor troup building (temp.txt)
						}
					}

					if (village.hasToResearch()) {
						if (this.hasAccountManager()) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							// TODO forschen
						}
					}

					if (VillagenameUtils.getVillageBuildBuildings(village.getDorfname())) {
						if (this.hasAccountManager()) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							if (village.isNextBuildingbuildPossible()) {
								// TODO refactor Baue Gebäude (temp1.txt)
							}
						}
					}

					Thread.sleep((long) ((Math.random() * 800) + 400)); // Pause zwischen 400 und 1200 Millisekunden
				}
				Thread.sleep((long) ((Math.random() * (12 * 1000)) + 3000)); // Pause zwischen 3 und 15 Sekunden
			}

		} else {
			throw new IOException("Ver\u00E4ndertes Loginsystem oder falsche Accountdaten!");
		}
	}

	/**
	 * Loggt sich ein.
	 *
	 * @return Gibt einen boolean zurück, der angibt, ob der login erfolgreich
	 *         war.
	 * @throws IOException Wenn keine Verbindung aufgebaut werden konnte.
	 * @throws CaptchaException Wenn der Botschutz aktiviert worden ist.
	 * @throws SessionException Wenn sich der Benutzer eingeloggt hat, und die
	 *             Session abgelaufen ist.
	 */
	private boolean login() throws IOException, CaptchaException, SessionException {
		this.browser.get("https://www.die-staemme.de/");
		this.document = Jsoup.parse(this.browser.post("https://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + this.username + "&password=" + this.password + "&clear=true"));
		Element passwordElement = this.document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		this.browser.post("https://www.die-staemme.de/index.php?action=login&server_" + this.worldPrefix + this.worldNumber, "user=" + this.username + "&password=" + passwordHash);
		this.document = Jsoup.parse(this.browser.get("https://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?screen=overview&intro"));
		if (this.document.getElementById("menu_counter_profile") != null) {
			JSONObject object = RegexUtils.getVillageJSONFromHead(this.document.head().html()).getJSONObject("player");
			this.setPremium(object.getBoolean("premium"));
			this.setAccountManager(object.getBoolean("account_manager"));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Ruft die Dörferübersicht auf und aktualisiert die aktuell vorhandenen
	 * Dörfer
	 *
	 * @throws IOException Wenn keine Verbindung aufgebaut werden konnte.
	 * @throws CaptchaException Wenn der Botschutz aktiviert worden ist.
	 * @throws SessionException Wenn sich der Benutzer eingeloggt hat, und die
	 *             Session abgelaufen ist.
	 */
	private void analyzeVillages() throws IOException, SessionException, CaptchaException {
		ArrayList<Village> newVillages = new ArrayList<Village>();

		this.document = Jsoup.parse(this.browser.get("https://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?screen=overview_villages" + (this.hasPremium() ? "&mode=prod" : "")));

		JSONObject object = RegexUtils.getVillageJSONFromHead(this.document.head().html());
		JSONObject player = object.getJSONObject("player");
		this.setNewReport(((player.getInt("new_report") > 0) ? true : false));
		this.setPremium(player.getBoolean("premium"));
		this.setAccountManager(player.getBoolean("account_manager"));
		object = null;
		player = null;

		Elements villageRows, tableDatas;
		if (this.hasPremium()) {
			villageRows = this.document.getElementById("production_table").getElementsByTag("tr");
			villageRows.remove(0); // Header
			for (Element villageRow : villageRows) {
				tableDatas = villageRow.getElementsByTag("td");
				long id = Long.parseLong(tableDatas.get(1).getElementsByTag("span").get(0).attr("data-id"));
				String dorfname = tableDatas.get(1).getElementsByTag("span").get(2).attr("data-text");
				int[] coords = RegexUtils.getCoordsFromVillagename(tableDatas.get(1).getElementsByTag("span").get(2).html());
				int holz = Integer.parseInt(tableDatas.get(3).getElementsByClass("wood").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int lehm = Integer.parseInt(tableDatas.get(3).getElementsByClass("stone").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int eisen = Integer.parseInt(tableDatas.get(3).getElementsByClass("iron").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int speicher = Integer.parseInt(tableDatas.get(4).html());
				int[] population = RegexUtils.getPopulationFromOverview(tableDatas.get(6).html());

				Village village = new Village(id, dorfname, coords[0], coords[1]);
				village.setHolz(holz);
				village.setLehm(lehm);
				village.setEisen(eisen);
				village.setSpeicher(speicher);
				village.setPopulation(population[0]);
				village.setMaximalPopulation(population[1]);

				newVillages.add(village);
			}
		} else {
			villageRows = this.document.getElementById("production_table").getElementsByTag("tr");
			villageRows.remove(0); // Header
			for (Element villageRow : villageRows) {
				tableDatas = villageRow.getElementsByTag("td");
				long id = Long.parseLong(tableDatas.get(0).getElementsByTag("span").get(0).attr("data-id"));
				String dorfname = tableDatas.get(0).getElementsByTag("span").get(0).getElementsByTag("span").get(0).getElementsByTag("a").get(0).getElementsByTag("span").get(0).attr("data-text");
				int[] coords = RegexUtils.getCoordsFromVillagename(tableDatas.get(0).html());
				int holz = Integer.parseInt(tableDatas.get(2).getElementsByClass("wood").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int lehm = Integer.parseInt(tableDatas.get(2).getElementsByClass("stone").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int eisen = Integer.parseInt(tableDatas.get(2).getElementsByClass("iron").get(0).html().replace("<span class=\"grey\">.</span>", ""));
				int speicher = Integer.parseInt(tableDatas.get(3).html());
				int[] population = RegexUtils.getPopulationFromOverview(tableDatas.get(4).html());

				Village village = new Village(id, dorfname, coords[0], coords[1]);
				village.setHolz(holz);
				village.setLehm(lehm);
				village.setEisen(eisen);
				village.setSpeicher(speicher);
				village.setPopulation(population[0]);
				village.setMaximalPopulation(population[1]);

				newVillages.add(village);
			}
		}
		this.villages.compareToNewList(newVillages);

		this.document = null;
		villageRows = null;
		tableDatas = null; // save memory
		newVillages = null; // save memory

		System.gc();
	}

	/**
	 * Durchsucht die Berichte nach möglichen neuen Berichten und gibt die
	 * Anzahl der gelesenen Berichte zurück
	 *
	 * @return Die anzahl der gelesenen Berichte
	 * @throws IOException Wenn keine Verbindung aufgebaut werden konnte.
	 * @throws CaptchaException Wenn der Botschutz aktiviert worden ist.
	 * @throws SessionException Wenn sich der Benutzer eingeloggt hat, und die
	 *             Session abgelaufen ist.
	 */
	private int analyzeReports() throws IOException, SessionException, CaptchaException {
		// TODO analyze Reports
		int counter = 0;

		Elements reportRows;

		if (this.hasPremium()) {
			int from = 0;
			outerloop: while (true) {
				this.document = Jsoup.parse(this.browser.get("https://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?mode=all&from=" + from + "&screen=report"));
				reportRows = this.document.getElementById("report_list").getElementsByTag("tr");
				reportRows.remove(0); // Header
				reportRows.remove(reportRows.size() - 1); // Select all

				for (Element reportRow : reportRows) {
					long idReport = Long.parseLong(reportRow.getElementsByTag("td").get(1).getElementsByTag("span").get(0).attr("data-id"));
					if (!analyzeReport(idReport)) {
						break outerloop;
					} else {
						counter += 1;
					}
				}

				from += reportRows.size();
			}
		} else {
			throw new IOException("Berichte ohne Premium-Account auslesen wurde noch nicht implementiert!");
		}

		reportRows = null;

		this.setNewReport(false);
		return counter;
	}

	/**
	 * Ruft den Bericht auf und speichert ihn in die Datenbank.
	 * 
	 * @param idReport Die Bericht ID
	 * @return true wenn er noch nicht in der Datenbank vorhanden war, false
	 *         wenn er bereits vorhanden war
	 * @throws IOException Wenn keine Verbindung aufgebaut werden konnte.
	 * @throws CaptchaException Wenn der Botschutz aktiviert worden ist.
	 * @throws SessionException Wenn sich der Benutzer eingeloggt hat, und die
	 *             Session abgelaufen ist.
	 */
	private boolean analyzeReport(long idReport) throws IOException, SessionException, CaptchaException {
		this.document = Jsoup.parse(this.browser.get("https://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?view=" + idReport + "&screen=report"));

		if (Database.containsReport(idReport)) {
			return false;
		}

		if (this.hasPremium()) {
			Element buildingSpyTable = this.document.getElementById("attack_spy_building_data");
			Element resourceSpyTable = this.document.getElementById("attack_spy_resources");
			if ((buildingSpyTable != null) && (resourceSpyTable != null)) {
				int wood = 0, stone = 0, iron = 0, wall = 0, spyedResources = 0;
				Date attackTime = RegexUtils.getTime(this.document.html());
				long idFarm = Long.parseLong(this.document.getElementsByClass("village_anchor").get(1).attr("data-id"));
				long idDefender = Long.parseLong(this.document.getElementsByClass("village_anchor").get(1).attr("data-player"));
				if (idDefender > 0) {
					Database.removeFarm(idFarm);
				}

				Elements resources = resourceSpyTable.getElementsByTag("tr").get(0).getElementsByClass("nowrap");
				for (Element resource : resources) {
					spyedResources += Integer.parseInt(resource.text().replace(".", ""));
				}

				JSONArray object = new JSONArray(buildingSpyTable.attr("value"));
				JSONObject temp;
				for (int i = 0; i < object.length(); i++) {
					temp = object.getJSONObject(i);
					switch (temp.getString("id")) {
					case "wood": {
						wood = temp.getInt("level");
						break;
					}
					case "stone": {
						stone = temp.getInt("level");
						break;
					}
					case "iron": {
						iron = temp.getInt("level");
						break;
					}
					case "wall": {
						wall = temp.getInt("level");
						break;
					}
					default:
						continue;
					}
				}

				Database.insertReport(idReport, idFarm, attackTime, spyedResources, wood, stone, iron, wall);
			} else {
				Database.insertReport(idReport);
			}

			buildingSpyTable = null; // save memory
			resourceSpyTable = null; // save memory
		} else {
			throw new IOException("Berichte ohne Premium-Account auslesen wurde noch nicht implementiert!");
		}

		return true;
	}

	/**
	 * Fügt alle möglichen Farmen der Datenbank hinzu. Quelle ist der Dorffinder
	 * von de.twstats.com
	 *
	 * @param x Ausgangsdorf X
	 * @param y Ausgangsdorf Y
	 * @return Die Anzahl der Farmen
	 */
	private int analyzeFarms(Village village) {
		Element widgetTable;
		Elements tableVillages, tableData;
		int counter = 0;

		try {
			this.document = Jsoup.connect("http://de.twstats.com/" + this.worldPrefix + this.worldNumber + "/index.php?page=village_locator&stage=3&source=village").data("village_coords", village.getX() + "|" + village.getY()).timeout(10 * 1000).post();
			widgetTable = this.document.getElementsByClass("widget").get(3);
			tableVillages = widgetTable.getElementsByTag("tr");
			tableVillages.remove(0); // Table header
			for (Element tableVillage : tableVillages) {
				tableData = tableVillage.getElementsByTag("td");

				if (tableData.get(6).getElementsByTag("a").get(0).html().compareTo("") == 0) {
					long farmID = RegexUtils.getIDFromTwStatsLink(tableData.get(5).getElementsByTag("a").get(0).attr("href"));
					int[] farmCoords = RegexUtils.getCoordsFromVillagename(tableData.get(5).getElementsByTag("a").get(0).html());

					Database.insertFarm(farmID, village.getID(), farmCoords[0], farmCoords[1]);
					counter += 1;
				}
			}
		} catch (Exception e) {
			Logger.logMessage("Fehler beim aktualisieren der Farmen!", e);
			System.exit(4);
		}

		this.document = null;
		widgetTable = null;
		tableVillages = null;
		tableData = null;

		return counter;
	}

	public Village[] getMyVillages() {
		return this.villages.toArray(new Village[this.villages.size()]);
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public void setAccountManager(boolean accountManager) {
		this.accountManager = accountManager;
	}

	public void setNewReport(boolean newReport) {
		this.newReport = newReport;
	}

	public boolean hasPremium() {
		return premium;
	}

	public boolean hasAccountManager() {
		return accountManager;
	}

	public boolean hasNewReport() {
		return newReport;
	}

}
