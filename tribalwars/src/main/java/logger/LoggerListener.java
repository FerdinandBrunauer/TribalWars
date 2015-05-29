package logger;

public interface LoggerListener {

	public void logMessage(String message);

	public void logMessage(String message, Throwable e);

}
