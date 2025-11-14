package be.npremont.swingy.model;

public enum HeroClass
{
	WARRIOR(
		"Warrior",
		new BaseStats(150, 12, 15),
		new GrowthRates(1.15, 1.08, 1.12)
	),
	ARCHER(
		"Archer",
		new BaseStats(100, 15, 10),
		new GrowthRates(1.1, 1.1, 1.1)
	),
	ASSASSIN(
		"Assassin",
		new BaseStats(150, 12, 15),
		new GrowthRates(1.05, 1.15, 1.05)
	);

	private final String display_name;
	private final BaseStats base_stats;
	private final GrowthRates growth_rates;

	HeroClass(String display_name, BaseStats base_stats, GrowthRates growth_rates)
	{
		this.display_name = display_name;
		this.base_stats = base_stats;
		this.growth_rates = growth_rates;
	}

	//Getter
	public String getDisplayName()
	{
		return display_name;
	}

	public BaseStats getBaseStats()
	{
		return base_stats;
	}

	public GrowthRates getGrowthRates()
	{
		return growth_rates;
	}
}
