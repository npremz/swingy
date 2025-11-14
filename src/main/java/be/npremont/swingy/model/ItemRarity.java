package be.npremont.swingy.model;

public enum ItemRarity
{
	COMMON(
		"Common",
		5,
		10,
		35.0
	),
	UNCOMMON(
		"Uncommon",
		11,
		20,
		15.0
	),
	RARE(
		"Rare",
		21,
		35,
		7.0
	),
	EPIC(
		"Epic",
		36,
		50,
		2.5
	),
	LEGENDARY(
		"Legendary",
		51,
		75,
		0.5
	);

	private final String displayName;
	private final int minBonus;
	private final int maxBonus;
	private final double dropChance;

	ItemRarity(String displayName, int minBonus, int maxBonus, double dropChance)
	{
		this.displayName = displayName;
		this.minBonus = minBonus;
		this.maxBonus = maxBonus;
		this.dropChance = dropChance;
	}

	// Getters
	public String getDisplayName()
	{
		return displayName;
	}

	public int getMinBonus()
	{
		return minBonus;
	}

	public int getMaxBonus()
	{
		return maxBonus;
	}

	public double getDropChance()
	{
		return dropChance;
	}

	public int generateBonus()
	{
		return minBonus + (int)(Math.random() * (maxBonus - minBonus + 1));
	}
}
