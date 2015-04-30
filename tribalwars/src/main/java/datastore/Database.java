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
		this.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				instance.stop(true);
			}
		});
	}

	private synchronized static Database getInstance() {
		if (instance == null) {
			File databaseFile = new File("database.d3b");
			// databaseFile.delete(); // TODO remove '//' when changing the sql
			// file
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
							System.out.println(line);
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

	public static void logBuilding(final String dorfname, final int x, final int y, String building, int toLevel) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		String formattedTime = format.format(date);
		final String logMessage = "<table style=\"width: 100%;\"><tr>" + "<td style=\"width: 20%\">" + formattedTime + "</td>" + "<td style=\"width: 20%\">" + dorfname + "(" + x + "|" + y + ")" + "</td>" + "<td style=\"width: 60%\">Ausbau von " + building + " auf Stufe " + toLevel + "</td>"
				+ "</tr></table>";
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO BuildLog (message) VALUES (?);");
				statement.bind(1, logMessage);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static void logAttack(final String destinationDorfname, final int destinationX, final int destinationY, final String targetDorfname, final int targetX, final int targetY) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		String formattedTime = format.format(date);
		final String logMessage = "<table style=\"width: 100%;\"><tr>" + "<td style=\"width: 20%\">" + formattedTime + "</td>" + "<td style=\"width: 20%\">" + destinationDorfname + "(" + destinationX + "|" + destinationY + ")</td>" + "<td style=\"width: 20%\">" + targetDorfname + "(" + targetX
				+ "|" + targetY + ")</td>" + "<td style=\"width: 40%\"></td>" + "</tr></table>";
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `AngriffLog` (message) VALUES (?);");
				statement.bind(1, logMessage);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static List<String> getLogBuildings(final long startID, final int count) {
		return getInstance().execute(new SQLiteJob<List<String>>() {
			@Override
			protected List<String> job(SQLiteConnection connection) throws Throwable {
				List<String> messages = new ArrayList<String>();

				SQLiteStatement statement = connection.prepare("SELECT `message` FROM `BuildLog` WHERE `idLogItem` >=? ORDER BY `idLogItem` DESC LIMIT ?;");
				statement.bind(1, startID);
				statement.bind(2, count);

				while (statement.step()) {
					messages.add(statement.columnString(0));
				}

				return messages;
			}
		}).complete();
	}

	public static List<String> getLogAttack(final long startID, final int count) {
		return getInstance().execute(new SQLiteJob<List<String>>() {
			@Override
			protected List<String> job(SQLiteConnection connection) throws Throwable {
				List<String> messages = new ArrayList<String>();

				SQLiteStatement statement = connection.prepare("SELECT `message` FROM `AngriffLog` WHERE `idLogItem` >=? ORDER BY `idLogItem` DESC LIMIT ?;");
				statement.bind(1, startID);
				statement.bind(2, count);

				while (statement.step()) {
					messages.add(statement.columnString(0));
				}

				return messages;
			}
		}).complete();
	}
}
