package tribalwars;

public class FarmVorlage {

	private Unit unit;
	private int count;
	
	public FarmVorlage(Unit unit, int count) {
		this.unit = unit;
		this.count = count;
	}

	public Unit getUnit() {
		return unit;
	}

	public int getCount() {
		return count;
	}
	
}
