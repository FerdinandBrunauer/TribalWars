package tribalwars.building;

import org.jsoup.nodes.Document;

import tribalwars.Village;

public class VillageHeadquarters extends Building {

	public VillageHeadquarters(Village owner) {
		super(owner);
	}

	@Override
	protected void updateLevel() {
		// TODO updateLevel
	}

	public void levelChangeEvent(Document htmlDocument) {
		// TODO Auto-generated method stub
	}

}