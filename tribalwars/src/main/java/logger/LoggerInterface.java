package logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class LoggerInterface implements LoggerListener {

	public static String getTime() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
	}

}
