package tribalwars;

import java.util.ArrayList;

import datastore.Configuration;
import browser.CaptchaException;
import browser.SessionException;

public class Account implements Runnable {

	private ArrayList<Village> myVillages = new ArrayList<Village>();
	private String username;
	private String password;
	private String world;

	public Account(String username, String password, String world) {
		this.username = username;
		this.password = password;
		this.world = world;

		new Thread(this, "GameLogic").start();
	}

	@Override
	public void run() {
		Village[] villages;
		while (true) {
			if (Boolean.parseBoolean(Configuration.getProperty("active", "true"))) {
				// try {
				villages = getMyVillages();
				for (Village actualVillage : villages) {

				}

				// } catch (SessionException e) {
				// relogin after a specified time
				// } catch (CaptchaException e) {
				// return;
				// }
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}

	public Village[] getMyVillages() {
		return myVillages.toArray(new Village[myVillages.size()]);
	}

}
