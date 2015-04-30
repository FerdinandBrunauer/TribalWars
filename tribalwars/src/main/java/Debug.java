import tribalwars.Utils;
import tribalwars.Village;
import datastore.Database;

public class Debug {

	public static void main1(String[] args) {
		Database.logBuilding("Hochburg", 212, 313, "<img src=\"images/haupthaus.png\">&nbsp;Hauptgeb&auml;de", 11);
		Database.logBuilding("Hochburg", 212, 313, "<img src=\"images/werkstaette.png\">&nbsp;Werkstatt", 21);
	}
	
	public static void main(String[] args) {
		Village village = new Village(10, 5, 3, 0, 0, 5, 1, 2, 17, 17, 16, 10, 14, 2, 8);
		System.out.println(Utils.calculateNextBuilding(1, village));
	}
}
