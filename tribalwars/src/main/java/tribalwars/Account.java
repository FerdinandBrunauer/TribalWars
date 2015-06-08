package tribalwars;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
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
	private long lastFarmRefresh = 0;
	private String username;
	private String password;
	private String worldPrefix;
	private String worldNumber;
	private boolean premium = false;
	private boolean accountManager = false;
	private boolean newMessage = false;
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

		// new Thread(this, "GameLogic").start();
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
						Logger.logMessage(analyzeFarms(village.getX(), village.getY()) + " Farmen für Dorf " + village.getDorfname() + " (" + village.getX() + "|" + village.getY() + ") hinzugef\u00FCgt.");
					}
					this.lastFarmRefresh = System.currentTimeMillis();
				}

				analyzeVillages(); // Ressourcen aktualisieren und überprüfen, ob der Spieler ein Dorf verloren hat.

				// Refresh reports
				if (this.newReport) {
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
						if (this.accountManager) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							// TODO refactor troup building (temp.txt)
						}
					}

					if (village.hasToResearch()) {
						if (this.accountManager) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							// TODO forschen
						}
					}

					if (VillagenameUtils.getVillageBuildBuildings(village.getDorfname())) {
						if (this.accountManager) { // Wenn der accountManager aktiv ist, wird nichts gebaut
							if (village.isNextBuildingbuildPossible()) {
								// TODO refactor Baue Gebäude
								HashMap<String, Integer> building = villageOverview(village.getID());
								String nextBuilding = BuildingUtils.calculateNextBuilding(2, building);
								if (nextBuilding != null) {
									Document mainOverview = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?village=" + village.getID() + "&screen=main"));
									Element buildqueue = mainOverview.getElementById("buildqueue");
									if (buildqueue != null) {
										String timer = buildqueue.getElementsByClass("timer").get(0).html();
										village.setNextBuildingbuildPossible(new Date(System.currentTimeMillis() + RegexUtils.convertTimestringToMilliseconds(timer)));

										String actualBuildingBuilding = buildqueue.getElementsByClass("lit-item").get(0).getElementsByTag("img").get(0).attr("title");
										Logger.logMessage("\"" + village.getDorfname() + "\" baut noch bis um " + new SimpleDateFormat("HH:mm:ss").format(village.getNextBuildingbuildPossible()) + " an " + actualBuildingBuilding);
									} else {
										Element buildrow = mainOverview.getElementById("main_buildrow_" + nextBuilding);
										if (buildrow != null) {
											if (buildrow.getElementsByTag("td").get(6).getElementsByClass("inactive").size() < 1) { // buildable
												long time = RegexUtils.convertTimestringToMilliseconds(buildrow.getElementsByTag("td").get(4).html());
												String hWert = RegexUtils.getHWert(mainOverview.head().html());

												ArrayList<SimpleEntry<String, String>> additionalHeader = new ArrayList<SimpleEntry<String, String>>();
												additionalHeader.add(new SimpleEntry<String, String>("TribalWars-Ajax", "1"));
												this.browser.post(
														"http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?village=" + village.getID() + "&ajaxaction=upgrade_building&h=" + hWert + "&type=main&screen=main&&client_time=" + (System.currentTimeMillis() + "").substring(0, 10),
														"id=" + nextBuilding + "&force=1&destroy=0&source=" + village.getID(), additionalHeader);
												Logger.logMessage("Auftrag: \"" + village.getDorfname() + "\" baut nun an " + BuildingUtils.getFullBuildingname(nextBuilding) + ".");
												village.setNextBuildingbuildPossible(new Date(System.currentTimeMillis() + time));
											} else {
												village.setNextBuildingbuildPossible(new Date(System.currentTimeMillis() + (5 * 60 * 1000)));
												// In 5 Minuten noch einmal schauen
											}
										}
									}
								} else {
									Logger.logMessage("\"" + village.getDorfname() + "\" ist vollst\u00E4ndig Ausgebaut! Bitte \u00E4ndern sie den Namen des Dorfes!");
									village.setNextBuildingbuildPossible(new Date(System.currentTimeMillis() + (30 * 60 * 1000)));
								}
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
		this.browser.get("http://www.die-staemme.de/");
		this.document = Jsoup.parse(this.browser.post("http://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + this.username + "&password=" + this.password + "&clear=true"));
		Element passwordElement = this.document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		this.browser.post("http://www.die-staemme.de/index.php?action=login&server_" + this.worldPrefix + this.worldNumber, "user=" + this.username + "&password=" + passwordHash);
		this.document = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?screen=overview&intro"));
		if (this.document.getElementById("menu_counter_profile") != null) {
			JSONObject object = RegexUtils.getVillageJSONFromHead(this.document.head().html()).getJSONObject("player");
			this.premium = object.getBoolean("premium");
			this.accountManager = object.getBoolean("account_manager");
			Logger.logMessage("Premium: \"" + this.premium + "\"");
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

		this.document = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?screen=overview_villages"));

		JSONObject object = RegexUtils.getVillageJSONFromHead(this.document.head().html());
		JSONObject player = object.getJSONObject("player");
		this.newReport = (player.getInt("new_report") > 0) ? true : false;
		this.newMessage = (player.getInt("new_igm") > 0) ? true : false;
		this.premium = player.getBoolean("premium");
		this.accountManager = player.getBoolean("account_manager");
		object = null;
		player = null;

		Elements villageRows = this.document.getElementById("production_table").getElementsByTag("tr");
		villageRows.remove(0); // Header
		Elements tableDatas;
		for (Element villageRow : villageRows) {
			tableDatas = villageRow.getElementsByTag("td");
			String id = tableDatas.get(0).getElementsByTag("span").get(0).attr("data-id");
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

		/* Elements reports;
		Element spyedResources;
		String json;
		int pager = 0;
		boolean paginate = true;
		while (paginate) {
			this.document = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?mode=attack&from=" + pager + "&screen=report"));
			reports = this.document.getElementById("report_list").getElementsByTag("tr");
			reports.remove(0); // Header
			reports.remove(reports.size() - 1); // check all
			System.out.println("Size: " + reports.size());
			if (reports.size() < 1) {
				paginate = false;
				break;
			}
			for (Element report : reports) {
				long idReport = Long.parseLong(report.getElementsByClass("quickedit").get(0).attr("data-id"));
				if (idReport <= this.lastReadReportID) {
					paginate = false;
					break;
				} else {
					this.document = Jsoup.parse(this.browser.get("http://" + this.worldPrefix + this.worldNumber + ".die-staemme.de/game.php?mode=attack&view=" + idReport + "&screen=report"));
					spyedResources = this.document.getElementById("attack_spy_resources");
					if (spyedResources != null) {
						json = this.document.getElementById("attack_spy_building_data").attr("value");
						if ((json != null) && (json.compareTo("") != 0)) {
							int spyedWood = Integer.parseInt(spyedResources.getElementsByClass("nowrap").get(0).text().replace(".", ""));
							int spyedStone = Integer.parseInt(spyedResources.getElementsByClass("nowrap").get(1).text().replace(".", ""));
							int spyedIron = Integer.parseInt(spyedResources.getElementsByClass("nowrap").get(2).text().replace(".", ""));

							Date attackTime = RegexUtils.getTime(this.document.html());
							long idVillage = Long.parseLong(this.document.getElementsByClass("village_anchor").get(1).attr("data-id"));

							int wood = RegexUtils.getBuildinglevelFromReport(json, "wood");
							int stone = RegexUtils.getBuildinglevelFromReport(json, "stone");
							int iron = RegexUtils.getBuildinglevelFromReport(json, "iron");
							int wall = RegexUtils.getBuildinglevelFromReport(json, "wall");

							Database.insertReport(idReport, idVillage, attackTime, spyedWood, spyedStone, spyedIron, wood, stone, iron, wall);
							counter += 1;
						}
					}
				}
				pager += 1;
			}
		}

		// http://dep5.die-staemme.de/game.php?mode=attack&group_id=8382&screen=report*/
		this.newReport = false;
		return counter;
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
		Element widgetTable;
		Elements tableVillages, tableData;
		int counter = 0;

		try {
			this.document = Jsoup.connect("http://de.twstats.com/" + this.worldPrefix + this.worldNumber + "/index.php?page=village_locator&stage=3&source=village").data("village_coords", x + "|" + y).timeout(10 * 1000).post();
			widgetTable = this.document.getElementsByClass("widget").get(3);
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

		this.document = null;
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
	}

	private long getRemainingBuildtime() {
		try {
			Elements table = this.document.getElementsByClass("trainqueue_wrap").get(0).getElementsByTag("tr");
			if (table.size() > 0) {
				table.remove(0);
				return RegexUtils.convertTimestringToMilliseconds(table.get(0).getElementsByTag("td").get(1).html());
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public Village[] getMyVillages() {
		return this.villages.toArray(new Village[this.villages.size()]);
	}

	public boolean hasNewMessage() {
		return this.newMessage;
	}

}
