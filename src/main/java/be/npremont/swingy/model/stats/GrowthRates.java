package be.npremont.swingy.model.stats;

public class GrowthRates
{
	private final double hp_multiplier;
	private final double attack_multiplier;
	private final double defense_multiplier;

	public GrowthRates(double hp_multiplier, double attack_multiplier, double defense_multiplier)
	{
		this.hp_multiplier = hp_multiplier;
		this.attack_multiplier = attack_multiplier;
		this.defense_multiplier = defense_multiplier;
	}

	// Getters
	public double getHpMultiplier()
	{
		return hp_multiplier;
	}

	public double getAttackMultiplier()
	{
		return attack_multiplier;
	}

	public double getDefenseMultiplier()
	{
		return defense_multiplier;
	}
}
