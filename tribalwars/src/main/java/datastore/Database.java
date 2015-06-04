package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

import datastore.memoryObjects.BuildingPattern;
import datastore.memoryObjects.VorlageItem;

public class Database extends SQLiteQueue {

	private static Database instance = null;

	private Database(File databaseFile) {
		super(databaseFile);
		start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				instance.stop(true);
			}
		});
	}

	private synchronized static Database getInstance() {
		if (instance == null) {
			java.util.logging.Logger.getLogger("com.almworks.sqlite4java").setLevel(java.util.logging.Level.OFF);

			File databaseFile = new File("database.db3");
			boolean exists = databaseFile.exists();
			instance = new Database(databaseFile);
			if (!exists) {
				createDatabase();
			}
		}
		return instance;
	}

	private static void createDatabase() {
		logger.Logger.logMessage("Erstellen der Datenbank gestartet!");
		long startTime = System.currentTimeMillis();

		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				try (BufferedReader br = new BufferedReader(new FileReader("database.sql"))) {
					for (String line; (line = br.readLine()) != null;) {
						if (line.startsWith("--") || line.equals("")) {
							continue;
						} else {
							connection.prepare(line).stepThrough();
						}
					}
				} catch (Exception e) {
					logger.Logger.logMessage("Konnte Datenbank nicht erstellen!", e);
					System.exit(1);
				}
				return null;
			}
		}).complete();

		long executionTime = System.currentTimeMillis() - startTime;

		int seconds = (int) (executionTime / 1000) % 60;
		int minutes = (int) ((executionTime / (1000 * 60)) % 60);
		int milliseconds = (int) executionTime - (minutes * 1000 * 60) - (seconds * 1000);

		String time = String.format("%d " + ((minutes == 1) ? "Minute" : "Minuten") + " %d " + ((seconds == 1) ? "Sekunde" : "Sekunden") + " %d " + ((milliseconds == 1) ? "Millisekunde" : "Millisekunden"), minutes, seconds, milliseconds);
		logger.Logger.logMessage("Erstellen der Datenbank abgeschlossen! Dauer: \"" + time + "\"");
	}

	public static HashMap<String, Integer> getMaximalBuildinglevelFromVorlage(final long vorlageID) {
		return getInstance().execute(new SQLiteJob<HashMap<String, Integer>>() {
			@Override
			protected HashMap<String, Integer> job(SQLiteConnection connection) throws Throwable {
				HashMap<String, Integer> buildings = new HashMap<String, Integer>();
				SQLiteStatement statement = connection.prepare("SELECT `name` FROM `Building`;");
				while (statement.step()) {
					buildings.put(statement.columnString(0), 0);
				}

				statement = connection.prepare("SELECT Building.name AS 'building', MAX(VorlageItem.level) AS 'level' FROM VorlageItem JOIN Building ON VorlageItem.idBuilding=Building.idBuilding WHERE VorlageItem.idVorlage=? GROUP BY VorlageItem.idBuilding");
				statement.bind(1, vorlageID);

				while (statement.step()) {
					buildings.put(statement.columnString(0), statement.columnInt(1));
				}

				return buildings;
			}
		}).complete();
	}

	public static List<BuildingPattern> getBuildingPattern() {
		return getInstance().execute(new SQLiteJob<List<BuildingPattern>>() {
			@Override
			protected List<BuildingPattern> job(SQLiteConnection connection) throws Throwable {
				List<BuildingPattern> pattern = new ArrayList<BuildingPattern>();

				SQLiteStatement statement = connection.prepare("SELECT `idVorlage`, `name` FROM `Vorlage`;");
				while (statement.step()) {
					pattern.add(new BuildingPattern(statement.columnLong(0), statement.columnString(1)));
				}

				return pattern;
			}
		}).complete();
	}

	public static List<String> getBuildingPatternContentFormatted(final long id) {
		return getInstance().execute(new SQLiteJob<List<String>>() {
			@Override
			protected List<String> job(SQLiteConnection connection) throws Throwable {
				List<String> content = new ArrayList<>();

				SQLiteStatement statement = connection.prepare("SELECT `Building`.`displayName`, `VorlageItem`.`level` FROM `VorlageItem` JOIN `Building` ON `VorlageItem`.`idBuilding`=`Building`.`idBuilding` WHERE `VorlageItem`.`idVorlage`=? ORDER BY `VorlageItem`.`position` ASC;");
				statement.bind(1, id);
				while (statement.step()) {
					content.add("<table style=\"width: 100%;\"><tr><td style=\"text-align: left;\">" + statement.columnString(0) + "</td><td style=\"text-align: right;\">" + statement.columnInt(1) + "</td></tr></table>");
				}

				return content;
			}
		}).complete();
	}

	public static List<VorlageItem> getBuildingPatternContent(final long id) {
		return getInstance().execute(new SQLiteJob<List<VorlageItem>>() {
			@Override
			protected List<VorlageItem> job(SQLiteConnection connection) throws Throwable {
				List<VorlageItem> items = new ArrayList<VorlageItem>();

				SQLiteStatement statement = connection.prepare("SELECT `Building`.`name`, `VorlageItem`.`level` FROM `VorlageItem` JOIN `Building` ON `VorlageItem`.`idBuilding`=`Building`.`idBuilding` WHERE `VorlageItem`.`idVorlage`=? ORDER BY `VorlageItem`.`position` ASC;");
				statement.bind(1, id);
				while (statement.step()) {
					items.add(new VorlageItem(statement.columnString(0), statement.columnInt(1)));
				}

				return items;
			}
		}).complete();
	}

	public static void insertVillage(final long idVillage, final int x, final int y) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT OR IGNORE INTO `Village` (`idVillage`, `x`, `y`) VALUES (?, ?, ?);");
				statement.bind(1, idVillage);
				statement.bind(2, x);
				statement.bind(3, y);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static void insertReport(final long idReport, final long idVillage, final Date attackTime, final int spyedWood, final int spyedStone, final int spyedIron, final int wood, final int stone, final int iron, final int wall) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Report`(`idReport`,`idVillage`,`attackTime`, `spyedWood`, `spyedStone`, `spyedIron`, `wood`, `stone`, `iron`, `wall`) VALUES (?,?,?,?,?,?,?,?,?,?);");
				statement.bind(1, idReport);
				statement.bind(2, idVillage);
				statement.bind(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(attackTime));
				statement.bind(4, spyedWood);
				statement.bind(5, spyedStone);
				statement.bind(6, spyedIron);
				statement.bind(7, wood);
				statement.bind(8, stone);
				statement.bind(9, iron);
				statement.bind(10, wall);

				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static long getMaximalReportID() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT ifnull(MAX(`idReport`), 0) FROM `Report`;");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static void insertFarmAttack(final long idVillage, final Date arrival, final int possibleLoot) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `FarmAttack`(`idVillage`,`arrival`,`possibleLoot`) VALUES (?,?,?);");
				statement.bind(1, idVillage);
				statement.bind(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arrival));
				statement.bind(3, possibleLoot);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}
}
