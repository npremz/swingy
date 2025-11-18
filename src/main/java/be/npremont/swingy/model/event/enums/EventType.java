package be.npremont.swingy.model.event.enums;

public enum EventType
{
	ALLY_ENCOUNTER(false, 30.0),
	TREASURE_CHEST(false, 30.0),
	SHRINE(true, 15.0),
	TRAP(false, 15.0),
	AMBUSH(false, 10.0);

	private final boolean unique;
	private final double spawnWeight;

	EventType(boolean unique, double spawnWeight)
	{
		this.unique = unique;
		this.spawnWeight = spawnWeight;
	}

	// Getters
	public boolean isUnique()
	{
		return unique;
	}

	public double getSpawnWeight()
	{
		return spawnWeight;
	}
}
