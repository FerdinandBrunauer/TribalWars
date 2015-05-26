package tribalwars;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
