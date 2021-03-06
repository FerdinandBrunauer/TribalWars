package browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.sun.webkit.network.CookieManager;

@SuppressWarnings("restriction")
public class WebBrowser {

	private String referrer;

	public WebBrowser() {
		CookieHandler.setDefault(new CookieManager());
		this.referrer = "http://www.google.at";
	}

	public String post(String link, String post) throws IOException, CaptchaException {
		return post(link, post, null);
	}

	public String post(String link, String post, List<SimpleEntry<String, String>> additionalHeader) throws IOException, CaptchaException {
		String output = "";
		URL url = new URL(link);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Accept-Encoding", "");
		connection.setRequestProperty("Referer", getReferrer());
		if (additionalHeader != null) {
			for (SimpleEntry<String, String> entry : additionalHeader) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(post);
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

		for (String line; (line = reader.readLine()) != null;) {
			output += line;
		}

		writer.close();
		reader.close();

		setNewReferrer(link);

		if (output.contains("Botschutz")) {
			throw new CaptchaException();
		}

		sleepRandom();

		return output;
	}

	public String get(String link) throws IOException, SessionException, CaptchaException {
		String output = "";

		URL url = new URL(link);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Accept-Encoding", "");
		connection.setRequestProperty("Referer", getReferrer());

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		for (String line; (line = reader.readLine()) != null;) {
			output += line;
		}

		reader.close();

		setNewReferrer(link);

		if (output.contains("Botschutz")) {
			throw new CaptchaException();
		}

		if (!link.contains("screen=report")) {
			sleepRandom();
		}

		if (connection.getHeaderField("Location") != null) {
			if (connection.getHeaderField("Location").endsWith("sid_wrong.php")) {
				throw new SessionException();
			}
			return get(connection.getHeaderField("Location"));
		} else {
			return output;
		}
	}

	private void setNewReferrer(String url) {
		this.referrer = url;
	}

	private String getReferrer() {
		return this.referrer;
	}

	private void sleepRandom() throws IOException {
		long time = (long) (Math.random() * 1500) + 900;
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignore) {
			throw new IOException("Pause des Browsers wurde unterbrochen!");
		}
	}
}
