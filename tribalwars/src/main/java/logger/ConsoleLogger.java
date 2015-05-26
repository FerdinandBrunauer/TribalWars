package logger;

public class ConsoleLogger extends LoggerInterface {

	public ConsoleLogger() {
		Logger.addListener(this);
	}

	@Override
	public void logMessage(String message) {
		System.out.println(String.format("%-20s\t%-40s", new Object[] { getTime(), message }));
	}

	@Override
	public void logMessage(String message, Throwable e) {
		System.err.println(String.format("%-20s\t%-40s\t%-100s", new Object[] { getTime(), message, e.getMessage() }));
	}

}
