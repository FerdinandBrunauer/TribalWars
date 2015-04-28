package tribalwars;

import java.util.ArrayList;

public class Account {

	private ArrayList<Village> myVillages = new ArrayList<Village>();
	
	public Account(String username, String password, String world) {
		
	}
	
	public Village[] getMyVillages() {
		return myVillages.toArray(new Village[myVillages.size()]);
	}

}
