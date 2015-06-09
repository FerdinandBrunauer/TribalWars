package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

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

	public static void insertFarm(final long idFarm, final long idVillage, final int x, final int y) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Farm` (`idFarm`, `idVillage`, `x`, `y`) VALUES (?, ?, ?, ?);");
				statement.bind(1, idFarm);
				statement.bind(2, idVillage);
				statement.bind(3, x);
				statement.bind(4, y);
				statement.stepThrough();
				return null;
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

	public static void insertReport(final long idReport, final long idFarm, final Date attackTime, final int spyedResources, final int wood, final int stone, final int iron, final int wall) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("INSERT INTO `Report`(`idReport`, `idFarm`, `attackTime`, `spyedResources`, `wood`, `stone`, `iron`, `wall`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
				statement.bind(1, idReport);
				statement.bind(2, idFarm);
				statement.bind(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(attackTime));
				statement.bind(4, spyedResources);
				statement.bind(5, wood);
				statement.bind(6, stone);
				statement.bind(7, iron);
				statement.bind(8, wall);
				statement.stepThrough();
				return null;
			}
		}).complete();
	}

	// TODO insert Farm Attack
	/* public static void insertFarmAttack(final long idVillage, final Date arrival, final int possibleLoot) {
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
	} */

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
		return getInstance().execute(new SQLiteJob<Double>() {
			@Override
			protected Double job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("SELECT IFNULL(AVG(`spyedResources`), 0) FROM `Report`;"); // TODO avgCountFarm
				statement.step();
				return statement.columnDouble(0);
			}
		}).complete();
	}
}
