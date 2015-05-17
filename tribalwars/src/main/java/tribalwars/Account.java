package tribalwars;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import datastore.Database;
import browser.CaptchaException;
import browser.SessionException;
import browser.WebBrowser;

public class Account implements Runnable {

	private ArrayList<Village> myVillages = new ArrayList<Village>();
	private String username;
	private String password;
	private String welt;
	private String weltNummer;
	private boolean newMessage = false;
	private WebBrowser browser;
	private Document document;

	//private FarmVorlage[] vorlagen = { new FarmVorlage(Unit.Axt, 50), new FarmVorlage(Unit.LKAV, 7), new FarmVorlage(Unit.Speer, 20)};
	private FarmVorlage[] vorlagen = { new FarmVorlage(0, 0, 50, 0, 0, 0, 0, 0, 0, 0),
			new FarmVorlage(0, 0, 0, 0, 0, 7, 0, 0, 0, 0),
			new FarmVorlage(10, 10, 0, 0, 0, 0, 0, 0, 0, 0),
			new FarmVorlage(10, 0, 10, 0, 0, 0, 0, 0, 0, 0)};
	
	public Account(String username, String password, String world, String worldNumber) {
		this.username = username;
		this.password = password;
		this.welt = world;
		this.weltNummer = worldNumber;

		new Thread(this, "GameLogic").start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				gameLogic();
			} catch (IOException e) {
				return;
			} catch (CaptchaException e) {
				// TODO solve captcha
				return;
			} catch (SessionException e) {
				continue;
			}
		}
	}

	private void gameLogic() throws CaptchaException, SessionException, IOException {
		browser = new WebBrowser();
		if (login()) {
			// TODO when there are more villages in one account, then check the page
			// TODO check Bot-Security
			refreshVillages();
			for (Village village : myVillages) {
				village.completeRefresh();
				browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview_villages");
			}
			while (true) {
				for(Village currentVillage : myVillages) {
					// TODO check if the current village has to farm 
					document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview_villages");
					currentVillage.completeRefresh();
					List<Farm> farmen = Database.getFarms(currentVillage.getId(), true, true);
					List<Farm> farmed = new ArrayList<Farm>();
					if(currentVillage.farmPossible(vorlagen) && farmen.size() > 0)  {
						document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + currentVillage.getId() + "&screen=place");
						while(currentVillage.farmPossible(vorlagen) && farmen.size() > 0) {
							Element input = document.getElementById("units_form").getElementsByTag("input").get(0);
							String hashName = input.attr("name");
							String hashValue = input.attr("value");
							for(FarmVorlage vorlage : vorlagen) {
								if(currentVillage.canFarm(vorlage)) {							
									document = browser.POST("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + currentVillage.getId() + "&try=confirm&screen=place", hashName + "=" + 
											hashValue + "&template_id=&spear=" + vorlage.getSpeertraeger() + "&sword=" + vorlage.getSchwertkaempfer() + "&axe=" + vorlage.getAxtkaempfer() + 
											"&archer=" + vorlage.getBogenschuetzen() + "&spy=" + vorlage.getSpaeher() + "&light=" + vorlage.getLeichteKavallerie() + "&marcher=" + 
											vorlage.getBerittenerBogenschuetze() + "&heavy=" + vorlage.getSchwereKavallerie() + "&ram=" + vorlage.getRammboecke() + "&catapult=" + 
											vorlage.getKatapult() + "&snob=&x=" + farmen.get(0).x + "&y=" + farmen.get(0).y + "&target_type=coord&input=" +  farmen.get(0).x  + "%7C" +  
											farmen.get(0).y  + "&attack=Angreifen");
									Element actionForm = document.getElementById("command-confirm-form");
									String hWert = actionForm.attr("action").replace("/game.php?village=" + currentVillage.getId() + "&action=command&h=", "").replace("&screen=place", "");
									String chWert = actionForm.getElementsByAttributeValue("name", "ch").get(0).attr("value");
									String action_ID = actionForm.getElementsByAttributeValue("name", "action_id").get(0).attr("value");
									document = browser.POST("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + currentVillage.getId() + 
											"&action=command&h=" + hWert + "&screen=place", "attack=true&ch=" + chWert + "&x=" + farmen.get(0).x + "&y=" + farmen.get(0).y +
											"&action_id=" + action_ID + "&spear=" + vorlage.getSpeertraeger() + "&sword=" + vorlage.getSchwertkaempfer() + 
											"&axe=" + vorlage.getAxtkaempfer() + "&archer=" + vorlage.getBogenschuetzen() + "&spy=" + vorlage.getSpaeher() + "&light=" + 
											vorlage.getLeichteKavallerie() + "&marcher=" + vorlage.getBerittenerBogenschuetze() + "&heavy=" + vorlage.getSchwereKavallerie() + 
											"&ram=" + vorlage.getRammboecke() + "&catapult=" + vorlage.getKatapult() + "&snob=0");
									currentVillage.removeUnit(vorlage);
									farmed.add(farmen.get(0));
									farmen.remove(0);
									break;
								}
							}
						}
						Database.setFarmed(farmed);
					}
				}
				/*deprecated
				if (myVillages.size() > 1) {
					throw new IOException("Unsupported!");
				} else if (myVillages.size() == 1) {
					if (myVillages.get(0).farmPossible(vorlagen)) {
						//document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + myVillages.get(0).getId() + "&screen=overview");
						document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + myVillages.get(0).getId() + "&screen=am_farm");

						String hWert = Village.getHValue(document.head().html());
						Elements farms = document.getElementById("plunder_list").getElementsByTag("tr");
						for (int i = 1; i < farms.size(); i++) {
							// TODO read out vorlage id
						}
					}
				} else {
					throw new IOException("Fehler! Keine D\u00F6rfer!");
				}*/
			}
		} else {
			throw new IOException("Ver\u00E4ndertes Loginsystem oder falsche Accountdaten!");
		}
	}

	private boolean login() throws IOException, CaptchaException, SessionException {
		document = browser.GET("http://www.die-staemme.de/");
		document = browser.POST("http://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + username + "&password=" + password + "&clear=true");
		Element passwordElement = document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		System.out.println("Passworthash: \"" + passwordHash + "\"");
		document = browser.POST("http://www.die-staemme.de/index.php?action=login&server_" + welt + weltNummer, "user=" + username + "&password=" + passwordHash);
		document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview&intro");
		return document.getElementById("menu_counter_profile") != null;
	}
	
	private void refreshVillages() throws IOException, CaptchaException, SessionException {
		//TODO Check if you have lost a village thats save in the db
		myVillages = new ArrayList<Village>();
		//check Villages (not guaranted that it works)
		document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview_villages");
			Elements villages = document.getElementsByAttributeValue("class", "nowrap tooltip-delayed");
			Elements tempCoords = document.getElementsByTag("b");
			ArrayList<Point> coords = new ArrayList<Point>(); // TODO Queue verwenden
			//ArrayList<Point> coords = new ArrayList<Point>();
			for(Element possibleCoordElement : tempCoords) {
				if(possibleCoordElement.attr("class").compareTo("nowrap") == 0) {
					String value = possibleCoordElement.textNodes().get(0).getWholeText().split(" ")[0]; 
					String test = value.substring(1, value.length()-1);
					String[] tempCoord = test.split("\\|");
					coords.add(new Point(Integer.valueOf(tempCoord[0]), Integer.valueOf(tempCoord[1])));
				}
			}
			for(Element village:villages) {
				String villageName = village.textNodes().get(0).getWholeText();
				String villageId= village.attr("href").replace("/game.php?village=", "").replace("&screen=overview", "");
				Point coord = coords.get(0);
				Village temp = new Village(this, villageId, villageName, coord.x, coord.y);
				myVillages.add(temp);
				Database.addVillageToDatabase(temp);
				coords.remove(0);
			}
	}

	public Village[] getMyVillages() {
		return myVillages.toArray(new Village[myVillages.size()]);
	}

	public boolean hasNewMessage() {
		return newMessage;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public String getWelt() {
		return welt;
	}

	public String getWeltNummer() {
		return weltNummer;
	}

}
