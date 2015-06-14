package datastore.memoryObject;

public class Farm {

	private final double distance;
	private final double possibleResources;
	private final int wall;
	private final int x;
	private final int y;

	public Farm(int x, int y, double distance, double possibleResources, int wall) {
		this.distance = distance;
		this.possibleResources = possibleResources;
		this.wall = wall;
		this.x = x;
		this.y = y;
	}

	public double getDistance() {
		return this.distance;
	}

	public double getPossibleResources() {
		return this.possibleResources;
	}

	public int getWall() {
		return this.wall;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
