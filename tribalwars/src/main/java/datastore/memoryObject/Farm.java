package datastore.memoryObject;

public class Farm {

	private final double distance;
	private final double possibleResources;
	private final int wall;

	private final double resourcesPerDistance;

	public Farm(double distance, double possibleResources, int wall) {
		this.distance = distance;
		this.possibleResources = possibleResources;
		this.wall = wall;

		this.resourcesPerDistance = this.possibleResources / this.distance;
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

	public double getResourcesPerDistance() {
		return this.resourcesPerDistance;
	}

}
