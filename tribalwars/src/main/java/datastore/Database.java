package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tribalwars.Account;
import tribalwars.Village;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

import datastore.memoryObject.Farm;

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

	public static void insertFarm(final long idFarm, final long idVillage, final int x, final int y, final double distance) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Farm` (`idFarm`, `idVillage`, `x`, `y`, `distance`) VALUES (?, ?, ?, ?, ?);");
				statement.bind(1, idFarm);
				statement.bind(2, idVillage);
				statement.bind(3, x);
				statement.bind(4, y);
				statement.bind(5, distance);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static ArrayList<Farm> getFarms(final long idVillage) {
		return getInstance().execute(new SQLiteJob<ArrayList<Farm>>() {
			@Override
			protected ArrayList<Farm> job(SQLiteConnection connection) throws Throwable {
				ArrayList<Farm> farmen = new ArrayList<Farm>();

				SQLiteStatement statement = connection
						.prepare("SELECT `Farm`.`x`, `Farm`.`y`, `Farm`.`distance`, CASE WHEN ((wood.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) > storage.`storage` THEN storage.`storage` ELSE ((wood.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) END + CASE WHEN ((stone.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) > storage.`storage` THEN storage.`storage` ELSE ((stone.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) END + CASE WHEN ((iron.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) > storage.`storage` THEN storage.`storage` ELSE ((iron.`production` * ?) * (strftime(\"%s\", datetime('now', 'localtime')) - strftime(\"%s\", `Report`.`attackTime`))) END AS 'possibleResources', `Report`.`wall`FROM `Report`JOIN `Farm` ON `Farm`.`idFarm` = `Report`.`idFarm`JOIN `Production` AS wood ON wood.`level` = `Report`.`wood`JOIN `Production` AS stone ON stone.`level` = `Report`.`stone`JOIN `Production` AS iron ON iron.`level` = `Report`.`iron`JOIN `Storage` AS `storage` ON `storage`.`level` = `Report`.`storage`WHERE `Farm`.`idVillage` = ? GROUP BY `Report`.`idFarm`ORDER BY `Farm`.`distance` ASC;");
				for (int i = 1; i <= 6; i++) {
					statement.bind(i, Integer.parseInt(Configuration.getProperty(Configuration.configuration_worldspeed, "")));
				}
				statement.bind(7, idVillage);
				while (statement.step()) {
					farmen.add(new Farm(statement.columnInt(0), statement.columnInt(1), statement.columnDouble(2), statement.columnDouble(3), statement.columnInt(4)));
				}
				return farmen;
			}
		}).complete();
	}

	public static void removeFarm(final long idFarm) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("DELETE FROM `Farm` WHERE `idFarm`=?;");
				statement.bind(1, idFarm);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static int getFarmCount(final long idVillage) {
		return getInstance().execute(new SQLiteJob<Integer>() {
			@Override
			protected Integer job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT COUNT(`idFarm`) FROM `Farm` WHERE `idVillage`=?;");
				statement.bind(1, idVillage);
				statement.step();
				return statement.columnInt(0);
			}
		}).complete();
	}

	public static boolean containsReport(final long idReport) {
		return getInstance().execute(new SQLiteJob<Boolean>() {
			@Override
			protected Boolean job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(MAX(`idReport`), 0) FROM `Report` WHERE `idReport`=?;");
				statement.bind(1, idReport);
				statement.step();
				return statement.columnLong(0) > 0;
			}
		}).complete();
	}

	public static void insertReport(final long idReport) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Report`(`idReport`) VALUES (?);");
				statement.bind(1, idReport);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static void insertReport(final long idReport, final long idFarm, final Date attackTime, final int spyedResources, final int wood, final int stone, final int iron, final int storage, final int wall) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Report`(`idReport`, `idFarm`, `attackTime`, `spyedResources`, `wood`, `stone`, `iron`, `storage`, `wall`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				statement.bind(1, idReport);
				statement.bind(2, idFarm);
				statement.bind(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(attackTime));
				statement.bind(4, spyedResources);
				statement.bind(5, wood);
				statement.bind(6, stone);
				statement.bind(7, iron);
				statement.bind(8, storage);
				statement.bind(9, wall);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	public static void insertFarmAttack(final long idFarm, final int loot, final boolean containsRam) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `FarmAttack`(`idFarm`, `attackTime`, `possibleLoot`, `ram`) VALUES (?, ?, ?, ?);");
				statement.bind(1, idFarm);
				statement.bind(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				statement.bind(3, loot);
				statement.bind(4, (containsRam) ? 1 : 0);
				statement.stepThrough();

				return null;
			}
		}).complete();
	}

	public static boolean needRamAttack(final long idFarm) {
		return getInstance().execute(new SQLiteJob<Boolean>() {
			@Override
			protected Boolean job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT (COUNT(`FarmAttack`.`idFarmAttack`)) FROM `FarmAttack` JOIN `Report` ON `Report`.`idFarm`=`FarmAttack`.`idFarm` WHERE strftime(\"%s\", `FarmAttack`.`attackTime`) > strftime(\"%s\", `Report`.`attackTime`)");
				statement.bind(1, idFarm);
				statement.step();
				if (statement.columnInt(0) > 0) {
					return true;
				} else {
					return false;
				}
			}
		}).complete();
	}

	public static long getIDFarm(final int x, final int y) {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT `idFarm` FROM `Farm` WHERE `x`=? AND `y`=?;");
				statement.bind(1, x);
				statement.bind(2, y);
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static long getCountReports() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT COUNT(`idReport`) FROM `Report`;");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static long getCountFarms() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT COUNT(`idFarm`) FROM `Farm`;");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static long getCountFarmAttack() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT COUNT(`idFarmAttack`) FROM `FarmAttack`;");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static long getMaximalRessourcesSpyed() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(MAX(`spyedResources`), 0) AS countRessources FROM `Report` ORDER BY countRessources DESC LIMIT 1;");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static long getTotalChanges() {
		return getInstance().execute(new SQLiteJob<Long>() {
			@Override
			protected Long job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT TOTAL_CHANGES();");
				statement.step();
				return statement.columnLong(0);
			}
		}).complete();
	}

	public static double getAvgSpyedResources() {
		return getInstance().execute(new SQLiteJob<Double>() {
			@Override
			protected Double job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(AVG(`spyedResources`), 0) FROM `Report`;");
				statement.step();
				return statement.columnDouble(0);
			}
		}).complete();
	}

	public static double getAvgCountFarm() {
		final Village[] villages;
		try {
			villages = Account.getInstance().getMyVillages();
		} catch (IOException e) {
			return 0.0d;
		}
		if (villages.length < 1) {
			return 0.0d;
		}

		return getInstance().execute(new SQLiteJob<Double>() {
			@Override
			protected Double job(SQLiteConnection connection) throws Throwable {
				String questionMarks = "";
				for (int i = 0; i < villages.length; i++) {
					questionMarks += "?, ";
				}
				questionMarks = questionMarks.substring(0, questionMarks.length() - 2);
				SQLiteStatement statement = connection.prepare("SELECT COUNT(`idFarm`)/? FROM `Farm` WHERE `idVillage` IN (" + questionMarks + ");");
				statement.bind(1, villages.length);
				for (int i = 0; i < villages.length; i++) {
					statement.bind(i + 2, villages[i].getID());
				}
				statement.step();
				return statement.columnDouble(0);
			}
		}).complete();
	}

	public static double getAvgDistanceFarm() {
		return getInstance().execute(new SQLiteJob<Double>() {
			@Override
			protected Double job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(AVG(`distance`), 0) FROM `Farm`;");
				statement.step();
				return statement.columnDouble(0);
			}
		}).complete();
	}

	public static double getMaxDistanceFarm() {
		return getInstance().execute(new SQLiteJob<Double>() {
			@Override
			protected Double job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(MAX(`distance`), 0) FROM `Farm`;");
				statement.step();
				return statement.columnDouble(0);
			}
		}).complete();
	}
}
