package datastore.memoryObjects;

import java.util.ArrayList;
import java.util.Date;

public class Village {

	private final String id;
	private String dorfname;
	private final int x;
	private final int y;
	private int holz = 0;
	private int lehm = 0;
	private int eisen = 0;
	private int speicher = 0;
	private int population = 0;
	private int maximalPopulation = 0;
	private ArrayList<String> needResearch = new ArrayList<String>();
	private Date nextBuildingbuildPossible = new Date();
	private Date nextTroupBuildBarracksPossible = new Date();
	private Date nextTroupBuildStablePossible = new Date();
	private Date nextTroupBuildWorkshopPossible = new Date();
	private Date nextFarmattackPossible = new Date();

	public Village(String id, String dorfname, int x, int y) {
		this.id = id;
		this.dorfname = dorfname;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Village) {
			Village village = (Village) object;
			if (village.getID().compareTo(getID()) == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	};

	public String getID() {
		return this.id;
	}

	public void setDorfname(String dorfname) {
		this.dorfname = dorfname;
	}

	public String getDorfname() {
		return this.dorfname;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getHolz() {
		return this.holz;
	}

	public int getLehm() {
		return this.lehm;
	}

	public int getEisen() {
		return this.eisen;
	}

	public int getSpeicher() {
		return this.speicher;
	}

	public void setHolz(int holz) {
		this.holz = holz;
	}

	public void setLehm(int lehm) {
		this.lehm = lehm;
	}

	public void setEisen(int eisen) {
		this.eisen = eisen;
	}

	public void setSpeicher(int speicher) {
		this.speicher = speicher;
	}

	public boolean isNextBuildingbuildPossible() {
		return new Date().after(this.nextBuildingbuildPossible);
	}

	public void setNextBuildingbuildPossible(Date nextBuildingbuildPossible) {
		this.nextBuildingbuildPossible = nextBuildingbuildPossible;
	}

	public Date getNextBuildingbuildPossible() {
		return this.nextBuildingbuildPossible;
	}

	public boolean isNextTroupBuildBarracksPossible() {
		return new Date().after(this.nextTroupBuildBarracksPossible);
	}

	public void setNextTroupBuildBarracksPossible(Date nextTroupBuildBarracksPossible) {
		this.nextTroupBuildBarracksPossible = nextTroupBuildBarracksPossible;
	}

	public Date getNextTroupBuildBarracks() {
		return this.nextTroupBuildBarracksPossible;
	}

	public boolean isNextTroupBuildStablePossible() {
		return new Date().after(this.nextTroupBuildStablePossible);
	}

	public void setNextTroupBuildStablePossible(Date nextTroupBuildStablePossible) {
		this.nextTroupBuildStablePossible = nextTroupBuildStablePossible;
	}

	public Date getNextTroupBuildStable() {
		return this.nextTroupBuildStablePossible;
	}

	public boolean isNextTroupBuildWorkshopPossible() {
		return new Date().after(this.nextTroupBuildWorkshopPossible);
	}

	public void setNextTroupBuildWorkshopPossible(Date nextTroupBuildWorkshopPossible) {
		this.nextTroupBuildWorkshopPossible = nextTroupBuildWorkshopPossible;
	}

	public Date getNextTroupBuildWorkshop() {
		return this.nextTroupBuildWorkshopPossible;
	}

	public boolean isNextFarmattackPossible() {
		return new Date().after(this.nextFarmattackPossible);
	}

	public void setNextFarmattackPossible(Date nextFarmattackPossible) {
		this.nextFarmattackPossible = nextFarmattackPossible;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getMaximalPopulation() {
		return maximalPopulation;
	}

	public void setMaximalPopulation(int maximalPopulation) {
		this.maximalPopulation = maximalPopulation;
	}

	public void addResearchOrder(String unit) {
		if (!this.needResearch.contains(unit)) {
			this.needResearch.add(unit);
		}
	}

	public String getNextResearchOrder() {
		if (this.needResearch.size() > 0) {
			return this.needResearch.remove(0);
		} else {
			return null;
		}
	}

	public boolean hasToResearch() {
		return this.needResearch.size() > 0;
	}

}
