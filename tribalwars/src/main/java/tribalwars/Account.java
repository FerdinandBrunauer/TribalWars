package tribalwars;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import browser.CaptchaException;
import browser.SessionException;
import browser.WebBrowser;

public class Account implements Runnable {

	private ArrayList<Village> myVillages = new ArrayList<Village>();
	private String username;
	private String password;
	private String welt;
	private boolean newMessage = false;
	private WebBrowser browser;
	private Document document;

	public Account(String username, String password, String world) {
		this.username = username;
		this.password = password;
		this.welt = world;

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
			// TODO check villages
			myVillages.add(new Village(this, "17105", "CODE 100", 523, 370));
			
			while(true) {
				for(Village actualVillage : myVillages) {
					actualVillage.refresh();
					throw new IOException(); // TODO do something with this information
				}
			}
		} else {
			throw new IOException("Changed LoginSystem or wrong credentials!");
		}
	}

	private boolean login() throws IOException, CaptchaException, SessionException {
		document = browser.GET("http://www.die-staemme.de/");
		document = browser.POST("http://www.die-staemme.de/index.php?action=login&show_server_selection=1", "user=" + username + "&password=" + password + "&clear=true");
		Element passwordElement = document.getElementsByTag("input").get(1);
		String passwordHash = passwordElement.attr("value").replace("&quot;", "").replace("\"", "").replace("\\", "");
		System.out.println("Passworthash: \"" + passwordHash + "\"");
		document = browser.POST("http://www.die-staemme.de/index.php?action=login&server_de" + welt, "user=" + username + "&password=" + passwordHash);
		document = browser.GET("http://de" + welt + ".die-staemme.de/game.php?screen=overview&intro");
		return document.getElementById("menu_counter_profile") != null;
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

}
