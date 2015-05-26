package tribalwars;

import java.io.IOException;
import java.util.ArrayList;

import logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import datastore.Database;
import browser.CaptchaException;
import browser.SessionException;
import browser.WebBrowser;

public class Account implements Runnable {

	private ArrayList<Village> villages = new ArrayList<Village>();
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
				gameLogic();
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
		}
	}

	private void gameLogic() throws CaptchaException, SessionException, IOException, InterruptedException {
		browser = new WebBrowser();
		if (login()) {
			analyzeVillages();
			for (Village village : villages) {
				Logger.logMessage(analyzeFarms(village.getX(), village.getY()) + " Farmen für Dorf " + village.getName() + " (" + village.getX() + "|" + village.getY() + ") hinzugef\u00FCgt.");
			}

			analyzeReports();
		} else {
			throw new IOException("Ver\u00E4ndertes Loginsystem oder falsche Accountdaten!");
		}
	}

	private boolean login() throws IOException, CaptchaException, SessionException {
		browser.get("http://www.die-staemme.de/");
		document = Jsoup.parse(browser.post("http://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + username + "&password=" + password + "&clear=true"));
		Element passwordElement = document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		browser.post("http://www.die-staemme.de/index.php?action=login&server_" + worldPrefix + worldNumber, "user=" + username + "&password=" + passwordHash);
		document = Jsoup.parse(browser.get("http://" + worldPrefix + worldNumber + ".die-staemme.de/game.php?screen=overview&intro"));
		return document.getElementById("menu_counter_profile") != null;
	}

	/**
	 * Ruft die Dorfübersicht auf und aktualisiert die aktuell vorhandenen Dörfer
	 */
	private void analyzeVillages() {
		// optimized logic
	}

	/**
	 * Durchsucht die Berichte nach möglichen neuen
	 */
	private void analyzeReports() {
		// read and insert into database

		// http://dep5.die-staemme.de/game.php?village=56872&mode=attack&group_id=-1&screen=report
		// http://dep5.die-staemme.de/game.php?village=56872&mode=attack&group_id=8382&screen=report
	}

	/**
	 * Fügt alle möglichen Farmen der Datenbank hinzu
	 * 
	 * @param x Ausgangsdorf X
	 * @param y Ausgangsdorf Y
	 * @return Die Anzahl der Farmen
	 */
	private int analyzeFarms(int x, int y) {
		// TODO test

		Document document;
		Element widgetTable;
		Elements tableVillages, tableData;
		int counter = 0;

		try {
			document = Jsoup.connect("http://de.twstats.com/" + worldPrefix + worldNumber + "/index.php?page=village_locator&stage=3&source=village").data("village_coords", x + "|" + y).timeout(10 * 1000).post();
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

	public Village[] getMyVillages() {
		return villages.toArray(new Village[villages.size()]);
	}

	public boolean hasNewMessage() {
		return newMessage;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public String getWelt() {
		return worldPrefix;
	}

	public String getWeltNummer() {
		return worldNumber;
	}

}
