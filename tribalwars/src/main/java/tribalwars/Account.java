package tribalwars;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private final String username;
	private final String password;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public List<World> getPlayableWorlds() {
		return new ArrayList<World>(); // TODO get playable worlds
	}

	public boolean login(String worldName) {
		return true; // TODO
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

}
