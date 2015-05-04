import tribalwars.Account;
import webservice.WebService;
import datastore.Configuration;


public class Main {

	public static void main(String[] args) {
		 new WebService(new Account(Configuration.getProperty("tribalwars_username", "MrLordFred"), Configuration.getProperty("tribalswars_password", "Ferdinand1!$"), Configuration.getProperty("tribalswars_world", "116")), Integer.parseInt(Configuration.getProperty("port", "8080")));
	}

}