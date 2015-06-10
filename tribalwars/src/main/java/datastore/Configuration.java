package datastore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import logger.Logger;

public class Configuration {

	public static final String configuration_username = "username";
	public static final String configuration_password = "password";
	public static final String configuration_worldprefix = "worldprefix";
	public static final String configuration_worldnumber = "worldnumber";
	public static final String configuration_9kweu_javaapikey = "9kwApiKey";
	public static final String configuration_webserviceport = "webserviceport";
	public static final String configuration_webserviceusername = "webserviceusername";
	public static final String configuration_webservicepassword = "webservicepassword";
	public static final String configuration_worldspeed = "worldspeed";
	public static final String configuration_troupspeed = "troupspeed";
	public static final String configuration_minimum_spys = "minimum_spys";

	private static Properties defaultProperties = new Properties();

	static {
		try {
			FileInputStream in = new FileInputStream("configuration.prop");
			defaultProperties.load(in);
			in.close();
		} catch (Exception ignore) {
			// Datei existiert nicht
		}
	}

	public static void setProperty(String key, String value) {
		if (value != null) {
			defaultProperties.setProperty(key, value);
			try {
				OutputStream out = new FileOutputStream("configuration.prop");
				defaultProperties.store(out, null);
				out.close();
			} catch (Exception e) {
				Logger.logMessage("Konnte Property-Datei nicht schreiben!", e);
			}
		}
	}

	public static String getProperty(String key, String defaultValue) {
		String prop = defaultProperties.getProperty(key);
		if (prop == null) {
			setProperty(key, defaultValue);
			return defaultValue;
		} else {
			return prop;
		}
	}

	public static boolean isDebugmodeEnabled() {
		return true;
	}

}
