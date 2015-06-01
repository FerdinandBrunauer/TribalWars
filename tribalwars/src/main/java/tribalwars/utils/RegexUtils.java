package tribalwars.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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

	public static JSONObject getJsonFromHead(String head) {
		Pattern pattern = Pattern.compile("var\\sgame_data\\s=\\s(.*);");
		Matcher matcher = pattern.matcher(head);
		if (matcher.find()) {
			return new JSONObject(matcher.group(1));
		} else {
			return null;
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
			int date = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(2));
			int year = Integer.parseInt("20" + matcher.group(3));
			int hours = Integer.parseInt(matcher.group(4));
			int minutes = Integer.parseInt(matcher.group(5));
			int seconds = Integer.parseInt(matcher.group(6));

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, date);
			calendar.set(Calendar.HOUR, hours);
			calendar.set(Calendar.MINUTE, minutes);
			calendar.set(Calendar.SECOND, seconds);
			return calendar.getTime();
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

}
