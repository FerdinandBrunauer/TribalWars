import java.util.HashMap;
import java.util.Scanner;

import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import tribalwars.Account;
import webservice.WebService;
import datastore.Configuration;

public class Main {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		new ConsoleLogger();
		new FileLogger();
		System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
		
		String username = getConfiguration(Configuration.configuration_username, "Bitte Benutzername eingeben: ");
		Logger.logMessage("Benutzername: \"" + username + "\"");
		String password = getConfiguration(Configuration.configuration_password, "Bitte Passwort eingeben: ");
		Logger.logMessage("Passwort: \"" + password + "\"");
		String worldPrefix = getConfiguration(Configuration.configuration_worldprefix, "Bitte den Weltprefix eingeben (z.B. \"de\", oder \"deq\" ohne \"): ");
		Logger.logMessage("Weltprefix: \"" + worldPrefix + "\"");
		String worldNumber = getConfiguration(Configuration.configuration_worldnumber, "Bitte die Weltenzahl eingeben (z.B. \"115\", \"116\" ohne \"): ");
		Logger.logMessage("Weltenzahl: \"" + worldNumber + "\"");

		checkWorldspeed(worldPrefix, worldNumber);
		check9kw();

		Account account = new Account(username, password, worldPrefix, worldNumber);
		new WebService(account, Integer.parseInt(Configuration.getProperty(Configuration.configuration_webserviceport, "8080")));
	}

	private static String getConfiguration(String configuration_prefix, String consoleQuestion) {
		String configurationValue = Configuration.getProperty(configuration_prefix, null);
		if (configurationValue == null) {
			System.out.print(consoleQuestion);
			configurationValue = scanner.nextLine();
			Configuration.setProperty(configuration_prefix, configurationValue);
		}
		return configurationValue;
	}

	private static void check9kw() {
		getConfiguration(Configuration.configuration_9kweu_javaapikey, "Bitte geben sie ihren API-Key f\u00FCr 9kw.eu ein: ");
		try {
			HashMap<String, String> parameter = new HashMap<String, String>();
			parameter.put("action", "usercaptchaguthaben");
			parameter.put("apikey", Configuration.getProperty(Configuration.configuration_9kweu_javaapikey, null));

			Document creditPage = Jsoup.connect("https://www.9kw.eu/index.cgi").timeout(10 * 1000).data(parameter).get();
			int credits = Integer.parseInt(creditPage.body().html());
			Logger.logMessage("9kw.eu Credits: \"" + credits + "\"");
			if (credits < 50) {
				Logger.logMessage("Zu wenige Credits vorhanden. Bitte erh\u00F6hen sie ihre Credits!");
				System.exit(3);
			}
		} catch (Exception e) {
			Logger.logMessage("Konnte 9kw.eu Credits nicht lesen!", e);
			System.exit(2);
		}
	}

	private static void checkWorldspeed(String worldPrefix, String worldNumber) {
		try {
			Document worldSettings = Jsoup.connect("http://de.twstats.com/" + worldPrefix + worldNumber + "/index.php?page=settings").timeout(10 * 1000).get();
			String worldSpeed = worldSettings.getElementsByClass("widget").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(1).html();
			Configuration.setProperty(Configuration.configuration_worldspeed, worldSpeed);
			Logger.logMessage("Weltgeschwindigkeit: \"" + worldSpeed + "\"");
		} catch (Exception e) {
			Logger.logMessage("Konnte Weltgeschwindigkeit nicht lesen!", e);
			System.exit(1);
		}
	}
}