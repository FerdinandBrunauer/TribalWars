package tribalwars;

import java.util.ArrayList;

import datastore.memoryObjects.Village;

public class VillageList extends ArrayList<Village> {
	private static final long serialVersionUID = -728676498937570851L;

	/**
	 * Liefert den Pointer(Resourcen schonen) des gesuchten Dorfes zurück
	 * 
	 * @param dorfID Die eindeutige ID eines jeden Dorfes
	 * @return Den Pointer des Dorf Objektes wenn vorhanden
	 */
	public Village getVillage(String dorfID) {
		for (int i = 0; i < this.size(); i++) {
			if (get(i).getDorfname().compareTo(dorfID) == 0) {
				return get(i);
			}
		}
		return null;
	}
}
