import tribalwars.Account;
import webservice.WebService;
import datastore.Configuration;

public class Main {

	public static void main(String[] args) {
		// Database.logBuilding("Hochburg", 212, 313,
		// "<img src=\"images/haupthaus.png\">&nbsp;Hauptgeb&auml;de", 11);
		// Database.logBuilding("Hochburg", 212, 313,
		// "<img src=\"images/werkstaette.png\">&nbsp;Werkstatt", 21);

		// TODO dynamic loading from Username, Password and World
		new WebService(new Account(Configuration.getProperty("tribalwars_username", "MrLordFred"), Configuration.getProperty("tribalswars_password", "Ferdinand1!$"), Configuration.getProperty("tribalswars_world", "116")), Integer.parseInt(Configuration.getProperty("port", "8080")));
	}
	
}