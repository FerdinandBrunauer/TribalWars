package tribalwars;

import java.util.ArrayList;
import java.util.List;

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
		for (int i = 0; i < size(); i++) {
			if (get(i).getID().compareTo(dorfID) == 0) {
				return get(i);
			}
		}
		return null;
	}

	/**
	 * Gleicht die ausgelesene Dorfübersicht mit der vorhandenen ab und
	 * aktualisiert die Werte
	 * 
	 * @param newList Die ausgelesene Dorfübersicht
	 */
	public void compareToNewList(List<Village> newList) {
		ArrayList<String> idList = getIDs();

		for (Village newVillage : newList) {
			if (idList.remove(newVillage.getID())) {
				Village oldVillage = getVillage(newVillage.getID());
				oldVillage.setDorfname(newVillage.getDorfname());
				oldVillage.setHolz(newVillage.getHolz());
				oldVillage.setLehm(newVillage.getLehm());
				oldVillage.setEisen(newVillage.getEisen());
			} else {
				this.add(newVillage);
			}
		}

		if (idList.size() > 0) {
			// Dorf/Dörfer verloren
			for (String id : idList) {
				this.remove(indexOf(new Village(id, "undefined", 0, 0)));
			}
		}
	}

	private ArrayList<String> getIDs() {
		ArrayList<String> idList = new ArrayList<String>();

		for (Village village : this) {
			idList.add(village.getID());
		}

		return idList;
	}
}
