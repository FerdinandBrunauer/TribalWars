package browser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class SafetyManager {

	public static Document checkCaptcha(WebBrowser browser, Document document, String url) throws CaptchaException, MalformedURLException, IOException, SessionException {
		// TODO check captcha
		if (document.toString().contains("Botschutz")) {
			File botSchutz = new File("test.html");
			PrintWriter writer = new PrintWriter(botSchutz);
			writer.append(document.toString());
			writer.flush();
			writer.close();
			Pattern pattern = Pattern.compile("document\\.getElementById\\('bot_check_image'\\).src = '(.+)';");
			Matcher matcher = pattern.matcher(document.toString());
			String link = "";
			if (matcher.find()) {
				link = matcher.group(1);
			} else {
				writer = new PrintWriter(new File("output.txt"));
				writer.append("Found Botschutz\n");
				writer.flush();
				writer.close();
				pattern = Pattern.compile("\\$\\('#bot_check_image'\\)\\.attr\\('src', '(.+?)'\\)");
				matcher = pattern.matcher(document.toString());
				if (!matcher.find()) {
					throw new IOException("Unexpected Error");
				}
				link = matcher.group(1);
			}
			pattern = Pattern.compile("http:\\/\\/(.+?)\\.");
			matcher = pattern.matcher(url);
			matcher.find();
			String worldAndNumber = matcher.group(1);
			if (worldAndNumber.compareTo("www") != 0) {
				InputStream in = new BufferedInputStream(new URL("http://" + worldAndNumber + ".die-staemme.de" + link).openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
				Captcha testCaptcha = new Captcha(response);
				String solution = testCaptcha.solveCapture();
				return browser.POST(url, "code=" + solution);
			}
		}
		return document;
		/*
		 * <div id="bot_check"> <h2>Botschutz</h2>
		 * 
		 * <div id="bot_check_error"
		 * style="color:red; font-size:large; display: none"></div> <img
		 * id="bot_check_image" src="/human.php?s=75ad99cc1c26&amp;small"
		 * "="" alt=""><br><br> <form id="bot_check_form" method="post"
		 * action=""> Gib die Nummern und Buchstaben in das Textfeld ein: <input
		 * id="bot_check_code" type="text" name="code" style="width: 70px">
		 * <input id="bot_check_submit" class="btn" type="submit"
		 * value="Weiter"> </form> </div>
		 */
	}

	public static void checkSession(Document document) throws SessionException {
		if (document.getElementsByTag("body").get(0).getAllElements().size() <= 1) {
			System.out.println("Session expired - Reloggin");
			throw new SessionException();
		}
	}

}
