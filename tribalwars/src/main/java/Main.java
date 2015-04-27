import java.util.List;

import tribalwars.Account;
import tribalwars.World;

public class Main {

	private static String username = "MrLordFred";
	private static String password = "Ferdinand1!$";
	private static String worldName = "";

	public static void main(String[] args) {
		Account account = new Account(username, password);
		List<World> worlds = account.getPlayableWorlds();
		if (worlds == null) {
			System.err.println("Login failed!");
		} else if (worlds.contains(new World(worldName))) {
			if(account.login(worldName)) {
				// TODO
			} else {
				System.err.println("Login failed!");
			}
		} else {
			System.err.println("Never played on this World!");
		}
	}
}