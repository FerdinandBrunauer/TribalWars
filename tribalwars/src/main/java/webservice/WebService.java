package webservice;

import logger.Logger;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import tribalwars.Account;

public class WebService implements Runnable {

	private final Account account;
	private final Server server;

	public WebService(Account account, int port) {
		this.account = account;

		this.server = new Server();

		ServerConnector connector = new ServerConnector(this.server);
		connector.setPort(port);
		this.server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new AjaxService(this.account)), "/ajax/*");

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase("./WebService/");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		this.server.setHandler(handlers);

		new Thread(this, "WebService").start();
	}

	@Override
	public void run() {
		try {
			this.server.start();
			this.server.join();
		} catch (Exception e) {
			Logger.logMessage("Konnte Server nicht starten!", e);
			System.exit(4);
		}
	}

}
