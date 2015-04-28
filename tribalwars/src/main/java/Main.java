import tribalwars.Account;
import webservice.WebService;
import datastore.Configuration;

public class Main {

	public static void main(String[] args) {
		// TODO dynamic loading from Username, Password and World
		new WebService(new Account("MrLordFred", "Ferdinand1!$", "116"), Integer.parseInt(Configuration.getProperty("port", "8080")));
	}

}