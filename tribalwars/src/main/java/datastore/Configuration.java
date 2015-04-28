package datastore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	private static Properties defaultProperties = new Properties();
	
	static {
		try {
			FileInputStream in = new FileInputStream("configuration.prop");
			defaultProperties.load(in);
			in.close();
		} catch(Exception e) {
			// TODO log message
		}
	}
	
	public static void setProperty(String key, String value) {
		defaultProperties.setProperty(key, value);
		try {
			OutputStream out = new FileOutputStream("configuration.prop");
			defaultProperties.store(out, null);
			out.close();
		} catch (Exception e) {
			// TODO log message
		}
	}
	
	public static String getProperty(String key, String defaultValue) {
		String prop = defaultProperties.getProperty(key);
		if(prop == null) {
			return defaultValue;
		} else {
			return prop;
		}
	}
	
}
