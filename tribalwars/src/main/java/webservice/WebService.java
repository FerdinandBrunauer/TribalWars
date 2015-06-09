package webservice;

import java.io.File;
import java.util.logging.Level;

import javax.servlet.ServletException;

import logger.Logger;

import org.apache.catalina.startup.Tomcat;

public class WebService implements Runnable {

	private final Tomcat tomcat;

	public WebService(int port) {
		this.tomcat = new Tomcat();
		this.tomcat.setPort(port);
		this.tomcat.setSilent(true);
		java.util.logging.Logger.getLogger("org.apache.catalina").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.jasper").setLevel(Level.OFF);

		try {
			this.tomcat.addWebapp("/", new File("WebService").getAbsolutePath());
		} catch (ServletException e) {
			Logger.logMessage("Konnte WebService nicht binden!", e);
		}

		new Thread(this, "WebService").start();
	}

	@Override
	public void run() {
		try {
			this.tomcat.start();
			this.tomcat.getServer().await();
		} catch (Exception e) {
			Logger.logMessage("Konnte Server nicht starten!", e);
			System.exit(4);
		}
	}

}
