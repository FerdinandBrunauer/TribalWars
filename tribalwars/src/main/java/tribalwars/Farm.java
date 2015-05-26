package tribalwars;

public class Farm {
	public int farmID;
	public int x;
	public int y;
	public int farmed;
	public boolean owned;
	
	public Farm(int farmID, int x, int y, int farmed, boolean owned) {
		this.farmID = farmID;
		this.x = x;
		this.y = y;
		this.farmed = farmed;
		this.owned = owned;
	}
}
