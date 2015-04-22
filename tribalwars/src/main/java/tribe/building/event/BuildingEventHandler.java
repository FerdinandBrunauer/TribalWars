package tribe.building.event;

import java.util.ArrayList;

public class BuildingEventHandler {

	private ArrayList<BuildingEventListener> listener = new ArrayList<BuildingEventListener>();

	public synchronized void addListener(BuildingEventListener listener) {
		this.listener.add(listener);
	}

	public void fireLevelChangeEvent() {
		BuildingEventListener[] listener = this.listener.toArray(new BuildingEventListener[this.listener.size()]);
		for (BuildingEventListener buildingEventListener : listener) {
			buildingEventListener.levelChangeEvent();
		}
		listener = null;
	}

	public synchronized void removeListener(BuildingEventListener listener) {
		this.listener.remove(listener);
	}

}
