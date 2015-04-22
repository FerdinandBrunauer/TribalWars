package tribalwars;

import tribe.building.VillageHeadquarters;
import tribe.building.event.BuildingEventHandler;

public class Village extends BuildingEventHandler {

	private final VillageHeadquarters headquarter = new VillageHeadquarters(this);

	public VillageHeadquarters getHeadquarter() {
		return headquarter;
	}

}
