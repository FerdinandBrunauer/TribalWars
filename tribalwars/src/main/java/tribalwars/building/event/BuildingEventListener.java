package tribalwars.building.event;

import org.jsoup.nodes.Document;

public interface BuildingEventListener {

	public abstract void levelChangeEvent(Document htmlDocument);

}
