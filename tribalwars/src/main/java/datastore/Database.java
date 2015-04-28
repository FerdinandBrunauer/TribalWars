package datastore;

import java.io.File;

import com.almworks.sqlite4java.SQLiteQueue;

public class Database extends SQLiteQueue {

	private static Database instance = null;

	private Database() {
		super(new File("database.d3b"));
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
			instance = new Database();
		}
		return instance;
	}

}
