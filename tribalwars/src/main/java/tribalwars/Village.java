package tribalwars;

import tribalwars.building.Academy;
import tribalwars.building.Barracks;
import tribalwars.building.ClayPit;
import tribalwars.building.Farm;
import tribalwars.building.HidingPlace;
import tribalwars.building.IronMine;
import tribalwars.building.Market;
import tribalwars.building.RallyPoint;
import tribalwars.building.Smithy;
import tribalwars.building.Stable;
import tribalwars.building.TimberCamp;
import tribalwars.building.VillageHeadquarters;
import tribalwars.building.Wall;
import tribalwars.building.Warehouse;
import tribalwars.building.Workshop;
import tribalwars.building.event.BuildingEventHandler;

public class Village extends BuildingEventHandler {

	private final VillageHeadquarters headquarter = new VillageHeadquarters(this);
	private final Barracks barracks = new Barracks(this);
	private final Stable stable = new Stable(this);
	private final Workshop workshop = new Workshop(this);
	private final Academy academy = new Academy(this);
	private final Smithy smithy = new Smithy(this);
	private final RallyPoint rallyPoint = new RallyPoint(this);
	private final Market market = new Market(this);
	private final TimberCamp timberCamp = new TimberCamp(this);
	private final ClayPit clayPit = new ClayPit(this);
	private final IronMine ironMine = new IronMine(this);
	private final Farm farm = new Farm(this);
	private final Warehouse warehouse = new Warehouse(this);
	private final HidingPlace hidingPlace = new HidingPlace(this);
	private final Wall wall = new Wall(this);

	public VillageHeadquarters getHeadquarter() {
		return headquarter;
	}

	public Barracks getBarracks() {
		return barracks;
	}

	public Stable getStable() {
		return stable;
	}

	public Workshop getWorkshop() {
		return workshop;
	}

	public Academy getAcademy() {
		return academy;
	}

	public Smithy getSmithy() {
		return smithy;
	}

	public RallyPoint getRallyPoint() {
		return rallyPoint;
	}

	public Market getMarket() {
		return market;
	}

	public TimberCamp getTimberCamp() {
		return timberCamp;
	}

	public ClayPit getClayPit() {
		return clayPit;
	}

	public IronMine getIronMine() {
		return ironMine;
	}

	public Farm getFarm() {
		return farm;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public HidingPlace getHidingPlace() {
		return hidingPlace;
	}

	public Wall getWall() {
		return wall;
	}
	
}
