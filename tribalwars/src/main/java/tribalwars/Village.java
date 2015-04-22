package tribalwars;

import tribalwars.building.VillageHeadquarters;
import tribalwars.building.event.BuildingEventHandler;

public class Village extends BuildingEventHandler {

	private final VillageHeadquarters headquarter = new VillageHeadquarters(this);

	public VillageHeadquarters getHeadquarter() {
		return headquarter;
	}

}
