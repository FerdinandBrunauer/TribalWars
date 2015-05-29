package logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FileLogger extends LoggerInterface {

	private FileHandler fileHandler = null;

	public FileLogger() {
		try {
			File folder = new File("log/");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File[] files = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".lck");
				}
			});
			for (File file : files) {
				file.delete();
			}

			this.fileHandler = new FileHandler("log/log.%u.%g.txt", 1024 * 1024, 1, true);
			this.fileHandler.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord lr) {
					return lr.getMessage() + "\r\n";
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					FileLogger.this.fileHandler.close();
				}
			});
			Logger.addListener(this);
		} catch (Exception e) {
			Logger.logMessage("Konnte FileLogger nicht erstellen!", e);
		}
	}

	@Override
	public void logMessage(String message) {
		this.fileHandler.publish(new LogRecord(Level.ALL, String.format("%-20s\t%-40s", new Object[] { getTime(), message })));
	}

	@Override
	public void logMessage(String message, Throwable e) {
		this.fileHandler.publish(new LogRecord(Level.ALL, String.format("%-20s\t%-40s\t%-100s", new Object[] { getTime(), message, e.getMessage() })));
	}

}
