package logger;

import java.util.ArrayList;

public class Logger {

	private static ArrayList<LoggerListener> listeners = new ArrayList<LoggerListener>();

	public static void addListener(LoggerListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(LoggerListener listener) {
		listeners.remove(listener);
	}

	public static void logMessage(String message) {
		LoggerListener[] listeners = Logger.listeners.toArray(new LoggerListener[Logger.listeners.size()]);
		for (LoggerListener listener : listeners) {
			listener.logMessage(message);
		}
	}

	public static void logMessage(String message, Throwable e) {
		LoggerListener[] listeners = Logger.listeners.toArray(new LoggerListener[Logger.listeners.size()]);
		for (LoggerListener listener : listeners) {
			listener.logMessage(message, e);
		}
	}

}
