package datastore;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;

public class Database extends SQLiteQueue {

	private static Database instance = null;

	private Database() {
		super(new File("database.d3b"));
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
			instance = new Database();
			createTables();
			createTriggers();
		}
		return instance;
	}

	private static void createTables() {
		getInstance().execute(new SQLiteJob<Void>() {
			@Override
			protected Void job(SQLiteConnection connection) throws Throwable {
				// TODO
				return null;
			}
		}).complete();
	}

	private static void createTriggers() {

	}

}
