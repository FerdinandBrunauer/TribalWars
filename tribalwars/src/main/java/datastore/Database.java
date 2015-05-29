package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

import datastore.memoryObjects.BuildingPattern;
import datastore.memoryObjects.VorlageItem;

public class Database extends SQLiteQueue {

	private static Database instance = null;

	static {
		if (!Configuration.isDebugmodeEnabled()) {
			Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
		}
	}

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
					System.err.println("Konnte Datenbank nicht erstellen! Error: \"" + e.getMessage() + "\"");
					System.exit(1);
				}
				return null;
			}
		}).complete();
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

	public static void insertReport() {
		// TODO insert Report
	}

}
