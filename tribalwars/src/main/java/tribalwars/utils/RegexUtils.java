package tribalwars.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class RegexUtils {

	public static long getIDFromTwStatsLink(String link) {
		Pattern pattern = Pattern.compile("id=(\\d{1,6})");
		Matcher matcher = pattern.matcher(link);
		if (matcher.find()) {
			return Long.parseLong(matcher.group(1));
		} else {
			return 0;
		}
	}

	public static int[] getCoordsFromVillagename(String name) {
		Pattern pattern = Pattern.compile("(.*)\\s\\((\\d{3})\\|(\\d{3})\\)");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			return new int[] { Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)) };
		} else {
			return null;
		}
	}

	public static JSONObject getVillageJSONFromHead(String head) throws IOException {
		Pattern pattern = Pattern.compile("var\\sgame_data\\s=\\s(.*);");
		Matcher matcher = pattern.matcher(head);
		if (matcher.find()) {
			return new JSONObject(matcher.group(1));
		} else {
			throw new IOException("Konnte Dorf\u00FCbersicht-JSON nicht finden!");
		}
	}

	public static JSONObject getTroupJSON(String html) throws IOException {
		Pattern pattern = Pattern.compile("UnitPopup.unit_data\\s=\\s(.*);");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			return new JSONObject(matcher.group(1));
		} else {
			throw new IOException("Konnte Truppen\u00FCbersicht-Json nicht finden!");
		}
	}

	public static long convertTimestringToMilliseconds(String time) {
		Pattern pattern = Pattern.compile("(\\d\\d?):(\\d\\d):(\\d\\d)");
		Matcher matcher = pattern.matcher(time);
		if (matcher.find()) {
			long hours = Long.parseLong(matcher.group(1));
			long minutes = Long.parseLong(matcher.group(2));
			long seconds = Long.parseLong(matcher.group(3));

			return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
		} else {
			return 5 * 60 * 1000; // 5 Minuten
		}
	}

	public static String getHWert(String html) throws IOException {
		Pattern pattern = Pattern.compile("h=([a-z0-9]{8})");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			throw new IOException("Konnte H-Wert nicht im Quelltext finden!");
		}
	}

	public static Date getTime(String html) throws IOException {
		Pattern pattern = Pattern.compile("(\\d\\d)\\.(\\d\\d)\\.(15)\\s(\\d\\d):(\\d\\d):(\\d\\d)");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			try {
				DateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.GERMAN);
				Date date = format.parse(matcher.group(0));
				return date;
			} catch (Exception ignore) {
				throw new IOException("Konnte Datum in Quelltext nicht parsen! Datum: \"" + matcher.group(0) + "\"");
			}
		} else {
			throw new IOException("Konnte Datum nicht im Quelltext finden!");
		}
	}

	public static int getBuildinglevelFromReport(String json, String building) throws IOException {
		Pattern pattern = Pattern.compile("\\\"id\\\":\\\"" + building + "\\\",\\\"level\\\":\\\"(\\d\\d?)\\\"");
		Matcher matcher = pattern.matcher(json);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		} else {
			return 0; // Building does not exist.
		}
	}

	public static int[] getPopulationFromOverview(String tableData) throws IOException {
		Pattern pattern = Pattern.compile("(\\d{1,5})\\/(\\d{1,5})");
		Matcher matcher = pattern.matcher(tableData);
		if (matcher.find()) {
			return new int[] { Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) };
		} else {
			throw new IOException("Konnte Bev\u00F6lkerung nicht im Quelltext finden!");
		}
	}

}
