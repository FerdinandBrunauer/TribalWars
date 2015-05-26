package browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import logger.Logger;

public class WebBrowser {

	private HashMap<String, String> cookies;

	public WebBrowser() {
		cookies = new HashMap<String, String>();
	}

	public String post(String link, String post) throws IOException {
		String output = "";
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Cookie", getCookieString());

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(post);
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

		String newCookies = connection.getHeaderField("Set-Cookie");
		addCoockieToStore(newCookies);

		for (String line; (line = reader.readLine()) != null;) {
			output += line;
		}

		writer.close();
		reader.close();
		return output;
	}

	public String get(String link) throws IOException, SessionException {
		String output = "";
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Accept-Encoding", "");
		connection.setRequestProperty("Cookie", getCookieString());

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		for (String line; (line = reader.readLine()) != null;) {
			output += line;
		}

		reader.close();

		String newCookies = connection.getHeaderField("Set-Cookie");
		addCoockieToStore(newCookies);

		if (connection.getHeaderField("Location") != null) {
			if (connection.getHeaderField("Location").endsWith("sid_wrong.php")) {
				throw new SessionException();
			} else {
				System.out.println("Weiterleitung -> \"" + connection.getHeaderField("Location") + "\"");
				return get(connection.getHeaderField("Location"));
			}
		} else {
			return output;
		}
	}

	private String getCookieString() {
		String cookieString = "";
		Iterator<Entry<String, String>> it = cookies.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			cookieString += entry.getKey() + "=" + entry.getValue() + "; ";
		}
		if (cookieString.endsWith("; ")) {
			cookieString = cookieString.substring(0, cookieString.length() - 2);
		}
		return cookieString;
	}

	private void addCoockieToStore(String newCookie) {
		try {
			String[] cookies = newCookie.split("; ");
			for (String cookie : cookies) {
				try {
					String key = cookie.substring(0, cookie.lastIndexOf("="));
					String value = cookie.substring(cookie.lastIndexOf("=") + 1);

					WebBrowser.this.cookies.put(key, value);
				} catch (Exception e) {
					if (cookie.toLowerCase().compareTo("httponly") != 0) {
						Logger.logMessage("Falschen Cookie erhalten. Cookie: \"" + cookie + "\"", e);
					}
				}
			}
		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				Logger.logMessage("Konnte Cookies nicht parsen! Cookies: \"" + newCookie + "\"", e);
			}
		}
	}

}
