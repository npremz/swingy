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

	private final String display_name;
	private final int min_bonus;
	private final int max_bonus;
	private final double drop_chance;

	ItemRarity(String display_name, int min_bonus, int max_bonus, double drop_chance)
	{
		this.display_name = display_name;
		this.min_bonus = min_bonus;
		this.max_bonus = max_bonus;
		this.drop_chance = drop_chance;
	}

	// Getters
	public String getDisplayName()
	{
		return display_name;
	}

	public int getMinBonus()
	{
		return min_bonus;
	}

	public int getMaxBonus()
	{
		return max_bonus;
	}

	public double getDropChance()
	{
		return drop_chance;
	}

	public int generateBonus()
	{
		return min_bonus + (int)(Math.random() * (max_bonus - min_bonus + 1));
	}
}
