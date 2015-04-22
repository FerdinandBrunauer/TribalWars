package tribalwars.building;

import tribalwars.Village;
import tribalwars.building.event.BuildingEventListener;

public class VillageHeadquarters extends Building implements BuildingEventListener {

	public VillageHeadquarters(Village owner) {
		super(owner);
	}

	@Override
	protected void updateLevel() {
		// TODO updateLevel
	}

	public void levelChangeEvent() {
		// TODO Auto-generated method stub
		
	}

}