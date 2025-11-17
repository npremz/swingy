package be.npremont.swingy.model.enums;

import be.npremont.swingy.model.stats.BaseStats;
import be.npremont.swingy.model.stats.GrowthRates;

public enum HeroClass
{
	WARRIOR(
		"Warrior",
		new BaseStats(120, 10, 12),
		new GrowthRates(1.12, 1.08, 1.12)
	),
	ARCHER(
		"Archer",
		new BaseStats(100, 12, 8),
		new GrowthRates(1.1, 1.12, 1.08)
	),
	ASSASSIN(
		"Assassin",
		new BaseStats(80, 15, 6),
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
