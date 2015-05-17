package datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import tribalwars.Farm;
import tribalwars.Village;

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
	
	public static List<Farm> getFarms(final String villageID, final boolean onlyFarmable, final boolean resetIfEmpty) {
		List<Farm> out = getInstance().execute(new SQLiteJob<List<Farm>>() {
			@Override
			protected List<Farm> job(SQLiteConnection connection) throws Throwable {
				List<Farm> farms = new ArrayList<Farm>();
				String query = "Select Farm.xCoord, Farm.yCoord, Farm.FarmID, Farm.farm from Farm left join FarmAssignation on Farm.FarmID = FarmAssignation.FarmID and FarmAssignation.VillageID=?";
				if(onlyFarmable) {
					query += " where Farm.farm = 1 and FarmAssignation.farmed = 0;";
				} else {
					query += ";";
				}
				SQLiteStatement statement = connection.prepare(query);
				statement.bind(1, villageID);

				while (statement.step()) {
					farms.add(new Farm(statement.columnInt(3), statement.columnInt(0) ,statement.columnInt(1), statement.columnInt(3)));
				}
				return farms;
			}
		}).complete();
		if(out.size() == 0 && resetIfEmpty) {
			resetFarms(villageID);
			out = getFarms(villageID, onlyFarmable, false);
		}
		return out;
	}
	
	public static void resetFarms(final String villageID) {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				SQLiteStatement statement = connection.prepare("Update FarmAssignation Set farmed=0 where FarmAssignation.VillageID=?;");
				statement.bind(1, villageID);
				statement.step();
				return null;
			}
		}).complete();
	}
	
	public static void setFarmed(Farm farm) {
		ArrayList<Farm> farms = new ArrayList<Farm>();
		farms.add(farm);
		setFarmed(farms);
	}
	
	public static void setFarmed(final List<Farm> farms) {
		getInstance().execute(new SQLiteJob<Boolean>() {
			@Override
			protected Boolean job(SQLiteConnection connection) throws Throwable {
				String query = "Update FarmAssignation Set FarmAssignation.farmed = 1 where ";
				for(int i = 0; i < farms.size()-1; i++) {
					query += "FarmAssignation.FarmAssignationID=? or ";
				}
				query += "FarmAssignation.FarmAssignationID=?;";
				SQLiteStatement statement = connection.prepare(query);
				for(int i = 0; i < farms.size()-1; i++) {
					statement.bind(i+1, farms.get(i).farmID);
				}	
				return true;
			}
		}).complete();
	}
	
	public static void addVillageToDatabase(final Village village) {
		//check if the village is already in the db
		boolean isInDB = getInstance().execute(new SQLiteJob<Boolean>() {
			@Override
			protected Boolean job(SQLiteConnection connection) throws Throwable {
				String query = "Select Villages.VillageID from Villages Where VillageID=?;";
				SQLiteStatement statement = connection.prepare(query);
				statement.bind(1, village.getId());
				if(statement.step()) {
					return true;
				} else {
					return false;
				}
			}
		}).complete();
		
		if(!isInDB) {
			getInstance().execute(new SQLiteJob<Void>() {
				@Override
				protected Void job(SQLiteConnection connection) throws Throwable {
					String query = "Insert Into Villages (VillageID,xCoord,yCoord,name,farm) Values (?,?,?,?,?);";
					SQLiteStatement statement = connection.prepare(query);
					statement.bind(1, village.getId());
					statement.bind(2, village.getX());
					statement.bind(3, village.getY());
					statement.bind(4, village.getName());
					statement.bind(5, 0);
					statement.step();
					return null;
				}
			}).complete();
		} else {
			getInstance().execute(new SQLiteJob<Void>() {
				@Override
				protected Void job(SQLiteConnection connection) throws Throwable {
					String query = "Update Villages Set name=? Where VillageID=?;";
					SQLiteStatement statement = connection.prepare(query);
					statement.bind(1, village.getName());
					statement.bind(2, village.getId());
					statement.step();
					return null;
				}
			}).complete();
		}
	}
}
