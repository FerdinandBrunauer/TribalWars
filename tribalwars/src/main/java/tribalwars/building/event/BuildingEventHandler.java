package tribalwars.building.event;

import java.util.ArrayList;

import org.jsoup.nodes.Document;

public class BuildingEventHandler {

	private ArrayList<BuildingEventListener> listener = new ArrayList<BuildingEventListener>();

	public synchronized void addListener(BuildingEventListener listener) {
		this.listener.add(listener);
	}

	public void fireLevelChangeEvent(Document htmlDocument) {
		BuildingEventListener[] listener = this.listener.toArray(new BuildingEventListener[this.listener.size()]);
		for (BuildingEventListener buildingEventListener : listener) {
			buildingEventListener.levelChangeEvent(htmlDocument);
		}
		listener = null;
	}

	public synchronized void removeListener(BuildingEventListener listener) {
		this.listener.remove(listener);
	}

}
