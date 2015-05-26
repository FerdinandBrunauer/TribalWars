package tribalwars;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private FarmVorlage[] vorlagen = { new FarmVorlage(0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0),
			new FarmVorlage(0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0),
			new FarmVorlage(0, 10, 0, 10, 0, 0, 0, 0, 0, 0, 0)};
	
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
			} catch (InterruptedException e) {
				//do nothing
			}
		}
	}

	private void gameLogic() throws CaptchaException, SessionException, IOException, InterruptedException {
		browser = new WebBrowser();
		if (login()) {
			// TODO check Bot-Security (done but not tested)
			refreshVillages();
			//<---Abgleich zwischen DB und myVillages--->
			Map<String, Village> dbVillages = Database.getVillages();
			List<Village> overviewVillages = (List<Village>) myVillages.clone(); 
			int i = 0;
			while (i < overviewVillages.size()) {
				if(dbVillages.get(overviewVillages.get(i).getId()) != null) {
					if(dbVillages.get(overviewVillages.get(i).getId()).getName().compareTo(overviewVillages.get(i).getName()) != 0) {
						Database.addVillageToDatabase(overviewVillages.get(i));
					}
					dbVillages.remove(overviewVillages.get(i).getId());
					overviewVillages.remove(i);
				} else {
					i++;
				}
			}
			for(Village temp : overviewVillages) {
				Database.addVillageToDatabase(temp);
			}
			for(String temp : dbVillages.keySet()) {
				Database.deleteVillage(dbVillages.get(temp).getId());
			}
			//<---Abgleich beendet--->
			//<---Bei allen Dörfern prüfen, ob sie farmen sollen und Wert setzen--->
			dbVillages = Database.getVillages();
			for(Village tempVillage : myVillages) {
				tempVillage.setFarm(dbVillages.get(tempVillage.getId()).isFarming());
			}
			List<Farm> addFarms = Database.addFarmsFromFile();
			for(Farm temp : addFarms) {
				Database.addFarmToDatabase(myVillages.get(0).getId(), temp.x, temp.y, false, true);
			}
			
			Thread.sleep(1638);			
			for (Village village : myVillages) {
				village.completeRefresh();
				browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview_villages");
				Thread.sleep(1937);
			}
			while (true) {
				for(Village currentVillage : myVillages) {
					// TODO check if the current village has to farm 
					document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?screen=overview_villages");
					Thread.sleep(1996);
					currentVillage.completeRefresh();
					if(currentVillage.isFarming()) {
						List<Farm> farmen = Database.getFarms(currentVillage.getId(), true, true);
						List<Farm> farmed = new ArrayList<Farm>();
						if(currentVillage.farmPossible(vorlagen) && farmen.size() > 0)  {
							document = browser.GET("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + currentVillage.getId() + "&screen=place");
							Thread.sleep(1796);
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
										//<---Prüfung ob Farm == Barbarendorf bzw. wenn Farm != Barbarendorf ob Farm angegriffen werden darf--->
										if(!farmen.get(0).owned) {
											Pattern pattern = Pattern.compile("<a href=\"\\/game\\.php\\?village=.+?&amp;id=.+?&amp;screen=info_player\">(.+?)<\\/a>");
											Matcher matcher = pattern.matcher(document.toString());
											if(matcher.find()) {
												Database.markFarm("" + farmen.get(0).farmID);
												farmen.remove(0);
												break;
											}
										}
										Thread.sleep(2296);
										Element actionForm = document.getElementById("command-confirm-form");
										Pattern pattern = Pattern.compile("h=(.+?)\\&");
										Matcher matcher = pattern.matcher(actionForm.attr("action"));
										String hWert = "";
										if(matcher.find()) {
											hWert = matcher.group(1);
										} else {
											throw new IOException("could not fin h-Value in link!");
										}
										String chWert = actionForm.getElementsByAttributeValue("name", "ch").get(0).attr("value");
										String action_ID = actionForm.getElementsByAttributeValue("name", "action_id").get(0).attr("value");
										document = browser.POST("http://" + welt + weltNummer + ".die-staemme.de/game.php?village=" + currentVillage.getId() + 
											"&action=command&h=" + hWert + "&screen=place", "attack=true&ch=" + chWert + "&x=" + farmen.get(0).x + "&y=" + farmen.get(0).y +
											"&action_id=" + action_ID + "&spear=" + vorlage.getSpeertraeger() + "&sword=" + vorlage.getSchwertkaempfer() + 
											"&axe=" + vorlage.getAxtkaempfer() + "&archer=" + vorlage.getBogenschuetzen() + "&spy=" + vorlage.getSpaeher() + "&light=" + 
											vorlage.getLeichteKavallerie() + "&marcher=" + vorlage.getBerittenerBogenschuetze() + "&heavy=" + vorlage.getSchwereKavallerie() + 
											"&ram=" + vorlage.getRammboecke() + "&catapult=" + vorlage.getKatapult() + "&snob=0");
										Thread.sleep(1596);
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
				}
				if(myVillages.size() <= 3) {
					Thread.sleep(3 * 60 * 1000);
				} else {
					Thread.sleep(2378);
				}
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
