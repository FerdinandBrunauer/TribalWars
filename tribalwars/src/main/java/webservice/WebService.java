package webservice;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import tribalwars.Account;

public class WebService implements Runnable {

	private final Account account;
	private final Server server;

	public WebService(Account account, int port) {
		this.account = account;
		
		this.server = new Server();
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new AjaxService(this.account)), "/ajax/*");
		
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase("./WebService/");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		server.setHandler(handlers); 

		new Thread(this, "WebService").start();
	}

	public void run() {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.err.println("Konnte Server nicht starten"); // TODO log error message
			System.exit(1);
		}
	}

}
