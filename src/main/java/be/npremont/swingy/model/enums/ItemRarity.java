package be.npremont.swingy.model.enums;

public enum ItemRarity
{
	COMMON(
		"Common",
		5,
		10,
		2.0,
		5.0,
		35.0
	),
	UNCOMMON(
		"Uncommon",
		11,
		20,
		5.0,
		10.0,
		15.0
	),
	RARE(
		"Rare",
		21,
		35,
		10.0,
		15.0,
		7.0
	),
	EPIC(
		"Epic",
		36,
		50,
		15.0,
		25.0,
		2.5
	),
	LEGENDARY(
		"Legendary",
		51,
		75,
		25.0,
		40.0,
		0.5
	);

	private final String display_name;
	private final int min_flat_bonus;
	private final int max_flat_bonus;
	private final double min_percent_bonus;
	private final double max_percent_bonus;
	private final double drop_chance;

	ItemRarity(String display_name, int min_flat_bonus, int max_flat_bonus,
		double min_percent_bonus, double max_percent_bonus, double drop_chance)
	{
		this.display_name = display_name;
		this.min_flat_bonus = min_flat_bonus;
		this.max_flat_bonus = max_flat_bonus;
		this.min_percent_bonus = min_percent_bonus;
		this.max_percent_bonus = max_percent_bonus;
		this.drop_chance = drop_chance;
	}

	// Getters
	public String getDisplayName()
	{
		return display_name;
	}

	public int getMinFlatBonus()
	{
		return min_flat_bonus;
	}

	public int getMaxFlatBonus()
	{
		return max_flat_bonus;
	}

	public double getMinPercentBonus()
	{
		return min_percent_bonus;
	}

	public double getMaxPercentBonus()
	{
		return max_percent_bonus;
	}

	public double getDropChance()
	{
		return drop_chance;
	}

	public int generateFlatBonus()
	{
		return min_flat_bonus + (int)(Math.random() * (max_flat_bonus - min_flat_bonus + 1));
	}

	public double generatePercentBonus()
	{
		return min_percent_bonus + (Math.random() * (max_percent_bonus - min_percent_bonus));
	}
}
